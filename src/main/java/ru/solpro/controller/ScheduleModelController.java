/*
 * @(#)ScheduleModelController.java 1.0 17.12.2016
 */

package ru.solpro.controller;

import ru.solpro.MainApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Created by danila on 17.12.2016.
 *
 * @author Protsvetov Danila
 * @version 1.0
 */
public class ScheduleModelController {

    /**
     * Переменная для хранения экземпляра ScheduleModelController.
     */
    private static ScheduleModelController ourInstance;

    /**
     * Переменная для хранения экземпляра Database.
     * @see Database
     */
    private static Database database;

    private ScheduleModelController() {
        database = Database.getInstance();
    }

    /**
     * Получение текущего экземпляра.
     * @return  экземпляр ScheduleModelController
     */
    public static ScheduleModelController getInstance() {
        if (ourInstance == null) {
            ourInstance = new ScheduleModelController();
        }
        return ourInstance;
    }

    /**
     * Добавление расписания в базу.
     * @param trainId           id поезда
     * @param routeId           id маршрута
     * @param depDateTime       дата и время отправления
     * @param timeArrHours      время движения (часов)
     * @param timeArrMinutes    время движения (минут)
     */
    public void addSchedule(int trainId, int routeId, LocalDateTime depDateTime, int timeArrHours, int timeArrMinutes) {
        String sql;
        if (timeArrMinutes <= 0) {
            sql = "insert into `schedule` (`train_id`, `route_id`, `date`, `hour`) values ('" + trainId + "', '" + routeId + "', '" + depDateTime + "', '" + timeArrHours + "');";
        } else {
            sql = "insert into `schedule` (`train_id`, `route_id`, `date`, `hour`, `min`) values ('" + trainId + "', '" + routeId + "', '" + depDateTime + "', '" + timeArrHours + "', '" + timeArrMinutes + "');";
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

    /**
     * Удаление расписания по id.
     * @param idSchedule    id расписания.
     */
    public void deleteSchedule(int idSchedule) {
        String sql = "delete from `schedule` where `id` = '" + idSchedule + "';";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    /**
     * Удаление всего расписания у поезда.
     * @param idTrain    id поезда
     */
    public void deleteScheduleTrain(int idTrain) {
        String sql = "delete from `schedule` where `train_id` = '" + idTrain + "';";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    /**
     * Расписание всех поездов за ближайшие 24 часа
     */
    public void viewSchedule() {
        String sql = "SELECT \n" +
                "    t1.id,\n" +
                "    t2.number AS 'train',\n" +
                "    t4.name AS 'dep',\n" +
                "    t5.name AS 'arr',\n" +
                "    t1.date AS 'time_dep',\n" +
                "    DATE_ADD(DATE_ADD(t1.date,\n" +
                "            INTERVAL t1.min MINUTE),\n" +
                "        INTERVAL t1.hour HOUR) AS 'time_arr'\n" +
                "FROM\n" +
                "    schedule t1\n" +
                "        JOIN\n" +
                "    trains t2 ON t1.train_id = t2.id\n" +
                "        JOIN\n" +
                "    routes t3 ON t1.route_id = t3.id\n" +
                "        JOIN\n" +
                "    stations t4 ON t3.dep_id = t4.id\n" +
                "        JOIN\n" +
                "    stations t5 ON t3.arr_id = t5.id\n" +
                "WHERE\n" +
                "    t1.date >= NOW()\n" +
                "        AND t1.date <= DATE_ADD(NOW(), INTERVAL 1 DAY)\n" +
                "ORDER BY t1.date;";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("[" + resultSet.getInt("id") + "] " +
                        "поезд " + resultSet.getInt("train") + " " +
                        resultSet.getString("dep") + "->" +
                        resultSet.getString("arr") + " \t" +
                        resultSet.getString("time_dep") + " " +
                        resultSet.getString("time_arr"));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    /**
     * Вывод расписания у определённого поезда
     * @param numberTrain   номер поезда
     */
    public void viewSchedule(int numberTrain) {
        String sql = "SELECT \n" +
                "    `t1`.`id`,\n" +
                "    `t2`.`number` AS 'train',\n" +
                "    `t4`.`name` AS 'dep',\n" +
                "    `t5`.`name` AS 'arr',\n" +
                "    `t1`.`date` AS 'time_dep',\n" +
                "    DATE_ADD(DATE_ADD(t1.date, INTERVAL t1.min MINUTE), INTERVAL t1.hour HOUR) AS 'time_arr'\n" +
                "FROM\n" +
                "    schedule t1\n" +
                "        JOIN\n" +
                "    trains t2 ON `t1`.`train_id` = `t2`.`id`\n" +
                "        JOIN\n" +
                "    routes t3 ON `t1`.`route_id` = `t3`.`id`\n" +
                "        JOIN\n" +
                "    stations t4 ON `t3`.`dep_id` = `t4`.`id`\n" +
                "        JOIN\n" +
                "    stations t5 ON `t3`.`arr_id` = `t5`.`id`\n" +
                "WHERE\n" +
                "    `t2`.`number` = '"+ numberTrain +"'" +
                "ORDER BY t1.date;";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("[" + resultSet.getInt("id") + "] " +
                        "поезд " + resultSet.getInt("train") + " " +
                        resultSet.getString("dep") + "->" +
                        resultSet.getString("arr") + " " +
                        resultSet.getString("time_dep") + " " +
                        resultSet.getString("time_arr"));
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
