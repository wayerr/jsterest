package wayerr.jsterest.nashorn;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import wayerr.jsterest.AssertionError;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
class Console {
    private static final Logger LOG = Logger.getLogger(Console.class.getName());

    Console() {
    }

    @JsMethod(name = "assert")
    private void _assert(Object... args) {
        if(args.length == 0) {
            return;
        }
        Object expr = args[0];
        if(expr != null && (expr instanceof Boolean) && (Boolean)expr) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        Throwable th = printMsg(args, sb);
        if(th != null) {
            throw new AssertionError(sb.toString(), th);
        }
        throw new AssertionError(sb.toString());
    }

    @JsMethod
    public void debug(Object... args) {
        log(Level.INFO, args);
    }

    @JsMethod
    public void log(Object... args) {
        log(Level.INFO, args);
    }

    @JsMethod
    public void warn(Object... args) {
        log(Level.WARNING, args);
    }

    @JsMethod
    public void error(Object... args) {
        log(Level.SEVERE, args);
    }

    private void log(Level level, Object[] args) {
        StringBuilder sb = new StringBuilder();
        Throwable t = printMsg(args, sb);
        LOG.log(level, sb.toString(), t);
    }

    private Throwable printMsg(Object[] args, StringBuilder sb) {
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
        return t;
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
