package ca.qc.ircm.smoothing.service;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;

import javax.inject.Inject;

import ca.qc.ircm.progress_bar.SimpleProgressBar;
import ca.qc.ircm.smoothing.validation.WarningHandlerNoter;

import com.google.inject.assistedinject.Assisted;

/**
 * Create graphs based on analysis results.
 */
public class SmoothingTask extends Task<Void> {
    private class InternalProgressBar extends SimpleProgressBar {
	private static final long serialVersionUID = 3822582140618354796L;

	@Override
	public void messageChanged(String newMessage) {
	    updateMessage(newMessage);
	}

	@Override
	public void progressChanged(double newProgress) {
	    SmoothingTask.this.updateProgress(newProgress, 1.0);
	}
    }

    private final SmoothingService smoothingService;
    private final SmoothingParameters parameters;
    private List<String> warnings = new ArrayList<>();

    @Inject
    protected SmoothingTask(SmoothingService smoothingService, @Assisted SmoothingParameters parameters) {
	this.smoothingService = smoothingService;
	this.parameters = parameters;
    }

    @Override
    protected Void call() throws Exception {
	WarningHandlerNoter warningHandler = new WarningHandlerNoter();
	smoothingService.smoothing(parameters, new InternalProgressBar(), warningHandler);
	warnings.clear();
	warnings.addAll(warningHandler.getWarnings());
	return null;
    }

    public List<String> getWarnings() {
	return warnings;
    }
}
