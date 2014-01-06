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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BedWithColor))
			return false;
		BedWithColor other = (BedWithColor) obj;
		if (fileProperty == null) {
			if (other.fileProperty != null)
				return false;
		} else if (!fileProperty.equals(other.fileProperty))
			return false;
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
