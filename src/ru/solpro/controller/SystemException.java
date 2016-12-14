/*
 * @(#)SystemException.java 1.0 11.12.2016
 */

package ru.solpro.controller;

/**
 * Класс для генерирования внутренних ошибок
 * возникающих в программе во время работы пользователя.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class SystemException extends Throwable {
    /**
     * Сообщение об ошибке
     */
    private String errorString;

    /**
     * Конструктор исключения
     * @param message    Текст ошибки.
     */
    public SystemException(String message) {
        errorString = message;
    }

    /**
     * Для вывода сообщения об ошибке.
     * @return Сообщение об ошибке
     */
    @Override
    public String toString() {
        return errorString;
    }
}
