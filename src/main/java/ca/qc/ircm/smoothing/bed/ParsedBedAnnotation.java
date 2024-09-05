package ca.qc.ircm.smoothing.bed;

public interface ParsedBedAnnotation extends BedAnnotation {
  /**
   * Returns true if annotation passed validation, false otherwise.
   *
   * @return true if annotation passed validation, false otherwise
   */
  public boolean isValid();
}