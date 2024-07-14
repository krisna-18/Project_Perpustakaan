package gui;

import entities.Member;
import model.MemberModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditMemberDialog extends JDialog {
    private MemberModel memberModel;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleField;
    private Member member;
    private Runnable onUpdate;

    public EditMemberDialog(JFrame parent, Member member, Runnable onUpdate) {
        super(parent, "Edit Member", true);
        this.memberModel = new MemberModel();
        this.member = member;
        this.onUpdate = onUpdate;
        initUI();
        fillData();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;

        // Label dan input untuk setiap field
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        roleField = new JComboBox<>(new String[]{"admin", "member"});

        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; add(roleField, gbc);

        // Tombol Simpan
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2; // Mengatur tombol untuk mengambil dua kolom
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::saveMember);
        add(saveButton, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private void fillData() {
        nameField.setText(member.getName());
        emailField.setText(member.getEmail());
        phoneField.setText(member.getPhone());
        usernameField.setText(member.getUsername());
        passwordField.setText(member.getPassword());
        roleField.setSelectedItem(member.getRole());
    }

    private void saveMember(ActionEvent event) {
        try {
            member.setName(nameField.getText());
            member.setEmail(emailField.getText());
            member.setPhone(phoneField.getText());
            member.setUsername(usernameField.getText());
            member.setPassword(new String(passwordField.getPassword()));
            member.setRole((String) roleField.getSelectedItem());
            memberModel.updateMember(member); // Pastikan Anda memiliki metode ini di MemberModel
            onUpdate.run();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update member: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
