package com.star.webssh.config;

import java.sql.*;

public class HiveTest {
    public static void cs() throws SQLException {
        String url = "jdbc:hive2://localhost:10000/ssh_project";
        String user = "hive";
        String password = "hive";
        String query = "SELECT * FROM ssh_server LIMIT 10";

        Connection conn = DriverManager.getConnection(url, user, password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
        conn.close();
    }

    public static void main(String[] args) throws Exception {
        cs();
    }

}
