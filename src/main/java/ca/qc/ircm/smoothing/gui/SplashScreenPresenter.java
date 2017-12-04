package ca.qc.ircm.smoothing.gui;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

/**
 * Splash screen presenter.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
