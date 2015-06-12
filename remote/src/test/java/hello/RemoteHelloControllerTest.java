package hello;

import guru.nidi.ramltester.RamlDefinition;
import guru.nidi.ramltester.SimpleReportAggregator;
import guru.nidi.ramltester.junit.ExpectedUsage;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static guru.nidi.ramltester.RamlLoaders.fromClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * {@code RemoteHelloControllerTest} tests {@link RemoteHelloController}.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RemoteHelloMain.class)
@WebAppConfiguration
public class RemoteHelloControllerTest {
    private static final RamlDefinition api = fromClasspath(
            RemoteHelloMain.class).
            load("remote-hello.raml");
    private static final SimpleReportAggregator report
            = new SimpleReportAggregator();

    @ClassRule
    public static ExpectedUsage usage = new ExpectedUsage(report);

    private MockMvc mvc;

    @Before
    public void setUp()
            throws Exception {
        mvc = standaloneSetup(new RemoteHelloController()).build();
    }

    @Test
    public void shouldGreetBob()
            throws Exception {
        mvc.perform(post("/greet").
                header("X-Correlation-ID", "Fred").
                contentType(APPLICATION_JSON).
                content("{\"name\":\"Bob\"}").
                accept(APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().
                        contentTypeCompatibleWith(APPLICATION_JSON)).
                andExpect(jsonPath("$.message",
                        is(equalTo("Hats off to you, Bob!")))).
                andExpect(api.matches().aggregating(report));
    }
}
