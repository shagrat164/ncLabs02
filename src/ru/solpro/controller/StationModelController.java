/*
 * @(#)StationModelController.java 1.0 11.12.2016
 */

package ru.solpro.controller;

import ru.solpro.model.Station;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Контроллер для работы с моделью <code>Station</code> (станция)
 * @see Station
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */

@XmlRootElement(name = "stations")
public class StationModelController implements ModelController<Station>, Serializable {

    /**
     * Переменная для хранения экземпляра StationModelController.
     */
    private static StationModelController instance;

    /**
     * Коллекция для хранения станций в системе.
     */
    private TreeSet<Station> stations;

    /**
     * Private конструктор с ленивой инициализацией.
     */
    private StationModelController() {
        stations = new TreeSet<>();
    }

    /**
     * Метод с ленивой инициализацией.
     * Получение текущего экземпляра.
     * @return  Экземпляр StationModelController
     */
    public static StationModelController getInstance() {
        if (instance == null) {
            instance = new StationModelController();
        }
        return instance;
    }

    /**
     * Метод необходим для восстановления данных после десериализации.
     * @param instance    Экземпляр StationModelController
     */
    public static void setInstance(StationModelController instance) {
        StationModelController.instance = instance;
    }

    /**
     * Геттер станций
     * @return Коллекция со станциями
     */
    @XmlElement(name = "station")
    public TreeSet<Station> getStations() {
        return stations;
    }

    /**
     * Сеттер станций
     * @param stations    коллекция станций
     */
    public void setStations(TreeSet<Station> stations) {
        this.stations = stations;
    }

    /**
     * Метод осуществляет поиск станции по строке поиска.
     *
     * @param find Параметры поиска.
     *             Может включать [*] - для пропуска нескольких символов
     *             и [?] - для пропуска одного символа.
     * @return Список найденных станций
     */
    @Override
    public ArrayList<Station> search(String find) {
        ArrayList<Station> result = new ArrayList<>();
        if (find.contains("*")) {
            find = find.replace("*", "[а-яА-ЯёЁa-zA-Z0-9-\\s]*");
        }
        if (find.contains("?")) {
            find = find.replace("?", "[а-яА-ЯёЁa-zA-Z0-9-\\s]");
        }
        Pattern p = Pattern.compile("^" + find.toUpperCase() + "$");
        Matcher m;
        for (Station station : stations) {
            m = p.matcher(station.toString().toUpperCase());
            if (m.matches()) {
                result.add(station);
            }
        }
        return result;
    }

    /**
     * Метод осуществляет поиск станции по её id.
     * @param id    id станции для поиска
     * @return      <code>Station</code> или null если станция не найдена
     */
    @Override
    public Station search(int id) {
        for (Station station : stations) {
            if (station.getId() == id) {
                return station;
            }
        }
        return null;
    }

    /**
     * Метод удаляет станцию из списка станций.
     * @param id Идентификатор станции
     * @return true - станция успешно удалена
     *         false - станция не найдена для удаления
     */
    @Override
    public boolean remove(int id) {
        for (Station station : stations) {
            if (station.getId() == id) {
                return stations.remove(station);
            }
        }
        return false;
    }

    /**
     * Удаление станции из списка станций по его экземпляру.
     * @param station    Станция для удаления
     * @return true - станция успешно удалена
     *         false - станция не найдена для удаления
     */
    @Override
    public boolean remove(Station station) {
        return stations.remove(station);
    }

    /**
     * Добавление станции в список станций по её экземпляру.
     * @param station    Станция для добавления
     * @return true - станция успешно добавлена
     *         false - станцию невозможно добавить
     */
    @Override
    public boolean add(Station station) {
        return stations.add(station);
    }

    /**
     * Добавление станции по названию.
     * Перевод названия в верхний регистр.
     * @param name    название станции
     * @return        true - станция успешно добавлена
     *                false - станцию невозможно добавить
     */
    public boolean add(String name) {
        return stations.add(new Station(name.toUpperCase()));
    }
}
