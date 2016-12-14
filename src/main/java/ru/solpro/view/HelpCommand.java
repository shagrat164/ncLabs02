/*
 * @(#)HelpCommand.java 1.0 11.12.2016
 */

package ru.solpro.view;

import ru.solpro.controller.CommandController;
import ru.solpro.controller.SystemException;

import java.io.IOException;
import java.util.Map;

/**
 * Команда помощи.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */

public class HelpCommand implements Command {
    /**
     * Разделитель.
     */
    private static final String MSG_SPLIT = "==========================================";

    /**
     * Коллекция для хранения команд.
     */
    private Map<String, Command> commands = CommandController.getCommands();

    /**
     * Выполнение команды.
     * @param args    аргументы
     * @return true - продолжить выполнение, false - завершить выполнение.
     * @throws SystemException  ошибка при работе пользователя с программой.
     * @throws IOException  ошибка ввыода/вывода
     */
    @Override
    public boolean execute(String[] args) throws SystemException, IOException {
        if (args == null) {
            System.out.println("Доступные команды:\n" + MSG_SPLIT);
            for (Command cmd : commands.values()) {
                System.out.println(cmd.getName() + ": " + cmd.getDescription());
            }
            System.out.println(MSG_SPLIT);
            System.out.println("HELP [команда] - выводит справку по команде.");
            System.out.println(MSG_SPLIT);
        } else {
            for (String cmd : args) {
                System.out.println("Справка по команде " + cmd + ":\n" + MSG_SPLIT);
                Command command = commands.get(cmd.toUpperCase());
                if (command == null) {
                    System.out.println("Команда не найдена.");
                } else {
                    command.printHelp();
                }
                System.out.println(MSG_SPLIT);
            }
        }
        return true;
    }

    /**
     * Распечатать справку по команде.
     */
    @Override
    public void printHelp() {
        System.out.println(getDescription());
    }

    /**
     * Имя команды.
     * @return имя команды.
     */
    @Override
    public String getName() {
        return "HELP";
    }

    /**
     * Описание команды.
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "Выводит список всех доступных комманд.";
    }
}
