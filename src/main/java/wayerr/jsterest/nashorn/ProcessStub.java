package wayerr.jsterest.nashorn;

import java.util.Map;

/**
 * Class which is provide some things like node.js 'process' object.
 */
public class ProcessStub {

    public Map<String, String> getEnv() {
        return System.getenv();
    }
}
