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
