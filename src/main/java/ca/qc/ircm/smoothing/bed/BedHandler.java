package ca.qc.ircm.smoothing.bed;

/**
 * Handles BED elements.
 */
public interface BedHandler {
  public void handleTrack(ParsedBedTrack track);

  public void handleAnnotation(ParsedBedAnnotation annotation, ParsedBedTrack track);

  public boolean handleInvalid();
}
