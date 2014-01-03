package ca.qc.ircm.smoothing.util.javafx;

import java.io.File;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

/**
 * Utilities for JavaFX.
 */
public class JavaFXUtils {
	public static void setMaxSizeForScreen(Region region) {
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		region.setMaxHeight(visualBounds.getHeight() - 35);
		region.setMaxWidth(visualBounds.getWidth() - 20);
	}

	public static void setValidInitialDirectory(FileChooser fileChooser) {
		File initialDirectory = fileChooser.getInitialDirectory();
		while (initialDirectory != null && !initialDirectory.exists()) {
			initialDirectory = initialDirectory.getParentFile();
		}
		fileChooser.setInitialDirectory(initialDirectory);
	}

	public static void setValidInitialDirectory(DirectoryChooser directoryChooser) {
		File initialDirectory = directoryChooser.getInitialDirectory();
		while (initialDirectory != null && !initialDirectory.exists()) {
			initialDirectory = initialDirectory.getParentFile();
		}
		directoryChooser.setInitialDirectory(initialDirectory);
	}
}
