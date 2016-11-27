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
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import wayerr.jsterest.Test;

/**
 *
 * @author wayerr
 */
public class NashornTestFactory {

    private final NashornScriptEngine engine;

    public NashornTestFactory() throws Exception {
        ScriptEngineManager sem = new ScriptEngineManager();
        this.engine = (NashornScriptEngine) sem.getEngineByName("js");
        DefaultBindings.init(this.engine);
    }

    public Test create(Path path) throws Exception {
        JSObject test;
        final String pathString = path.toAbsolutePath().toString();
        CompiledScript script;
        try (FileReader fr = new FileReader(path.toFile())) {
            script = engine.compile(fr);
        }
        return new TestOnNashorn(path, (tc) -> {
            //below is importatnt because allow us to debug js files in Java Ide
            final SimpleBindings local = new SimpleBindings();
            local.put(ScriptEngine.FILENAME, pathString);
            JSObject res = (JSObject) script.eval(local);
            if(res == null || !res.isFunction()) {
                throw new IllegalArgumentException("Test script '" + pathString + "' must return function.");
            }
            res.call(null, tc);
        });
    }
}
