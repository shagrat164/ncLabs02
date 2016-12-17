/*
 * @(#)Route.java 1.0 17.12.2016
 */

package ru.solpro.model;

/**
 * Created by danila on 17.12.2016.
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

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", dep='" + dep + '\'' +
                ", arr='" + arr + '\'' +
                '}';
    }
}
