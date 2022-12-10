package controllers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import Dao.DBConnect;
import application.DynamicTable;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * admin controller
 */
public class AdminController {

    @FXML
    private Pane pane1;
    @FXML
    private Pane pane2;
    @FXML
    private Pane pane3;

    @FXML
    private TextField txtBookAuthor;

    @FXML
    private TextField txtBookTitle;
    @FXML
    private TextField txtBookId;

    @FXML
    private TextField txtBookIdUpdate;

    @FXML
    private TextField txtBookTitleUpdate;

    @FXML
    private Label lbMsg;


    // Declare DB objects
    DBConnect conn = null;
    Statement stmt = null;

    PreparedStatement pstmt = null;

    public AdminController() {
        conn = new DBConnect();
    }

    public void viewUsers() {
        lbMsg.setText("");
        DynamicTable d = new DynamicTable();
        // call method from DynamicTable class and pass some arbitrary query string
        d.buildData("Select uname,case when admin = 0 then 'normal user' else 'admin' end as type from chen_users");
    }

    public void viewBooks() {
        lbMsg.setText("");
        DynamicTable d = new DynamicTable();
        // call method from DynamicTable class and pass some arbitrary query string
        d.buildData("Select bid, title,author from chen_books");
    }

    public void updateBook() {
        lbMsg.setText("");
        pane3.setVisible(false);
        pane2.setVisible(false);
        pane1.setVisible(true);
    }

    public void deleteBook() {
        lbMsg.setText("");
        pane1.setVisible(false);
        pane2.setVisible(true);
        pane3.setVisible(false);
    }

    public void submitBook() {
        // INSERT INTO books table
        try {
            System.out.println("Inserting books into the table...");
            stmt = conn.getConnection().createStatement();
            String sql = "insert into chen_books(title,author) values ('" + txtBookTitle.getText() + "','" + txtBookAuthor.getText()
                    + "')";
            int flag = stmt.executeUpdate(sql);
            if (flag > 0) {
                lbMsg.setText("book created");
            } else {
                lbMsg.setText("book create failed");
            }
        } catch (SQLException se) {
            lbMsg.setText("book create failed");
            se.printStackTrace();
        }
    }

    public void submitUpdate() {

        // update book table
        try {
            System.out.println("updating books id " + txtBookIdUpdate.getText() + " update title:"+txtBookTitleUpdate.getText());

            String sql = "update chen_books set title= ? where bid = ?";
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1,txtBookTitleUpdate.getText());
            pstmt.setInt(2,Integer.parseInt(txtBookIdUpdate.getText()));
            int flag = pstmt.executeUpdate();
            if (flag > 0) {
                lbMsg.setText("book updated");
            } else {
                lbMsg.setText("book updated failed");
            }
        } catch (SQLException se) {
            lbMsg.setText("book update failed");
            se.printStackTrace();
        }

    }

    public void submitDelete() {
        // delete book record
        try {
            // Execute a query
            System.out.println("delete books id "  + txtBookId.getText());
            int bookId = Integer.parseInt(txtBookId.getText());
            String sql = "delete from chen_books where bid = ?";
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setInt(1, bookId);
            int flag = pstmt.executeUpdate();
            if (flag > 0) {
                lbMsg.setText("book deleted");
            } else {
                lbMsg.setText("book delete failed");
            }
        } catch (SQLException se) {
            lbMsg.setText("book delete failed");
            se.printStackTrace();
        }

    }

    @FXML
    void logout() {
        try {
            conn.getConnection().close();
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application/styles.css").toExternalForm());
            Main.stage.setScene(scene);
            Main.stage.setTitle("Login");
        } catch (Exception e) {
            System.out.println("Error occured while inflating view: " + e);
        }
    }

}
