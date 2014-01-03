package ca.qc.ircm.smoothing.service;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Parses annotations present in a file.
 */
public interface BedParser {
	/**
	 * A validation warning occurred that prevents processing a specific annotation.
	 */
	public static interface Warning {
		public static enum Type {
			NO_TRACK, INVALID_TRACK, NO_NAME, NO_DATABASE
		}

		/**
		 * Returns validation warning type.
		 * 
		 * @return validation warning type
		 */
		public Type getType();

		/**
		 * Returns validation warning message.
		 * 
		 * @param locale
		 *            locale
		 * @return validation warning message
		 */
		public String getMessage(Locale locale);
	}

	/**
	 * Handles validation warnings.
	 */
	public static interface WarningHandler {
		public void handleWarning(Warning warning);
	}

	/**
	 * Validates first track found in file.
	 * 
	 * @param file
	 *            BED file
	 * @param warningHandler
	 *            handles warnings
	 * @throws IOException
	 *             could not parse BED file
	 */
	public void validateFirstTrack(File file, WarningHandler warningHandler) throws IOException;

	/**
	 * Parses BED file.
	 * 
	 * @param file
	 *            BED file
	 * @return first track found in file
	 * @throws IOException
	 *             could not parse BED file
	 */
	public BedTrack parseFirstTrack(File file) throws IOException;
}
