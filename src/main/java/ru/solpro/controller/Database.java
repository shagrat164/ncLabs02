/*
 * Database.java v1.0 14.12.2016
 */
package ru.solpro.controller;

import java.sql.*;

/**
 * Класс-контроллер для работы с базой данных MySQL.
 * @version 1.0
 * @author Protsvetov Danila
 */
public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/itrain?" +
            "useUnicode=true&" +
            "useSSL=true&" +
            "useJDBCCompliantTimezoneShift=true&" +
            "useLegacyDatetimeCode=false&" +
            "serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public Database() {
        connection = null;
        statement = null;
        resultSet = null;
    }

    /**
     * Метод соединения с базой данных.
     */
    public void dbConnect() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод отсоединения от базы данных.
     */
    public void dbDisconnect() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Выполнение SQL запроса.
     * @param SQL
     *        Строка запроса SQL
     * @return Тип ResultSet.
     */
    public ResultSet execSQL (String SQL) {
        try {
            resultSet = statement.executeQuery(SQL);
            return resultSet;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }
}