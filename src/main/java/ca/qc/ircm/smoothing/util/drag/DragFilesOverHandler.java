package ca.qc.ircm.smoothing.util.drag;

import ca.qc.ircm.smoothing.util.FileUtils;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

import java.io.File;

/**
 * Handles drag over for TextField containing file path.
 */
public class DragFilesOverHandler extends DragOverHandler implements EventHandler<DragEvent> {
  private final Node dragNode;

  public DragFilesOverHandler(Node dropNode, Node dragNode) {
    super(dropNode);
    this.dragNode = dragNode;
  }

  @Override
  protected boolean accept(DragEvent event) {
    boolean accept = false;
    if (event.getGestureSource() != dragNode) {
      // Accept files.
      accept |= event.getDragboard().hasFiles() && validFiles(event.getDragboard().getFiles());
      // Accept any string.
      accept |= event.getDragboard().hasString();
    }
    return accept;
  }

  private boolean validFiles(Iterable<File> files) {
    boolean validFiles = true;
    for (File file : files) {
      file = FileUtils.resolveWindowsShorcut(file);
      if (!file.isFile()) {
        validFiles = false;
      }
    }
    return validFiles;
  }

  @Override
  protected void setAcceptTransferModes(DragEvent event) {
    if (event.getGestureSource() instanceof Node) {
      event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
    } else {
      event.acceptTransferModes(TransferMode.COPY);
    }
  }
}
