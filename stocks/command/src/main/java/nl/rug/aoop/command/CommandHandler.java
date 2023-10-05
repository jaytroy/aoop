package nl.rug.aoop.command;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();

    public void registerCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public void executeCommand(String commandName, Map<String, Object> params) {
        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(params);
        }
    }
}