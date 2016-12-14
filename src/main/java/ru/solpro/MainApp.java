/*
 * @(#)MainApp.java 1.0 11.12.2016
 *
 * Информационная система "Расписание электричек"
 */

package ru.solpro;

import ru.solpro.controller.CommandController;
import ru.solpro.controller.Database;
import ru.solpro.controller.parser.DataParser;
import ru.solpro.controller.parser.SerializationData;
import ru.solpro.controller.parser.XmlData;

import java.sql.SQLException;

/**
 * Главный класс приложения.
 *
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */

public class MainApp {
    /**
     * Инициализация при запуске приложения.
     */
    private static void init() {
//        DataParser dataParser = new SerializationData();
//        DataParser dataParser = new XmlData();
//        dataParser.load();
    }

    /**
     * Точка входа в приложение при запуске.
     * @param args    Аргументы запуска
     */
    public static void main(String[] args) throws SQLException {

        Database db = new Database();
        db.dbConnect();
        db.dbDisconnect();
//        init();
//        CommandController commandController = new CommandController();
//        commandController.execute();
    }
}
