package se.callista.cadec.eda.invoicing.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class IntegrationConfig {

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
