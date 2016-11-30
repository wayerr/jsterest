package wayerr.jsterest;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 */
public class AppTest {

    @Rule
    public WireMockRule wireMock = new WireMockRule(18080);

    @Before
    public void before() {
        wireMock.stubFor(post(urlEqualTo("/api/login"))
          .willReturn(response()
            .withBody("{\"key\":\"xTOKEN\"}")));
        wireMock.stubFor(get(urlEqualTo("/api/list"))
          .willReturn(response()
            .withBody("[1,2,3,4]")));
    }

    private static ResponseDefinitionBuilder response() {
        return aResponse()
          .withHeader("Content-Type", "application/json");
    }

    @Test
    public void test() throws Exception {
        App app = new App();
        String projectDir = System.getProperty("user.dir");
        String tmpDir = System.getProperty("tmp.dir");
        app.run(new String[]{"-l", tmpDir + "/jsterest/log/", "-t", projectDir + "/src/test/resources/", "sample"});
    }
}
