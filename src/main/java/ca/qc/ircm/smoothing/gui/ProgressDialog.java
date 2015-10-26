package ca.qc.ircm.smoothing.gui;

import java.util.ResourceBundle;

import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import ca.qc.ircm.util.javafx.JavaFXUtils;

/**
 * Progress dialog for file filtering.
 */
public class ProgressDialog {
    private final ResourceBundle resources;
    private final Stage stage;

    public ProgressDialog(Window owner, Worker<?> worker) {
	ProgressDialogView view = new ProgressDialogView();
	ProgressDialogPresenter presenter = (ProgressDialogPresenter) view.getPresenter();
	resources = view.getResourceBundle();

	stage = new Stage();
	stage.initOwner(owner);
	stage.initModality(Modality.WINDOW_MODAL);
	JavaFXUtils.setMaxSizeForScreen(stage);

	presenter.progressProperty().bind(worker.progressProperty());
	presenter.messageProperty().bind(worker.messageProperty());
	presenter.cancelledProperty().addListener((ov, o, n) -> {
	    worker.cancel();
	});
	presenter.focusOnDefault();

	Scene scene = new Scene(view.getView());
	stage.setScene(scene);
	stage.setTitle(resources.getString("title"));

	stage.show();
    }

    public void hide() {
	stage.hide();
    }
}
