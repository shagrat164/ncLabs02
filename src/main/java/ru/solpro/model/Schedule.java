/*
 * @(#)Schedule.java 1.0 17.12.2016
 */

package ru.solpro.model;

/**
 * Created by danila on 17.12.2016.
 *
 * @author Protsvetov Danila
 * @version 1.0
 */
public class Schedule {
    private int id;
    private int numbeTrain;
    private String dep;
    private String arr;
    private String timeDep;
    private String timeArr;

    public void setId(int id) {
        this.id = id;
    }

    public void setNumbeTrain(int numbeTrain) {
        this.numbeTrain = numbeTrain;
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

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", numbeTrain=" + numbeTrain +
                ", dep='" + dep + '\'' +
                ", arr='" + arr + '\'' +
                ", timeDep='" + timeDep + '\'' +
                ", timeArr='" + timeArr + '\'' +
                '}';
    }
}
