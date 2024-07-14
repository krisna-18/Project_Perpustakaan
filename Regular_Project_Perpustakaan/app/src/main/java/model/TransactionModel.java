package model;

import entities.Book;
import entities.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionModel {
    private Connection connection;

    public TransactionModel() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_perpustakaan", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTransaction(Transaction transaction) throws SQLException {
        String query = "INSERT INTO transactions (book_id, member_id, borrow_date, due_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transaction.getBookId());
            statement.setInt(2, transaction.getMemberId());
            statement.setDate(3, new java.sql.Date(transaction.getBorrowDate().getTime()));
            statement.setDate(4, new java.sql.Date(transaction.getDueDate().getTime()));
            statement.setNull(5, Types.DATE); // return_date initially NULL
            statement.executeUpdate();

            // Update availability status of the book
            updateBookAvailability(transaction.getBookId(), false); // Set available to false after borrowing
        }
    }

    public void updateTransaction(Transaction transaction) throws SQLException {
    String query = "UPDATE transactions SET book_id = ?, member_id = ?, borrow_date = ?, due_date = ?, return_date = ?, late_fee = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, transaction.getBookId());
        statement.setInt(2, transaction.getMemberId());
        statement.setDate(3, new java.sql.Date(transaction.getBorrowDate().getTime()));
        statement.setDate(4, new java.sql.Date(transaction.getDueDate().getTime()));
        statement.setDate(5, new java.sql.Date(transaction.getReturnDate().getTime()));
        statement.setDouble(6, transaction.getLateFee()); // Tambahkan ini untuk menyimpan denda
        statement.setInt(7, transaction.getId());
        statement.executeUpdate();
        
        // Update status ketersediaan buku
        if (transaction.getReturnDate() != null) {
            updateBookAvailability(transaction.getBookId(), true); // Set available to true after returning
        } else {
            updateBookAvailability(transaction.getBookId(), false); // Set available to false after borrowing
        }
    }
}


    private void updateBookAvailability(int bookId, boolean available) throws SQLException {
        String query = "UPDATE books SET available = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, available);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getInt("book_id"),
                        resultSet.getInt("member_id"),
                        resultSet.getDate("borrow_date"),
                        resultSet.getDate("due_date"),
                        resultSet.getDate("return_date")
                ));
            }
        }
        return transactions;
    }

    public Transaction getTransactionById(int id) throws SQLException {
        String query = "SELECT * FROM transactions WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Transaction(
                            resultSet.getInt("id"),
                            resultSet.getInt("book_id"),
                            resultSet.getInt("member_id"),
                            resultSet.getDate("borrow_date"),
                            resultSet.getDate("due_date"),
                            resultSet.getDate("return_date")
                    );
                }
            }
        }
        return null;
    }

    public Book getBookById(int id) throws SQLException {
        String query = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Book(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getBoolean("available")
                    );
                }
            }
        }
        return null;
    }
}
