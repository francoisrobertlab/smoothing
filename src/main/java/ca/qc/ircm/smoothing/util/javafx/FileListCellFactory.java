package ca.qc.ircm.smoothing.util.javafx;

import java.io.File;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Cell factory for {@link File}.
 */
public class FileListCellFactory implements Callback<ListView<File>, ListCell<File>> {
	@Override
	public ListCell<File> call(ListView<File> list) {
		return new FileListCell();
	}
}
