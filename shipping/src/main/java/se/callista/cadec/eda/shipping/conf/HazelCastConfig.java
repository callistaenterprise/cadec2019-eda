package se.callista.cadec.eda.shipping.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;

@Configuration
public class HazelCastConfig {

  @Bean
  Config hazelCast() {
    Config config = new Config();
    config.setInstanceName("hazelcast-customer")
        .addMapConfig(new MapConfig()
            .setName("customer")
            .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
            .setTimeToLiveSeconds(0));
    return config;
  }

}
