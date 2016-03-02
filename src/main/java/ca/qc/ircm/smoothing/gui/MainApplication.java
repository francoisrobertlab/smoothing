package ca.qc.ircm.smoothing.gui;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ca.qc.ircm.smoothing.ApplicationModule;
import ca.qc.ircm.util.javafx.AfterburnerGuiceInitializer;
import ca.qc.ircm.util.javafx.JavaFXUtils;
import ca.qc.ircm.util.javafx.message.MessageDialog;
import ca.qc.ircm.util.javafx.message.MessageDialog.MessageDialogType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main application.
 */
public class MainApplication extends Application {
  private final Logger logger = LoggerFactory.getLogger(MainApplication.class);
  private final ResourceBundle resources;

  public MainApplication() {
    resources = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());
  }

  @Override
  public void start(final Stage stage) throws Exception {
    final SplashScreen splash = new SplashScreen();
    splash.show();

    // Initialise application in background.
    class InitialiseTask extends Task<Void> {
      @Override
      public Void call() throws Exception {
        Injector injector = Guice.createInjector(new ApplicationModule());
        injector.getInstance(AfterburnerGuiceInitializer.class);
        Platform.runLater(() -> {
          startApp();
        });
        return null;
      }
    }
    final InitialiseTask initialiseTask = new InitialiseTask();
    initialiseTask.setOnSucceeded(e -> splash.hide());
    initialiseTask.setOnFailed(e -> {
      splash.hide();
      logger.error("Could not start application", initialiseTask.getException());
      new MessageDialog(stage, MessageDialogType.ERROR, resources.getString("error.title"),
          initialiseTask.getException().getMessage());
    });
    Thread thread = new Thread(initialiseTask);
    thread.start();
  }

  private void startApp() {
    MainPaneView view = new MainPaneView();

    Stage stage = new Stage();
    JavaFXUtils.setMaxSizeForScreen(stage);
    stage.setTitle(resources.getString("title"));
    Scene scene = new Scene(view.getView(), 1500, 800);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    Application.launch(args);
  }
}
