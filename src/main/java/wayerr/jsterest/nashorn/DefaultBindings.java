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

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import wayerr.jsterest.TestsRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;

/**
 *
 * @author wayerr
 */
class DefaultBindings {
    static void init(ScriptEngine se, TestsRegistry testsRegistry) throws Exception {
        Bindings target = se.getBindings(ScriptContext.GLOBAL_SCOPE);
        target.put("console", new Console());
        target.put("io", new IO());
        load(se, "http.js");
        Loader loader = new Loader(testsRegistry, se);
        target.put("include", loader);
    }

    private static void load(ScriptEngine se, String resource) throws Exception {
        Bindings engineScope = se.getBindings(ScriptContext.ENGINE_SCOPE);
        Bindings globalScope = se.getBindings(ScriptContext.GLOBAL_SCOPE);
        URL url = DefaultBindings.class.getResource(resource);
        try(InputStream is = url.openStream()) {
            engineScope.put(ScriptEngine.FILENAME, url.getFile());
            se.eval(new InputStreamReader(is, StandardCharsets.UTF_8));
            engineScope.remove(ScriptEngine.FILENAME);
            globalScope.putAll(engineScope);
            engineScope.clear();
        }
    }

    public static class Console {
        private static final Logger LOG = Logger.getLogger(Console.class.getName());
        
        public Console() {
        }

        public void debug(Object ... args) {
            log(Level.INFO, args);
        }

        public void warning(Object ... args) {
            log(Level.WARNING, args);
        }

        public void error(Object ... args) {
            log(Level.SEVERE, args);
        }

        private void log(Level level, Object[] args) {
            StringBuilder sb = new StringBuilder();
            Throwable t = null;
            for(Object o: args) {
                if(o instanceof Throwable) {
                    t = (Throwable)o;
                }
                if(sb.length() > 0) {
                    sb.append(' ');
                }
                toString(sb, o);
            }
            LOG.log(level, sb.toString(), t);
        }

        private void toString(StringBuilder sb, Object o) {
            if(o instanceof ScriptObjectMirror) {
                try {
                    String str = ((ScriptObjectMirror) o).to(String.class);
                    sb.append(str);
                    return;
                } catch (Exception e) {
                    // nothing
                }
            }
            sb.append(o);
        }
    }

    public static class IO {

        /**
         * Write as UTF-8 and close.
         * @param src
         * @param os
         * @throws IOException
         */
        public void writeFully(String src, OutputStream os) throws IOException {
            try {
                os.write(src.getBytes(StandardCharsets.UTF_8));
            } finally {
                os.close();
            }
        }

        /**
         * read as UTF-8 and close
         * @param is
         * @return
         * @throws IOException
         */
        public String readFully(InputStream is) throws IOException {
            return readFully(new InputStreamReader(is, StandardCharsets.UTF_8));
        }

        /**
         * Read and close
         * @param r
         * @return
         * @throws IOException
         */
        public String readFully(Reader r) throws IOException {
            try {
                char[] buff = new char[1024];
                int readed;
                StringBuilder sb = new StringBuilder();
                while((readed = r.read(buff)) != -1) {
                    sb.append(buff, 0, readed);
                }
                return sb.toString();
            } finally {
                r.close();
            }
        }
    }
}
