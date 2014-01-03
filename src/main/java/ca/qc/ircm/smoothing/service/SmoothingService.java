package ca.qc.ircm.smoothing.service;

import java.io.File;
import java.io.IOException;

import ca.qc.ircm.progress_bar.ProgressBar;

/**
 * Services for smoothing BED files.
 */
public interface SmoothingService {
	public static interface SmoothingProgressBar extends ProgressBar {
		public void setFile(File file);
	}

	/**
	 * Smooth BED files.
	 * 
	 * @param parameters
	 *            smoothing parameters
	 * @param progressBar
	 *            records progression
	 * @throws IOException
	 *             could not parse BED file or could not execute smoothing
	 */
	public void smoothing(SmoothingParameters parameters, SmoothingProgressBar progressBar) throws IOException;
}
