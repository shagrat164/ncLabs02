/*
 * @(#)SearchCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.*;
import ru.solpro.model.Route;
import ru.solpro.model.Station;
import ru.solpro.model.Train;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

        ArrayList<Station> result = stationModelController.searchStation(strFind);

        if (!result.isEmpty()) {
            for (Station station : result) {
                System.out.println(station);
            }
        } else {
            System.out.println("По запросу ничего не найдено.");
        }
    }

	/**
	 * Поиск маршрута.
	 */
    private void searchRoute() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        RouteModelController routeModelController = RouteModelController.getInstance();

        System.out.print("\tВведите строку для поиска: ");
        String strFind = reader.readLine();

        ArrayList<Route> result = routeModelController.searchRoute(strFind);

        if (!result.isEmpty()) {
            for (Route route : result) {
                System.out.println(route);
            }
        } else {
            System.out.println("По запросу ничего не найдено.");
        }
    }

	/**
	 * Поиск поезда.
	 */
    private void searchTrain() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TrainModelController trainModelController = TrainModelController.getInstance();

        System.out.print("\tВведите номер поезда: ");
        String strFind = reader.readLine();

        ArrayList<Train> result = trainModelController.searchTrain(strFind);

        if (!result.isEmpty()) {
            for (Train train : result) {
                System.out.println(train);
            }
        } else {
            System.out.println("По запросу ничего не найдено.");
        }
    }
}
