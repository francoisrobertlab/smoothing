package ca.qc.ircm.smoothing.bed;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import ca.qc.ircm.smoothing.validation.WarningHandler;

/**
 * Parses annotations present in a file.
 */
public interface BedParser {
    /**
     * Validates BED file.
     *
     * @param file
     *            BED file
     * @param locale
     *            user's locale
     * @param warningHandler
     *            handles warnings
     * @throws IOException
     *             could not parse BED file
     */
    public void validate(File file, Locale locale, WarningHandler warningHandler) throws IOException;

    /**
     * Parses BED file.
     *
     * @param file
     *            BED file
     * @param handler
     *            handles valid BED elements (and invalid BED elements if handler implements {@link InvalidBedHandler})
     * @throws IOException
     *             could not parse BED file
     */
    public void parse(File file, BedHandler handler) throws IOException;
}
