package ca.qc.ircm.smoothing.service;

import com.google.inject.assistedinject.Assisted;

import ca.qc.ircm.progress_bar.SimpleProgressBar;
import ca.qc.ircm.smoothing.validation.WarningHandlerNoter;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Create graphs based on analysis results.
 */
public class SmoothingTask extends Task<Void> {
  private class InternalProgressBar extends SimpleProgressBar {
    private static final long serialVersionUID = 3822582140618354796L;

    @Override
    public void messageChanged(String newMessage) {
      updateMessage(newMessage);
    }

    @Override
    public void progressChanged(double newProgress) {
      SmoothingTask.this.updateProgress(newProgress, 1.0);
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(SmoothingTask.class);
  private final SmoothingService smoothingService;
  private final SmoothingParameters parameters;
  private List<String> warnings = new ArrayList<>();

  @Inject
  protected SmoothingTask(SmoothingService smoothingService,
      @Assisted SmoothingParameters parameters) {
    this.smoothingService = smoothingService;
    this.parameters = parameters;
  }

  @Override
  protected Void call() throws Exception {
    try {
      WarningHandlerNoter warningHandler = new WarningHandlerNoter();
      smoothingService.smoothing(parameters, new InternalProgressBar(), warningHandler);
      warnings.clear();
      warnings.addAll(warningHandler.getWarnings());
      return null;
    } catch (Exception e) {
      logger.error("Smoothing did not complete normaly", e);
      throw e;
    }
  }

  public List<String> getWarnings() {
    return warnings;
  }
}
