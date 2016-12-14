/*
 * @(#)DataParser.java 1.0 11.12.2016
 */

package ru.solpro.controller.parser;

/**
 * Интерфейс для работа с внешними данными.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public interface DataParser {

    /**
     * Метод выполняющий маршаллизацию данных из приложения.
     */
    void save();

    /**
     * Метод выполняющий демаршаллизацию данных из приложения.
     */
    void load();
}
