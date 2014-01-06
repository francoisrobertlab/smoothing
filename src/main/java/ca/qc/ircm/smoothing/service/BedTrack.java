package ca.qc.ircm.smoothing.service;

import javafx.scene.paint.Color;

/**
 * Track inside a BED file.
 */
public interface BedTrack {
	/**
	 * Returns track's name.
	 * 
	 * @return track's name
	 */
	public String getName();

	/**
	 * Returns track's database.
	 * 
	 * @return track's database
	 */
	public String getDatabase();

	/**
	 * Returns track's color.
	 * 
	 * @return track's color
	 */
	public Color getColor();
}
