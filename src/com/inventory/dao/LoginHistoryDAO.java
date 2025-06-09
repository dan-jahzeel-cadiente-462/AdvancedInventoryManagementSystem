package com.inventory.dao;

import com.inventory.model.LoginHistory;
import com.inventory.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginHistoryDAO {

    public void insertLogin(int userId) throws SQLException {
        String sql = "INSERT INTO LoginHistory (UserID, LoginTime) VALUES (?, NOW())";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }

    public void updateLogout(int userId) throws SQLException {
        String sql = "UPDATE LoginHistory SET LogoutTime = NOW() WHERE UserID = ? AND LogoutTime IS NULL";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }

    public List<LoginHistory> getLoginHistoryByUserId(int userId) throws SQLException {
        List<LoginHistory> historyList = new ArrayList<>();
        String sql = "SELECT LoginTime, LogoutTime FROM LoginHistory WHERE UserID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LoginHistory history = new LoginHistory();
                history.setLoginTime(rs.getTimestamp("LoginTime"));
                history.setLogoutTime(rs.getTimestamp("LogoutTime"));
                historyList.add(history);
            }
        }
        return historyList;
    }
}