/*
 * @(#)Database.java 1.0 11.12.2016
 */

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
    private static Database instance;

    private Connection connection;
    private Statement statement;

    private Database() {
        connect();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Метод соединения с базой данных.
     */
    public void connect() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
            if (statement == null) {
                statement = connection.createStatement();
            }
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