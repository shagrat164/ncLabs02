/*
 * @(#)SerializationData.java 1.0 11.12.2016
 */

package ru.solpro.controller.parser;

import ru.solpro.controller.TrainModelController;
import ru.solpro.controller.RouteModelController;
import ru.solpro.controller.StationModelController;
import ru.solpro.model.Route;
import ru.solpro.model.Station;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Сериализация/десериализация со сжатием данных.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class SerializationData implements DataParser {

    /**
     * Файл для хранения станций.
     */
    private String stationsFileName = "stations.data";

    /**
     * Файл для хранения маршрутов.
     */
    private String routesFileName = "routes.data";

    /**
     * Файл для хранения поездов.
     */
    private String trainsFileName = "trains.data";

    /**
     * Метод выполняющий маршаллизацию данных из приложения.
     */
    @Override
    public void save() {
        StationModelController stationModelController = StationModelController.getInstance();
        RouteModelController routeModelController = RouteModelController.getInstance();
        TrainModelController trainModelController = TrainModelController.getInstance();

        FileOutputStream fileOutputStream;
        GZIPOutputStream gzipOutputStream;
        ObjectOutputStream objectOutputStream;

        try {
            fileOutputStream = new FileOutputStream(stationsFileName);
            gzipOutputStream = new GZIPOutputStream(fileOutputStream);
            objectOutputStream = new ObjectOutputStream(gzipOutputStream);

            Station.serializeStatic(objectOutputStream);
            objectOutputStream.writeObject(stationModelController);
            objectOutputStream.flush();
            objectOutputStream.close();

            fileOutputStream = new FileOutputStream(routesFileName);
            gzipOutputStream = new GZIPOutputStream(fileOutputStream);
            objectOutputStream = new ObjectOutputStream(gzipOutputStream);

            Route.serializeStatic(objectOutputStream);
            objectOutputStream.writeObject(routeModelController);
            objectOutputStream.flush();
            objectOutputStream.close();

            fileOutputStream = new FileOutputStream(trainsFileName);
            gzipOutputStream = new GZIPOutputStream(fileOutputStream);
            objectOutputStream = new ObjectOutputStream(gzipOutputStream);

            objectOutputStream.writeObject(trainModelController);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * Метод выполняющий демаршаллизацию данных из приложения.
     */
    @Override
    public void load() {
        FileInputStream fileInputStream;
        GZIPInputStream gzipInputStream;
        ObjectInputStream objectInputStream;
        try {
            fileInputStream = new FileInputStream(stationsFileName);
            gzipInputStream = new GZIPInputStream(fileInputStream);
            objectInputStream = new ObjectInputStream(gzipInputStream);
            Station.deserializeStatic(objectInputStream);
            StationModelController.setInstance((StationModelController) objectInputStream.readObject());
            fileInputStream.close();

            fileInputStream = new FileInputStream(routesFileName);
            gzipInputStream = new GZIPInputStream(fileInputStream);
            objectInputStream = new ObjectInputStream(gzipInputStream);
            Route.deserializeStatic(objectInputStream);
            RouteModelController.setInstance((RouteModelController) objectInputStream.readObject());
            fileInputStream.close();

            fileInputStream = new FileInputStream(trainsFileName);
            gzipInputStream = new GZIPInputStream(fileInputStream);
            objectInputStream = new ObjectInputStream(gzipInputStream);
            TrainModelController.setInstance((TrainModelController) objectInputStream.readObject());
            fileInputStream.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}