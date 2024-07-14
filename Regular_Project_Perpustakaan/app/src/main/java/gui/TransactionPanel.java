package gui;

import entities.Book;
import model.TransactionModel;
import entities.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionPanel extends JPanel {
    private TransactionModel transactionModel;
    private BookPanel bookPanel;
    private JTable table;
    private DefaultTableModel tableModel;

    public TransactionPanel(TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
        this.bookPanel = bookPanel;
        initUI();
        loadTransactions();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Book ID", "Member ID", "Borrow Date", "Due Date", "Return Date", "Late Fee"}, 0);
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton borrowBookButton = new JButton("Borrow Book");
        borrowBookButton.addActionListener(e -> openBorrowBookDialog());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(borrowBookButton, gbc);

        JButton returnBookButton = new JButton("Return Book");
        returnBookButton.addActionListener(e -> {
            try {
                returnBook();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        gbc.gridx = 1;
        buttonPanel.add(returnBookButton, gbc);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadTransactions() {
        try {
            List<Transaction> transactions = transactionModel.getAllTransactions();
            tableModel.setRowCount(0);
            for (Transaction transaction : transactions) {
                tableModel.addRow(new Object[]{
                        transaction.getId(),
                        transaction.getBookId(),
                        transaction.getMemberId(),
                        transaction.getBorrowDate(),
                        transaction.getDueDate(),
                        transaction.getReturnDate(),
                        transaction.getLateFee()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load transactions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openBorrowBookDialog() {
        JTextField bookIdField = new JTextField();
        JTextField memberIdField = new JTextField();

        Object[] message = {
                "Book ID:", bookIdField,
                "Member ID:", memberIdField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Borrow Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int bookId = Integer.parseInt(bookIdField.getText());
            int memberId = Integer.parseInt(memberIdField.getText());

            try {
                Book book = transactionModel.getBookById(bookId);
                if (book != null && book.isAvailable()) {
                    Date borrowDate = new Date(); // Tanggal peminjaman sekarang
                    Date dueDate = new Date(borrowDate.getTime() + (14L * 24 * 60 * 60 * 1000)); // 2 minggu dari sekarang

                    Transaction transaction = new Transaction(bookId, memberId, borrowDate, dueDate, null);
                    transactionModel.addTransaction(transaction);
                    loadTransactions();
                    bookPanel.refreshData(); // Memperbarui status ketersediaan buku di BookPanel
                } else {
                    JOptionPane.showMessageDialog(this, "Book is not available for borrowing.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to borrow book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid ID format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void returnBook() throws SQLException {
    int selectedRow = table.getSelectedRow();
    if (selectedRow >= 0) {
        int transactionId = (int) tableModel.getValueAt(selectedRow, 0);
        Transaction transaction = transactionModel.getTransactionById(transactionId);
        if (transaction.getReturnDate() == null) {
            transaction.setReturnDate(new Date()); // Tanggal pengembalian sekarang

            // Hitung denda
            long daysLate = (transaction.getReturnDate().getTime() - transaction.getDueDate().getTime()) / (1000 * 60 * 60 * 24);
            if (daysLate > 0) {
                double lateFee = daysLate * 1000; // Misal denda 1000 per hari
                transaction.setLateFee(lateFee);
            } else {
                transaction.setLateFee(0);
            }

            transactionModel.updateTransaction(transaction);
            loadTransactions();
            bookPanel.refreshData(); // Memperbarui status ketersediaan buku di BookPanel
        } else {
            JOptionPane.showMessageDialog(this, "Book already returned.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select a transaction to return book.", "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
}
