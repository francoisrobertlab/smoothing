package ca.qc.ircm.smoothing.gui;

import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Progress dialog presenter.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProgressDialogPresenter {
  private final BooleanProperty cancelledProperty = new SimpleBooleanProperty();
  @FXML
  private ResourceBundle resources;
  @FXML
  private Pane view;
  @FXML
  private ProgressBar progressBar;
  @FXML
  private ProgressIndicator progressIndicator;
  @FXML
  private Label message;
  @FXML
  private Button cancel;

  @FXML
  private void initialize() {
    progressIndicator.progressProperty().bind(progressBar.progressProperty());
  }

  public DoubleProperty progressProperty() {
    return progressBar.progressProperty();
  }

  public StringProperty messageProperty() {
    return message.textProperty();
  }

  public ReadOnlyBooleanProperty cancelledProperty() {
    return cancelledProperty;
  }

  @FXML
  private void cancel(ActionEvent event) {
    cancelledProperty.set(true);
    view.getScene().getWindow().hide();
  }

  void focusOnDefault() {
    cancel.requestFocus();
  }
}
