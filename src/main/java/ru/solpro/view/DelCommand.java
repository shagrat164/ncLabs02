/*
 * @(#)DelCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.*;
import ru.solpro.model.Train;
import ru.solpro.model.Route;
import ru.solpro.model.Station;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Команда удаления данных
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class DelCommand extends AlwaysCommand implements Command {

    /**
     * Выполнение команды.
     * @param args    аргументы
     * @return true - продолжить выполнение, false - завершить выполнение.
     * @throws SystemException  ошибка при работе пользователя с программой.
     * @throws IOException  ошибка ввыода/вывода
     */
    @Override
    public boolean execute(String[] args) throws IOException, SystemException {
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
     */
    private void delSchedule() throws IOException, SystemException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Database database = new Database();
        database.connect();

        System.out.print("\tВведите id расписания: ");
        int idSchedule = Integer.parseInt(reader.readLine());
        String sql = "delete from `schedule` where `id` = '" + idSchedule + "';";

        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        database.disconnect();
    }

    /**
     * Удаление поезда
     */
    private void delTrain() throws IOException, SystemException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Database database = new Database();
        database.connect();

        System.out.print("\tВведите номер поезда: ");
        int numberTrain = Integer.parseInt(reader.readLine());
        String sql = "delete from `trains` where `number` = '" + numberTrain + "';";

        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        database.disconnect();
    }

    /**
     * Удаление маршрута
     */
    private void delRoute() throws IOException, SystemException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Database database = new Database();
        database.connect();

        System.out.print("Введите id маршрута который требуется удалить: ");
        int idRoute = Integer.parseInt(reader.readLine());

        String sql = "delete from `routes` where `id` = '" + idRoute + "';";

        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        database.disconnect();
    }

    /**
     * удаление станции
     */
    private void delStation() throws IOException, SystemException {
        //  delete from stations where id = 7;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Database database = new Database();
        database.connect();

        System.out.print("Введите id станции которую требуется удалить: ");
        int idStation = Integer.parseInt(reader.readLine());

        String sql = "delete from `stations` where `id` = '" + idStation + "';";

        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        database.disconnect();
    }
}
