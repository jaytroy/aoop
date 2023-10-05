package nl.rug.aoop.command;

import java.util.Map;

public interface Command {
    /**
     * Can be used to pass various key value pairs. In our example, putCommand and message.
     * @param params
     */
    void execute(Map<String, Object> params);
}
