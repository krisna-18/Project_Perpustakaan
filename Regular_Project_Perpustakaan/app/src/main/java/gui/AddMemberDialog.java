package gui;

import entities.Member;
import model.MemberModel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddMemberDialog extends JDialog {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField roleField;
    private Runnable refreshCallback;

    public AddMemberDialog(JFrame parent, Runnable refreshCallback) {
        super(parent, "Add Member", true);
        this.refreshCallback = refreshCallback;
        initUI();
    }

    private void initUI() {
        setLayout(new GridLayout(8, 2));

        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        roleField = new JTextField();

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Phone:"));
        add(phoneField);
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Role:"));
        add(roleField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addMember());
        add(addButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        setSize(400, 400);
        setLocationRelativeTo(getParent());
    }

    private void addMember() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = roleField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Member member = new Member(0, name, email, phone, username, password, role);

        MemberModel memberModel = new MemberModel();
        try {
            memberModel.addMember(member);
            refreshCallback.run(); // Memanggil callback untuk merefresh data
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add member: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
