/*
 * @(#)RouteModelController.java 1.0 11.12.2016
 */

package ru.solpro.controller;

import ru.solpro.MainApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Контроллер для работы с моделью  <code>Route</code> (маршрут).
 * Является синглтоном.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class RouteModelController {
    /**
     * Переменная для хранения экземпляра RouteModelController.
     */
    private static RouteModelController instance;

    /**
     * Переменная для хранения экземпляра Database.
     * @see Database
     */
    private static Database database;

    private RouteModelController() {
        database = Database.getInstance();
    }

    /**
     * Получение текущего экземпляра.
     * @return  экземпляр RouteModelController
     */
    public static RouteModelController getInstance() {
        if (instance == null) {
            instance = new RouteModelController();
        }
        return instance;
    }

    public void addRoute(int idDepStation, int idArrStation) {
        String sql = "INSERT INTO `routes` (`dep_id`, `arr_id`) VALUES ("+ idDepStation + ", " + idArrStation + ");";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Станции не существует.");
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void viewRoute() {
        String sql = "SELECT t1.id, t2.name as 'dep', t3.name as 'arr' FROM routes t1 " +
                "JOIN stations t2 ON (t1.dep_id = t2.id) " +
                "JOIN stations t3 ON (t1.arr_id = t3.id);";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("[" + resultSet.getInt("id") + "] " + resultSet.getString("dep") + "->" + resultSet.getString("arr"));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void editRoute(int number, int idDep, int idArr) {
        String sql;
        if (idDep > 0 && idArr == 0) {
            sql = "UPDATE `itrain`.`routes` SET `dep_id`='" + idDep + "' WHERE `id`='" + number + "';";
        } else if (idDep == 0 && idArr > 0) {
            sql = "UPDATE `itrain`.`routes` SET `arr_id`='" + idArr + "' WHERE `id`='" + number + "';";
        } else {
            sql = "UPDATE `itrain`.`routes` SET `dep_id`='" + idDep + "', `arr_id`='" + idArr + "' WHERE `id`='" + number + "';";
        }
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void searchRoute(String strFind) {
        if (strFind.contains("*")) {
            strFind = strFind.replace("*", "%");
        }
        if (strFind.contains("?")) {
            strFind = strFind.replace("?", "_");
        }
        String sql = "select `t1`.`id`, `t2`.`name` as 'dep_name', `t3`.`name` as 'arr_name' " +
                "from `itrain`.`routes` t1 " +
                "join `itrain`.`stations` t2 on `t1`.`dep_id` = `t2`.`id` " +
                "join `itrain`.`stations` t3 on `t1`.`arr_id` = `t3`.`id` " +
                "where `t2`.`name` LIKE '" + strFind + "' OR `t3`.`name` LIKE '" + strFind + "';";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("[" + resultSet.getInt("id") + "] " + resultSet.getString("dep_name") + "->" + resultSet.getString("arr_name"));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void deleteRoute(int idRoute) {
        String sql = "delete from `routes` where `id` = '" + idRoute + "';";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Удаление не возможно. На маршрут ссылаются поезда.");
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }
}
