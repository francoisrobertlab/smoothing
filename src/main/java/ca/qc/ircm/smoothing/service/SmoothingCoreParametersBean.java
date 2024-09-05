package ca.qc.ircm.smoothing.service;

import java.io.File;

public class SmoothingCoreParametersBean implements SmoothingCoreParameters {
  private File input;
  private File output;
  private String trackName;
  private String trackDatabase;
  private int standardDeviation;
  private int rounds;
  private int stepLength;
  private boolean includeSmoothedTrack;
  private boolean includeMinimumTrack;
  private Double minimumThreshold;
  private boolean includeMaximumTrack;
  private Double maximumThreshold;

  @Override
  public File getInput() {
    return input;
  }

  public SmoothingCoreParametersBean input(File input) {
    this.input = input;
    return this;
  }

  @Override
  public File getOutput() {
    return output;
  }

  public SmoothingCoreParametersBean output(File output) {
    this.output = output;
    return this;
  }

  @Override
  public String getTrackName() {
    return trackName;
  }

  public SmoothingCoreParametersBean trackName(String trackName) {
    this.trackName = trackName;
    return this;
  }

  @Override
  public String getTrackDatabase() {
    return trackDatabase;
  }

  public SmoothingCoreParametersBean trackDatabase(String trackDatabase) {
    this.trackDatabase = trackDatabase;
    return this;
  }

  @Override
  public int getStandardDeviation() {
    return standardDeviation;
  }

  public SmoothingCoreParametersBean standardDeviation(int standardDeviation) {
    this.standardDeviation = standardDeviation;
    return this;
  }

  @Override
  public int getRounds() {
    return rounds;
  }

  public SmoothingCoreParametersBean rounds(int rounds) {
    this.rounds = rounds;
    return this;
  }

  @Override
  public int getStepLength() {
    return stepLength;
  }

  public SmoothingCoreParametersBean stepLength(int stepLength) {
    this.stepLength = stepLength;
    return this;
  }

  @Override
  public boolean isIncludeSmoothedTrack() {
    return includeSmoothedTrack;
  }

  public SmoothingCoreParametersBean includeSmoothedTrack(boolean includeSmoothedTrack) {
    this.includeSmoothedTrack = includeSmoothedTrack;
    return this;
  }

  @Override
  public boolean isIncludeMinimumTrack() {
    return includeMinimumTrack;
  }

  public SmoothingCoreParametersBean includeMinimumTrack(boolean includeMinimumTrack) {
    this.includeMinimumTrack = includeMinimumTrack;
    return this;
  }

  @Override
  public Double getMinimumThreshold() {
    return minimumThreshold;
  }

  public SmoothingCoreParametersBean minimumThreshold(Double minimumThreshold) {
    this.minimumThreshold = minimumThreshold;
    return this;
  }

  @Override
  public boolean isIncludeMaximumTrack() {
    return includeMaximumTrack;
  }

  public SmoothingCoreParametersBean includeMaximumTrack(boolean includeMaximumTrack) {
    this.includeMaximumTrack = includeMaximumTrack;
    return this;
  }

  @Override
  public Double getMaximumThreshold() {
    return maximumThreshold;
  }

  public SmoothingCoreParametersBean maximumThreshold(Double maximumThreshold) {
    this.maximumThreshold = maximumThreshold;
    return this;
  }
}
