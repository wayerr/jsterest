/*
 * Copyright 2016 wayerr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wayerr.jsterest.nashorn;

import javax.script.ScriptEngine;
import jdk.nashorn.api.scripting.AbstractJSObject;
import wayerr.jsterest.Test;
import wayerr.jsterest.TestContext;
import wayerr.jsterest.TestsRegistry;

/**
 *
 * @author wayerr
 */
public class Loader extends AbstractJSObject {

    private final ScriptEngine engine;
    private final TestsRegistry tr;

    public Loader(TestsRegistry tr, ScriptEngine engine) {
        this.tr = tr;
        this.engine = engine;
    }

    @Override
    public Object call(Object thiz, Object... args) {
        String name = (String) args[0];
        if(name == null) {
            throw new IllegalArgumentException("First argument: name of script is null.");
        }
        Test test = tr.get(name);
        try {
            return test.run(TestContext.getCurrent());
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
