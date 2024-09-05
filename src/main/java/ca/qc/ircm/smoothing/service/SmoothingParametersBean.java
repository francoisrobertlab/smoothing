package ca.qc.ircm.smoothing.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 * Simple implementation of {@link SmoothingParameters}.
 */
public class SmoothingParametersBean implements SmoothingParameters {
  private List<File> files;
  private int standardDeviation;
  private int rounds;
  private int stepLength;
  private boolean includeSmoothedTrack;
  private boolean includeMinimumTrack;
  private Double minimumThreshold;
  private boolean includeMaximumTrack;
  private Double maximumThreshold;
  private Map<File, Color> colors;

  @Override
  public Color getColor(File file) {
    return colors != null ? colors.get(file) : null;
  }

  @Override
  public int getStandardDeviation() {
    return standardDeviation;
  }

  public void setStandardDeviation(Integer standardDeviation) {
    this.standardDeviation = standardDeviation;
  }

  @Override
  public int getRounds() {
    return rounds;
  }

  public void setRounds(Integer rounds) {
    this.rounds = rounds;
  }

  @Override
  public int getStepLength() {
    return stepLength;
  }

  public void setStepLength(Integer stepLength) {
    this.stepLength = stepLength;
  }

  @Override
  public boolean isIncludeSmoothedTrack() {
    return includeSmoothedTrack;
  }

  public void setIncludeSmoothedTrack(boolean includeSmoothedTrack) {
    this.includeSmoothedTrack = includeSmoothedTrack;
  }

  @Override
  public boolean isIncludeMinimumTrack() {
    return includeMinimumTrack;
  }

  public void setIncludeMinimumTrack(boolean includeMinimumTrack) {
    this.includeMinimumTrack = includeMinimumTrack;
  }

  @Override
  public Double getMinimumThreshold() {
    return minimumThreshold;
  }

  public void setMinimumThreshold(Double minimumThreshold) {
    this.minimumThreshold = minimumThreshold;
  }

  @Override
  public boolean isIncludeMaximumTrack() {
    return includeMaximumTrack;
  }

  public void setIncludeMaximumTrack(boolean includeMaximumTrack) {
    this.includeMaximumTrack = includeMaximumTrack;
  }

  @Override
  public Double getMaximumThreshold() {
    return maximumThreshold;
  }

  public void setMaximumThreshold(Double maximumThreshold) {
    this.maximumThreshold = maximumThreshold;
  }

  @Override
  public List<File> getFiles() {
    return files;
  }

  public void setFiles(List<File> files) {
    this.files = files;
  }

  public Map<File, Color> getColors() {
    return colors;
  }

  public void setColors(Map<File, Color> colors) {
    this.colors = colors;
  }
}
