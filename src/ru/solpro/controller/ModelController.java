/*
 * @(#)ModelController.java 1.0 11.12.2016
 */

package ru.solpro.controller;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Интерфейс для контроллеров моделей в приложении.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 * @param <T>    Тип модели
 */
public interface ModelController<T> {

    /**
     * Метод осуществляет поиск по строке поиска.
     *
     * @param find Параметры поиска.
     *             Может включать [*] - для пропуска нескольких символов
     *             и [?] - для пропуска одного символа.
     * @return Список найденных экземпляров <code>T</code>
     */
    ArrayList<T> search(String find);

    /**
     * Метод поиска по id
     * @param id    id для поиска
     * @return
     */
    T search(int id);

    /**
     * Удаление по id
     * @param id    id для удаления
     * @return
     */
    boolean remove(int id);

    /**
     * Удаление по экземпляру
     * @param o
     * @return
     */
    boolean remove(T o);

    /**
     * Добавление по экземпляру
     * @param o
     * @return
     */
    boolean add(T o);
}
