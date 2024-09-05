package ca.qc.ircm.javafx;

import java.io.File;
import javafx.geometry.Rectangle2D;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Utilities for JavaFX.
 */
public class JavafxUtils {
  /**
   * Sets maximum size for screen.
   *
   * @param stage
   *          stage
   */
  public static void setMaxSizeForScreen(Stage stage) {
    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    stage.setMaxHeight(visualBounds.getHeight());
    stage.setMaxWidth(visualBounds.getWidth());
  }

  /**
   * Sets a valid directory in file chooser if FileChooser's directory is invalid.
   *
   * @param fileChooser
   *          file chooser
   */
  public static void setValidInitialDirectory(FileChooser fileChooser) {
    File initialDirectory = fileChooser.getInitialDirectory();
    while (initialDirectory != null && !initialDirectory.exists()) {
      initialDirectory = initialDirectory.getParentFile();
    }
    fileChooser.setInitialDirectory(initialDirectory);
  }

  /**
   * Sets a valid directory in directory chooser if FileChooser's directory is invalid.
   *
   * @param directoryChooser
   *          directory chooser
   */
  public static void setValidInitialDirectory(DirectoryChooser directoryChooser) {
    File initialDirectory = directoryChooser.getInitialDirectory();
    while (initialDirectory != null && !initialDirectory.exists()) {
      initialDirectory = initialDirectory.getParentFile();
    }
    directoryChooser.setInitialDirectory(initialDirectory);
  }
}
