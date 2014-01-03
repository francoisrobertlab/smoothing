package ca.qc.ircm.smoothing.util.javafx;

import java.io.File;

import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

/**
 * {@link TextFieldTableCell} that shows files.
 */
public class FileTableCell<S> extends TextFieldTableCell<S, File> {
	private static class FileConverter extends StringConverter<File> {
		@Override
		public File fromString(String input) {
			return new File(input);
		}

		@Override
		public String toString(File file) {
			return file != null ? file.getPath() : "";
		}
	}

	public FileTableCell() {
		super(new FileConverter());
	}

	@Override
	public void updateItem(File file, boolean empty) {
		super.updateItem(file, empty);
		if (file != null) {
			setText(file.getName());
			setTooltip(new Tooltip(file.getPath()));
		} else {
			setText(null);
			setTooltip(null);
		}
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		File file = getItem();
		if (file != null) {
			setText(file.getName());
			setTooltip(new Tooltip(file.getPath()));
		} else {
			setText(null);
			setTooltip(null);
		}
	}
}
