package ca.qc.ircm.smoothing.gui;

import ca.qc.ircm.smoothing.BedWithColor;
import ca.qc.ircm.smoothing.ErrorHandler;
import ca.qc.ircm.smoothing.bed.BedService;
import ca.qc.ircm.smoothing.bed.BedTrack;
import ca.qc.ircm.smoothing.util.FileUtils;
import ca.qc.ircm.smoothing.util.drag.DragExitedHandler;
import ca.qc.ircm.smoothing.util.drag.DragFilesOverHandler;
import ca.qc.ircm.smoothing.util.javafx.ColorConverter;
import ca.qc.ircm.smoothing.util.javafx.FileTableCell;
import ca.qc.ircm.smoothing.util.javafx.FileTableCellFactory;
import ca.qc.ircm.util.javafx.JavaFXUtils;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Cell;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;

/**
 * Table containing BED files.
 */
public class BedTablePresenter {
  private static final Color DEFAULT_COLOR = Color.DARKORANGE;

  private class CellIndexListener implements ChangeListener<Number> {
    private final Cell<?> cell;
    private final StyleClassCellListener styleClassCellListener;

    private CellIndexListener(Cell<?> cell) {
      this.cell = cell;
      styleClassCellListener = new StyleClassCellListener(cell);
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue,
        Number newValue) {
      if (oldValue != null) {
        fileCellClassesProperty.get(oldValue.intValue()).removeListener(styleClassCellListener);
      }
      if (newValue != null) {
        fileCellClassesProperty.get(newValue.intValue()).addListener(styleClassCellListener);
        cell.getStyleClass().setAll(fileCellClassesProperty.get(newValue.intValue()));
      }
    }
  }

  private class StyleClassCellListener implements SetChangeListener<String> {
    private final Cell<?> cell;

    private StyleClassCellListener(Cell<?> cell) {
      this.cell = cell;
    }

    @Override
    public void onChanged(Change<? extends String> change) {
      cell.getStyleClass().setAll(change.getSet());
    }
  }

  private class BedFileTableCellFactory extends FileTableCellFactory<BedWithColor> {
    @Override
    public TableCell<BedWithColor, File> call(TableColumn<BedWithColor, File> column) {
      TableCell<BedWithColor, File> cell = super.call(column);
      cell.indexProperty().addListener(new CellIndexListener(cell));
      return cell;
    }
  }

  private class BedWithColorFileCallback
      implements Callback<TableColumn.CellDataFeatures<BedWithColor, File>, ObservableValue<File>> {
    @Override
    public ObservableValue<File> call(CellDataFeatures<BedWithColor, File> features) {
      return features.getValue().fileProperty();
    }
  }

  private class ColorCell<S> extends TableCell<S, Color> {
    private final ColorPicker colorPicker = new ColorPicker();
    private final Label label = new Label();
    private final ColorConverter converter = new ColorConverter();

