package gui;

import entities.Book;
import model.BookModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class EditBookDialog extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private JCheckBox availableCheckBox;
    private BookModel bookModel;
    private Book book;
    private BookPanel bookPanel;

    public EditBookDialog(Window owner, BookModel bookModel, Book book, BookPanel bookPanel) {
        super(owner, "Edit Book", ModalityType.APPLICATION_MODAL);
        this.bookModel = bookModel;
        this.book = book;
        this.bookPanel = bookPanel;
        initComponents();
        setLocationRelativeTo(owner);
        loadBookData();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;

        // Label dan input untuk setiap field
        add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; titleField = new JTextField(20); add(titleField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Author:"), gbc);
        gbc.gridx = 1; authorField = new JTextField(20); add(authorField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Available:"), gbc);
        gbc.gridx = 1; availableCheckBox = new JCheckBox(); add(availableCheckBox, gbc);

        // Tombol Simpan
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2; // Mengatur tombol untuk mengambil dua kolom
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::saveBook);
        add(saveButton, gbc);

        pack();
    }

    private void loadBookData() {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        availableCheckBox.setSelected(book.isAvailable());
    }

    private void saveBook(ActionEvent e) {
        book.setTitle(titleField.getText());
        book.setAuthor(authorField.getText());
        book.setAvailable(availableCheckBox.isSelected());

        try {
            bookModel.updateBook(book);
            bookPanel.refreshData(); // Refresh data di BookPanel
            dispose(); // Tutup dialog setelah menyimpan
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to update book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
