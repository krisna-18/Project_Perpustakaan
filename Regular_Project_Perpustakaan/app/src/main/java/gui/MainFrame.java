package gui;

import model.BookModel;
import model.MemberModel;
import model.TransactionModel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private BookModel bookModel;
    private MemberModel memberModel;
    private TransactionModel transactionModel;

    public MainFrame(BookModel bookModel, MemberModel memberModel, TransactionModel transactionModel) {
        this.bookModel = bookModel;
        this.memberModel = memberModel;
        this.transactionModel = transactionModel;
        initUI();
    }

    private void initUI() {
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Books", new BookPanel(bookModel));
        tabbedPane.addTab("Members", new MemberPanel(memberModel));
        tabbedPane.addTab("Transactions", new TransactionPanel(transactionModel));

        add(tabbedPane, BorderLayout.CENTER);

        // Tambahkan panel untuk tombol logout
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        logoutPanel.add(logoutButton);

        add(logoutPanel, BorderLayout.NORTH);
    }

    private void logout() {
        // Kembali ke login screen
        dispose(); // Menutup MainFrame
        new LoginFrame(memberModel, bookModel, transactionModel).setVisible(true);
    }
}
