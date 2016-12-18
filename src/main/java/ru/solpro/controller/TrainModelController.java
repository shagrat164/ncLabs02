/*
 * @(#)TrainModelController.java 1.0 11.12.2016
 */

package ru.solpro.controller;

import ru.solpro.MainApp;
import ru.solpro.model.Route;
import ru.solpro.model.Train;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    /**
     * Добавить поезд.
     * @param numberTrain    номер поезда.
     */
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

    /**
     * Удаление поезда.
     * @param numberTrain    номер поезда.
     */
    public void deleteTrain(int numberTrain) {
        //TODO: оставить так?
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

    /**
     * Поиск поезда
     * @param strFind    строка поиска.
     * @return коллекция с поездами.
     */
    public ArrayList<Train> searchTrain(String strFind) {
        ArrayList<Train> result = new ArrayList<>();

        if (strFind.contains("*")) {
            strFind = strFind.replace("*", "%");
        }
        if (strFind.contains("?")) {
            strFind = strFind.replace("?", "_");
        }
        String sql = "SELECT * FROM `trains` WHERE `number` LIKE '" + strFind + "';";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                Train train = new Train();
                train.setId(resultSet.getInt("id"));
                train.setNumber(resultSet.getInt("number"));
                result.add(train);
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
     * Просмотр поездов.
     * @return коллекция с поездами и маршрутом.
     */
    public LinkedHashMap<Train, Route> viewTrain() {
        LinkedHashMap<Train, Route> result = new LinkedHashMap<>();
        String sql = "SELECT " +
                "t1.id, " +
                "t1.number, " +
                "t2.route_id, " +
                "t4.name as 'dep', " +
                "t5.name as 'arr' " +
                "FROM `trains` t1 " +
                "LEFT JOIN `schedule` t2 ON t1.id = t2.train_id " +
                "LEFT JOIN `routes` t3 ON t2.route_id = t3.id " +
                "LEFT JOIN `stations` t4 ON t3.dep_id = t4.id " +
                "LEFT JOIN `stations` t5 ON t3.arr_id = t5.id " +
                "GROUP BY t1.number;";

        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                Train train = new Train();
                Route route = new Route();

                train.setId(resultSet.getInt("id"));
                train.setNumber(resultSet.getInt("number"));

                if (resultSet.getString("dep") == null || resultSet.getString("arr") == null) {
                    route = null;
                } else {
                    route.setId(resultSet.getInt("route_id"));
                    route.setDep(resultSet.getString("dep"));
                    route.setArr(resultSet.getString("arr"));
                }

                result.put(train, route);
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
        String sql = "SELECT `id` FROM `trains` WHERE `number`='" + numberTrain + "';";
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

    /**
     * Изменение номера поезда.
     * @param idTrain           id поезда.
     * @param newTrainNumber    новый номер поезда.
     */
    public void editTrainNumber(int idTrain, int newTrainNumber) {
        String sql = "UPDATE `trains` SET `number`='"+ newTrainNumber + "' WHERE `id`='" + idTrain + "';";
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
     * Изменение маршрута у поезда.
     * @param idTrain           id поезда.
     * @param idRoute           id маршрута.
     * @param depDateTime       дата/время отправления.
     * @param timeArrHours      время движения (часы)
     * @param timeArrMinutes    время движения (минуты)
     */
    public void editTrainRoute(int idTrain,
                               int idRoute,
                               LocalDateTime depDateTime,
                               int timeArrHours,
                               int timeArrMinutes) {
        String sql;
        sql = "DELETE FROM `schedule` WHERE `train_id`='" + idTrain + "';";
        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            if (MainApp.DEBUG) {
                System.out.println(sql);
            }
        }

        if (timeArrMinutes <= 0) {
            sql = "INSERT INTO `schedule` (`train_id`, `route_id`, `date`, `hour`) " +
                    "VALUES ('" + idTrain + "', '" + idRoute + "', " +
                    "'" + depDateTime + "', '" + timeArrHours + "');";
        } else {
            sql = "INSERT INTO `schedule` (`train_id`, `route_id`, `date`, `hour`, `min`) " +
                    "VALUES ('" + idTrain + "', '" + idRoute + "', " +
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
}
