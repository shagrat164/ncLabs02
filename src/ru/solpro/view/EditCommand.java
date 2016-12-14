/*
 * @(#)EditCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.TrainModelController;
import ru.solpro.controller.RouteModelController;
import ru.solpro.controller.StationModelController;
import ru.solpro.controller.SystemException;
import ru.solpro.model.Train;
import ru.solpro.model.Route;
import ru.solpro.model.Schedule;
import ru.solpro.model.Station;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Команда редактирования.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class EditCommand extends AlwaysCommand implements Command {

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
            System.out.println("Неверный аргумент у команды.");
            printHelp();
            return true;
        }
        switch (args[0].toUpperCase()) {
            case "STATION":
                editStation();
                break;
            case "ROUTE":
                editRoute();
                break;
            case "TRAIN":
                editTrain();
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
        System.out.println("Данная команда позволяет изменять данные в системе.");
        System.out.println("Список параметров команды:");
        System.out.println("STATION - изменение станции.");
        System.out.println("ROUTE - изменение маршрута.");
        System.out.println("TRAIN - изменение поезда.");
    }

    /**
     * Имя команды.
     * @return имя команды.
     */
    @Override
    public String getName() {
        return "EDIT";
    }

    /**
     * Описание команды.
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "Редактирование данных.";
    }

    private void editTrain() throws IOException, SystemException {
        // получить id записи номера поезда:
        // SELECT `id` FROM `itrain`.`trains` WHERE `number`='1030';
        // обновить номер поезда:
        // UPDATE `itrain`.`trains` SET `number`='1031' WHERE `id`='11';

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        RouteModelController routeModelController = RouteModelController.getInstance();
        TrainModelController trainModelController = TrainModelController.getInstance();

        System.out.print("\tВведите номер поезда для редактирования: ");
        Integer numberTrain = Integer.parseInt(reader.readLine());
        Train editTrain = trainModelController.search(numberTrain);
        if (editTrain == null) {
            error("Не найден поезд для редактирования.");
        }
        System.out.println("\tВыбран поезд: " + editTrain);

        System.out.print("\tВведите новый номер поезда " +
                "(если изменять не нужно, оставте поле пустым): ");
        String str1 = reader.readLine();
        Integer newNumberTrain;
        if ("".equals(str1)) {
            newNumberTrain = editTrain.getTrainNumber();
        } else {
            newNumberTrain = Integer.parseInt(str1);
        }

        System.out.print("\tВведите новый id маршрута " +
                "(если изменять не нужно, оставте поле пустым): ");
        String str2 = reader.readLine();
        Integer newRouteId;
        if ("".equals(str2)) {
            newRouteId = editTrain.getTrainTimetable().last().getRoute().getId();
        } else {
            newRouteId = Integer.parseInt(str2);
            Route route = routeModelController.search(newRouteId);
            if (route == null) {
                error("Маршрут не найден.");
            }
            editTrain.clearTrainTimetable();
            editTrainRoute(editTrain, route);
        }

        for (Schedule schedule : editTrain.getTrainTimetable()) {
            schedule.setRoute(routeModelController.search(newRouteId));
        }
        editTrain.setTrainNumber(newNumberTrain);
    }

    /**
     * Меняет маршрут и добавляет первую дату в расписание.
     * @throws IOException
     * @throws SystemException
     */
    private void editTrainRoute(Train train,
                                Route routeId) throws IOException, SystemException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TrainModelController trainModelController = TrainModelController.getInstance();

        System.out.println("\tПосле изменения маршрута " +
                "необходимо добавить запись в расписание.");
        System.out.print("\tДата отправления (dd.mm.yyyy): ");
        String strDateDep = reader.readLine();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateDep = LocalDate.parse(strDateDep, dateFormatter);

        System.out.print("\tВремя отправления (hh:mm): ");
        String strTimeDep = reader.readLine();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime timeDep = LocalTime.parse(strTimeDep, timeFormatter);
        LocalDateTime depDateTime = LocalDateTime.of(dateDep, timeDep);

        System.out.print("\tВремя движения до конечного пункта (часов): ");
        String strTimeArrHours = reader.readLine();
        Integer timeArrHours = Integer.parseInt(strTimeArrHours);

        System.out.print("\tВремя движения до конечного пункта (минут): ");
        String strTimeArrMinutes = reader.readLine();
        if ("".equals(strTimeArrMinutes)) {
            strTimeArrMinutes = "0";
        }
        Integer timeArrMinutes = Integer.parseInt(strTimeArrMinutes);

        if (timeArrMinutes == 0) {
            trainModelController.addScheduleLine(routeId.getId(),
                    train.getTrainNumber(), depDateTime, timeArrHours);
        } else {
            trainModelController.addScheduleLine(routeId.getId(),
                    train.getTrainNumber(), depDateTime, timeArrHours, timeArrMinutes);
        }
        System.out.println("Расписание успешно добавлено.");
    }

	/**
	 * Редактирование маршрута.
	 */
    private void editRoute() throws IOException, SystemException {
        //  #UPDATE `itrain`.`routes` SET `dep_id`='2' WHERE `id`='3';
        //  #UPDATE `itrain`.`routes` SET `dep_id`='3', `arr_id`='8' WHERE `id`='3';

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StationModelController stationModelController = StationModelController.getInstance();
        RouteModelController routeModelController = RouteModelController.getInstance();

        System.out.print("\tВведите id маршрута для редактирования: ");
        Integer number = Integer.parseInt(reader.readLine());
        Route editRoute = routeModelController.search(number);
        if (editRoute == null) {
            error("Не найден маршрут для редактирования.");
        }

        System.out.print("\tВведите новый id станции отправления " +
                "(если изменять не нужно, оставте поле пустым): ");
        String str1 = reader.readLine();
        Integer newIdDepSt;
        if ("".equals(str1)) {
            newIdDepSt = editRoute.getIdDeparture();
        } else {
            newIdDepSt = Integer.parseInt(str1);
        }
        Station newDepSt = stationModelController.search(newIdDepSt);
        if (newDepSt == null) {
            error("Станция с id=" + newIdDepSt + " не найдена.");
        }

        System.out.print("\tВведите новый id станции назначения " +
                "(если изменять не нужно, оставте поле пустым): ");
        String str2 = reader.readLine();
        Integer newIdArrSt;
        if ("".equals(str2)) {
            newIdArrSt = editRoute.getIdArrival();
        } else {
            newIdArrSt = Integer.parseInt(str2);
        }
        Station newArrSt = stationModelController.search(newIdArrSt);
        if (newArrSt == null) {
            error("Станция с id=" + newIdArrSt + " не найдена.");
        }

        editRoute.setIdDeparture(newIdDepSt);
        editRoute.setIdArrival(newIdArrSt);
    }

	/**
	 * Редактирование станции.
	 */
    private void editStation() throws IOException, SystemException {
        //  UPDATE `itrain`.`stations` SET `name`='АТКАРСК-1' WHERE `id`='3';

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StationModelController stationModelController = StationModelController.getInstance();

        System.out.print("\tВведите номер станции для редактирования: ");
        Integer number = Integer.parseInt(reader.readLine());
        Station editStation = stationModelController.search(number);
        if (editStation == null) {
            error("Не найдена станция для редактирования.");
        }
        System.out.print("\tВведите новое название станции: ");
        String newNameStation = reader.readLine();
        if (!stationModelController.search(newNameStation).isEmpty()) {
            error("Такое название уже существует.");
        }
        editStation.setNameStation(newNameStation.toUpperCase());
        System.out.println("\tРеадктирование завершено.");
    }
}
