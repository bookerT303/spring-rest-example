package hello;

import hello.exception.CustomerNotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class GreetingController {
    private static final String useDefaultValue = "#{null}";
    private static final String template = "%s %s";
    private final AtomicLong counter = new AtomicLong();
    private final String defaultName;
    private final CustomerRepository repository;

    @Autowired
    public GreetingController(String defaultUserName, CustomerRepository repository) {
        this.defaultName = defaultUserName;
        this.repository = repository;
    }

    @ApiOperation(value = "Returns user greeting", notes = "Returns a greeting for the user.", response = Greeting.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of user detail", response = Greeting.class),
            @ApiResponse(code = 404, message = "User with given username does not exist")}
    )
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = useDefaultValue) String name, Model model)
            throws RuntimeException {
        if (name == null || useDefaultValue.equals(name)) {
            name = defaultName;
        }

        List<Customer> customers = repository.findByLastName(name);
        if (customers == null || customers.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Customer customer = customers.get(0);

        model.addAttribute("greeting", new Greeting(counter.incrementAndGet(),
                String.format(template, customer.getFirstName(), customer.getLastName())));
        return "greeting";

    }
}
