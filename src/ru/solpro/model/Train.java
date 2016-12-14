/*
 * @(#)Train.java 1.0 11.12.2016
 */

package ru.solpro.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.TreeSet;

/**
 * Класс-модель для электропоезда (Номер состава, <code>Schedule</code>)
 * @see Schedule
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */

@XmlRootElement(name = "train")
public class Train implements Comparable<Train>, Serializable {

    /**
     * Номер поезда.
     */
    private int trainNumber;

    /**
     * Расписание.
     * @see Schedule
     */
    private TreeSet<Schedule> trainTimetable;

    /**
     * Конструктор без параметров.
     * Используется для работы с xml.
     */
    public Train() {
        this.trainTimetable = new TreeSet<>();
    }

    /**
     * Конструктор для создания поезда
     * @param trainNumber Номер поезда
     */
    public Train(int trainNumber) {
        this.trainNumber = trainNumber;
        this.trainTimetable = new TreeSet<>();
    }

    /**
     * Геттер.
     * @return
     */
    @XmlElement
    public TreeSet<Schedule> getTrainTimetable() {
        return trainTimetable;
    }

    /**
     * Сеттер.
     * @param trainTimetable
     */
    public void setTrainTimetable(TreeSet<Schedule> trainTimetable) {
        this.trainTimetable = trainTimetable;
    }

    /**
     * Геттер.
     * @return
     */
    @XmlElement(name = "number")
    public int getTrainNumber() {
        return trainNumber;
    }

    /**
     * Сеттер.
     * @param trainNumber
     */
    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    /**
     * Добавление строки в расписание.
     * @param route                маршрут.
     * @param departureDateTime    дата/время отправления.
     * @param hours                время движения (часов).
     * @param min                  время движения (минут).
     */
    public void addScheduleLine(Route route,
                                LocalDateTime departureDateTime,
                                long hours,
                                long min) {
        int idSchedule = 1;
        if (!this.trainTimetable.isEmpty()) {
            idSchedule = this.trainTimetable.last().getId() + 1;
        }
        this.trainTimetable.add(new Schedule(idSchedule, route,
                departureDateTime, hours, min));
    }

    /**
     * Удаление строки в расписании.
     * @param schedule    удаляемый объект.
     * @return true - удалилась,
     *         false - не удалилась.
     */
    public boolean delScheduleLine(Schedule schedule) {
        return this.trainTimetable.remove(schedule);
    }

    /**
     * Очистить расписание.
     */
    public void clearTrainTimetable() {
        if (!trainTimetable.isEmpty()) {
            trainTimetable.clear();
        }
    }

    /**
     * Переопределение для реализации интерфейса Comparable.
     * @param o    экземпляр
     * @return 1 - текущий объект больше o
     *         -1 - текущий объект меньше о
     *         0 - объекты равны
     */
    @Override
    public int compareTo(Train o) {
        if (trainNumber > o.getTrainNumber()) {
            return 1;
        } else if (trainNumber < o.getTrainNumber()) {
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
        if (trainTimetable.isEmpty()) {
            return "номер поезда=[" + trainNumber +
                    "] маршрут не определён";
        }
        return "номер поезда=[" + trainNumber +
                "] маршрут=[" + trainTimetable.first().getRoute().getId() +
                "] " + trainTimetable.first().getRoute();
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

        Train that = (Train) o;

        if (trainNumber != that.trainNumber) return false;
        return trainTimetable != null ? trainTimetable.equals(that.trainTimetable) : that.trainTimetable == null;
    }

    /**
     * Хеш-функция.
     * @return hash-код
     */
    @Override
    public int hashCode() {
        int result = trainNumber;
        result = 31 * result + (trainTimetable != null ? trainTimetable.hashCode() : 0);
        return result;
    }
}
