package org.projeti.Service;

import org.projeti.entites.Wallet;

import java.sql.*;

public class WalletService {
    private Connection connection;

    public WalletService(Connection connection) {
        this.connection = connection;
    }

    public void addScore(int userId, float amount) throws SQLException {
        String query = "INSERT INTO wallet (user_id, score) VALUES (?, ?) ON DUPLICATE KEY UPDATE score = score + ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setFloat(2, amount);
            ps.setFloat(3, amount);
            ps.executeUpdate();
        }
    }

    public Wallet getWallet(int userId) throws SQLException {
        String query = "SELECT * FROM wallet WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Wallet(userId, rs.getFloat("score"));
            }
        }
        return new Wallet(userId, 0);
    }

    public void updateWallet(Wallet wallet) throws SQLException {
        String query = "UPDATE wallet SET score = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setFloat(1, wallet.getScore());
            ps.setInt(2, wallet.getUserId());
            ps.executeUpdate();
        }
    }
}