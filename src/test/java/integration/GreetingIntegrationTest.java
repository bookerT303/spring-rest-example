package integration;

import hello.Application;
import hello.GreetingController;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)   // 1
@SpringApplicationConfiguration(classes = Application.class)   // 2
@WebAppConfiguration   // 3
@IntegrationTest("server.port:8081")   // 4
public class GreetingIntegrationTest {

    RestTemplate template = new TestRestTemplate();

    @Test
    public void canGetGreeting() {
        ResponseEntity<String> response = template.getForEntity("http://localhost:8081/greeting", String.class);
        assertEquals("Should have been 200 ok", response.getStatusCode(), HttpStatus.OK);
        assertTrue("Should have greeted Tester but got "+response.getBody(), response.getBody().contains("Hello, alex Tester!"));
    }

    @Test
    public void canGetGreetingForUser() {
        ResponseEntity<String> response = template.getForEntity("http://localhost:8081/greeting?name=User", String.class);
        assertEquals("Should have been 200 ok", response.getStatusCode(), HttpStatus.OK);
        assertTrue("Should have greeted User but got "+response.getBody(), response.getBody().contains("Hello, brad User!"));
    }
}
