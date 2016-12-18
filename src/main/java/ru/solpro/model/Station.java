/*
 * @(#)Station.java 1.0 17.12.2016
 */

package ru.solpro.model;

/**
 * Класс для объекта станция.
 *
 * @author Protsvetov Danila
 * @version 1.0
 */
public class Station {
    private int id;
    private String name;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
