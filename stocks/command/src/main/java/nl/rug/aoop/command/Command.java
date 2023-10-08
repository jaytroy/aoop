package nl.rug.aoop.command;

import java.util.Map;

/**
 * Command interface.
 */

public interface Command {
    /**
     * Can be used to pass various key value pairs. In our example, putCommand and message.
     * @param params the command.
     */
    void execute(Map<String, Object> params);
}
