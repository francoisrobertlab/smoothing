package ca.qc.ircm.smoothing.service;

import javafx.scene.paint.Color;

public class BedTrackDefault implements BedTrack {
	public String name;
	public String database;
	public Color color;

	public BedTrackDefault() {
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	@Override
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("BedTrackDefault_");
		builder.append(name);
		return builder.toString();
	}
}