package ca.qc.ircm.smoothing.service;

import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.concurrent.Task;

import javax.inject.Inject;

import ca.qc.ircm.progress_bar.SimpleProgressBar;
import ca.qc.ircm.smoothing.service.SmoothingService.SmoothingProgressBar;

import com.google.inject.assistedinject.Assisted;

/**
 * Create graphs based on analysis results.
 */
public class SmoothingTask extends Task<Void> {
	private class InternalProgressBar extends SimpleProgressBar implements SmoothingProgressBar {
		private static final long serialVersionUID = 3822582140618354796L;

		@Override
		public void setProgress(double progress) {
			super.setProgress(progress);
			SmoothingTask.this.updateProgress(getProgress(), 1.0);
		}

		@Override
		public void setFile(File file) {
			updateMessage(MessageFormat.format(bundle.getString("message.file"), file.getName()));
		}
	}

	private final ResourceBundle bundle;
	private final SmoothingService smoothingService;
	private final SmoothingParameters parameters;

	@Inject
	protected SmoothingTask(SmoothingService smoothingService, @Assisted SmoothingParameters parameters) {
		bundle = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());
		this.smoothingService = smoothingService;
		this.parameters = parameters;
	}

	@Override
	protected Void call() throws Exception {
		smoothingService.smoothing(parameters, new InternalProgressBar());
		return null;
	}
}
