package wayerr.jsterest.nashorn;

import jdk.nashorn.api.scripting.AbstractJSObject;

/**
 */
class JsFunctionObject extends AbstractJSObject {
    private final JsFunction func;

    public JsFunctionObject(JsFunction func) {
        this.func = func;
    }

    @Override
    public Object call(Object thiz, Object... args) {
        try {
            return func.apply(thiz, args);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isFunction() {
        return true;
    }
}
