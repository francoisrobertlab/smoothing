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

package ca.qc.ircm.smoothing;

import java.io.File;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * Bed file with track color.
 */
public class BedWithColor {
  private final ObjectProperty<File> fileProperty = new SimpleObjectProperty<File>();
  private final ObjectProperty<Color> colorProperty = new SimpleObjectProperty<Color>();

  public BedWithColor() {
  }

  public BedWithColor(File file) {
    setFile(file);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fileProperty == null) ? 0 : fileProperty.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BedWithColor)) {
      return false;
    }
    BedWithColor other = (BedWithColor) obj;
    if (fileProperty == null) {
      if (other.fileProperty != null) {
        return false;
      }
    } else if (!fileProperty.equals(other.fileProperty)) {
      return false;
    }
    return true;
  }

  public ObjectProperty<File> fileProperty() {
    return fileProperty;
  }

  public ObjectProperty<Color> colorProperty() {
    return colorProperty;
  }

  public File getFile() {
    return fileProperty.get();
  }

  public void setFile(File file) {
    fileProperty.set(file);
  }

  public Color getColor() {
    return colorProperty.get();
  }

  public void setColor(Color color) {
    colorProperty.set(color);
  }
}
