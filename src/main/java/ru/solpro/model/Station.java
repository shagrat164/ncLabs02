/*
 * @(#)Station.java 1.0 11.12.2016
 */

package ru.solpro.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Класс-модель для станции
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */

@XmlRootElement(name = "station")
public class Station implements Comparable<Station>, Serializable {

    /**
     * Статическая переменная-счётчик для присвоения id.
     */
    private static int count;

    /**
     * id станции.
     */
    private int id;

    /**
     * Название станции.
     */
    private String nameStation;

    /**
     * Конструктор без параметров.
     * Используется для работы с xml.
     */
    public Station() {}

    /**
     * Создание станции.
     * @param name    название станции.
     */
    public Station(String name) {
        count += 1;
        this.id = count;
        this.nameStation = name;
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
     * Геттер.
     * @return
     */
    public static int getCount() {
        return count;
    }

    /**
     * Сеттер.
     * @param count
     */
    public static void setCount(int count) {
        Station.count = count;
    }

    /**
     * Геттер.
     * @return
     */
    @XmlAttribute(name = "id")
    public int getId() {
        return id;
    }

    /**
     * Сеттер.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Геттер.
     * @return
     */
    @XmlElement(name = "name")
    public String getNameStation() {
        return nameStation;
    }

    /**
     * Сеттер.
     * @param nameStation
     */
    public void setNameStation(String nameStation) {
        this.nameStation = nameStation;
    }

    /**
     * Переопределение для реализации интерфейса Comparable.
     * @param o    экземпляр
     * @return 1 - текущий объект больше o
     *         -1 - текущий объект меньше о
     *         0 - объекты равны
     */
    @Override
    public int compareTo(Station o) {
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
        return nameStation;
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

        Station station = (Station) o;

        return nameStation.equals(station.nameStation);
    }

    /**
     * Хеш-функция.
     * @return hash-код
     */
    @Override
    public int hashCode() {
        return nameStation.hashCode();
    }
}
