package ca.qc.ircm.smoothing.service;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import ca.qc.ircm.smoothing.validation.WarningHandler;

/**
 * Parses annotations present in a file.
 */
public interface BedParser {
    public static enum WarningType {
	NO_TRACK, INVALID_TRACK, NO_NAME, NO_DATABASE
    }

    /**
     * Validates first track found in file.
     *
     * @param file
     *            BED file
     * @param warningHandler
     *            handles warnings
     * @param locale
     *            locale of warning messages
     * @throws IOException
     *             could not parse BED file
     */
    public void validateFirstTrack(File file, WarningHandler warningHandler, Locale locale) throws IOException;

    /**
     * Returns first track found in BED file.
     *
     * @param file
     *            BED file
     * @return first track found in file
     * @throws IOException
     *             could not parse BED file
     */
    public BedTrack parseFirstTrack(File file) throws IOException;
}
