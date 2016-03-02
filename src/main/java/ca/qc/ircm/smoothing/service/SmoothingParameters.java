package ca.qc.ircm.smoothing.service;

import javafx.scene.paint.Color;

import java.io.File;
import java.util.List;

/**
 * Smoothing parameters.
 */
public interface SmoothingParameters {
  public List<File> getFiles();

  public int getStandardDeviation();

  public int getRounds();

  public int getStepLength();

  public boolean isIncludeSmoothedTrack();

  public boolean isIncludeMinimumTrack();

  public Double getMinimumThreshold();

  public boolean isIncludeMaximumTrack();

  public Double getMaximumThreshold();

  public Color getColor(File file);
}
