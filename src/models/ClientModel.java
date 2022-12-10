package models;

import java.awt.print.Book;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Dao.DBConnect;

public class ClientModel extends DBConnect {

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	Book book; //set up Book object

	public ClientModel() {
		conn = new DBConnect();
	}

	/**
	 * borrow book or return book
	 *
	 * @param bId
	 * @param uId
	 * @param action 0 return book 1 borrow book
	 */
	public Boolean insertBookOrder(int bId, int uId, int action) {
		Boolean flag = false;
		try {
			if (isExists(bId)) {
				PreparedStatement stmt;
				// borrow book or return book
				if ((action == 1 && !isOrdered(bId)) || action == 0 && isOrdered(bId)) {
					// insert book order
					String sql1 = "insert into chen_orders(bid, uId, action) values(?,?,?);";
					stmt = conn.getConnection().prepareStatement(sql1);
					stmt.setInt(1, bId);
					stmt.setInt(2, uId);
					stmt.setInt(3, action);
					stmt.executeUpdate();
					System.out.println("add book order user id " + uId + " borrow book id " + bId);

					// update book status
					String sql2 = "update chen_books set status = ? where bid = ?;";
					stmt = conn.getConnection().prepareStatement(sql2);
					stmt.setInt(1, action);
					stmt.setInt(2, bId);
					stmt.executeUpdate();
					System.out.println("update book id " + bId + " status as unavailable");
					flag = true;
				}
			}
			return flag;
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return null;
	}

	/**
	 * check book availability
	 *
	 * @param bId
	 * @return true is orderes false stands for available
	 */
	private Boolean isOrdered(int bId) throws SQLException {
		System.out.println("checking book id " + bId + " status...");
		String sql = "select status from chen_books where bid = ?";

		PreparedStatement statement = conn.getConnection().prepareStatement(sql);
		statement.setInt(1, bId);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			System.out.println("book id status is ordered: " + resultSet.getBoolean("status"));
			return resultSet.getBoolean("status");
		}
		return Boolean.TRUE;
	}

	/**
	 * check book id exists
	 *
	 * @param bId book id
	 * @return true book
	 * @throws SQLException
	 */
	private Boolean isExists(int bId) throws SQLException {
		System.out.println("checking book id " + bId + " exists...");
		String sql = "select count(1) as cnt from chen_books where bid = ?";

		PreparedStatement statement = conn.getConnection().prepareStatement(sql);
		statement.setInt(1, bId);
		ResultSet resultSet = statement.executeQuery();
		int cnt = 0;
		while (resultSet.next()) {
			cnt = resultSet.getInt("cnt");
		}

		if (cnt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * search books by book title
	 *
	 * @param title
	 * @return
	 */
	public List<models.Book> searchBooks(String title) {
		List<models.Book> books = new ArrayList<>();
		String query = "SELECT bid,author,title,status FROM chen_books where title like ?;";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, "%" + title + "%");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				models.Book book = new models.Book();
				book.setBid(resultSet.getInt("bid"));
				book.setAuthor(resultSet.getString("author"));
				book.setTitle(resultSet.getString("title"));
				String status = resultSet.getBoolean("status") ? "unavailable" : "available";
				book.setStatus(status);
				// grab record data by table field name into ClientModel account object
				books.add(book); // add account data to arraylist
			}

		} catch (SQLException e) {
			System.out.println("Error fetching books: " + e);
		}

		return books; // return arraylist
	}


	/**
	 * get book list and show book status
	 *
	 * @return
	 */
	public List<models.Book> getBooks() {
		List<models.Book> books = new ArrayList<>();
		String query = "SELECT bid,author,title,status FROM chen_books;";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				models.Book book = new models.Book();
				book.setBid(resultSet.getInt("bid"));
				book.setAuthor(resultSet.getString("author"));
				book.setTitle(resultSet.getString("title"));
				String status = resultSet.getBoolean("status") ? "unavailable" : "available";
				book.setStatus(status);
				// grab record data by table field name into ClientModel account object
				books.add(book); // add account data to arraylist

			}
		} catch (SQLException e) {
			System.out.println("Error fetching books: " + e);
		}
		return books; // return arraylist
	}
}