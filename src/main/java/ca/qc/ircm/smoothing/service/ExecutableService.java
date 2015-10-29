package ca.qc.ircm.smoothing.service;

import java.io.IOException;

/**
 * Launch external programs.
 */
public interface ExecutableService {
    /**
     * Listens for events coming from vap program.
     */
    public interface SmoothingEventListener {
	/**
	 * Program's progression changed.
	 *
	 * @param progress
	 *            new progression
	 */
	public void processProgress(double progress);

	public void setRawDataCount(int count);

	public void setChromosomeCount(int count);
    }

    public void smoothing(SmoothingCoreParameters parameters, SmoothingEventListener listener) throws IOException;
}
