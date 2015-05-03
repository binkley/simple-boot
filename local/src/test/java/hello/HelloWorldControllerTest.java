package hello;

import hello.HelloWorldController.RemoteHello;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static hello.HelloWorldController.In;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * {@code RemoteHelloControllerTest} tests {@link HelloWorldController}.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HelloWorldMain.class)
@WebAppConfiguration
public class HelloWorldControllerTest {
    private RemoteHello remote;
    private MockMvc mvc;

    @Before
    public void setUp()
            throws Exception {
        remote = mock(RemoteHello.class);
        mvc = standaloneSetup(new HelloWorldController(remote)).build();
    }

    @Test
    public void shouldSayHello()
            throws Exception {
        final ArgumentCaptor<In> in = ArgumentCaptor.forClass(In.class);
        when(remote.greet(in.capture())).
                thenReturn(Greeting.builder().message("Hello, Bob!").build());

        mvc.perform(get("/hello/Bob").
                accept(APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().
                        contentTypeCompatibleWith(APPLICATION_JSON)).
                andExpect(jsonPath("$.message", is(equalTo("Hello, Bob!"))));

        assertThat(in.getValue().getName(), is(equalTo("Bob")));
    }
}
