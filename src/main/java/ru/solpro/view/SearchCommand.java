/*
 * @(#)SearchCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

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
     * @throws IOException  ошибка ввыода/вывода
     */
    @Override
    public boolean execute(String[] args) throws IOException {
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StationModelController stationModelController = StationModelController.getInstance();

        System.out.print("\tВведите строку для поиска: ");
        String strFind = reader.readLine();

        stationModelController.searchStation(strFind);
    }

	/**
	 * Поиск маршрута.
	 */
    private void searchRoute() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        RouteModelController routeModelController = RouteModelController.getInstance();

        System.out.print("\tВведите строку для поиска: ");
        String strFind = reader.readLine();

        routeModelController.searchRoute(strFind);
    }

	/**
	 * Поиск поезда.
	 */
    private void searchTrain() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TrainModelController trainModelController = TrainModelController.getInstance();

        System.out.print("\tВведите номер поезда: ");
        String strFind = reader.readLine();

        trainModelController.searchTrain(strFind);
    }
}
