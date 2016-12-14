/*
 * @(#)ParsedCommand.java 1.0 11.12.2016
 */

package ru.solpro.controller;

/**
 * Разделение команды и параметров.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class ParsedCommand {
    /**
     * Команда
     */
    String command;

    /**
     * Массив параметров
     */
    String[] args;

    /**
     * Конструктор
     * @param line    строка ввода
     */
    ParsedCommand(String line) {
        String parts[] = line.split(" ");
        if (parts != null) {
            command = parts[0];
            if (parts.length > 1) {
                args = new String[parts.length - 1];
                System.arraycopy(parts, 1, args, 0, args.length);
            }
        }
    }
}
