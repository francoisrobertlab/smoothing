package ca.qc.ircm.smoothing.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Splash screen.
 */
public class SplashScreen {
  private final Stage stage;
  private ResourceBundle resources = ResourceBundle.getBundle(SplashScreen.class.getName());
  private HBox view = new HBox();
  private Label label = new Label();

  /**
   * Creates splash screen.
   *
   * @param stage
   *          stage
   */
  public SplashScreen(Stage stage) {
    this.stage = stage;
    stage.initStyle(StageStyle.UNDECORATED);
    URL stylesheet = SplashScreen.class.getResource(SplashScreen.class.getSimpleName() + ".css");
    view.getStylesheets().add(stylesheet.toExternalForm());
    view.getStyleClass().add("splash");
    view.setCursor(Cursor.WAIT);
    view.getChildren().add(label);
    label.setText(resources.getString("label"));
    Scene scene = new Scene(view);
    stage.setScene(scene);
  }

  public void show() {
    stage.show();
  }

  public void hide() {
    stage.hide();
  }
}
