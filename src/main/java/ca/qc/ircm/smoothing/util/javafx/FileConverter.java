package ca.qc.ircm.smoothing.util.javafx;

import javafx.util.StringConverter;

import java.io.File;

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
