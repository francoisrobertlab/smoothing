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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandlerDefault implements ErrorHandler {
  private static class ValidationErrorDefault implements ValidationError {
    private final String message;
    private final String tooltip;

    private ValidationErrorDefault(String message) {
      this(message, null);
    }

    private ValidationErrorDefault(String message, String tooltip) {
      this.message = message;
      this.tooltip = tooltip;
    }

    @Override
    public String message() {
      return message;
    }

    @Override
    public String tooltip() {
      return tooltip;
    }
  }

  private final Logger logger = LoggerFactory.getLogger(ErrorHandlerDefault.class);
  private boolean hasError = false;
  private Collection<ValidationError> errors = new ArrayList<ValidationError>();

  @Override
  public void handleError(String message) {
    hasError = true;
    errors.add(new ValidationErrorDefault(message));
    logger.debug(message);
  }

  @Override
  public void handleError(String message, String tooltip) {
    hasError = true;
    errors.add(new ValidationErrorDefault(message, tooltip));
    logger.debug(message);
  }

  @Override
  public boolean hasErrors() {
    return hasError;
  }

  @Override
  public List<String> messages() {
    List<String> messages = new ArrayList<String>();
    for (ValidationError error : errors()) {
      messages.add(error.message());
    }
    return messages;
  }

  @Override
  public Collection<ValidationError> errors() {
    return errors;
  }

  public void setHasError(boolean hasError) {
    this.hasError = hasError;
  }
}