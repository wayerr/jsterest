package wayerr.jsterest;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

/**
 */
public class AppTest {

    @Rule
    public WireMockRule wireMock = new WireMockRule(18080);

    @Test
    public void test() throws Exception {
        App app = new App();
        String projectDir = System.getProperty("user.dir");
        String tmpDir = System.getProperty("tmp.dir");
        app.run(new String[]{"-l", tmpDir + "/jsterest/log/", "-t", projectDir + "/src/test/resources/", "sample"});
    }
}
