package ca.qc.ircm.smoothing.gui;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ca.qc.ircm.smoothing.util.javafx.FxmlResources;

/**
 * Splash screen.
 */
public class SplashScreen extends Stage {
	private final ResourceBundle bundle;

	@FXML
	private ProgressIndicator progressIndicator;

	public SplashScreen() {
		initStyle(StageStyle.UNDECORATED);

		bundle = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());

		Parent layout = (Parent) FxmlResources.loadFxml(this, bundle);
		layout.setCursor(Cursor.WAIT);
		Scene scene = new Scene(layout);
		setScene(scene);
	}

	public void setProgress(double value) {
		progressIndicator.setProgress(value);
	}
}
