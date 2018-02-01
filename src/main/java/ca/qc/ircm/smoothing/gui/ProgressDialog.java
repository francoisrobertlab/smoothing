/*
 * Copyright (c) 2015 Institut de recherches cliniques de Montreal (IRCM)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
