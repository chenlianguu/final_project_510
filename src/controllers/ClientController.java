package controllers;

import application.DynamicTable;
import application.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.Book;
import models.ClientModel;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;

/**
 * client controller
 */
public class ClientController implements Initializable {

	private ClientModel cm;

	static int userid;

	@FXML
	private TextField bookSearchTxt;
	@FXML
	private TextField borrowBookTxt;
	@FXML
	private TextField returnBookTxt;

	@FXML
	private TableColumn<Book, String> author;
	@FXML
	private TableColumn<Book, String> bid;
	@FXML
	private TableColumn<Book, String> title;
	@FXML
	private TableColumn<Book, String> status;
	@FXML
	private TableView<Book> bookList;


	public ClientController() {
		cm = new ClientModel();
	}

	@FXML
	void checkBookList() {
		bookList.getItems().setAll(cm.getBooks());
		bookList.setVisible(true);
	}

	@FXML
	void search() {
		System.out.println("userid:"+userid+" search book for "+ bookSearchTxt.getText());
		bookList.getItems().setAll(cm.searchBooks(bookSearchTxt.getText()));
		bookList.setVisible(true);
	}

	/**
	 * 0 return book 1 borrow book
	 *
	 */
	@FXML
	void borrowBook() {
		Boolean flag = cm.insertBookOrder(Integer.parseInt(borrowBookTxt.getText()), userid, 1);
		if (!flag) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("WARNING MESSSAGE");
			alert.setHeaderText("IIT library");
			alert.setContentText("the book you borrow is not available or exists"); alert.showAndWait();

		}
		// refresh table
		checkBookList();
	}

	@FXML
	void checkOrderHistory() {
		System.out.println("checking userid:"+userid+" order history");
		DynamicTable d = new DynamicTable();
		// call method from DynamicTable class and pass some arbitrary query string
		d.buildData("SELECT\n" +
				"    title,\n" +
				"    CASE WHEN action = 1 THEN\n" +
				"        'borrow book'\n" +
				"    ELSE\n" +
				"        'return book'\n" +
				"    END AS action,\n" +
				"    chen_orders.create_time\n" +
				"FROM\n" +
				"    chen_orders\n" +
				"    right JOIN chen_books USING (bid) where uid = " + userid);
	}

	@FXML
	void logout() {
		try {
			cm.getConnection().close();
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/styles.css").toExternalForm());
			Main.stage.setScene(scene);
			Main.stage.setTitle("Login");
		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	}

	@FXML
	void returnBook() {
		Boolean flag = cm.insertBookOrder(Integer.parseInt(returnBookTxt.getText()), userid, 0);
		if (!flag) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("WARNING MESSSAGE");
			alert.setHeaderText("IIT library");
			alert.setContentText("the book you borrow is not available or exists"); alert.showAndWait();

		}
		// refresh table
		checkBookList();
	}

	public void initialize(URL location, ResourceBundle resources) {
		bid.setCellValueFactory(new PropertyValueFactory<Book, String>("bid"));
		author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		status.setCellValueFactory(new PropertyValueFactory<Book, String>("status"));

		// auto adjust width of columns depending on their content
		bookList.setColumnResizePolicy((param) -> true);
		Platform.runLater(() -> customResize(bookList));

		bookList.setVisible(false); // set invisible initially
	}

	public void customResize(TableView<?> view) {

		AtomicLong width = new AtomicLong();
		view.getColumns().forEach(col -> {
			width.addAndGet((long) col.getWidth());
		});
		double tableWidth = view.getWidth();

		if (tableWidth > width.get()) {
			view.getColumns().forEach(col -> {
				col.setPrefWidth(col.getWidth()+((tableWidth-width.get())/view.getColumns().size()));
			});
		}
	}

	public static void setUserid(int user_id) {
		userid = user_id;
		System.out.println("Welcome id " + userid);
	}


}
