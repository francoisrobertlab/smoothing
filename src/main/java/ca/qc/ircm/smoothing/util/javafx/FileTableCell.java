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
