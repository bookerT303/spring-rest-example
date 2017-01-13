package unit;

import hello.Customer;
import hello.CustomerRepository;
import hello.Greeting;
import hello.GreetingController;
import hello.exception.CustomerNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GreetingControllerTest {
    GreetingController controller;

    @Mock
    CustomerRepository repository;
    @Mock
    Model model;

    @Before
    public void setup() throws Exception {
        controller = new GreetingController("World", repository);
    }

    @Test
    public void testGetGreeting() throws Exception {
        Customer world = new Customer("IamD", "World");
        List<Customer> asList = asList(world);
        when(repository.findByLastName(anyString())).thenReturn(asList);

        String templateName = controller.greeting(null, model);
        assertThat(templateName, equalTo("greeting"));

        ArgumentCaptor<Greeting> greetingCaptor = ArgumentCaptor.forClass(Greeting.class);
        verify(model).addAttribute(eq("greeting"), greetingCaptor.capture());

        assertThat(greetingCaptor.getValue().getFullName(), equalTo("IamD World"));

        verify(repository).findByLastName("World");
    }

    @Test
    public void testGetGreetingForUser() throws Exception {
        Customer user = new Customer("Iam", "User");
        List<Customer> asList = asList(user);
        when(repository.findByLastName(anyString())).thenReturn(asList);

        String templateName = controller.greeting("User", model);
        assertThat(templateName, equalTo("greeting"));

        ArgumentCaptor<Greeting> greetingCaptor = ArgumentCaptor.forClass(Greeting.class);
        verify(model).addAttribute(eq("greeting"), greetingCaptor.capture());

        assertThat(greetingCaptor.getValue().getFullName(), equalTo("Iam User"));

        verify(repository).findByLastName("User");
    }

    @Test
    public void testGetGreetingIncrementsId() throws Exception {
        List<Customer> asList1 = asList(new Customer("Iam", "Tester"));
        List<Customer> asList2 = asList(new Customer("Iam", "Tester 2"));
        when(repository.findByLastName(anyString()))
            .thenReturn(asList1)
            .thenReturn(asList2);

        controller.greeting("User1", model);

        ArgumentCaptor<Greeting> greetingCaptor = ArgumentCaptor.forClass(Greeting.class);
        verify(model).addAttribute(eq("greeting"), greetingCaptor.capture());

        assertThat(greetingCaptor.getValue().getFullName(), equalTo("Iam Tester"));
        long user1Id = greetingCaptor.getValue().getId();

        reset(model);
        controller.greeting("User2", model);

        ArgumentCaptor<Greeting> greetingCaptor2 = ArgumentCaptor.forClass(Greeting.class);
        verify(model).addAttribute(eq("greeting"), greetingCaptor2.capture());

        assertThat(greetingCaptor2.getValue().getFullName(), equalTo("Iam Tester 2"));
        assertThat(greetingCaptor2.getValue().getId() - 1, equalTo(user1Id));
        verify(repository).findByLastName("User1");
        verify(repository).findByLastName("User2");
    }

    @Test
    public void testGetGreetingWhenNoneExists() throws Exception {
        when(repository.findByLastName(anyString())).thenReturn(emptyList());

        try {
            controller.greeting("User", model);
            fail("Expected exception not thrown");
        } catch (CustomerNotFoundException expected) {
            assertNotNull(expected);
        } catch (Exception unexpected) {
            fail("Received unexpected exception" + unexpected.getMessage());
        }

        verify(repository).findByLastName("User");
    }
}
