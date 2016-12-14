/*
 * @(#)LocalDateTimeAdapter.java 1.0 11.12.2016
 */

package ru.solpro.controller.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

/**
 * Класс-адаптер для корректной обработки типа данных LocalDateTime
 * из xml файла.
 * Для того чтобы им воспользоваться, необходимо предварить переменную
 * типа LocalDateTime строкой:
 * "@XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)"
 *
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    /**
     * Метод для конвертации строки из файла xml в формат
     * LocalDateTime.
     * @param v    Строка из файла для обработки
     * @return     Переменную формата LocalDateTime
     * @throws Exception    Возникает при ошибках в обработке
     */
    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        return LocalDateTime.parse(v);
    }

    /**
     * Метод для конвертации переменной типа LocalDateTime
     * в строку для дальнейшей записи в xml файл.
     * @param v    Переменная типа LocalDateTime
     * @return     Строка для записи в xml файл
     * @throws Exception    Возникает при ошибках в обработке
     */
    @Override
    public String marshal(LocalDateTime v) throws Exception {
        return v.toString();
    }
}
