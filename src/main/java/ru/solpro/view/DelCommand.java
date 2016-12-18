/*
 * @(#)DelCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Команда удаления данных
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class DelCommand implements Command {

    /**
     * Выполнение команды.
     * @param args    аргументы
     * @return true - продолжить выполнение, false - завершить выполнение.
     * @throws IOException  ошибка ввыода/вывода
     */
    @Override
    public boolean execute(String[] args) throws IOException {
        if (args == null || args.length < 1 || args.length > 1) {
            System.out.println("Неверный аргумент у команды.");
            printHelp();
            return true;
        }
        switch (args[0].toUpperCase()) {
            case "STATION":
                delStation();
                break;
            case "ROUTE":
                delRoute();
                break;
            case "TRAIN":
                delTrain();
                break;
            case "SCHEDULE":
                delSchedule();
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
        System.out.println("Данная команда позволяет удалять данные из системы.");
        System.out.println("Список параметров команды:");
        System.out.println("STATION - удаление станции.");
        System.out.println("ROUTE - удаление маршрута.");
        System.out.println("TRAIN - удаление поезда.");
        System.out.println("SCHEDULE - удаление расписания у определённого поезда.");
    }

    /**
     * Имя команды.
     * @return имя команды.
     */
    @Override
    public String getName() {
        return "DEL";
    }

    /**
     * Описание команды.
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "Удаление информации";
    }

    /**
     * Удаление расписания у поезда
     * @throws IOException ошибка ввыода/вывода
     */
    private void delSchedule() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ScheduleModelController scheduleModelController = ScheduleModelController.getInstance();

        System.out.print("\tВведите id расписания: ");
        int idSchedule = Integer.parseInt(reader.readLine());

        scheduleModelController.deleteSchedule(idSchedule);
    }

    /**
     * Удаление поезда
     * @throws IOException ошибка ввыода/вывода
     */
    private void delTrain() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TrainModelController trainModelController = TrainModelController.getInstance();

        System.out.print("\tВведите номер поезда: ");
        int numberTrain = Integer.parseInt(reader.readLine());

        trainModelController.deleteTrain(numberTrain);
    }

    /**
     * Удаление маршрута
     * @throws IOException ошибка ввыода/вывода
     */
    private void delRoute() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        RouteModelController routeModelController = RouteModelController.getInstance();

        System.out.print("Введите id маршрута который требуется удалить: ");
        int idRoute = Integer.parseInt(reader.readLine());

        routeModelController.deleteRoute(idRoute);
    }

    /**
     * удаление станции
     * @throws IOException ошибка ввыода/вывода
     */
    private void delStation() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StationModelController stationModelController = StationModelController.getInstance();

        System.out.print("Введите id станции которую требуется удалить: ");
        int idStation = Integer.parseInt(reader.readLine());

        stationModelController.deleteStation(idStation);
    }
}
