package wayerr.jsterest.nashorn;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.NativeJSON;
import jdk.nashorn.internal.runtime.Undefined;
import wayerr.jsterest.AssertionError;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 */
class Console {
    private static final Logger LOG = Logger.getLogger("console");

    Console() {
    }

    @JsMethod(name = "assert")
    private void _assert(Object expr, Object... args) {
        if(args.length == 0) {
            return;
        }
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
        LogRecord lr = new LogRecord(level, sb.toString());
        lr.setThrown(t);
        //it need for prevent logger from walking over call stack
        // also it force to use logger name
        lr.setSourceClassName(null);
        lr.setLoggerName(LOG.getName());
        LOG.log(lr);
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
                Undefined undef = Undefined.getUndefined();
                String str = (String) NativeJSON.stringify(undef, o, undef, undef);
                sb.append(str);
                return;
            } catch (Exception e) {
                // nothing
            }
        }
        sb.append(o);
    }
}
