/*
 * @(#)StationModelController.java 1.0 11.12.2016
 */

package ru.solpro.controller;

import ru.solpro.MainApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Контроллер для работы с моделью <code>Station</code> (станция)
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class StationModelController {

    /**
     * Переменная для хранения экземпляра StationModelController.
     */
    private static StationModelController instance;

    /**
     * Переменная для хранения экземпляра Database.
     * @see Database
     */
    private static Database database;

    private StationModelController() {
        database = Database.getInstance();
    }

    /**
     * Получение текущего экземпляра.
     * @return  экземпляр StationModelController
     */
    public static StationModelController getInstance() {
        if (instance == null) {
            instance = new StationModelController();
        }
        return instance;
    }

    public void addStation(String nameStation) {
        String sql = "INSERT INTO stations (name) VALUES ('" + nameStation.toUpperCase() + "')";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Станция с таким названием существует.");
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void viewStation() {
        String sql = "SELECT `id`, `name` FROM `stations`";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("[" + resultSet.getInt("id") + "] " + resultSet.getString("name"));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void editStation(int idStation, String newNameStation) {
        String sql = "UPDATE `itrain`.`stations` SET `name`='" + newNameStation + "' WHERE `id`='" + idStation + "';";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void deleteStation(int idStation) {
        String sql = "delete from `stations` where `id` = '" + idStation + "';";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Удаление невозможно. Станция содержится в маршруте.");
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void searchStation(String strFind) {
        if (strFind.contains("*")) {
            strFind = strFind.replace("*", "%");
        }
        if (strFind.contains("?")) {
            strFind = strFind.replace("?", "_");
        }
        String sql = "SELECT * FROM `itrain`.`stations` WHERE `name` LIKE '" + strFind + "';";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                System.out.print("[" + resultSet.getInt("id") + "] " + resultSet.getString("name"));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }
}
