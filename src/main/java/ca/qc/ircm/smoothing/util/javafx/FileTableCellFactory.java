package ca.qc.ircm.smoothing.util.javafx;

import java.io.File;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Cell factory for {@link File}.
 */
public class FileTableCellFactory<S> implements Callback<TableColumn<S, File>, TableCell<S, File>> {
	@Override
	public TableCell<S, File> call(TableColumn<S, File> list) {
		return new FileTableCell<S>();
	}
}
