package wayerr.jsterest.nashorn;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
public class Console {
    private static final Logger LOG = Logger.getLogger(Console.class.getName());

    public Console() {
    }

    public void debug(Object... args) {
        log(Level.INFO, args);
    }

    public void log(Object... args) {
        log(Level.INFO, args);
    }

    public void warning(Object... args) {
        log(Level.WARNING, args);
    }

    public void error(Object... args) {
        log(Level.SEVERE, args);
    }

    private void log(Level level, Object[] args) {
        StringBuilder sb = new StringBuilder();
        Throwable t = null;
        for (Object o : args) {
            if (o instanceof Throwable) {
                t = (Throwable) o;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            toString(sb, o);
        }
        LOG.log(level, sb.toString(), t);
    }

    private void toString(StringBuilder sb, Object o) {
        if (o instanceof ScriptObjectMirror) {
            try {
                String str = ((ScriptObjectMirror) o).to(String.class);
                sb.append(str);
                return;
            } catch (Exception e) {
                // nothing
            }
        }
        sb.append(o);
    }
}
