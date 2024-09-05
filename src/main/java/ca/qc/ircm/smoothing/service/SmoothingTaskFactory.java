package ca.qc.ircm.smoothing.service;

import javax.inject.Inject;
import org.springframework.stereotype.Component;

/**
 * Factory for {@link SmoothingTask}.
 */
@Component
public class SmoothingTaskFactory {
  @Inject
  private SmoothingService smoothingService;

  public SmoothingTask create(SmoothingParameters parameters) {
    return new SmoothingTask(smoothingService, parameters);
  }
}
