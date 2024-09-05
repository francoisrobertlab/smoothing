package ca.qc.ircm.smoothing;

import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Spring.
 */
@Configuration
public class SpringConfiguration {
  @Bean
  public Executor executor() {
    return new DefaultExecutor();
  }
}