    private ColorCell() {
      getStyleClass().add("color-cell");
      setGraphic(label);
      colorPicker.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          commitEdit(colorPicker.getValue());
        }
      });
      colorPicker.setOnHidden(new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
          if (isEditing()) {
            cancelEdit();
          }
        }
      });
    }

    @Override
    protected void updateItem(Color color, boolean empty) {
      super.updateItem(color, empty);
      if (!empty) {
        if (color == null) {
          color = DEFAULT_COLOR;
        }
        label.setText(converter.toString(color));
        label.setStyle("-fx-background-color:" + converter.toString(color)
            + "; -fx-text-fill: ladder(" + converter.toString(color) + ", white 49%, black 50%)");
        colorPicker.setValue(color);
        setGraphic(label);
      } else {
        setGraphic(null);
      }
    }

    @Override
    public void cancelEdit() {
      super.cancelEdit();
      setGraphic(label);
    }

    @Override
    public void commitEdit(Color color) {
      super.commitEdit(color);
      setGraphic(label);
    }

    @Override
    public void startEdit() {
      super.startEdit();
      setGraphic(colorPicker);
    }
  }

  private class ColorCellFactory<S>
      implements Callback<TableColumn<S, Color>, TableCell<S, Color>> {
    @Override
    public TableCell<S, Color> call(TableColumn<S, Color> column) {
      return new ColorCell<S>();
    }
  }

  private class ColorCellValueFactory implements
      Callback<TableColumn.CellDataFeatures<BedWithColor, Color>, ObservableValue<Color>> {
    @Override
    public ObservableValue<Color> call(CellDataFeatures<BedWithColor, Color> features) {
      return features.getValue().colorProperty();
    }
  }

  private MapProperty<Integer, ObservableSet<String>> fileCellClassesProperty =
      new SimpleMapProperty<Integer, ObservableSet<String>>(
          FXCollections.observableMap(new HashMap<Integer, ObservableSet<String>>() {
            private static final long serialVersionUID = 3092430838184940222L;

            @Override
            public ObservableSet<String> get(Object key) {
              if (!containsKey(key)) {
                super.put((Integer) key,
                    FXCollections.observableSet(new HashSet<String>(fileCellDefaultClasses)));
              }
              return super.get(key);
            }
          }));
  @FXML
  private ResourceBundle resources;
  @FXML
  private Pane view;
  @FXML
  private TableView<BedWithColor> table;
  @FXML
  private TableColumn<BedWithColor, File> fileColumn;
  @FXML
  private TableColumn<BedWithColor, Color> colorColumn;
  private FileChooser fileChooser = new FileChooser();
  private final List<String> fileCellDefaultClasses;
  @Inject
  private BedService bedService;

  public BedTablePresenter() {
    fileCellDefaultClasses = Collections
        .unmodifiableList(new ArrayList<>(new FileTableCell<BedWithColor>().getStyleClass()));
  }

  @FXML
  private void initialize() {
    fileChooser.setTitle(resources.getString("fileChooser.title"));
    fileChooser.getExtensionFilters()
        .add(new ExtensionFilter(resources.getString("fileChooser.description"), "*", "*.*"));
    table.setItems(FXCollections.observableList(new ArrayList<BedWithColor>()));
    table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    table.setEditable(true);
    table.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
          removeSelected();
        }
      }
    });
    fileColumn.setCellFactory(new BedFileTableCellFactory());
    fileColumn.setCellValueFactory(new BedWithColorFileCallback());
    colorColumn.setCellFactory(new ColorCellFactory<BedWithColor>());
    colorColumn.setCellValueFactory(new ColorCellValueFactory());

    // Enable drag and drop.
    table.setOnDragDetected(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        Dragboard db = table.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        List<BedWithColor> beds = table.getSelectionModel().getSelectedItems();
        List<File> files = new ArrayList<File>(beds.size());
        for (BedWithColor bed : beds) {
          files.add(bed.getFile());
        }
        content.putFiles(files);
        StringBuilder builder = new StringBuilder();
        for (File file : files) {
          builder.append("\n");
          builder.append(file.getPath());
        }
        if (builder.length() > 0) {
          builder.deleteCharAt(0);
        }
        content.putString(builder.toString());
        db.setContent(content);
        event.consume();
      }
    });
    table.setOnDragDone(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent event) {
        if (event.getTransferMode() == TransferMode.MOVE) {
          for (String path : event.getDragboard().getString().split("\\n")) {
            table.getItems().remove(new BedWithColor(new File(path)));
          }
        }
        event.consume();
      }
    });
    table.setOnDragOver(new DragFilesOverHandler(table, table));
    table.setOnDragExited(new DragExitedHandler(table));
    table.setOnDragDropped(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent event) {
        boolean acceptFiles = true;
        if (event.getDragboard().hasFiles()) {
          for (File file : event.getDragboard().getFiles()) {
            file = FileUtils.resolveWindowsShorcut(file);
            acceptFiles &= file.isFile();
          }
        }
        if (event.getDragboard().hasFiles() && acceptFiles) {
          for (File file : event.getDragboard().getFiles()) {
            file = FileUtils.resolveWindowsShorcut(file);
            BedWithColor bed = new BedWithColor(file);
            setColor(bed, file);
            table.getItems().add(bed);
          }
          event.setDropCompleted(true);
          event.consume();
        } else if (event.getDragboard().hasString()) {
          for (String path : event.getDragboard().getString().split("\\n")) {
            File file = new File(path);
            BedWithColor bed = new BedWithColor(file);
            setColor(bed, file);
            table.getItems().add(bed);
          }
          event.setDropCompleted(true);
          event.consume();
        }
      }
    });
  }

  public ObjectProperty<File> initialDirectoryProperty() {
    return fileChooser.initialDirectoryProperty();
  }

  public ObservableList<BedWithColor> getItems() {
    return table.getItems();
  }

  @FXML
  private void browse(ActionEvent event) {
    JavaFXUtils.setValidInitialDirectory(fileChooser);
    List<File> selections = fileChooser.showOpenMultipleDialog(view.getScene().getWindow());
    if (selections != null) {
      if (!selections.isEmpty()) {
        fileChooser.setInitialDirectory(selections.get(0).getParentFile());
      }
      for (File selection : selections) {
        BedWithColor bed = new BedWithColor(selection);
        setColor(bed, selection);
        if (!table.getItems().contains(bed)) {
          table.getItems().add(bed);
        }
      }
    }
  }

  private void removeSelected() {
    List<Integer> selections =
        new ArrayList<Integer>(table.getSelectionModel().getSelectedIndices());
    Collections.sort(selections);
    Collections.reverse(selections);
    for (Integer selection : selections) {
      if (selection >= 0) {
        table.getItems().remove(selection.intValue());
        List<Integer> styleClassKeys = new ArrayList<Integer>(fileCellClassesProperty.keySet());
        Collections.sort(selections);
        fileCellClassesProperty.remove(selection.intValue());
        for (Integer styleClassKey : styleClassKeys) {
          if (styleClassKey > selection.intValue()) {
            fileCellClassesProperty.put(styleClassKey - 1,
                fileCellClassesProperty.get(styleClassKey));
          }
        }
      }
    }
  }

  public void validate(ErrorHandler errorHandler) {
    for (ObservableSet<String> classes : fileCellClassesProperty.values()) {
      classes.remove("error");
    }
    boolean error = false;
    List<BedWithColor> beds = table.getItems();
    if (beds == null || beds.isEmpty()) {
      errorHandler.handleError(resources.getString("error.files.required"));
      error = true;
    } else {
      int index = -1;
      for (BedWithColor bed : beds) {
        index++;
        File file = bed.getFile();
        if (!file.isFile()) {
          errorHandler.handleError(
              MessageFormat.format(resources.getString("error.files.notExists"), file.getName()),
              file.getPath());
          error = true;
          fileCellClassesProperty.get(index).add("error");
        }
      }
    }
    if (error) {
      view.getStyleClass().add("error");
    }
  }

  private void setColor(final BedWithColor bed, File file) {
    bed.setColor(DEFAULT_COLOR);
    try {
      BedTrack track = bedService.parseFirstTrack(file);
      if (track != null && track.getColor() != null) {
        java.awt.Color color = track.getColor();
        bed.setColor(Color.rgb(color.getRed(), color.getGreen(), color.getBlue()));
      }
    } catch (IOException e) {
      // Ignore.
    }
  }
}
