package ca.qc.ircm.smoothing.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks all handled validation warnings.
 */
public class WarningHandlerNoter implements WarningHandler {
  private List<String> warnings = new ArrayList<String>();
  private WarningHandler delegate;

  public WarningHandlerNoter() {
  }

  public WarningHandlerNoter(WarningHandler delegate) {
    this.delegate = delegate;
  }

  @Override
  public void handle(String warning) {
    warnings.add(warning);
    if (delegate != null) {
      delegate.handle(warning);
    }
  }

  public boolean hasWarning() {
    return !warnings.isEmpty();
  }

  public List<String> getWarnings() {
    return warnings;
  }
}
