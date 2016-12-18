/*
 * @(#)ViewCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.*;
import ru.solpro.model.Route;
import ru.solpro.model.Schedule;
import ru.solpro.model.Station;
import ru.solpro.model.Train;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Команда просмотра.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class ViewCommand implements Command {

    /**
     * Выполнение команды.
     * @param args    аргументы
     * @return true - продолжить выполнение, false - завершить выполнение.
     * @throws IOException  ошибка ввыода/вывода
     */
    @Override
    public boolean execute(String[] args) throws IOException{
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
    private void viewStations() {
        StationModelController stationModelController = StationModelController.getInstance();
        ArrayList<Station> result = stationModelController.viewStation();

        if (result.isEmpty()) {
            System.out.println("Не определено ни одной станции.");
        } else {
            for (Station station : result) {
                System.out.println(station);
            }
        }
    }

    /**
     * Вывод списка всех маршрутов в системе
     */
    private void viewRoutes() {
        RouteModelController routeModelController = RouteModelController.getInstance();
        ArrayList<Route> result = routeModelController.viewRoute();

        if (result.isEmpty()) {
            System.out.println("Не определено ни одного маршрута.");
        } else {
            for (Route route : result) {
                System.out.println(route);
            }
        }
    }

    /**
     * Вывод списка всех поездов в системе
     */
    private void viewTrains() {
        TrainModelController trainModelController = TrainModelController.getInstance();
        LinkedHashMap<Train, Route> result = trainModelController.viewTrain();

        if (result.isEmpty()) {
            System.out.println("Не определено ни одного поезда.");
        } else {
            for (Map.Entry<Train, Route> trainRouteEntry : result.entrySet()) {
                System.out.print(trainRouteEntry.getKey());
                if (trainRouteEntry.getValue() != null) {
                    System.out.println("\t" + trainRouteEntry.getValue());
                } else {
                    System.out.println("\tМаршрут не определён.");
                }
            }
        }
    }

    /**
     * Расписание всех поездов за ближайшие 24 часа
     */
    private void viewSchedule() {
        ScheduleModelController scheduleModelController = ScheduleModelController.getInstance();
        ArrayList<Schedule> result = scheduleModelController.viewSchedule();

        if (result.isEmpty()) {
            System.out.println("Расписания нет.");
        } else {
            for (Schedule schedule : result) {
                System.out.println(schedule);
            }
        }
    }

    /**
     * Вывод расписания у определённого поезда
     * @param numberTrain   номер поезда
     */
    private void viewSchedule(int numberTrain) {
        ScheduleModelController scheduleModelController = ScheduleModelController.getInstance();
        ArrayList<Schedule> result = scheduleModelController.viewSchedule(numberTrain);

        if (result.isEmpty()) {
            System.out.println("Расписания нет.");
        } else {
            for (Schedule schedule : result) {
                System.out.println(schedule);
            }
        }
    }
}
