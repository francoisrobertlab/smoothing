package ca.qc.ircm.smoothing.gui;

import ca.qc.ircm.javafx.JavafxUtils;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application window.
 */
public class MainGui {
  private ResourceBundle resources;
  private Stage stage;

  /**
   * Creates application window.
   */
  public MainGui() {
    MainPaneView view = new MainPaneView();
    resources = view.getResourceBundle();
    stage = new Stage();
    stage.setTitle(resources.getString("title"));
    Scene scene = new Scene(view.getView());
    stage.setScene(scene);
    JavafxUtils.setMaxSizeForScreen(stage);
  }

  public void show() {
    stage.show();
  }

  public void hide() {
    stage.hide();
  }
}
