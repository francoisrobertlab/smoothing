package ca.qc.ircm.smoothing.service;


/**
 * Factory for {@link SmoothingTask}.
 */
public interface SmoothingTaskFactory {
	public SmoothingTask create(SmoothingParameters parameters);
}
