package unit;

import hello.Customer;
import hello.CustomerRepository;
import hello.GreetingController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class GreetingControllerTest {
    @InjectMocks
    GreetingController controller;

    CustomerRepository repository = mock(CustomerRepository.class);

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        controller = new GreetingController("World", repository);
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void testGetGreeting() throws Exception {
        Customer world = new Customer("Iam", "World");
        List<Customer> asList = new ArrayList<>();
        asList.add(world);
        when(repository.findByLastName("World")).thenReturn(asList);

        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.content", equalTo("Hello, Iam World!")))
                .andExpect(header().string("content-type", "application/json;charset=UTF-8"));
        verify(repository).findByLastName("World");
    }

    @Test
    public void testGetGreetingForUser() throws Exception {
        Customer world = new Customer("Iam", "User");
        List<Customer> asList = new ArrayList<>();
        asList.add(world);
        when(repository.findByLastName("User")).thenReturn(asList);

        mockMvc.perform(get("/greeting?name=User"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.content", equalTo("Hello, Iam User!")))
                .andExpect(header().string("content-type", "application/json;charset=UTF-8"));
        verify(repository).findByLastName("User");
    }

    @Test
    public void testGetGreetingIncrementsId() throws Exception {
        Customer world = new Customer("Iam", "Tester");
        List<Customer> asList = new ArrayList<>();
        asList.add(world);
        when(repository.findByLastName("User1")).thenReturn(asList);
        when(repository.findByLastName("User2")).thenReturn(asList);

        mockMvc.perform(get("/greeting?name=User1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(header().string("content-type", "application/json;charset=UTF-8"));
        mockMvc.perform(get("/greeting?name=User2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(2)))
                .andExpect(header().string("content-type", "application/json;charset=UTF-8"));
        verify(repository).findByLastName("User1");
        verify(repository).findByLastName("User2");
    }

    @Test
    public void testGetGreetingWhenNoneExists() throws Exception {
        Customer world = new Customer("Iam", "User");
        List<Customer> asList = new ArrayList<>();
        when(repository.findByLastName("User")).thenReturn(asList);

        mockMvc.perform(get("/greeting?name=User"))
                .andExpect(status().isNotFound());
        verify(repository).findByLastName("User");
    }
}
