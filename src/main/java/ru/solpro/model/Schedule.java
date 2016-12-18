/*
 * @(#)Schedule.java 1.0 17.12.2016
 */

package ru.solpro.model;

/**
 * Класс для объекта расписание.
 *
 * @author Protsvetov Danila
 * @version 1.0
 */
public class Schedule {
    private int id;
    private int numberTrain;
    private String dep;
    private String arr;
    private String timeDep;
    private String timeArr;

    public void setId(int id) {
        this.id = id;
    }

    public void setNumberTrain(int numberTrain) {
        this.numberTrain = numberTrain;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public void setArr(String arr) {
        this.arr = arr;
    }

    public void setTimeDep(String timeDep) {
        this.timeDep = timeDep;
    }

    public void setTimeArr(String timeArr) {
        this.timeArr = timeArr;
    }

    public int getId() {
        return id;
    }

    public int getNumberTrain() {
        return numberTrain;
    }

    public String getDep() {
        return dep;
    }

    public String getArr() {
        return arr;
    }

    public String getTimeDep() {
        return timeDep;
    }

    public String getTimeArr() {
        return timeArr;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", numberTrain=" + numberTrain +
                ", dep='" + dep + '\'' +
                ", arr='" + arr + '\'' +
                ", timeDep='" + timeDep + '\'' +
                ", timeArr='" + timeArr + '\'' +
                '}';
    }
}
