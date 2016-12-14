/*
 * @(#)CommandController.java 1.0 11.12.2016
 */

package ru.solpro.controller;

import ru.solpro.view.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

/**
 * Контроллер команд приложения.
 * @version 1.0 11 декабря 2016
 * @author Protsvetov Danila
 */
public class CommandController {
    /**
     * Хранение всех команд приложения.
     */
    private static Map<String, Command> commands;

    /**
     * Геттер для получения всех команд приложения.
     * @return  Все команды приложения.
     */
    public static Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * Конструктор выполняет ленивую инициализацию
     * с последующим добавлением команд.
     */
    public CommandController() {
        commands = new TreeMap<>();
        Command cmd;

        cmd = new ExitCommand();
        commands.put(cmd.getName(), cmd);

        cmd = new ViewCommand();
        commands.put(cmd.getName(), cmd);

        cmd = new HelpCommand();
        commands.put(cmd.getName(), cmd);

        cmd = new AddCommand();
        commands.put(cmd.getName(), cmd);

        cmd = new DelCommand();
        commands.put(cmd.getName(), cmd);

        cmd = new SearchCommand();
        commands.put(cmd.getName(), cmd);

        cmd = new EditCommand();
        commands.put(cmd.getName(), cmd);
    }

    /**
     * Метод ожидает ввода команды и производит её выполнение.
     */
    public void execute() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean result = true;
        do {
            try {
                System.out.print("#> ");
                String fullCommand = reader.readLine();
                ParsedCommand parsedCommand = new ParsedCommand(fullCommand);
                if (parsedCommand.command == null || "".equals(parsedCommand.command)) {
                    continue;
                }
                Command cmd = commands.get(parsedCommand.command.toUpperCase());
                if (cmd == null) {
                    System.out.println("Команда не найдена.");
                    continue;
                }
                result = cmd.execute(parsedCommand.args);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            } catch (SystemException e) {
                System.out.println("Error: " + e);
            }
        } while (result);
    }
}
