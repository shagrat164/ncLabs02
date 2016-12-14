/*
 * @(#)AlwaysCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.SystemException;

/**
 * Абстрактный класс для создания исключения.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
abstract class AlwaysCommand {

    /**
     * Создание исключения при неправельной работе программы.
     * @param message    сообщение об ошибке.
     * @throws SystemException
     */
    void error(String message) throws SystemException {
        throw new SystemException(message);
    }
}
