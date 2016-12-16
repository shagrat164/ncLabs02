/*
 * @(#)MainApp.java 1.0 11.12.2016
 *
 * Информационная система "Расписание электричек"
 */

package ru.solpro;

import ru.solpro.controller.CommandController;

/**
 * Главный класс приложения.
 *
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */

public class MainApp {

    /**
     * Точка входа в приложение при запуске.
     * @param args    Аргументы запуска
     */
    public static void main(String[] args) {
        CommandController commandController = new CommandController();
        commandController.execute();
    }
}
