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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

/**
 *
 * @author wayerr
 */
public class App {

    private static final String NL = System.getProperty("line.separator");
    private static final Logger LOG = Logger.getLogger(App.class.getName());

    private class Arg {
        private final String name;
        private final String key;
        private final boolean want;
        private final List<String> values = new ArrayList<>(0);
        private boolean appear;

        public Arg(String name, char key, boolean wantValue) {
            this.name = "--" + name;
            this.key = "-" + key;
            this.want = wantValue;
        }

        public List<String> getValues() {
            return values;
        }

        public String getValue() {
            return values.isEmpty()? null : values.get(0);
        }

        void print(StringBuilder sb) {
            sb.append(key).append('\t').append(name).append('\t');
            if(want) {
                sb.append("[value]");
            }
            sb.append(NL);
        }
    }

    private final Map<String, Arg> argMap = new HashMap<>();
    private final List<Arg> argList = new ArrayList<>();
    private final Arg help;
    private final Arg tests;
    private final Arg logs;
    private final List<String> testNames = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        App main = new App();
        main.run(args);
    }

    App() {
        addArg(this.tests = new Arg("tests", 't', true));
        addArg(this.logs = new Arg("logs", 'l', true));
        addArg(this.help = new Arg("help", 'h', false));
    }

    private void addArg(Arg arg) {
        argList.add(arg);
        argMap.put(arg.name, arg);
        argMap.put(arg.key, arg);
    }

    void run(String[] args) throws Exception {
        parseArgs(args);
        if(args.length == 0 || help.appear) {
            printHelp();
            return;
        }
        configureLogging(logs.getValue());

        TestsRunner runner = new TestsRunner();
        runner.getSourceDirs().addAll(tests.getValues());
        runner.getTestsNames().addAll(testNames);

        runner.execute();
    }

    static void configureLogging(String logDir) throws Exception {
        // obtain root logger
        Logger global = LogManager.getLogManager().getLogger("");
        for(Handler handler : global.getHandlers()) {
            if(handler instanceof ConsoleHandler) {
                ConsoleHandler consoleHandler = (ConsoleHandler) handler;
                consoleHandler.setFormatter(LogFormatter.INSTANCE);
            }
        }

        if(logDir == null) {
            return;
        }
        if(logDir.indexOf('%') >= 0) {
            // we prevent path logging extensions, because must create dir for it, but can not interpolete its
            throw new IllegalArgumentException("Bad dir name: " + logDir);
        }
        Files.createDirectories(Paths.get(logDir));
        final FileHandler fh = new FileHandler(logDir + "/jsterest-%g.log", 1024 * 1024 * 100, 10, false);
        fh.setFormatter(LogFormatter.INSTANCE);
        global.addHandler(fh);
    }

    private void parseArgs(String[] args) {
        boolean expectValue = false;
        boolean expectTests = false;
        Arg curr = null;
        for(int i = 0; i < args.length; ++i) {
            final String arg = args[i];
            if(expectTests) {
                testNames.add(arg);
                continue;
            }
            if(!expectValue && arg.charAt(0) == '-') {
                curr = argMap.get(arg);
                if(curr == null) {
                    LOG.log(Level.WARNING, "Unknown arg: {0}", arg);
                    continue;
                }
                curr.appear = true;
                expectValue = curr.want;
                continue;
            } 
            expectValue = false;
            if(curr != null) {
                curr.values.add(arg);
                curr = null;
            } else {
                expectTests = true;
                testNames.add(arg);
            }
        }
    }

    private void printHelp() {
        StringBuilder sb = new StringBuilder();
        argList.forEach((v) -> {
            v.print(sb);
        });
        System.out.println(sb);
    }

}
