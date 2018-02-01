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
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

import java.io.File;

/**
 * Handles drag over for TextField containing directory path.
 */
public class DragDirectoryOverHandler extends DragOverHandler implements EventHandler<DragEvent> {
  private final Node dragNode;

  public DragDirectoryOverHandler(Node dropNode, Node dragNode) {
    super(dropNode);
    this.dragNode = dragNode;
  }

  @Override
  protected boolean accept(DragEvent event) {
    boolean accept = false;
    if (event.getGestureSource() != dragNode) {
      // Accept directory.
      accept |= event.getDragboard().hasFiles() && event.getDragboard().getFiles().size() == 1
          && validDirectory(event.getDragboard().getFiles().get(0));
      // Accept string with a single line.
      accept |=
          event.getDragboard().hasString() && !event.getDragboard().getString().contains("\n");
    }
    return accept;
  }

  private boolean validDirectory(File file) {
    file = FileUtils.resolveWindowsShorcut(file);
    return file.isDirectory();
  }

  @Override
  protected void setAcceptTransferModes(DragEvent event) {
    event.acceptTransferModes(TransferMode.COPY);
  }
}
