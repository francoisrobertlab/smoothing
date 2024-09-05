package ca.qc.ircm.smoothing;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Java FX application using Spring Boot.
 */
public abstract class AbstractSpringBootJavafxApplication extends Application {
  protected ConfigurableApplicationContext applicationContext;

  @Override
  public void init() throws Exception {
    applicationContext =
        SpringApplication.run(getClass(), getParameters().getRaw().toArray(new String[0]));
    applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    applicationContext.close();
  }
}
