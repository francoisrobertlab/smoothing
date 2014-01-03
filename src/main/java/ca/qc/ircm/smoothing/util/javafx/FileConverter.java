package ca.qc.ircm.smoothing.util.javafx;

import java.io.File;

import javafx.util.StringConverter;

/**
 * {@link StringConverter} for {@link File}.
 */
public class FileConverter extends StringConverter<File> {
	@Override
	public File fromString(String input) {
		return new File(input);
	}

	@Override
	public String toString(File file) {
		return file != null ? file.getName() : "";
	}
}
