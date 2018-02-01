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

package ca.qc.ircm.smoothing;

import java.util.Collection;
import java.util.List;

/**
 * Handles validation errors.
 */
public interface ErrorHandler {
  public interface ValidationError {
    String message();

    String tooltip();
  }

  public void handleError(String message);

  public void handleError(String message, String tooltip);

  /**
   * True if an error was reported.
   * 
   * @return true if an error was reported, false otherwise
   */
  public boolean hasErrors();

  public List<String> messages();

  public Collection<ValidationError> errors();
}
