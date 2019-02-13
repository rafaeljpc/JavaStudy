package io.github.rafaeljpc.server.oauthserver.config;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.rafaeljpc.server.oauthserver.helper.OAuthHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfig.class
)
@AutoConfigureMockMvc
public class HelloMvcTest {

    @Autowired
    private OAuthHelper helper;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }


    @Test
    @WithMockUser(username = "user@test.com", password = "password")
    public void shouldAllowUserWithNoAuthorities() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/hello?name=Raf")
                .with(helper.bearerToken("my-client"))
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.greetings", is("Welcome Raf!")));

    }

    @Test
    public void shouldRejectIfNoAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/hello?name=Seb")
                .accept(MediaType.ALL))
                .andExpect(status().isUnauthorized());

    }
}

