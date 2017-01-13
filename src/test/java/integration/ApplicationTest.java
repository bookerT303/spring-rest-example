package integration;

import hello.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void homePage() throws Exception {
        mockMvc.perform(get("/index.html"))
            .andExpect(content().string(containsString("Get your greeting")));
    }

    @Test
    public void greeting() throws Exception {
        mockMvc.perform(get("/greeting"))
            .andExpect(content().string(containsString("Hello, alex Tester!")));
    }

    @Test
    public void greetingWithUser() throws Exception {
        mockMvc.perform(get("/greeting").param("name", "User"))
            .andExpect(content().string(containsString("Hello, brad User!")));
    }

}
