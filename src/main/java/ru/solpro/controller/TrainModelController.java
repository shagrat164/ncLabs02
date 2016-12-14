/*
 * @(#)TrainModelController.java 1.0 11.12.2016
 */

package ru.solpro.controller;

import ru.solpro.model.Train;
import ru.solpro.model.Route;
import ru.solpro.model.Schedule;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeSet;

/**
 * Контроллер для работы с моделью <code>Train</code> (поезд).
 * Является синглтоном.
 * @see Train
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
@XmlRootElement(name = "trains")
public class TrainModelController implements ModelController<Train>, Serializable {

    /**
     * Переменная для хранения экземпляра TrainModelController.
     */
    private static TrainModelController instance;

    /**
     * Коллекция для хранения поездов в системе.
     */
    private TreeSet<Train> trains;

    /**
     * Private конструктор с ленивой инициализацией.
     */
    private TrainModelController() {
        trains = new TreeSet<>();
    }

    /**
     * Метод с ленивой инициализацией.
     * Получение текущего экземпляра.
     * @return  Экземпляр TrainModelController
     */
    public static TrainModelController getInstance() {
        if (instance == null) {
            instance = new TrainModelController();
        }
        return instance;
    }

    /**
     * Метод необходим для восстановления данных после десериализации.
     * @param instance    Экземпляр TrainModelController.
     */
    public static void setInstance(TrainModelController instance) {
        TrainModelController.instance = instance;
    }

    /**
     * Геттер поездов.
     * @return Коллекция <code>Train</code>
     */
    @XmlElement(name = "train")
    public TreeSet<Train> getTrains() {
        return trains;
    }

    /**
     * Сеттер поездов.
     * @param trains    коллекция поездов.
     */
    public void setTrains(TreeSet<Train> trains) {
        this.trains = trains;
    }

    /**
     * Метод-заглушка.
     * @param find Параметры поиска.
     *             Может включать [*] - для пропуска нескольких символов
     *             и [?] - для пропуска одного символа.
     * @return  null
     */
    @Override
    public ArrayList<Train> search(String find) {
        return null;
    }

    /**
     * Поиск по номеру поезда.
     * @param numberTrain    номер поезда
     * @return <code>Train</code> или null если поезд не найден.
     */
    @Override
    public Train search(int numberTrain) {
        for (Train train : trains) {
            if (train.getTrainNumber() == numberTrain) {
                return train;
            }
        }
        return null;
    }

    /**
     * Добавление поезда в список поездов по его экземпляру.
     * @param train    Поезд для добавления
     * @return true - поезд успешно добавлен
     *         false - поезд невозможно добавить
     */
    @Override
    public boolean add(Train train) {
        return trains.add(train);
    }

    /**
     * Добавление поезда в список поездов по номеру.
     * @param trainNumber    номер поезда.
     * @return true - поезд успешно добавлен,
     *         false - поезд невозможно добавить.
     */
    public boolean add(int trainNumber) {
        return trains.add(new Train(trainNumber));
    }

    /**
     * Удаление поезда по номеру.
     * @param number    номер поезда.
     * @return true - поезд успешно удалён,
     *         false - поезд невозможно удалить.
     */
    @Override
    public boolean remove(int number) {
        for (Train train : trains) {
            if (train.getTrainNumber() == number) {
                return trains.remove(train);
            }
        }
        return false;
    }

    /**
     * Удаление поезда по его экземпляру.
     * @param train    экземпляр поезда.
     * @return true - успешное удаление,
     *         false - не удалено.
     */
    @Override
    public boolean remove(Train train) {
        return trains.remove(train);
    }

    /**
     * Метод добавления строки расписания для поезда.
     * @param routeId              id маршрута.
     * @param numberTrains         номер поезда.
     * @param departureDateTime    дата и время итправления.
     * @param hours                время движения до пункта назначения (часов).
     * @return true - успешное добавление,
     *         false - не добавлено.
     */
    public boolean addScheduleLine(int routeId,
                                   int numberTrains,
                                   LocalDateTime departureDateTime,
                                   long hours) {
        Route route = RouteModelController.getInstance().search(routeId);
        Train train = search(numberTrains);

        if (train != null && route != null) {
            train.addScheduleLine(route, departureDateTime, hours, 0);
            return true;
        }
        return false;
    }

    /**
     * Метод добавления строки расписания для поезда.
     * @param routeId              id маршрута.
     * @param numberTrain          номер поезда.
     * @param departureDateTime    дата и время итправления.
     * @param hours                время движения до пункта назначения (часов).
     * @param min                  время движения до пункта назначения (минут).
     * @return true - успешное добавление,
     *         false - не добавлено.
     */
    public boolean addScheduleLine(int routeId,
                                   int numberTrain,
                                   LocalDateTime departureDateTime,
                                   long hours,
                                   long min) {
        Route route = RouteModelController.getInstance().search(routeId);
        Train train = search(numberTrain);

        if (train != null && route != null) {
            train.addScheduleLine(route, departureDateTime, hours, min);
            return true;
        }
        return false;
    }

    /**
     * Метод удаления строки в расписании поезда.
     * @param scheduleId     id строки в расписании
     * @param numberTrain    номер поезда у которого нужно удалить расписание.
     * @return true - успешное удаление,
     *         false - не удалено.
     */
    public boolean delScheduleLine(int scheduleId, int numberTrain) {
        Train train = search(numberTrain);

        if (train != null) {
            TreeSet<Schedule> schedule = train.getTrainTimetable();
            if (!schedule.isEmpty()) {
                for (Schedule schedule1 : schedule) {
                    if (schedule1.getId() == scheduleId) {
                        return train.delScheduleLine(schedule1);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Расписание поездов за ближайшие 24 часа от текущего времени.
     * @return Расписание.
     */
    public LinkedHashMap<Train, ArrayList<Schedule>> viewSchedule() {
        LinkedHashMap<Train, ArrayList<Schedule>> result = new LinkedHashMap<>();
        LocalDateTime borderDateTime = LocalDateTime.now().plusHours(24);

        for (Train train : trains) {
            ArrayList<Schedule> resultSchedule = new ArrayList<>();
            for (Schedule schedule : train.getTrainTimetable()) {
                if (schedule.getDepartureDateTime().isBefore(borderDateTime)) {
                    resultSchedule.add(schedule);
                }
            }
            result.put(train, resultSchedule);
        }
        return result;
    }

    /**
     * Расписание определённого поезда
     * @param numberTrain    номер поезда
     * @return null - если поезд не найден,
     *         массив с расписанием.
     */
    public ArrayList<Schedule> viewSchedule(int numberTrain) {
        Train train = search(numberTrain);
        if (train == null) {
            return null;
        }
        return new ArrayList<>(train.getTrainTimetable());
    }
}
