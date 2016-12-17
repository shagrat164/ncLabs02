/*
 * @(#)AddCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Команда добавления данных.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class AddCommand implements Command {

    /**
     * Выполнение команды.
     * @param args    аргументы
     * @return true - продолжить выполнение, false - завершить выполнение.
     * @throws SystemException  ошибка при работе пользователя с программой.
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
                addStation();
                break;
            case "ROUTE":
                addRoute();
                break;
            case "TRAIN":
                addTrain();
                break;
            case "SCHEDULE":
                addSchedule();
                break;
            default:
                System.out.print("Неверный аргумент у команды. ");
                printHelp();
        }
        return true;
    }

    /**
     * Распечатать справку по команде.
     */
    @Override
    public void printHelp() {
        System.out.println("Данная команда позволяет добавлять данные в систему.");
        System.out.println("Список параметров команды:");
        System.out.println("STATION - добавление новой станций.");
        System.out.println("ROUTE - добавление нового маршрута.");
        System.out.println("TRAIN - добавление нового поезда.");
        System.out.println("SCHEDULE - добавление расписания у определённого поезда.");
    }

    /**
     * Имя команды.
     * @return имя команды.
     */
    @Override
    public String getName() {
        return "ADD";
    }

    /**
     * Описание команды.
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "Добавление информации";
    }

    /**
     * Добавление новой станции.
     * @throws IOException
     */
    private void addStation() throws IOException {
        StationModelController stationModelController = StationModelController.getInstance();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Для завершения операции добавления введите exit.");
        while (true) {
            System.out.print("\tНаименование станции: ");
            String nameStation = reader.readLine();
            if (isExitOperation(nameStation)) {
                break;
            }
            stationModelController.addStation(nameStation);
        }
    }

    /**
     * Добавление нового маршрута
     * @throws IOException
     */
    private void addRoute() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        RouteModelController routeModelController = RouteModelController.getInstance();

        System.out.println("Для завершения операции добавления введите exit.");

        while (true) {
            System.out.print("\tid станции отправления: ");
            String s = reader.readLine();
            if (isExitOperation(s)) {
                break;
            }
            Integer idDepStation = Integer.parseInt(s);

            System.out.print("\tid станции назначения: ");
            String s2 = reader.readLine();
            if (isExitOperation(s2)) {
                break;
            }
            Integer idArrStation = Integer.parseInt(s2);

            routeModelController.addRoute(idDepStation, idArrStation);
        }
    }

    /**
     * Добавление нового поезда
     * @throws SystemException
     * @throws IOException
     */
    private void addTrain() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TrainModelController trainModelController = TrainModelController.getInstance();

        System.out.println("Для завершения операции добавления введите exit.");
        while (true) {
            System.out.print("\tНомер поезда: ");
            String strNumberTrain = reader.readLine();
            if (isExitOperation(strNumberTrain)) {
                break;
            }
            Integer numberTrain = Integer.parseInt(strNumberTrain);

            trainModelController.addTrain(numberTrain);
        }
    }

    /**
     * Добавление расписания у определённого поезда
     * @throws SystemException
     * @throws IOException
     */
    private void addSchedule() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ScheduleModelController scheduleModelController = ScheduleModelController.getInstance();
        TrainModelController trainModelController = TrainModelController.getInstance();

        System.out.println("Для завершения операции добавления введите exit.");

        while (true) {
            System.out.print("\tНомер поезда: ");
            String strNumberTrain = reader.readLine();
            if (isExitOperation(strNumberTrain)) {
                break;
            }
            Integer numberTrain = Integer.parseInt(strNumberTrain);

            int trainId = trainModelController.getTrainId(numberTrain);
            if (trainId == 0) {
                System.out.println("Error: Поезда с таким номером нет в базе.");
                break;
            }

            int routeId = trainModelController.getRouteId(numberTrain);

            if (routeId == 0) {
                System.out.print("\tУ данного поезда отсутствует маршрут. " +
                        "Введите id маршрута: ");
                routeId = Integer.parseInt(reader.readLine());
            }

            System.out.print("\tДата отправления (dd.mm.yyyy): ");
            String strDateDep = reader.readLine();
            if (isExitOperation(strDateDep)) {
                break;
            }
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate dateDep = LocalDate.parse(strDateDep, dateFormatter);

            System.out.print("\tВремя отправления (hh:mm): ");
            String strTimeDep = reader.readLine();
            if (isExitOperation(strTimeDep)) {
                break;
            }
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime timeDep = LocalTime.parse(strTimeDep, timeFormatter);

            LocalDateTime depDateTime = LocalDateTime.of(dateDep, timeDep);

            System.out.print("\tВремя движения до конечного пункта (часов): ");
            String strTimeArrHours = reader.readLine();
            if (isExitOperation(strTimeArrHours)) {
                break;
            }
            Integer timeArrHours = Integer.parseInt(strTimeArrHours);

            System.out.print("\tВремя движения до конечного пункта (минут): ");
            String strTimeArrMinutes = reader.readLine();
            if ("".equals(strTimeArrMinutes)) {
                strTimeArrMinutes = "0";
            }
            Integer timeArrMinutes = Integer.parseInt(strTimeArrMinutes);

            scheduleModelController.addSchedule(trainId, routeId, depDateTime, timeArrHours, timeArrMinutes);
        }
    }

    //проверка на команду выхода из процесса добавления
    private boolean isExitOperation(String string) {
        return string.length() == 0 || "exit".equals(string) || "".equals(string);
    }
}
