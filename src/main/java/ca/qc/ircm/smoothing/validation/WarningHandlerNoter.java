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
