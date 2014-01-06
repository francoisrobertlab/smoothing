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