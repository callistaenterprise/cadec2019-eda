package se.callista.cadec.eda.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ShippingApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShippingApplication.class, args);
  }
}
