/*
 * @(#)Schedule.java 1.0 11.12.2016
 */

package ru.solpro.model;

import ru.solpro.controller.adapter.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс-модель для расписания каждой электрички
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */

@XmlType(name = "schedule", propOrder = {
        "route",
        "departureDateTime",
        "hour",
        "min"})
public class Schedule implements Comparable<Schedule>, Serializable {

    /**
     * id расписания.
     */
    private int id;

    /**
     * Маршрут.
     */
    private Route route;

    /**
     * Дата и время отправления.
     */
    private LocalDateTime departureDateTime;

    /**
     * Время движения в часах.
     */
    private long hour;

    /**
     * Время движения в минутах (0-59)
     */
    private long min;

    /**
     * Конструктор без параметров.
     * Используется для работы с xml.
     */
    public Schedule() {}

    /**
     * Создание расписания.
     * @param id                   id строки расписания.
     * @param route                маршрут.
     * @param departureDateTime    дата/время отправления.
     * @param hour                 время движения (часов)
     * @param min                  время движения (минут)
     */
    Schedule(int id,
             Route route,
             LocalDateTime departureDateTime,
             long hour,
             long min) {
        this.id = id;
        this.route = route;
        this.departureDateTime = departureDateTime;
        this.hour = hour;
        this.min = min;
    }

    /**
     * Геттер
     * @return
     */
    @XmlElement
    public long getHour() {
        return hour;
    }

    /**
     * Сеттер
     * @param hour    время движения в часах
     */
    public void setHour(long hour) {
        this.hour = hour;
    }

    /**
     * Геттер
     * @return
     */
    @XmlElement
    public long getMin() {
        return min;
    }

    /**
     * Сеттер
     * @param min    время движения минут
     */
    public void setMin(long min) {
        this.min = min;
    }

    /**
     * Геттер
     * @return id
     */
    @XmlAttribute(name = "id")
    public int getId() {
        return id;
    }

    /**
     * Сеттер
     * @param id    id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Геттер
     * @return маршрут
     */
    @XmlElement
    public Route getRoute() {
        return route;
    }

    /**
     * Сеттер
     * @param route    маршрут
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * Геттер
     * @return дата/время отправления.
     */
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    /**
     * Сеттер
     * @param departureDateTime    дата/время отправления.
     */
    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    /**
     * Геттер
     * @return дата/время прибытия.
     */
    private LocalDateTime getArrivalDateTime() {
        return departureDateTime.plusHours(hour).plusMinutes(min);
    }

    /**
     * Геттер времени движения.
     * @return время движения HH:mm
     */
    private String getTravelTime() {
        return hour + ":" + min;
    }

    /**
     * Переопределение для реализации интерфейса Comparable.
     * @param o    экземпляр
     * @return 1 - текущий объект больше o
     *         -1 - текущий объект меньше о
     *         0 - объекты равны
     */
    @Override
    public int compareTo(Schedule o) {
        if (departureDateTime.isAfter(o.departureDateTime)) {
            return 1;
        } else if (departureDateTime.isBefore(o.departureDateTime)) {
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
        return "[" + id + "] Отправление " +
                departureDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) +
                "; Прибытие " +
                getArrivalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
}
