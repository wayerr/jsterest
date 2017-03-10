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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author wayerr
 */
public final class TestContext {

    public static class Enclosing implements SafeCloseable {
        private final Test test;
        private final TestContext owner;
        private final TestContext old;

        Enclosing(Test test, TestContext tc) {
            this.test = test;
            this.owner = tc;
            this.old = TL.get();
            TL.set(tc);
        }

        @Override
        public void close() {
            TestContext curr = TL.get();
            if(curr != this.owner) {
                throw new IllegalStateException("Current context is not identity with enclosing owner.");
            }
            TL.remove();
            TL.set(old);
            final Enclosing last = this.owner.stack.removeLast();
            if(last != this) {
                throw new IllegalStateException("Last enclosing of current context is not identity with 'this'.");
            }
        }
    }

    private static final ThreadLocal<TestContext> TL = new ThreadLocal<>();
    private final ConcurrentMap<String, Object> attrs = new ConcurrentHashMap<>();
    private final Deque<Enclosing> stack = new ArrayDeque<>();
    
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
     * @param test current test for context enclosing
     * @return handler to remove it from thread local
     */
    public Enclosing open(Test test) {
        final Enclosing enc = new Enclosing(test, this);
        stack.addLast(enc);
        return enc;
    }

    public static TestContext getCurrent() {
        return TL.get();
    }

    public Test getTest() {
        final Enclosing last = stack.peekLast();
        if(last == null) {
            return null;
        }
        return last.test;
    }
}
