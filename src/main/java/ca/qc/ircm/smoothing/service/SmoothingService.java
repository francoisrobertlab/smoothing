package ca.qc.ircm.smoothing.service;

import ca.qc.ircm.progressbar.ProgressBar;
import ca.qc.ircm.smoothing.validation.WarningHandler;

import java.io.IOException;

/**
 * Services for smoothing BED files.
 */
public interface SmoothingService {
  /**
   * Smooth BED files.
   *
   * @param parameters
   *          smoothing parameters
   * @param progressBar
   *          records progression
   * @param warningHandler
   *          handles warnings
   * @throws IOException
   *           could not parse BED file or could not execute smoothing
   */
  public void smoothing(SmoothingParameters parameters, ProgressBar progressBar,
      WarningHandler warningHandler) throws IOException;
}
