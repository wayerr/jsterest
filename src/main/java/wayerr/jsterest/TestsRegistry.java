/*
 * Copyright 2016 wayerr <radiofun@ya.ru>.
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
package wayerr.jsterest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wayerr
 */
public class TestsRegistry {
    private final Map<String, Test> tests = new HashMap<>();

    public void add(Test test) {
        this.tests.put(test.getName(), test);
    }

    public Test get(String name) {
        final Test test = this.tests.get(name);
        return test;
    }
}
