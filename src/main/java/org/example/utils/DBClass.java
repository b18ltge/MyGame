package org.example.utils;

import java.sql.*;
import java.util.Iterator;

public class DBClass {
    private static final String url = "jdbc:postgresql://localhost/GameDataBase";
    private static final String user = "game_role";
    private static final String password = "@Agent023";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return conn;
    }

    public static boolean containsUser(String login, String password) {
        var connection = getConnection();
        var sql = "SELECT id FROM users where login = ? and password = ?";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            statement.setString(1, login);
            statement.setString(2, password);
            var resultSet = statement.executeQuery();
            connection.commit();
            return resultSet.next();
        } catch (Exception exception) {
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            exception.printStackTrace();
        }
        return false;
    }
}
