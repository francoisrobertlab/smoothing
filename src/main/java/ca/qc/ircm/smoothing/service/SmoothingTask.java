package ca.qc.ircm.smoothing.service;

import ca.qc.ircm.progressbar.JavafxProgressBar;
import ca.qc.ircm.smoothing.validation.WarningHandlerNoter;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Create graphs based on analysis results.
 */
public class SmoothingTask extends Task<Void> {
  private static final Logger logger = LoggerFactory.getLogger(SmoothingTask.class);
  private final SmoothingService smoothingService;
  private final SmoothingParameters parameters;
  private List<String> warnings = new ArrayList<>();

  protected SmoothingTask(SmoothingService smoothingService, SmoothingParameters parameters) {
    this.smoothingService = smoothingService;
    this.parameters = parameters;
  }

  @Override
  protected Void call() throws Exception {
    try {
      WarningHandlerNoter warningHandler = new WarningHandlerNoter();
      JavafxProgressBar progressBar = new JavafxProgressBar();
      progressBar.message().addListener((observable, ov, nv) -> updateMessage(nv));
      progressBar.progress()
          .addListener((observable, ov, nv) -> updateProgress(nv.doubleValue(), 1.0));
      smoothingService.smoothing(parameters, progressBar, warningHandler);
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
