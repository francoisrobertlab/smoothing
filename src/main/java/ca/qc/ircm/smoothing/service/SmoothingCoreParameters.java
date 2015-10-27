package ca.qc.ircm.smoothing.service;

import java.io.File;

/**
 * Smoothing parameters for smoothing executable program.
 */
public interface SmoothingCoreParameters {
    public File getInput();

    public File getOutput();

    public String getTrackName();

    public String getTrackDatabase();

    public int getStandardDeviation();

    public int getRounds();

    public int getStepLength();

    public boolean isIncludeSmoothedTrack();

    public boolean isIncludeMinimumTrack();

    public Double getMinimumThreshold();

    public boolean isIncludeMaximumTrack();

    public Double getMaximumThreshold();
}
