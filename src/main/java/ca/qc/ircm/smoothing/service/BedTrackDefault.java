package ca.qc.ircm.smoothing.service;

public class BedTrackDefault implements BedTrack {
	public String name;
	public String database;

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
	public String toString() {
		StringBuilder builder = new StringBuilder("BedTrackDefault_");
		builder.append(name);
		return builder.toString();
	}
}