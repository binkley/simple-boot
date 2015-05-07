package hello;

import hello.HelloWorldController.FeignRemoteHello;
import hello.HelloWorldController.HystrixRemoteHello;
import hello.HelloWorldControllerTest.Mocks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * {@code RemoteHelloControllerTest} tests {@link HelloWorldController}.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {HelloWorldMain.class, Mocks.class})
@WebAppConfiguration
public class HelloWorldControllerTest {
    private MockMvc mvc;

    @Inject
    public FeignRemoteHello feign;
    @Inject
    public HystrixRemoteHello hystrix;

    @Configuration
    public static class Mocks {
        private final FeignRemoteHello feign = mock(FeignRemoteHello.class);

        @Bean
        @Primary
        public FeignRemoteHello feignRemoteHello() {
            return feign;
        }
    }

    @Before
    public void setUp()
            throws Exception {
        reset(feign);
        mvc = standaloneSetup(new HelloWorldController(hystrix)).build();
    }

    @Test
    public void shouldSayHelloToBob()
            throws Exception {
        final ArgumentCaptor<In> in = ArgumentCaptor.forClass(In.class);
        when(feign.greet(in.capture())).
                thenReturn(Greeting.builder().message("Hello, Bob!").build());

        mvc.perform(get("/hello/Bob").
                accept(APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(header().doesNotExist("Warning")).
                andExpect(content().
                        contentTypeCompatibleWith(APPLICATION_JSON)).
                andExpect(jsonPath("$.message", is(equalTo("Hello, Bob!"))));

        assertThat(in.getValue().getName(), is(equalTo("Bob")));
    }

    @Test
    public void shouldDieGracefully()
            throws Exception {
        when(feign.greet(any())).
                thenThrow(new RuntimeException("Things are broken."));

        mvc.perform(get("/hello/Bob").
                accept(APPLICATION_JSON)).
                andExpect(status().isServiceUnavailable()).
                andExpect(header().
                        string("Warning", "remote-hello unavailable")).
                andExpect(content().
                        contentTypeCompatibleWith(APPLICATION_JSON)).
                andExpect(
                        jsonPath("$.message", is(equalTo("No dice, Bob."))));
    }
}
