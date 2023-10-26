package nl.rug.aoop.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Command handler class.
 */

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Registers a command with a given name.
     *
     * @param commandName The name of the command to register.
     * @param command     The command to register.
     */
    public void registerCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * Executes a command with the specified name and parameters.
     *
     * @param commandName The name of the command to execute.
     * @param params      The parameters to pass to the command.
     */

    public void executeCommand(String commandName, Map<String, Object> params) {
        System.out.println("CommandHandler received " + commandName);
        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(params);
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}