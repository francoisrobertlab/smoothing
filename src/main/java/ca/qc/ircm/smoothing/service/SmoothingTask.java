package ca.qc.ircm.smoothing.service;

import javafx.concurrent.Task;

import javax.inject.Inject;

import ca.qc.ircm.progress_bar.SimpleProgressBar;

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

    @Inject
    protected SmoothingTask(SmoothingService smoothingService, @Assisted SmoothingParameters parameters) {
	this.smoothingService = smoothingService;
	this.parameters = parameters;
    }

    @Override
    protected Void call() throws Exception {
	smoothingService.smoothing(parameters, new InternalProgressBar());
	return null;
    }
}
