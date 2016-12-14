/*
 * @(#)Route.java 1.0 11.12.2016
 */

package ru.solpro.model;

import ru.solpro.controller.StationModelController;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Класс-модель для маршрута (Начальная станция, конечная станция)
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */

@XmlType(propOrder = {"idDeparture",
                      "idArrival"})
@XmlRootElement(name = "route")
public class Route implements Comparable<Route>, Serializable {

    /**
     * Статическая переменная-счётчик для присвоения id.
     */
    private static int count;

    /**
     * id маршрута.
     */
    private int id;

    /**
     * id станции отправления.
     */
    private int idDeparture;

    /**
     * id станци прибытия.
     */
    private int idArrival;

    /**
     * Конструктор без параметров.
     * Используется для работы с xml.
     */
    public Route() {}

    /**
     * Создание маршрута.
     * @param idDeparture     id станции отправления
     * @param idArrival       id станции прибытия
     */
    public Route(int idDeparture, int idArrival) {
        this.idDeparture = idDeparture;
        this.idArrival = idArrival;
        count += 1;
        this.id = count;
    }

    /**
     * Сериализация статических переменных.
     * @param objectOutputStream
     * @throws IOException
     */
    public static void serializeStatic(ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.writeInt(count);
    }

    /**
     * Десериализация статических переменных.
     * @param objectInputStream
     * @throws IOException
     */
    public static void deserializeStatic(ObjectInputStream objectInputStream)
            throws IOException {
        count = objectInputStream.readInt();
    }

    /**
     * Геттер id.
     * @return id маршрута
     */
    @XmlAttribute
    public int getId() {
        return id;
    }

    /**
     * Сеттер id.
     * @param id    id маршрута
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Геттер.
     * @return count
     */
    public static int getCount() {
        return count;
    }

    /**
     * Сеттер.
     * @param count    значение счётчика
     */
    public static void setCount(int count) {
        Route.count = count;
    }

    /**
     * Геттер станции отправления.
     * @return id станции отправления.
     */
    @XmlElement
    public int getIdDeparture() {
        return idDeparture;
    }

    /**
     * Сеттер станции отправления.
     * @param idDeparture    id станции отправления.
     */
    public void setIdDeparture(int idDeparture) {
        this.idDeparture = idDeparture;
    }

    /**
     * Геттер станции прибытия.
     * @return id станции прибытия.
     */
    @XmlElement
    public int getIdArrival() {
        return idArrival;
    }

    /**
     * Сеттер станции прибытия.
     * @param idArrival    id станции прибытия.
     */
    public void setIdArrival(int idArrival) {
        this.idArrival = idArrival;
    }

    /**
     * Переопределение для реализации интерфейса Comparable.
     * @param o    экземпляр
     * @return 1 - текущий объект больше o
     *         -1 - текущий объект меньше о
     *         0 - объекты равны
     */
    @Override
    public int compareTo(Route o) {
        if (id > o.getId()) {
            return 1;
        } else if (id < o.getId()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Для текущего объекта в виде строки.
     * @return Строка.
     */
    @Override
    public String toString() {
        // Не уверен в правильности этого решения,
        // но это первое что пришло в голову.
        return StationModelController.getInstance().search(idDeparture).getNameStation() +
                "->" + StationModelController.getInstance().search(idArrival).getNameStation();
    }

    /**
     * Проверка на совпадение.
     * @param o    то, с чем сравнивать.
     * @return true - равно,
     *         false - не равно.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (id != route.id) return false;
        if (idDeparture != route.idDeparture) return false;
        return idArrival == route.idArrival;

    }

    /**
     * Хеш-функция.
     * @return hash-код
     */
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + idDeparture;
        result = 31 * result + idArrival;
        return result;
    }
}
