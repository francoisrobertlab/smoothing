package ca.qc.ircm.smoothing.gui;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;

/**
 * Splash screen presenter.
 */
public class SplashScreenPresenter {
    @FXML
    private ResourceBundle resources;
    @FXML
    private Pane view;
    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private void initialize() {
	view.setCursor(Cursor.WAIT);
    }

    public void setProgress(double value) {
	progressIndicator.setProgress(value);
    }
}
