package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.UserAccount;
import model.UserRole;

public class LoginDAO extends DBContext {
    public UserAccount createUser(UserAccount user) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO Users (username, password, firstName, lastName, role) VALUES (?, ?, ?, ?, 1)"; // Default role to user
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getFirstName());
        stmt.setString(4, user.getLastName());
        stmt.executeUpdate();
        connection.commit();

        return getUserByUsername(user.getUsername());
    }

    public void updateUser(UserAccount user) throws SQLException {
        PreparedStatement stmt = null;

        String sql = "UPDATE Users SET firstName = ?, lastName = ?, password = ? WHERE username = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, user.getFirstName());
        stmt.setString(2, user.getLastName());
        stmt.setString(3, user.getPassword());
        stmt.setString(4, user.getUsername());
        stmt.executeUpdate();
        connection.commit();
    }

    public UserAccount getUserByUsername(String username) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Users WHERE username = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        rs = stmt.executeQuery();
        connection.commit();

        if (rs.next()) {
            UserAccount user = new UserAccount();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setRole(UserRole.fromInt(rs.getInt("role")));
            return user;
        }

        return null;
    }

    public void deleteUser(String username) throws SQLException {
        PreparedStatement stmt = null;

        String sql = "DELETE FROM Users WHERE username = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.executeUpdate();
        connection.commit();
    }
}
