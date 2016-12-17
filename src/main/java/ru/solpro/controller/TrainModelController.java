/*
 * @(#)TrainModelController.java 1.0 11.12.2016
 */

package ru.solpro.controller;

import ru.solpro.MainApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

/**
 * Контроллер для работы с моделью <code>Train</code> (поезд).
 * Является синглтоном.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class TrainModelController {

    /**
     * Переменная для хранения экземпляра TrainModelController.
     */
    private static TrainModelController ourInstance;

    /**
     * Переменная для хранения экземпляра Database.
     * @see Database
     */
    private static Database database;

    private TrainModelController() {
        database = Database.getInstance();
    }

    /**
     * Получение текущего экземпляра.
     * @return  Экземпляр TrainModelController
     */
    public static TrainModelController getInstance() {
        if (ourInstance == null) {
            ourInstance = new TrainModelController();
        }
        return ourInstance;
    }

    public void addTrain(int numberTrain) {
        String sql = "INSERT INTO `trains` (`number`) values ("+ numberTrain + ");";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Поезд с таким номером уже существует.");
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void deleteTrain(int numberTrain) {
        // оставить так?
        // или необходимо удалять расписание внутри метода
        // не пользуясь контроллером расписания?
        ScheduleModelController.getInstance().deleteScheduleTrain(getTrainId(numberTrain));

        String sql = "delete from `trains` where `number` = '" + numberTrain + "';";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void searchTrain(String strFind) {
        if (strFind.contains("*")) {
            strFind = strFind.replace("*", "%");
        }
        if (strFind.contains("?")) {
            strFind = strFind.replace("?", "_");
        }
        String sql = "SELECT * FROM `itrain`.`trains` WHERE `number` LIKE '" + strFind + "';";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                System.out.print("[" + resultSet.getInt("id") + "] " + resultSet.getString("number"));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void viewTrain() {
        String sql = "SELECT * FROM `trains`";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("[" + resultSet.getInt("id") + "] " + resultSet.getString("number"));
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
     * Получить номер маршрута у поезда.
     * @param numberTrain    номер поезда.
     * @return id маршрута. Если у поезда отсутствует маршрут, возвращает 0.
     */
    public int getRouteId(int numberTrain) {
        int result = 0;
        int trainId = getTrainId(numberTrain);
        String sql =  "select `route_id` from `schedule` where `train_id` = " + trainId + ";";

        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                result = resultSet.getInt("route_id");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
        return result;
    }

    /**
     * Получить id поезда.
     * @param numberTrain    номер поезда.
     * @return id поезда.
     */
    public int getTrainId(int numberTrain) {
        int idTrain = 0;
        String sql = "SELECT `id` FROM `itrain`.`trains` WHERE `number`='" + numberTrain + "';";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                idTrain = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
        return idTrain;
    }

    public void editTrainNumber(int idTrain, int newTrainNumber) {
        String sql = "UPDATE `itrain`.`trains` SET `number`='"+ newTrainNumber + "' WHERE `id`='" + idTrain + "';";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }
    }

    public void editTrainRoute(int idTrain, int idRoute, LocalDateTime depDateTime, int timeArrHours, int timeArrMinutes) {
        String sql;
        sql = "DELETE FROM `itrain`.`schedule` WHERE `train_id`='" + idTrain + "';";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }

        if (timeArrMinutes <= 0) {
            sql = "INSERT INTO `schedule` (`train_id`, `route_id`, `date`, `hour`) VALUES ('" + idTrain + "', '" + idRoute + "', '" + depDateTime + "', '" + timeArrHours + "');";
        } else {
            sql = "INSERT INTO `schedule` (`train_id`, `route_id`, `date`, `hour`, `min`) VALUES ('" + idTrain + "', '" + idRoute + "', '" + depDateTime + "', '" + timeArrHours + "', '" + timeArrMinutes + "');";
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
}
