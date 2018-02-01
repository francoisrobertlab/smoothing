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
