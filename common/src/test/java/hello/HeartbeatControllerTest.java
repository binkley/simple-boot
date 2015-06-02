package hello;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * {@code RemoteHelloControllerTest} tests {@link HeartbeatController}.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestMain.class)
@WebAppConfiguration
public class HeartbeatControllerTest {
    private static final String service = "with-a-smile";
    private static final int port = 13;

    private MockMvc mvc;

    @Before
    public void setUp()
            throws Exception {
        mvc = standaloneSetup(new HeartbeatController(service, port)).build();
    }

    @Test
    public void shouldBeatWithJson()
            throws Exception {
        mvc.perform(get("/heartbeat").
                accept(APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().
                        contentTypeCompatibleWith(APPLICATION_JSON)).
                andExpect(jsonPath("$.service", is(equalTo(service)))).
                andExpect(jsonPath("$.port", is(equalTo(port))));
    }

    @Test
    public void shouldBeatWithText()
            throws Exception {
        mvc.perform(get("/heartbeat")).
                andExpect(status().isOk()).
                andExpect(content().
                        contentTypeCompatibleWith(TEXT_PLAIN)).
                andExpect(content().
                        string(startsWith(format("%s:", service))));
    }
}
