/*
 * @(#)ViewCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.*;
import ru.solpro.controller.TrainModelController;
import ru.solpro.controller.StationModelController;
import ru.solpro.model.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Команда просмотра.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class ViewCommand extends AlwaysCommand implements Command {

    /**
     * Выполнение команды.
     * @param args    аргументы
     * @return true - продолжить выполнение, false - завершить выполнение.
     * @throws SystemException  ошибка при работе пользователя с программой.
     * @throws IOException  ошибка ввыода/вывода
     */
    @Override
    public boolean execute(String[] args) throws SystemException, IOException{
        if (args == null) {
            printHelp();
            return true;
        }
        for (int i = 0; i < args.length; i++) {
            switch (args[i].toUpperCase()) {
                case "STATIONS":
                    viewStations();
                    break;
                case "ROUTES":
                    viewRoutes();
                    break;
                case "TRAINS":
                    viewTrains();
                    break;
                case "SCHEDULE":
                    if ((args.length == 2)) {
                        viewSchedule(Integer.parseInt(args[i+1]));
                        return true;
                    } else {
                        viewSchedule();
                    }
                    break;
                default:
                    System.out.print("Неверный аргумент у команды. ");
                    printHelp();
            }
        }
        return true;
    }

    /**
     * Распечатать справку по команде.
     */
    @Override
    public void printHelp() {
        getDescription();
        System.out.println("Данная команда позволяет просматривать данные в системе.");
        System.out.println("Список параметров команды:");
        System.out.println("STATIONS - выводит список станций.");
        System.out.println("ROUTES - выводит список маршрутов.");
        System.out.println("TRAINS - выводит список поездов.");
        System.out.println("SCHEDULE - выводит расписание на ближайшие 24 часа.");
        System.out.println("SCHEDULE [номер поезда]- выводит расписание определённого поезда.");
    }

    /**
     * Имя команды.
     * @return имя команды.
     */
    @Override
    public String getName() {
        return "VIEW";
    }

    /**
     * Описание команды.
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "Отображение данных системы.";
    }

    /**
     * Вывод списка всех станций в системе
     */
    private void viewStations() throws SystemException {
        String sql = "SELECT `id`, `name` FROM `stations`";
        Database database = new Database();
        database.connect();
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("[" + resultSet.getInt("id") + "] " + resultSet.getString("name"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.disconnect();
    }

    /**
     * Вывод списка всех маршрутов в системе
     */
    private void viewRoutes() throws SystemException {
        String sql = "SELECT t1.id, t2.name as 'dep', t3.name as 'arr' FROM routes t1 " +
                    "JOIN stations t2 ON (t1.dep_id = t2.id) " +
                    "JOIN stations t3 ON (t1.arr_id = t3.id);";
        Database database = new Database();

        database.connect();
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);

            while (resultSet.next()) {
                System.out.println("[" + resultSet.getInt("id") + "] " + resultSet.getString("dep") + "->" + resultSet.getString("arr"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.disconnect();
    }

    /**
     * Вывод списка всех поездов в системе
     */
    private void viewTrains() throws SystemException {
        String sql = "SELECT * FROM `trains`";
        Database database = new Database();

        database.connect();
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("[" + resultSet.getInt("id") + "] " + resultSet.getString("number"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.disconnect();
    }

    /**
     * Расписание всех поездов за ближайшие 24 часа
     */
    private void viewSchedule() throws SystemException {
        String sql = "SELECT " +
                "t1.id, " +
                "t2.number AS 'train', " +
                "t4.name AS 'dep', " +
                "t5.name AS 'arr', " +
                "t1.date as 'time_dep', " +
                "DATE_ADD(DATE_ADD(t1.date, INTERVAL t1.min MINUTE), INTERVAL t1.hour HOUR) as 'time_arr' " +
                "FROM " +
                "schedule t1 " +
                "JOIN " +
                "trains t2 ON t1.train_num = t2.number " +
                "JOIN " +
                "routes t3 ON t1.route_id = t3.id " +
                "JOIN " +
                "stations t4 ON t3.dep_id = t4.id " +
                "JOIN " +
                "stations t5 ON t3.arr_id = t5.id " +
                "WHERE " +
                "t1.date >= DATE_ADD(NOW(), INTERVAL 1 DAY);";
        Database database = new Database();

        database.connect();
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
            e.printStackTrace();
        }
        database.disconnect();
    }

    /**
     * Вывод расписания у определённого поезда
     * @param numberTrain   номер поезда
     */
    private void viewSchedule(int numberTrain) throws SystemException {
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
                "    trains t2 ON `t1`.`train_num` = `t2`.`number`\n" +
                "        JOIN\n" +
                "    routes t3 ON `t1`.`route_id` = `t3`.`id`\n" +
                "        JOIN\n" +
                "    stations t4 ON `t3`.`dep_id` = `t4`.`id`\n" +
                "        JOIN\n" +
                "    stations t5 ON `t3`.`arr_id` = `t5`.`id`\n" +
                "WHERE\n" +
                "    `t2`.`number` = '"+ numberTrain +"';";
        Database database = new Database();

        database.connect();
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
            e.printStackTrace();
        }
        database.disconnect();
    }
}
