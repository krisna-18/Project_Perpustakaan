package model;

import entities.Member;
import helper.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberModel {
    public boolean validateAdmin(String username, String password) throws SQLException {
        String query = "SELECT * FROM members WHERE username = ? AND password = ? AND role = 'admin'";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";
        try (Connection conn = DatabaseHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Member member = new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                members.add(member);
            }
        }
        return members;
    }

    public void addMember(Member member) throws SQLException {
        String query = "INSERT INTO members (name, email, phone, username, password, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            stmt.setString(4, member.getUsername());
            stmt.setString(5, member.getPassword());
            stmt.setString(6, member.getRole());
            stmt.executeUpdate();
        }
    }

    public void deleteMember(int memberId) throws SQLException {
        String query = "DELETE FROM members WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            stmt.executeUpdate();
        }
    }

    public Member getMemberByUsername(String username) throws SQLException {
        Member member = null;
        String query = "SELECT * FROM members WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    member = new Member(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        }
        return member;
    }

    public Member authenticate(String username, String password) throws SQLException {
        Member member = null;
        String query = "SELECT * FROM members WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    member = new Member(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        }
        return member;
    }

    // Tambahkan metode pencarian berdasarkan nama
    public List<Member> searchMembersByName(String name) throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members WHERE name LIKE ?";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Member member = new Member(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                    members.add(member);
                }
            }
        }
        return members;
    }

    // Tambahkan metode pencarian berdasarkan ID
    public List<Member> searchMembersById(int id) throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Member member = new Member(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                    members.add(member);
                }
            }
        }
        return members;
    }
    public void updateMember(Member member) throws SQLException {
    String query = "UPDATE members SET name = ?, email = ?, phone = ?, username = ?, password = ?, role = ? WHERE id = ?";
    try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, member.getName());
        stmt.setString(2, member.getEmail());
        stmt.setString(3, member.getPhone());
        stmt.setString(4, member.getUsername());
        stmt.setString(5, member.getPassword());
        stmt.setString(6, member.getRole());
        stmt.setInt(7, member.getId());
        stmt.executeUpdate();
    }
}
public Member getMemberById(int memberId) throws SQLException {
    Member member = null;
    String query = "SELECT * FROM members WHERE id = ?";
    try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, memberId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                member = new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        }
    }
    return member;
}

}
        
