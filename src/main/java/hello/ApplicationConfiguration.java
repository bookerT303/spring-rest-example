package hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {
    @Value("${greeting.default.user}")
    String defaultUserName;

    @Bean
    public String defaultUserName() {
        return defaultUserName;
    }
}
