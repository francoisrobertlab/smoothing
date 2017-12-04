package ca.qc.ircm.smoothing.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Splash screen.
 */
public class SplashScreen {
  private final Stage stage;

  /**
   * Creates splash screen.
   *
   * @param stage
   *          stage
   */
  public SplashScreen(Stage stage) {
    this.stage = stage;
    stage.initStyle(StageStyle.UNDECORATED);
    SplashScreenView view = new SplashScreenView();
    Scene scene = new Scene(view.getView());
    stage.setScene(scene);
  }

  public void show() {
    stage.show();
  }

  public void hide() {
    stage.hide();
  }
}
