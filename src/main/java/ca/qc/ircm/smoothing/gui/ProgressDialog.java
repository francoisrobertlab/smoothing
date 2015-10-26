package ca.qc.ircm.smoothing.gui;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import ca.qc.ircm.smoothing.util.javafx.FxmlResources;
import ca.qc.ircm.smoothing.util.javafx.JavaFXUtils;

/**
 * Progress dialog for file filtering.
 */
public class ProgressDialog extends Stage {
	private final ResourceBundle bundle;
	private final Worker<?> worker;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private Label message;
	@FXML
	private Button cancel;

	public ProgressDialog(Window owner, Worker<?> worker) {
		this.initOwner(owner);
		this.initModality(Modality.WINDOW_MODAL);
		this.worker = worker;
		bundle = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());

		BorderPane layout = (BorderPane) FxmlResources.loadFxml(this, bundle);
		JavaFXUtils.setMaxSizeForScreen(layout);

		Scene scene = new Scene(layout);
		this.setScene(scene);
		this.setTitle(bundle.getString("title"));

		progressBar.progressProperty().bind(worker.progressProperty());
		progressIndicator.progressProperty().bind(worker.progressProperty());
		message.textProperty().bind(worker.messageProperty());

		cancel.requestFocus();

		this.show();
	}

	public void cancel(ActionEvent event) {
		worker.cancel();
		this.close();
	}
}
