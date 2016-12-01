package wayerr.jsterest;

import lombok.Data;
import wayerr.jsterest.nashorn.NashornTestFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
@Data
class TestsRunner {
    private static final Logger LOG = Logger.getLogger(TestsRunner.class.getName());
    private final TestsRegistry registry = new TestsRegistry();
    private final List<String> sourceDirs = new ArrayList<>();
    private final List<String> testsNames = new ArrayList<>();
    private boolean failAtFirstError = false;

    TestsRunner() {
    }

    private void findTests(TestsRegistry tr) throws Exception {
        LOG.info("Find tests");
        NashornTestFactory ntf = new NashornTestFactory(tr);
        for(String dir: sourceDirs) {
            LOG.log(Level.INFO, "Scan test dir: {0}", dir);
            final Path startDir = Paths.get(dir);
            File startDirFile = startDir.toFile();
            if(!startDirFile.isDirectory()) {
                LOG.severe("The path: " + startDirFile.getCanonicalPath() + " is not dir or not exists.");
            }
            Files.walk(startDir).forEach(file -> {
                if(!Files.isRegularFile(file) || !file.getFileName().toString().endsWith(".js")) {
                    return;
                }
                try {
                    LOG.log(Level.SEVERE, "Find test file: {0}", file);
                    final Test test = ntf.create(file);
                    LOG.log(Level.SEVERE, "Load test file: {0} as {1}", new Object[]{file, test.getName()});
                    tr.add(test);
                } catch(Exception e) {
                    LOG.log(Level.SEVERE, "Can not load test file: " + file + ", due to error.", e);
                }
            });
        }
    }

    void execute() throws Exception {
        TestsRegistry tr = new TestsRegistry();
        findTests(tr);
        LOG.info("Begin tests");
        TestContext tc = new TestContext();
        for(String testName: testsNames) {
            LOG.log(Level.INFO, "Excute test: {0}", testName);
            final Test test = tr.get(testName);
            if(test == null) {
                LOG.log(Level.SEVERE, "Can not find test: {0} \n exiting.", testName);
                return;
            }
            try {
                test.run(tc);
                LOG.log(Level.INFO, "Test: {0} is successfully executed.", testName);
            } catch(Exception e) {
                if(failAtFirstError) {
                    throw e;
                }
                LOG.log(Level.SEVERE, "Test: " + testName + " is executed with error.", e);
            }
        }
        LOG.info("End tests");
    }
}
