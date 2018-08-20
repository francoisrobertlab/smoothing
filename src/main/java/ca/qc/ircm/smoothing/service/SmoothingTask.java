/*
 * Copyright (c) 2015 Institut de recherches cliniques de Montreal (IRCM)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package ca.qc.ircm.smoothing.service;

import ca.qc.ircm.progressbar.JavafxProgressBar;
import ca.qc.ircm.smoothing.validation.WarningHandlerNoter;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
