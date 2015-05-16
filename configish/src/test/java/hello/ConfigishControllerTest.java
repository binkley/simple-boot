package hello;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * {@code RemoteHelloControllerTest} tests {@link ConfigishController}.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigishMain.class)
@WebAppConfiguration
public class ConfigishControllerTest {
    private MockMvc mvc;

    @Before
    public void setUp()
            throws Exception {
        mvc = standaloneSetup(new ConfigishController()).build();
    }

    @Test
    public void shouldGetConfiguration()
            throws Exception {
        mvc.perform(get("/config/test").
                accept(APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().
                        contentTypeCompatibleWith(APPLICATION_JSON)).
                andExpect(jsonPath("$.one", is(equalTo("two")))).
                andExpect(jsonPath("$['three.four']", is(equalTo(5))));
    }
}
