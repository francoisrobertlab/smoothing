package ca.qc.ircm.smoothing.util.javafx;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ca.qc.ircm.smoothing.util.FileUtils;
import ca.qc.ircm.smoothing.util.drag.DragExitedHandler;
import ca.qc.ircm.smoothing.util.drag.DragFilesOverHandler;

/**
 * Control for multiple files selection.
 */
public class MultipleFilesPane extends HBox {
	private class DropableFileCellFactory extends FileListCellFactory {
		@Override
		public ListCell<File> call(ListView<File> list) {
			final ListCell<File> cell = super.call(list);
			cell.itemProperty().addListener(new FileCellListener(cell));
			cell.setEditable(true);
			// Enable drop.
			cell.setOnDragOver(new DragFilesOverHandler(cell, cell) {
				@Override
				protected boolean accept(DragEvent event) {
					return DropableFileCellFactory.this.accept(cell, event);
				}

				@Override
				protected void setAcceptTransferModes(DragEvent event) {
					event.acceptTransferModes(TransferMode.MOVE);
				}
			});
			cell.setOnDragExited(new DragExitedHandler(cell));
			cell.setOnDragDropped(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					if (accept(cell, event)) {
						int dropIndex = cell.getIndex();
						if (dropIndex > files.getItems().size()) {
							dropIndex = files.getItems().size();
						}

						// Remove files to move.
						for (String path : event.getDragboard().getString().split("\\n")) {
							File file = new File(path);
							int index = files.getItems().indexOf(file);
							if (index >= 0 && dropIndex > index) {
								dropIndex--;
							}
							files.getItems().remove(file);
						}

						// Move file at new location.
						files.getSelectionModel().clearSelection();
						for (String path : event.getDragboard().getString().split("\\n")) {
							File file = new File(path);
							files.getItems().add(dropIndex, file);
							files.getSelectionModel().selectIndices(dropIndex);
							dropIndex++;
						}
						// Hack so that MOVE will not remove files from source.
						event.setDropCompleted(false);
						event.consume();
					}
				}
			});
			return cell;
		}

		private boolean accept(ListCell<File> cell, DragEvent event) {
			return event.getGestureSource() == files
					&& event.getDragboard().hasString()
					&& (cell.getItem() == null || !dragStringContains(event.getDragboard().getString(), cell.getItem()
							.getPath()));
		}

		private boolean dragStringContains(String dragString, String find) {
			boolean found = false;
			for (String element : dragString.split("\\n")) {
				found |= element.equals(find);
			}
			return found;
		}
	}

	private class FileCellListener implements ChangeListener<File> {
		private final Cell<File> cell;

		private FileCellListener(Cell<File> cell) {
			this.cell = cell;
		}

		@Override
		public void changed(ObservableValue<? extends File> observableValue, File oldValue, File newValue) {
			cell.getStyleClass().retainAll(fileCellDefaultClasses);
			if (newValue != null && fileClassesProperty.containsKey(newValue)) {
				cell.getStyleClass().addAll(fileClassesProperty.get(newValue));
			}
		}
	}

	public ListProperty<File> filesProperty = new SimpleListProperty<File>();
	private MapProperty<File, ObservableSet<String>> fileClassesProperty = new SimpleMapProperty<File, ObservableSet<String>>(
			FXCollections.observableMap(new HashMap<File, ObservableSet<String>>()));
	private final ResourceBundle bundle;
	@FXML
	private ListView<File> files;
	@FXML
	private TextField file;
	private FileChooser fileChooser = new FileChooser();
	private final List<String> fileCellDefaultClasses;

	public MultipleFilesPane() {
		bundle = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());

		FxmlResources.loadFxml(this, bundle);

		fileCellDefaultClasses = Collections.unmodifiableList(new FileListCell().getStyleClass());
		filesProperty.set(FXCollections.observableArrayList(new ArrayList<File>()));
		files.itemsProperty().bindBidirectional(filesProperty);
		files.setCellFactory(new DropableFileCellFactory());
		files.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		fileChooser.setTitle(bundle.getString("fileChooser.title"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter(bundle.getString("fileChooser.description"), "*"));
		files.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.DELETE) {
					removeSelectedFiles();
				}
			}
		});

		// Enable drag and drop.
		files.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Dragboard db = files.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				boolean allAbsolute = true;
				for (File file : files.getSelectionModel().getSelectedItems()) {
					allAbsolute &= file.isAbsolute();
				}
				if (allAbsolute) {
					content.putFiles(files.getSelectionModel().getSelectedItems());
				}
				StringBuilder builder = new StringBuilder();
				for (File file : files.getSelectionModel().getSelectedItems()) {
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
		files.setOnDragDone(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (event.getTransferMode() == TransferMode.MOVE) {
					for (String path : event.getDragboard().getString().split("\\n")) {
						files.getItems().remove(new File(path));
					}
				}
				event.consume();
			}
		});
		files.setOnDragOver(new DragFilesOverHandler(files, files));
		files.setOnDragExited(new DragExitedHandler(files));
		files.setOnDragDropped(new EventHandler<DragEvent>() {
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
						files.getItems().add(file);
					}
					event.setDropCompleted(true);
					event.consume();
				} else if (event.getDragboard().hasString()) {
					for (String path : event.getDragboard().getString().split("\\n")) {
						files.getItems().add(new File(path));
					}
					event.setDropCompleted(true);
					event.consume();
				}
			}
		});
	}

	public ListProperty<File> filesProperty() {
		return filesProperty;
	}

	public final ObjectProperty<File> initialDirectoryProperty() {
		return fileChooser.initialDirectoryProperty();
	}

	public List<File> getFiles() {
		List<File> files = new ArrayList<File>();
		if (filesProperty.get() != null) {
			for (File file : filesProperty.get()) {
				files.add(file);
			}
		}
		return files;
	}

	public void setFiles(Collection<File> files) {
		if (files != null) {
			filesProperty.set(FXCollections.observableArrayList(files));
		}
	}

	public void addFiles(ActionEvent event) {
		JavaFXUtils.setValidInitialDirectory(fileChooser);
		List<File> selections = fileChooser.showOpenMultipleDialog(files.getScene().getWindow());
		if (selections != null) {
			if (!selections.isEmpty()) {
				fileChooser.setInitialDirectory(selections.get(0).getParentFile());
			}
			for (File selection : selections) {
				if (!files.getItems().contains(selection)) {
					files.getItems().add(selection);
				}
			}
		}
	}

	@FXML
	private void addFile(ActionEvent event) {
		if (file.getText() != null && !file.getText().isEmpty()) {
			files.getItems().add(new File(file.getText()));
		}
	}

	public void removeFiles(ActionEvent event) {
		removeSelectedFiles();
	}

	private void removeSelectedFiles() {
		List<Integer> selections = new ArrayList<Integer>(files.getSelectionModel().getSelectedIndices());
		Collections.sort(selections);
		Collections.reverse(selections);
		for (Integer selection : selections) {
			if (selection >= 0) {
				files.getItems().remove(selection.intValue());
			}
		}
	}

	public void upFiles(ActionEvent event) {
		List<Integer> selections = new ArrayList<Integer>(files.getSelectionModel().getSelectedIndices());
		Collections.sort(selections);
		int numberOfElementsToSkip = 0;
		while (numberOfElementsToSkip < selections.size()
				&& numberOfElementsToSkip == selections.get(numberOfElementsToSkip)) {
			numberOfElementsToSkip++;
		}

		if (numberOfElementsToSkip < selections.size()) {
			for (int i = numberOfElementsToSkip; i < selections.size(); i++) {
				int sourceIndex = selections.get(i);

				File element = files.getItems().get(sourceIndex);
				files.getItems().remove(sourceIndex);
				files.getItems().add(sourceIndex - 1, element);

				selections.set(i, selections.get(i) - 1);
			}
		}
		files.getSelectionModel().clearSelection();
		for (Integer selection : selections) {
			files.getSelectionModel().select(selection.intValue());
		}
	}

	public void downFiles(ActionEvent event) {
		List<Integer> selections = new ArrayList<Integer>(files.getSelectionModel().getSelectedIndices());
		Collections.sort(selections);
		Collections.reverse(selections);
		int numberOfElementsToSkip = 0;
		while (numberOfElementsToSkip < selections.size()
				&& files.getItems().size() - numberOfElementsToSkip - 1 == selections.get(numberOfElementsToSkip)) {
			numberOfElementsToSkip++;
		}

		if (numberOfElementsToSkip < selections.size()) {
			for (int i = numberOfElementsToSkip; i < selections.size(); i++) {
				int sourceIndex = selections.get(i);

				File element = files.getItems().get(sourceIndex);
				files.getItems().remove(sourceIndex);
				files.getItems().add(sourceIndex + 1, element);

				selections.set(i, selections.get(i) + 1);
			}
		}
		files.getSelectionModel().clearSelection();
		for (Integer selection : selections) {
			files.getSelectionModel().select(selection.intValue());
		}
	}

	public void remvoveStyleClassFromCells(String classStyle) {
		for (Set<String> classes : fileClassesProperty.values()) {
			classes.remove(classStyle);
		}
		updateCellStyleClasses();
	}

	public void addStyleClass(File file, String styleClass) {
		if (!fileClassesProperty.containsKey(file))
			fileClassesProperty.put(file, FXCollections.observableSet(styleClass));
		else
			fileClassesProperty.get(file).add(styleClass);
		updateCellStyleClasses();
	}

	private void updateCellStyleClasses() {
		Set<Node> cells = files.lookupAll(".list-cell");
		for (Node node : cells) {
			if (node instanceof FileListCell) {
				FileListCell cell = (FileListCell) node;
				cell.getStyleClass().retainAll(fileCellDefaultClasses);
				if (cell.getItem() != null && fileClassesProperty.containsKey(cell.getItem())) {
					File file = cell.getItem();
					if (fileClassesProperty.containsKey(cell.getItem())) {
						cell.getStyleClass().addAll(fileClassesProperty.get(file));
					}
				}
			}
		}
	}

	public void setFileChooser(FileChooser fileChooser) {
		this.fileChooser = fileChooser;
	}
}
