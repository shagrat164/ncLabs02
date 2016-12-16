package ru.solpro.controller;

import java.sql.*;

/**
 * Класс-контроллер для работы с базой данных MySQL.
 * @version 1.0
 * @author Protsvetov Danila
 */
public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/itrain" +
            "?useUnicode=true" +
            "&useSSL=true" +
            "&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false" +
            "&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Connection connection;
    private Statement statement;

    public Database() {}

    /**
     * Метод соединения с базой данных.
     */
    public void connect() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            System.out.println("db connect");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод отсоединения от базы данных.
     */
    public void disconnect() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
                System.out.println("db disconnect");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Statement getStatement() {
        return statement;
    }
}