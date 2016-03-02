package ca.qc.ircm.smoothing.util.javafx;

import javafx.scene.paint.Color;
import javafx.util.StringConverter;

/**
 * {@link StringConverter} for {@link Color}.
 */
public class ColorConverter extends StringConverter<Color> {
  @Override
  public Color fromString(String input) {
    return Color.web(input);
  }

  @Override
  public String toString(Color color) {
    return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
  }
}
