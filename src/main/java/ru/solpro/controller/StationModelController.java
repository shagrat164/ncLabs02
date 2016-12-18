/*
 * @(#)StationModelController.java 1.0 11.12.2016
 */

package ru.solpro.controller;

import ru.solpro.MainApp;
import ru.solpro.model.Station;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

/**
 * Контроллер для работы с моделью <code>Station</code> (станция)
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class StationModelController {

    /**
     * Переменная для хранения экземпляра StationModelController.
     */
    private static StationModelController ourInstance;

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
        if (ourInstance == null) {
            ourInstance = new StationModelController();
        }
        return ourInstance;
    }

    public void addStation(String name) {
        String sql = "INSERT INTO stations (name) " +
                "VALUES ('" + name.toUpperCase() + "')";
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

    public ArrayList<Station> viewStation() {
        ArrayList<Station> result = new ArrayList<>();
        String sql = "SELECT `id`, `name` FROM `stations`";

        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                Station station = new Station();
                station.setId(resultSet.getInt("id"));
                station.setName(resultSet.getString("name"));
                result.add(station);
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
        return result;
    }

    public void editStation(int id, String name) {
        String sql = "UPDATE `stations` SET `name`='" + name + "' WHERE `id`='" + id + "';";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void deleteStation(int id) {
        String sql = "delete from `stations` where `id` = '" + id + "';";
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

    public ArrayList<Station> searchStation(String stringFind) {
        ArrayList<Station> result = new ArrayList<>();

        if (stringFind.contains("*")) {
            stringFind = stringFind.replace("*", "%");
        }
        if (stringFind.contains("?")) {
            stringFind = stringFind.replace("?", "_");
        }

        String sql = "SELECT * FROM `stations` WHERE `name` LIKE '" + stringFind + "';";

        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                Station station = new Station();
                station.setId(resultSet.getInt("id"));
                station.setName(resultSet.getString("name"));
                result.add(station);
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }

        return result;
    }
}
