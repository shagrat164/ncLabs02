/*
 * @(#)RouteModelController.java 1.0 11.12.2016
 */

package ru.solpro.controller;

import ru.solpro.model.Route;
import ru.solpro.model.Station;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Контроллер для работы с моделью  <code>Route</code> (маршрут).
 * Является синглтоном.
 * @see Route
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */

@XmlRootElement(name = "routes")
public class RouteModelController implements ModelController<Route>, Serializable {

    /**
     * Переменная для хранения экземпляра RouteModelController.
     */
    private static RouteModelController instance;

    /**
     * Коллекция для хранения маршрутов в системе.
     */
    private TreeSet<Route> routes;

    /**
     * Private конструктор с ленивой инициализацией.
     */
    private RouteModelController() {
        routes = new TreeSet<>();
    }

    /**
     * Метод необходим для восстановления данных после десериализации.
     * @param instance    Экземпляр RouteModelController
     */
    public static void setInstance(RouteModelController instance) {
        RouteModelController.instance = instance;
    }

    /**
     * Метод с ленивой инициализацией.
     * Получение текущего экземпляра.
     * @return  Экземпляр RouteModelController
     */
    public static RouteModelController getInstance() {
        if (instance == null) {
            instance = new RouteModelController();
        }
        return instance;
    }

    /**
     * Геттер маршрутов
     * @return Коллекция <code>Route</code>
     */
    @XmlElement(name = "route")
    public TreeSet<Route> getRoutes() {
        return routes;
    }

    /**
     * Сеттер маршрутов
     * @param routes    коллекция маршрутов
     */
    public void setRoutes(TreeSet<Route> routes) {
        this.routes = routes;
    }

    /**
     * Метод осуществляет поиск маршрута по строке поиска.
     * @param find Параметры поиска.
     *             Может включать [*] - для пропуска нескольких символов
     *             и [?] - для пропуска одного символа.
     * @return Список найденных маршрутов
     */
    @Override
    public ArrayList<Route> search(String find) {
        ArrayList<Route> result = new ArrayList<>();
        if (find.contains("*")) {
            find = find.replace("*", "[а-яА-ЯёЁa-zA-Z0-9-\\s]*");
        }
        if (find.contains("?")) {
            find = find.replace("?", "[а-яА-ЯёЁa-zA-Z0-9-\\s]");
        }
        Pattern p = Pattern.compile("^" + find.toUpperCase() + "$");
        Matcher m;
        for (Route route : routes) {
            m = p.matcher(route.toString().toUpperCase());
            if (m.matches()) {
                result.add(route);
            }
        }
        return result;
    }

    /**
     * Метод поиска по идентификатору
     * @param id Идентификатор для поиска
     * @return  <code>Route</code> или null если маршрут не найден
     */
    @Override
    public Route search(int id) {
        for (Route route : routes) {
            if (route.getId() == id) {
                return route;
            }
        }
        return null;
    }

    /**
     * Метод удаляет маршрут из списка маршрутов.
     * @param id Идентификатор маршрута
     * @return true - маршрут успешно удалён
     *         false - маршрут не найден для удаления
     */
    @Override
    public boolean remove(int id) {
        for (Route route : routes) {
            if (route.getId() == id) {
                return routes.remove(route);
            }
        }
        return false;
    }

    /**
     * Удаление маршрута из списка маршрутов по его экземпляру.
     * @param route    Маршрут для удаления
     * @return true - маршрут успешно удалён
     *         false - маршрут не найден для удаления
     */
    @Override
    public boolean remove(Route route) {
        return routes.remove(route);
    }

    /**
     * Добавление маршрута в список маршрутов по его экземпляру.
     * @param route    Маршрут для добавления
     * @return true - маршрут успешно добавлен
     *         false - маршрут невозможно добавить
     */
    @Override
    public boolean add(Route route) {
        return routes.add(route);
    }

    /**
     * Добавление маршрута по id станций отправления и назначения.
     * @param idDeparture    id станции отправления
     * @param idArrival      id станции назначения
     * @return true - маршрут успешно добавлен
     *         false - маршрут невозможно добавить
     */
    public boolean add(int idDeparture, int idArrival) {
        StationModelController stationController = StationModelController.getInstance();
        Station departure = stationController.search(idDeparture);
        Station arrival = stationController.search(idArrival);

        if (departure == null || arrival == null || departure.equals(arrival)) {
            return false;
        }
        return this.routes.add(new Route(idDeparture, idArrival));
    }
}
