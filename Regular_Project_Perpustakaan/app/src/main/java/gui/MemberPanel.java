package gui;

import entities.Member;
import model.MemberModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberPanel extends JPanel {
    private MemberModel memberModel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchCriteria;

    public MemberPanel(MemberModel memberModel) {
        this.memberModel = memberModel;
        initUI();
        loadMembers();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone", "Username", "Role"}, 0);
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel untuk pencarian
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchCriteria = new JComboBox<>(new String[]{"Name", "ID"});
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> searchMembers());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchCriteria);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        // Panel untuk tombol
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Member");
        addButton.addActionListener(e -> openAddMemberDialog());
        
        JButton editButton = new JButton("Edit Member");
        editButton.addActionListener(e -> {
            openEditMemberDialog();
        }); // Tambahkan listener untuk Edit Member
        
        JButton deleteButton = new JButton("Delete Member");
        deleteButton.addActionListener(e -> deleteMember());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton); // Tambahkan tombol Edit Member
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadMembers() {
        try {
            List<Member> members = memberModel.getAllMembers();
            tableModel.setRowCount(0);
            for (Member member : members) {
                tableModel.addRow(new Object[]{
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getPhone(),
                        member.getUsername(),
                        member.getRole()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load members: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchMembers() {
        String keyword = searchField.getText();
        String criteria = searchCriteria.getSelectedItem().toString();

        try {
            List<Member> members;
            if (criteria.equals("Name")) {
                members = memberModel.searchMembersByName(keyword);
            } else {
                int id = Integer.parseInt(keyword);
                members = memberModel.searchMembersById(id);
            }
            tableModel.setRowCount(0); // Membersihkan data yang ada
            for (Member member : members) {
                tableModel.addRow(new Object[]{
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getPhone(),
                        member.getUsername(),
                        member.getRole()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to search members: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddMemberDialog() {
        AddMemberDialog dialog = new AddMemberDialog((JFrame) SwingUtilities.getWindowAncestor(this), this::loadMembers);
        dialog.setVisible(true);
    }

    private void openEditMemberDialog() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a member to edit", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int memberId = (int) tableModel.getValueAt(selectedRow, 0);
    try {
        Member member = memberModel.getMemberById(memberId); // Pastikan Anda memiliki metode ini
        if (member != null) {
            EditMemberDialog dialog = new EditMemberDialog((JFrame) SwingUtilities.getWindowAncestor(this), member, this::loadMembers);
            dialog.setVisible(true);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Failed to load member: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void deleteMember() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int memberId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            memberModel.deleteMember(memberId);
            loadMembers();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to delete member: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
