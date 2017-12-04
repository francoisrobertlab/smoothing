package ca.qc.ircm.smoothing.service;

import org.springframework.stereotype.Component;

import javax.inject.Inject;

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
