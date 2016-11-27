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

import java.nio.file.Path;
import jdk.nashorn.api.scripting.JSObject;
import wayerr.jsterest.Test;
import wayerr.jsterest.TestContext;

/**
 *
 * @author wayerr
 */
class TestOnNashorn implements Test {

    private final Path path;
    private final String name;
    private final JSObject test;

    TestOnNashorn(Path path, JSObject test) {
        this.path = path;
        final String fn = path.getFileName().toString();
        this.name = fn.substring(0, fn.lastIndexOf('.'));
        this.test = test;
    }
    
    @Override
    public void run(TestContext tc) throws Exception {
        test.call(null, tc);
    }

    @Override
    public String getName() {
        return name;
    }

}
