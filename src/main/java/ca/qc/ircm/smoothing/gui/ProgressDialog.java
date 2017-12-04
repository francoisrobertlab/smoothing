package ca.qc.ircm.smoothing.gui;

import ca.qc.ircm.javafx.JavafxUtils;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ResourceBundle;

/**
 * Progress dialog for file filtering.
 */
public class ProgressDialog {
  private final ResourceBundle resources;
  private final Stage stage;

  /**
   * Creates a progress window.
   *
   * @param owner
   *          window's owner
   * @param worker
   *          worker
   */
  public ProgressDialog(Window owner, Worker<?> worker) {
    ProgressDialogView view = new ProgressDialogView();
    final ProgressDialogPresenter presenter = (ProgressDialogPresenter) view.getPresenter();
    resources = view.getResourceBundle();

    stage = new Stage();
    stage.initOwner(owner);
    stage.initModality(Modality.WINDOW_MODAL);
    JavafxUtils.setMaxSizeForScreen(stage);

    presenter.progressProperty().bind(worker.progressProperty());
    presenter.messageProperty().bind(worker.messageProperty());
    presenter.cancelledProperty().addListener((observable, oldvalue, newvalue) -> {
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
