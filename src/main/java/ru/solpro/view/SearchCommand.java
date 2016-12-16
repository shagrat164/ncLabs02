/*
 * @(#)SearchCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.*;
import ru.solpro.model.Train;
import ru.solpro.model.Route;
import ru.solpro.model.Station;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

/**
 * Команда поиска.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class SearchCommand implements Command {

    /**
     * Выполнение команды.
     * @param args    аргументы
     * @return true - продолжить выполнение, false - завершить выполнение.
     * @throws SystemException  ошибка при работе пользователя с программой.
     * @throws IOException  ошибка ввыода/вывода
     */
    @Override
    public boolean execute(String[] args) throws SystemException, IOException {
        if (args == null || args.length < 1 || args.length > 1) {
            printHelp();
            return true;
        }
        switch (args[0].toUpperCase()) {
            case "STATION":
                searchStation();
                break;
            case "ROUTE":
                searchRoute();
                break;
            case "TRAIN":
                searchTrain();
                break;
            default:
                printHelp();
        }
        return true;
    }

    /**
     * Распечатать справку по команде.
     */
    @Override
    public void printHelp() {
        System.out.println("Поддерживаются символы * и ?");
        System.out.println("Список параметров команды:");
        System.out.println("STATION - поиск станции.");
        System.out.println("ROUTE - поиск маршрута.");
        System.out.println("TRAIN - поиск поезда по его номеру.");
    }

    /**
     * Имя команды.
     * @return имя команды.
     */
    @Override
    public String getName() {
        return "SEARCH";
    }

    /**
     * Описание команды.
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "Поиск данных в системе.";
    }

	/**
	 * Поиск станции.
	 */
    private void searchStation() throws IOException {
        //  SELECT * FROM `itrain`.`stations` WHERE `name` LIKE 'ВОЛ_СК';

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StationModelController stationController = StationModelController.getInstance();

        System.out.print("\tВведите строку для поиска: ");
        String string = reader.readLine();
        ArrayList<Station> result = stationController.search(string);
        System.out.println("\tРезультат поиска:");
        if (result.isEmpty()) {
            System.out.println("\tНичего не найдено.");
            return;
        }
        for (Station station : result) {
            System.out.println("\t[" + station.getId() + "] " + station);
        }
    }

	/**
	 * Поиск маршрута.
	 */
    private void searchRoute() throws IOException {
        //  select `t1`.`id`, `t2`.`name`, `t3`.`name` from `itrain`.`routes` t1
        //  join `itrain`.`stations` t2 on `t1`.`dep_id` = `t2`.`id`
        //  join `itrain`.`stations` t3 on `t1`.`arr_id` = `t3`.`id`
        //  where `t2`.`name` LIKE 'САР%' OR `t3`.`name` LIKE '%ОВ%';

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        RouteModelController routeController = RouteModelController.getInstance();

        System.out.print("\tВведите строку для поиска: ");
        String string = reader.readLine();
        ArrayList<Route> result = routeController.search(string);
        System.out.println("\tРезультат поиска:");
        if (result.isEmpty()) {
            System.out.println("\tНичего не найдено.");
            return;
        }
        for (Route route : result) {
            System.out.println("\t[" + route.getId() + "] " + route);
        }
    }

	/**
	 * Поиск поезда.
	 */
    private void searchTrain() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Database database = new Database();
        database.connect();

        System.out.print("\tВведите номер поезда: ");
        String strFind = reader.readLine();
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
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }
}
