package gui;

import model.BookModel;
import model.MemberModel;
import model.TransactionModel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private MemberModel memberModel;
    private BookModel bookModel;
    private TransactionModel transactionModel;

    public LoginFrame(MemberModel memberModel, BookModel bookModel, TransactionModel transactionModel) {
        this.memberModel = memberModel;
        this.bookModel = bookModel;
        this.transactionModel = transactionModel;
        initUI();
    }

    private void initUI() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            try {
                login();
            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        buttonPanel.add(loginButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
        });
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void login() throws SQLException {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (memberModel.validateAdmin(username, password)) {
            new MainFrame(bookModel, memberModel, transactionModel).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MemberModel memberModel = new MemberModel();
            BookModel bookModel = new BookModel();
            TransactionModel transactionModel = new TransactionModel();
            new LoginFrame(memberModel, bookModel, transactionModel).setVisible(true);
        });
    }
}
