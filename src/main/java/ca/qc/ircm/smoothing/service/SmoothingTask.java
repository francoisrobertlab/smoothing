package ca.qc.ircm.smoothing.service;

import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.concurrent.Task;

import javax.inject.Inject;

import ca.qc.ircm.smoothing.service.SmoothingService.SmoothingProgressBar;

import com.google.inject.assistedinject.Assisted;

/**
 * Create graphs based on analysis results.
 */
public class SmoothingTask extends Task<Void> {
	public class InternalProgressBar implements SmoothingProgressBar {
		private final double start;
		private final double max;
		private final double step;
		// Prevent referring to JavaFX thread in task.
		private double progress;

		public InternalProgressBar(double start, double max) {
			this.start = start;
			this.max = max;
			this.step = max - start;
		}

		@Override
		public double getProgress() {
			return progress * step - start;
		}

		@Override
		public void setProgress(double progress) {
			this.progress = progress;
			SmoothingTask.this.updateProgress(Math.min(start + progress * step, max), 1.0);
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
		smoothingService.smoothing(parameters, new InternalProgressBar(0.0, 1.0));
		return null;
	}
}
