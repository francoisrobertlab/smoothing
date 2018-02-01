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
