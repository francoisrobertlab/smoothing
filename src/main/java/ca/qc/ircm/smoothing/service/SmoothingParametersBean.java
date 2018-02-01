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

import javafx.scene.paint.Color;

import java.io.File;
import java.util.List;
import java.util.Map;

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
