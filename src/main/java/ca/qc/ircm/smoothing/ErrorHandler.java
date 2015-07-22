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
