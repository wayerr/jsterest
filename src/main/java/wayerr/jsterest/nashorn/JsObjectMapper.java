package wayerr.jsterest.nashorn;

import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.JSObject;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 */
class JsObjectMapper extends AbstractJSObject {
    private final Map<String, JSObject> members = new HashMap<>();
    private final Object thiz;

    public JsObjectMapper(Object target) throws Exception {
        this.thiz = target;
        Class<?> clazz = target.getClass();
        for(Method method: clazz.getDeclaredMethods()) {
            JsMethod ann = method.getDeclaredAnnotation(JsMethod.class);
            if(ann == null) {
                continue;
            }
            String name = ann.name();
            if(name.isEmpty()) {
                name = method.getName();
            }
            method.setAccessible(true);
            String finalName = name;
            members.put(name, new JsFunctionObject((t, jsargs) -> {
                Object[] args = convertArgs(method, finalName, jsargs);
                try {
                    return method.invoke(thiz, args);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if(cause instanceof Exception) {
                        throw (Exception) cause;
                    }
                    throw e;
                }
            }));
        }
    }

    private Object[] convertArgs(Method method, String finalName, Object[] jsargs) {
        Class<?>[] types = method.getParameterTypes();
        Object[] args = new Object[types.length];
        final int argCnt = Math.min(jsargs.length, args.length);
        for(int i = 0; i < argCnt; ++i) {
            Object src = jsargs[i];
            final Class<?> type = types[i];
            final boolean varArg = method.isVarArgs() && types.length - 1 == i;
            Class<?> actualType = type;
            if(varArg) {
                actualType = type.getComponentType();
            }
            if(!actualType.isInstance(src)) {
                throw new IllegalArgumentException("Function " + finalName + " arg[" + i
                  + "] has value: '" + src + "' which is can not be converted to required type: " + actualType);
            }
            if(varArg) {
                int remain = jsargs.length - i;
                Object varArray = Array.newInstance(actualType, remain);
                for(int j = 0; j < remain; ++j) {
                    Object tmp = jsargs[i + j];
                    Array.set(varArray, j, tmp);
                }
                src = varArray;
            }
            args[i] = src;
        }
        return args;
    }

    @Override
    public boolean hasMember(String name) {
        return members.containsKey(name);
    }

    @Override
    public Object getMember(String name) {
        return members.get(name);
    }

}
