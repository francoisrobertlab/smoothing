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

package ca.qc.ircm.smoothing.util.drag;

import ca.qc.ircm.smoothing.util.FileUtils;
import java.io.File;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;

/**
 * Handles drag dropped for TextField containing directory path.
 */
public class DragDirectoryDroppedHandler extends DragFileDroppedHandler
    implements EventHandler<DragEvent> {
  public DragDirectoryDroppedHandler(TextField text) {
    super(text);
  }

  @Override
  protected boolean validFile(File file) {
    file = FileUtils.resolveWindowsShorcut(file);
    return file.isDirectory();
  }
}
