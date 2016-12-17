/*
 * @(#)Train.java 1.0 17.12.2016
 */

package ru.solpro.model;

/**
 * Created by danila on 17.12.2016.
 *
 * @author Protsvetov Danila
 * @version 1.0
 */
public class Train {
    private int id;
    private int number;

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                ", number=" + number +
                '}';
    }
}
