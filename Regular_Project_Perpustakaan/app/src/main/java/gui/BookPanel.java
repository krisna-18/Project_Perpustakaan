package gui;

import entities.Book;
import model.BookModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BookPanel extends JPanel {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private BookModel bookModel;
    private JTextField searchField;
    private JComboBox<String> searchCriteria;

    public BookPanel(BookModel bookModel) {
        this.bookModel = bookModel;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Available"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tidak memungkinkan pengeditan langsung di tabel
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        bookTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane, BorderLayout.CENTER);

        // Panel untuk pencarian
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchCriteria = new JComboBox<>(new String[]{"Title", "Author"});
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> searchBooks());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchCriteria);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        // Panel untuk tombol
        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> showAddBookDialog());

        JButton editBookButton = new JButton("Edit Book");
        editBookButton.addActionListener(e -> editBook());

        JButton deleteBookButton = new JButton("Delete Book");
        deleteBookButton.addActionListener(e -> deleteBook());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(addBookButton, gbc);

        gbc.gridx = 1;
        buttonPanel.add(editBookButton, gbc); // Tambahkan tombol Edit Book

        gbc.gridx = 2;
        buttonPanel.add(deleteBookButton, gbc);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        try {
            List<Book> books = bookModel.getAllBooks();
            tableModel.setRowCount(0); // Membersihkan data yang ada
            for (Book book : books) {
                tableModel.addRow(new Object[]{
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.isAvailable()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBooks() {
        String keyword = searchField.getText();
        String criteria = searchCriteria.getSelectedItem().toString();

        try {
            List<Book> books;
            if (criteria.equals("Title")) {
                books = bookModel.searchBooksByTitle(keyword);
            } else {
                books = bookModel.searchBooksByAuthor(keyword);
            }
            tableModel.setRowCount(0); // Membersihkan data yang ada
            for (Book book : books) {
                tableModel.addRow(new Object[]{
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.isAvailable()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to search books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshData() {
        SwingUtilities.invokeLater(this::loadData); // Memuat data kembali secara asinkron
    }

    private void showAddBookDialog() {
        AddBookDialog dialog = new AddBookDialog(SwingUtilities.getWindowAncestor(this), bookModel, this);
        dialog.setVisible(true);
    }

    private void editBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                Book book = bookModel.getBookById(bookId);
                if (book != null) {
                    EditBookDialog dialog = new EditBookDialog(SwingUtilities.getWindowAncestor(this), bookModel, book, this);
                    dialog.setVisible(true);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to load book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silakan pilih buku yang akan diedit", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                int option = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus buku ini?", "Konfirmasi Penghapusan", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    bookModel.deleteBook(bookId);
                    loadData(); // Muat ulang tabel buku setelah penghapusan
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Buku memiliki transaksi yang terkait")) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus buku: Buku memiliki transaksi yang terkait", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus buku: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silakan pilih buku yang akan dihapus", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
}
