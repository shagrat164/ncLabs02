/*
 * @(#)ScheduleModelController.java 1.0 17.12.2016
 */

package ru.solpro.controller;

import ru.solpro.MainApp;
import ru.solpro.model.Schedule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
    public void addSchedule(int trainId,
                            int routeId,
                            LocalDateTime depDateTime,
                            int timeArrHours,
                            int timeArrMinutes) {
        String sql;
        if (timeArrMinutes <= 0) {
            sql = "INSERT INTO `schedule` (`train_id`, `route_id`, `date`, `hour`) " +
                    "VALUES ('" + trainId + "', '" + routeId + "', " +
                    "'" + depDateTime + "', '" + timeArrHours + "');";
        } else {
            sql = "INSERT INTO `schedule` (`train_id`, `route_id`, `date`, `hour`, `min`) " +
                    "VALUES ('" + trainId + "', '" + routeId + "', " +
                    "'" + depDateTime + "', '" + timeArrHours + "', '" + timeArrMinutes + "');";
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
        String sql = "DELETE FROM `schedule` " +
                "WHERE `id` = '" + idSchedule + "';";
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
        String sql = "DELETE FROM `schedule` " +
                "WHERE `train_id` = '" + idTrain + "';";
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
    public ArrayList<Schedule> viewSchedule() {
        ArrayList<Schedule> result = new ArrayList<>();
        String sql = "SELECT " +
                "t1.id, " +
                "t2.number AS 'train', " +
                "t4.name AS 'dep', " +
                "t5.name AS 'arr', " +
                "t1.date AS 'time_dep', " +
                "DATE_ADD(DATE_ADD(t1.date, INTERVAL t1.min MINUTE), INTERVAL t1.hour HOUR) AS 'time_arr' " +
                "FROM schedule t1 " +
                "JOIN trains t2 ON t1.train_id = t2.id " +
                "JOIN routes t3 ON t1.route_id = t3.id " +
                "JOIN stations t4 ON t3.dep_id = t4.id " +
                "JOIN stations t5 ON t3.arr_id = t5.id " +
                "WHERE t1.date >= NOW() AND t1.date <= DATE_ADD(NOW(), INTERVAL 1 DAY) " +
                "ORDER BY t1.date;";

        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(resultSet.getInt("id"));
                schedule.setNumbeTrain(resultSet.getInt("train"));
                schedule.setDep(resultSet.getString("dep"));
                schedule.setArr(resultSet.getString("arr"));
                schedule.setTimeDep(resultSet.getString("time_dep"));
                schedule.setTimeArr(resultSet.getString("time_arr"));
                result.add(schedule);
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

    /**
     * Вывод расписания у определённого поезда
     * @param numberTrain   номер поезда
     */
    public ArrayList<Schedule> viewSchedule(int numberTrain) {
        ArrayList<Schedule> result = new ArrayList<>();
        String sql = "SELECT " +
                "t1.id, " +
                "t2.number AS 'train', " +
                "t4.name AS 'dep', " +
                "t5.name AS 'arr', " +
                "t1.date AS 'time_dep', " +
                "DATE_ADD(DATE_ADD(t1.date, INTERVAL t1.min MINUTE), INTERVAL t1.hour HOUR) AS 'time_arr' " +
                "FROM schedule t1 " +
                "JOIN trains t2 ON t1.train_id = t2.id " +
                "JOIN routes t3 ON t1.route_id = t3.id " +
                "JOIN stations t4 ON t3.dep_id = t4.id " +
                "JOIN stations t5 ON t3.arr_id = t5.id " +
                "WHERE t2.number = '"+ numberTrain +"'" +
                "ORDER BY t1.date;";

        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(resultSet.getInt("id"));
                schedule.setNumbeTrain(resultSet.getInt("train"));
                schedule.setDep(resultSet.getString("dep"));
                schedule.setArr(resultSet.getString("arr"));
                schedule.setTimeDep(resultSet.getString("time_dep"));
                schedule.setTimeArr(resultSet.getString("time_arr"));
                result.add(schedule);
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
