package wayerr.jsterest.nashorn;

import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;

import java.lang.reflect.Method;

/**
 */
class Clone extends AbstractJSObject {
    @Override
    public Object call(Object thiz, Object... args) {
        Object object = args[0];
        if(object instanceof ScriptObjectMirror) {
            ScriptObjectMirror mirror = (ScriptObjectMirror) object;
            ScriptObject so = (ScriptObject) ScriptUtils.unwrap(mirror);
            ScriptObject copy = so.copy();
            return ScriptUtils.wrap(copy);
        }
        try {
            Method clone = object.getClass().getDeclaredMethod("clone");
            clone.setAccessible(true);
            return clone.invoke(object);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException("Can not clone: " + object, e);
        }
    }
}
