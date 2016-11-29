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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author wayerr
 */
public final class TestContext {
    private static final ThreadLocal<TestContext> TL = new ThreadLocal<>();
    private final ConcurrentMap<String, Object> attrs = new ConcurrentHashMap<>();
    
    /**
     * Return mutable set of context attributes.
     * @return
     */
    public ConcurrentMap<String, Object> getAttributes() {
        return attrs;
    }

    @Override
    public String toString() {
        return "TestContext{" + "attributes=" + attrs + '}';
    }

    /**
     * Place this into thread local for {@link #getCurrent()}
     * @return handler to remove it from thread local
     */
    public SafeCloseable open() {
        TestContext old = TL.get();
        TL.set(this);
        return () -> {
            TestContext curr = TL.get();
            if(curr != this) {
                throw new IllegalStateException("Current context is not identity with 'this'.");
            }
            TL.remove();
            TL.set(old);
        };
    }


    public static TestContext getCurrent() {
        return TL.get();
    }
}
