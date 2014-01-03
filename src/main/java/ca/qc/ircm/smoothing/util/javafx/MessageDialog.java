package ca.qc.ircm.smoothing.util.javafx;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * About window.
 */
public class MessageDialog extends Stage {
	public static enum Type {
		ERROR, INFORMATION, QUESTION, WARNING,
	}

	private final ResourceBundle bundle;
	@FXML
	private VBox messageBox;
	@FXML
	private Button ok;

	public MessageDialog(Window owner, Type type, String title, String... messages) {
		bundle = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());
		init(owner, type, title, Arrays.asList(messages));
	}

	public MessageDialog(Window owner, Type type, String title, List<String> messages) {
		bundle = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());
		init(owner, type, title, messages);
	}

	private void init(Window owner, Type type, String title, List<String> messages) {
		this.initOwner(owner);
		this.initModality(Modality.WINDOW_MODAL);

		BorderPane layout = (BorderPane) FxmlResources.loadFxml(this, bundle);
		JavaFXUtils.setMaxSizeForScreen(layout);

		Scene scene = new Scene(layout);
		this.setScene(scene);
		this.setTitle(title);

		for (String message : messages) {
			messageBox.getChildren().add(new Label(message));
		}

		ok.requestFocus();

		this.show();
	}

	public void close(ActionEvent event) {
		this.close();
	}
}
