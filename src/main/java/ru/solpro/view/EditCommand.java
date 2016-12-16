/*
 * @(#)EditCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.*;
import ru.solpro.model.Train;
import ru.solpro.model.Route;
import ru.solpro.model.Schedule;
import ru.solpro.model.Station;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Команда редактирования.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class EditCommand implements Command {

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

    private void editTrain() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Database database = new Database();
        String sql;

        database.connect();

        System.out.print("\tВведите номер поезда для редактирования: ");
        int numberTrain = Integer.parseInt(reader.readLine());
        int idTrain = 0;

        sql = "SELECT `id` FROM `itrain`.`trains` WHERE `number`='" + numberTrain + "';";
        try {
            ResultSet resultSet = database.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                idTrain = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        System.out.print("\tВведите новый номер поезда " +
                "(если изменять не нужно, оставте поле пустым): ");
        String str1 = reader.readLine();
        int newTrainNumber = 0;
        if (!"".equals(str1)) {
            newTrainNumber = Integer.parseInt(str1);
            sql = "UPDATE `itrain`.`trains` SET `number`='"+ newTrainNumber + "' WHERE `id`='" + idTrain + "';";
            try {
                database.getStatement().executeUpdate(sql);
            } catch (SQLException e) {
                System.out.println("Error: " + e);
            }
        }

        System.out.print("\tВведите новый id маршрута " +
                "(если изменять не нужно, оставте поле пустым): ");
        String str2 = reader.readLine();
        if (!"".equals(str2)) {
            int newIdRoute = Integer.parseInt(str2);
            sql = "DELETE FROM `itrain`.`schedule` WHERE `train_id`='" + idTrain + "';";
            try {
                database.getStatement().executeUpdate(sql);
                editTrainRoute(database, idTrain, newIdRoute);
            } catch (SQLException e) {
                System.out.println("Error: " + e);
            }
        }
        database.disconnect();
    }

    /**
     * Меняет маршрут и добавляет первую дату в расписание.
     * @throws IOException
     */
    private void editTrainRoute(Database database, int idTrain, int idRoute) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String sql;

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

        if (timeArrMinutes <= 0) {
            sql = "INSERT INTO `schedule` (`train_id`, `route_id`, `date`, `hour`) VALUES ('" + idTrain + "', '" + idRoute + "', '" + depDateTime + "', '" + timeArrHours + "');";
        } else {
            sql = "INSERT INTO `schedule` (`train_id`, `route_id`, `date`, `hour`, `min`) VALUES ('" + idTrain + "', '" + idRoute + "', '" + depDateTime + "', '" + timeArrHours + "', '" + timeArrMinutes + "');";
        }
        try {
            database.getStatement().executeUpdate(sql);
            System.out.println("Расписание успешно добавлено.");
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
//        database.disconnect();
    }

	/**
	 * Редактирование маршрута.
	 */
    private void editRoute() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Database database = new Database();
        database.connect();

        System.out.print("\tВведите id маршрута для редактирования: ");
        Integer number = Integer.parseInt(reader.readLine());

        System.out.print("\tВведите новый id станции отправления " +
                "(если изменять не нужно, оставте поле пустым): ");
        String strIdDep = reader.readLine();
        if ("".equals(strIdDep)) {
            strIdDep = "0";
        }
        Integer idDep = Integer.parseInt(strIdDep);

        System.out.print("\tВведите новый id станции назначения " +
                "(если изменять не нужно, оставте поле пустым): ");
        String strIdArr = reader.readLine();
        if ("".equals(strIdArr)) {
            strIdArr = "0";
        }
        Integer idArr = Integer.parseInt(strIdArr);

        String sql;
        if (idDep > 0 && idArr == 0) {
            sql = "UPDATE `itrain`.`routes` SET `dep_id`='" + idDep + "' WHERE `id`='" + number + "';";
        } else if (idDep == 0 && idArr > 0) {
            sql = "UPDATE `itrain`.`routes` SET `arr_id`='" + idArr + "' WHERE `id`='" + number + "';";
        } else {
            sql = "UPDATE `itrain`.`routes` SET `dep_id`='" + idDep + "', `arr_id`='" + idArr + "' WHERE `id`='" + number + "';";
        }

        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        database.disconnect();
    }

	/**
	 * Редактирование станции.
	 */
    private void editStation() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Database database = new Database();
        database.connect();

        System.out.print("\tВведите номер станции для редактирования: ");
        String idStation = reader.readLine();
        System.out.print("\tВведите новое название станции: ");
        String newNameStation = reader.readLine();
        String sql = "UPDATE `itrain`.`stations` SET `name`='" + newNameStation + "' WHERE `id`='" + idStation + "';";

        try {
            database.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        database.disconnect();
    }
}
