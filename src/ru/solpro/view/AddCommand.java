/*
 * @(#)AddCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.*;
import ru.solpro.controller.TrainModelController;
import ru.solpro.controller.StationModelController;
import ru.solpro.model.Train;

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
public class AddCommand extends AlwaysCommand implements Command {

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
     * @throws SystemException
     * @throws IOException
     */
    private void addStation() throws SystemException, IOException {
        // INSERT INTO stations (name) VALUES ('ВОЛЬСК');

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StationModelController stationController = StationModelController.getInstance();

        System.out.println("Для завершения операции добавления введите exit.");
        while (true) {
            System.out.print("\tНаименование станции: ");
            String nameStation = reader.readLine();
            if (isExitOperation(nameStation)) {
                return;
            }
            if (stationController.search(nameStation).isEmpty() && stationController.add(nameStation)) {
                System.out.println("Станция успешно добавлена.");
            } else {
                error("Невозможно добавить станцию. Станция с таким названием существует.");
            }
        }
    }

    /**
     * Добавление нового маршрута
     * @throws SystemException
     * @throws IOException
     */
    private void addRoute() throws SystemException, IOException {
        //  insert into routes (dep_id, arr_id) values (2, 5);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        RouteModelController routeController = RouteModelController.getInstance();

        System.out.println("Для завершения операции добавления введите exit.");

        while (true) {
            System.out.print("\tid станции отправления: ");
            String s = reader.readLine();
            if (isExitOperation(s)) {
                return;
            }
            Integer idDepStation = Integer.parseInt(s);

            System.out.print("\tid станции назначения: ");
            String s2 = reader.readLine();
            if (isExitOperation(s2)) {
                return;
            }
            Integer idArrStation = Integer.parseInt(s2);

            if (routeController.add(idDepStation, idArrStation)) {
                System.out.println("Маршрут успешно добавлен.");
            } else {
                error("Невозможно добавить маршрут.");
            }
        }
    }

    /**
     * Добавление нового поезда
     * @throws SystemException
     * @throws IOException
     */
    private void addTrain() throws SystemException, IOException {
        //  insert into trains (number) values (1024);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TrainModelController trainModelController = TrainModelController.getInstance();

        System.out.println("Для завершения операции добавления введите exit.");

        while (true) {
            System.out.print("\tНомер поезда: ");
            String strNumberTrain = reader.readLine();
            if (isExitOperation(strNumberTrain)) {
                return;
            }
            Integer numberTrain = Integer.parseInt(strNumberTrain);

            if (trainModelController.search(numberTrain) == null &&
                    trainModelController.add(numberTrain)) {
                System.out.println("Поезд успешно добавлен.");
            } else {
                error("Невозможно добавить поезд.");
            }
        }
    }

    /**
     * Добавление расписания у определённого поезда
     * @throws SystemException
     * @throws IOException
     */
    private void addSchedule() throws SystemException, IOException {
        //  insert into schedule (train_id, route_id, date, hour) values (1, 1, '2016.12.13 21:01', 3);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        RouteModelController routeModelController = RouteModelController.getInstance();
        TrainModelController trainModelController = TrainModelController.getInstance();

        System.out.println("Для завершения операции добавления введите exit.");

        while (true) {
            System.out.print("\tНомер поезда: ");
            String strNumberTrain = reader.readLine();
            if (isExitOperation(strNumberTrain)) {
                return;
            }
            Integer numberTrain = Integer.parseInt(strNumberTrain);
            Train train = trainModelController.search(numberTrain);
            if (train == null) {
                error("Невозможно добавить расписание. Поезда с номером " +
                        numberTrain + " не существует.");
            }

            int routeId = 0;
            if (train.getTrainTimetable().isEmpty()) {
                System.out.print("\tУ данного поезда отсутствует маршрут. " +
                        "Введите id маршрута: ");
                routeId = Integer.parseInt(reader.readLine());

                if (routeModelController.search(routeId) == null) {
                    error("Маршрута не существует. Сначала создайте маршрут.");
                    return;
                }
            } else {
                routeId = train.getTrainTimetable().first().getRoute().getId();
            }

            System.out.print("\tДата отправления (dd.mm.yyyy): ");
            String strDateDep = reader.readLine();
            if (isExitOperation(strDateDep)) {
                return;
            }
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate dateDep = LocalDate.parse(strDateDep, dateFormatter);

            System.out.print("\tВремя отправления (hh:mm): ");
            String strTimeDep = reader.readLine();
            if (isExitOperation(strTimeDep)) {
                return;
            }
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime timeDep = LocalTime.parse(strTimeDep, timeFormatter);

            LocalDateTime depDateTime = LocalDateTime.of(dateDep, timeDep);

            System.out.print("\tВремя движения до конечного пункта (часов): ");
            String strTimeArrHours = reader.readLine();
            if (isExitOperation(strTimeArrHours)) {
                return;
            }
            Integer timeArrHours = Integer.parseInt(strTimeArrHours);
            System.out.print("\tВремя движения до конечного пункта (минут): ");
            String strTimeArrMinutes = reader.readLine();
            if ("".equals(strTimeArrMinutes)) {
                strTimeArrMinutes = "0";
            }
            Integer timeArrMinutes = Integer.parseInt(strTimeArrMinutes);

            if (timeArrMinutes == 0) {
                trainModelController.addScheduleLine(routeId,
                        numberTrain, depDateTime, timeArrHours);
                System.out.println("Расписание успешно добавлено.");
            } else {
                trainModelController.addScheduleLine(routeId,
                        numberTrain, depDateTime, timeArrHours, timeArrMinutes);
                System.out.println("Расписание успешно добавлено.");
            }
        }
    }

    //проверка на команду выхода из процесса добавления
    private boolean isExitOperation(String string) {
        return string.length() == 0 || "exit".equals(string) || "".equals(string);
    }
}
