package ca.qc.ircm.util.javafx;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import javax.inject.Inject;

/**
 * Registers Guice bindings in afterburner.fx.
 */
public class AfterburnerGuiceInitializer {
  private static final Logger logger = LoggerFactory.getLogger(AfterburnerGuiceInitializer.class);
  @Inject
  private Injector injector;

  @Inject
  private void init() {
    Map<Key<?>, Binding<?>> bindings = injector.getAllBindings();
    for (Map.Entry<Key<?>, Binding<?>> bindingEntry : bindings.entrySet()) {
      Key<?> bindingKey = bindingEntry.getKey();
      Binding<?> binding = bindingEntry.getValue();
      if (!bindingKey.hasAttributes() && bindingKey.getTypeLiteral().getType() instanceof Class
          && !((Class<?>) bindingKey.getTypeLiteral().getType())
              .isAnnotationPresent(IgnoreAfterburner.class)) {
        // Only bindings without annotation is supported because afterburner does not use annotations or generics.
        Class<?> rawType = bindingKey.getTypeLiteral().getRawType();
        Object instance = binding.getProvider().get();
        com.airhacks.afterburner.injection.Injector.setModelOrService(rawType, instance);
        logger.debug("Registered binding class {} in afterburner", rawType.getName());
        logger.trace("Registered instance instance {} for class {} in afterburner",
            instance.getClass().getName(), rawType.getName());
      }
    }
  }
}
