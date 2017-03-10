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

import java.io.FileReader;
import java.nio.file.Path;
import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import wayerr.jsterest.SafeCloseable;
import wayerr.jsterest.Test;
import wayerr.jsterest.TestsRegistry;

/**
 *
 * @author wayerr
 */
public class NashornTestFactory {

    private final NashornScriptEngine engine;

    public NashornTestFactory(TestsRegistry testsRegistry) throws Exception {
        // it enable es6
        System.setProperty("nashorn.option.strict", "true");
        System.setProperty("nashorn.option.language", "es6");
        ScriptEngineManager sem = new ScriptEngineManager();
        this.engine = (NashornScriptEngine) sem.getEngineByName("js");
        DefaultBindings.init(this.engine, testsRegistry);
    }

    public Test create(Path path) throws Exception {
        JSObject test;
        final String pathString = path.toAbsolutePath().toString();
        CompiledScript script;
        try (FileReader fr = new FileReader(path.toFile())) {
            final Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
            bindings.put(ScriptEngine.FILENAME, pathString);
            script = engine.compile(fr);
            bindings.remove(ScriptEngine.FILENAME);
        }
        return new TestOnNashorn(path, (tc) -> {
            JSObject res = (JSObject) script.eval(new SimpleBindings(tc.getAttributes()));
            if(res == null || !res.isFunction()) {
                throw new IllegalArgumentException("Test script '" + pathString + "' must return function.");
            }
            return res.call(null);
        });
    }
}
