package model;

import entities.Book;
import helper.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookModel {

    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, available) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setBoolean(3, book.isAvailable());
            statement.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                boolean available = resultSet.getBoolean("available");
                books.add(new Book(id, title, author, available));
            }
        }
        return books;
    }

    public void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, available = ? WHERE id = ?";
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setBoolean(3, book.isAvailable());
            statement.setInt(4, book.getId());
            statement.executeUpdate();
        }
    }

    public void deleteBook(int id) throws SQLException {
    if (hasTransactions(id)) {
        throw new SQLException("Buku memiliki transaksi yang terkait dan tidak dapat dihapus.");
    }
    
    String sql = "DELETE FROM books WHERE id = ?";
    try (Connection connection = DatabaseHelper.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, id);
        statement.executeUpdate();
    }
}



    // Tambahkan metode pencarian berdasarkan judul
    public List<Book> searchBooksByTitle(String title) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + title + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String bookTitle = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    boolean available = resultSet.getBoolean("available");
                    books.add(new Book(id, bookTitle, author, available));
                }
            }
        }
        return books;
    }

    // Tambahkan metode pencarian berdasarkan penulis
    public List<Book> searchBooksByAuthor(String author) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE author LIKE ?";
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + author + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String bookAuthor = resultSet.getString("author");
                    boolean available = resultSet.getBoolean("available");
                    books.add(new Book(id, title, bookAuthor, available));
                }
            }
        }
        return books;
    }

    public Book getBookById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    boolean available = resultSet.getBoolean("available");
                    return new Book(id, title, author, available);
                }
            }
        }
        return null;
    }
    public boolean hasTransactions(int bookId) throws SQLException {
    String query = "SELECT COUNT(*) AS count FROM transactions WHERE book_id = ?";
    try (Connection connection = DatabaseHelper.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, bookId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        }
    }
    return false;
}

}
