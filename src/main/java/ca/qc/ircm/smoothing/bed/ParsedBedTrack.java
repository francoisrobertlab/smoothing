package ca.qc.ircm.smoothing.bed;

public interface ParsedBedTrack extends BedTrack {
  /**
   * Returns true if track passed validation, false otherwise.
   *
   * @return true if track passed validation, false otherwise
   */
  public boolean isValid();
}