package ca.qc.ircm.smoothing;

import ca.qc.ircm.javafx.message.MessageDialog;
import ca.qc.ircm.javafx.message.MessageDialog.MessageDialogType;
import ca.qc.ircm.smoothing.gui.SplashScreen;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Preloader;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JavaFX application preloader.
 */
public class MainPreloader extends Preloader {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private Stage stage;
  private SplashScreen splash;

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.stage = primaryStage;
    splash = new SplashScreen(primaryStage);
    splash.show();
  }

  @Override
  public void handleApplicationNotification(PreloaderNotification info) {
    if (info instanceof ApplicationStarted) {
      splash.hide();
    }
  }

  @Override
  public boolean handleErrorNotification(ErrorNotification info) {
    logger.error("Could not start application", info.getCause());
    ResourceBundle resources = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());
    new MessageDialog(stage, MessageDialogType.ERROR, resources.getString("error.title"),
        info.getCause().getMessage()).showAndWait();
    return true;
  }
}
