package ca.qc.ircm.smoothing.service;

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
}