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
