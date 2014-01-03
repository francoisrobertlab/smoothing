package ca.qc.ircm.smoothing;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Cell;
import javafx.scene.control.ColorPicker;
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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import ca.qc.ircm.smoothing.util.FileUtils;
import ca.qc.ircm.smoothing.util.drag.DragExitedHandler;
import ca.qc.ircm.smoothing.util.drag.DragFilesOverHandler;
import ca.qc.ircm.smoothing.util.javafx.FileTableCell;
import ca.qc.ircm.smoothing.util.javafx.FileTableCellFactory;
import ca.qc.ircm.smoothing.util.javafx.FxmlResources;
import ca.qc.ircm.smoothing.util.javafx.JavaFXUtils;

/**
 * Table containing BED files.
 */
public class BedTable extends HBox {
	private static final Color DEFAULT_COLOR = Color.DARKORANGE;

	private class CellIndexListener implements ChangeListener<Number> {
		private final Cell<?> cell;
		private final StyleClassCellListener styleClassCellListener;

		private CellIndexListener(Cell<?> cell) {
			this.cell = cell;
			styleClassCellListener = new StyleClassCellListener(cell);
		}

		@Override
		public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
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

	private class BedWithColorFileCallback implements
			Callback<TableColumn.CellDataFeatures<BedWithColor, File>, ObservableValue<File>> {
		@Override
		public ObservableValue<File> call(CellDataFeatures<BedWithColor, File> features) {
			return features.getValue().fileProperty();
		}
	}

	private class ColorCell<S> extends TableCell<S, Color> {
		private final ColorPicker colorPicker = new ColorPicker();

		private ColorCell() {
		}

		@Override
		protected void updateItem(Color color, boolean empty) {
			super.updateItem(color, empty);
			if (!empty) {
				setGraphic(colorPicker);
			}
			if (color != null) {
				colorPicker.setValue(color);
			} else {
				colorPicker.setValue(DEFAULT_COLOR);
			}
		}
	}

	private class ColorCellFactory<S> implements Callback<TableColumn<S, Color>, TableCell<S, Color>> {
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

	private MapProperty<Integer, ObservableSet<String>> fileCellClassesProperty = new SimpleMapProperty<Integer, ObservableSet<String>>(
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
	private final ResourceBundle bundle;
	@FXML
	private TableView<BedWithColor> table;
	@FXML
	private TableColumn<BedWithColor, File> fileColumn;
	@FXML
	private TableColumn<BedWithColor, Color> colorColumn;
	private FileChooser fileChooser = new FileChooser();
	private final List<String> fileCellDefaultClasses;

	public BedTable() {
		bundle = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());

		FxmlResources.loadFxml(this, bundle);

		fileCellDefaultClasses = Collections.unmodifiableList(new FileTableCell<BedWithColor>().getStyleClass());
		fileChooser.setTitle(bundle.getString("fileChooser.title"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter(bundle.getString("fileChooser.description"), "*"));
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
						// TODO Get color from file.
						bed.setColor(DEFAULT_COLOR);
						table.getItems().add(bed);
					}
					event.setDropCompleted(true);
					event.consume();
				} else if (event.getDragboard().hasString()) {
					for (String path : event.getDragboard().getString().split("\\n")) {
						BedWithColor bed = new BedWithColor(new File(path));
						// TODO Get color from file.
						bed.setColor(DEFAULT_COLOR);
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
		List<File> selections = fileChooser.showOpenMultipleDialog(getScene().getWindow());
		if (selections != null) {
			if (!selections.isEmpty()) {
				fileChooser.setInitialDirectory(selections.get(0).getParentFile());
			}
			for (File selection : selections) {
				BedWithColor bed = new BedWithColor(selection);
				// TODO Get color from file.
				bed.setColor(DEFAULT_COLOR);
				if (!table.getItems().contains(bed)) {
					table.getItems().add(bed);
				}
			}
		}
	}

	private void removeSelected() {
		List<Integer> selections = new ArrayList<Integer>(table.getSelectionModel().getSelectedIndices());
		Collections.sort(selections);
		Collections.reverse(selections);
		for (Integer selection : selections) {
			if (selection >= 0) {
				table.getItems().remove(selection.intValue());
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
			errorHandler.handleError(bundle.getString("error.files.required"));
			error = true;
		} else {
			int index = -1;
			for (BedWithColor bed : beds) {
				index++;
				File file = bed.getFile();
				if (!file.isFile()) {
					errorHandler.handleError(
							MessageFormat.format(bundle.getString("error.files.notExists"), file.getName()),
							file.getPath());
					error = true;
					fileCellClassesProperty.get(index).add("error");
				}
			}
		}
		if (error) {
			getStyleClass().add("error");
		}
	}
}
