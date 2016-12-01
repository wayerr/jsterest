package wayerr.jsterest.nashorn;

/**
 */
interface JsFunction {
    Object apply(Object thiz, Object[] args) throws Exception;
}
