/*
 * @(#)Route.java 1.0 17.12.2016
 */

package ru.solpro.model;

/**
 * Класс для объекта маршрут.
 *
 * @author Protsvetov Danila
 * @version 1.0
 */
public class Route {
    private int id;
    private String dep;
    private String arr;

    public void setId(int id) {
        this.id = id;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public void setArr(String arr) {
        this.arr = arr;
    }

    public int getId() {
        return id;
    }

    public String getDep() {
        return dep;
    }

    public String getArr() {
        return arr;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", dep='" + dep + '\'' +
                ", arr='" + arr + '\'' +
                '}';
    }
}
