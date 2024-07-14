package gui;

import entities.Book;
import model.BookModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class AddBookDialog extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private BookModel bookModel;
    private BookPanel bookPanel;

    public AddBookDialog(Window parent, BookModel bookModel, BookPanel bookPanel) {
        super(parent, "Add Book", ModalityType.APPLICATION_MODAL);
        this.bookModel = bookModel;
        this.bookPanel = bookPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(0, 2));

        add(new JLabel("Title:"));
        titleField = new JTextField();
        add(titleField);

        add(new JLabel("Author:"));
        authorField = new JTextField();
        add(authorField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(this::addBook);
        add(addButton);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void addBook(ActionEvent e) {
        String title = titleField.getText();
        String author = authorField.getText();
        
        // Set available to true directly
        boolean available = true;

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Author are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = new Book(0, title, author, available); // ID will be auto-generated by the database
        try {
            bookModel.addBook(book);
            bookPanel.refreshData(); // Refresh the book panel after adding
            dispose(); // Close the dialog
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to add book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
