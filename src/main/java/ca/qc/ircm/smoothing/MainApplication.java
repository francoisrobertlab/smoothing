package ca.qc.ircm.smoothing;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.qc.ircm.smoothing.util.javafx.FxmlResources;
import ca.qc.ircm.smoothing.util.javafx.JavaFXUtils;
import ca.qc.ircm.smoothing.util.javafx.MessageDialog;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Main application.
 */
public class MainApplication extends Application {
    private final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    private final ResourceBundle bundle;

    public MainApplication() {
	bundle = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());
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
		FxmlResources.setInjector(injector);

		final MainPane mainPane = new MainPane();
		JavaFXUtils.setMaxSizeForScreen(mainPane);
		mainPane.setPrefHeight(Math.min(800, mainPane.getMaxHeight()));
		mainPane.setPrefWidth(Math.min(1500, mainPane.getMaxWidth()));

		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
			Stage stage = new Stage();
			stage.setTitle(bundle.getString("title"));
			Scene scene = new Scene(mainPane);
			stage.setScene(scene);
			stage.show();
		    }
		});
		return null;
	    }
	}
	final InitialiseTask initialiseTask = new InitialiseTask();
	initialiseTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		splash.hide();
	    }
	});
	initialiseTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		splash.hide();
		logger.error("Could not start application", initialiseTask.getException());
		new MessageDialog(stage, MessageDialog.Type.ERROR, bundle.getString("error.title"), initialiseTask
			.getException().getMessage());
	    }
	});
	Thread thread = new Thread(initialiseTask);
	thread.start();
    }

    public static void main(String[] args) {
	Application.launch(args);
    }
}
