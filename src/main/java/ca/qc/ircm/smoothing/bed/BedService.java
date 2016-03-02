package ca.qc.ircm.smoothing.bed;

import java.io.File;
import java.io.IOException;

/**
 * Services for BED files.
 */
public interface BedService {
  public BedTrack parseFirstTrack(File file) throws IOException;

  public int countFirstTrackData(File file) throws IOException;

  public int countFirstTrackChromosomes(File file) throws IOException;
}
