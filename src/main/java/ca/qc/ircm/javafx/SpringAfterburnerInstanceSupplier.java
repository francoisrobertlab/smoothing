package ca.qc.ircm.javafx;

import java.util.function.Function;
import org.springframework.context.ApplicationContext;

/**
 * Spring instance supplier for afterburner.fx.
 */
public class SpringAfterburnerInstanceSupplier implements Function<Class<?>, Object> {
  private ApplicationContext applicationContext;

  public SpringAfterburnerInstanceSupplier(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public Object apply(Class<?> clazz) {
    return applicationContext.getBean(clazz);
  }
}
