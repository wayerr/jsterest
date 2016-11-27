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
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import jdk.nashorn.api.scripting.JSObject;
import wayerr.jsterest.Test;

/**
 *
 * @author wayerr
 */
public class NashornTestFactory {

    private final ScriptEngine engine;

    public NashornTestFactory() {
        ScriptEngineManager sem = new ScriptEngineManager();
        this.engine = sem.getEngineByName("js");
    }

    public Test create(Path path) throws Exception {
        JSObject test;
        try (FileReader fr = new FileReader(path.toFile())) {
            test = (JSObject)engine.eval(fr);
        }
        if(!test.isFunction()) {
            throw new IllegalArgumentException("Test '" + path + "' must be a function.");
        }
        return new TestOnNashorn(path, test);
    }
}
