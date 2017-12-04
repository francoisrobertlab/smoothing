package ca.qc.ircm.smoothing.bed;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

/**
 * Services for BED files.
 */
@Service
public class BedService {
  @Inject
  private BedParser bedParser;

  protected BedService() {
  }

  protected BedService(BedParser bedParser) {
    this.bedParser = bedParser;
  }

  public BedTrack parseFirstTrack(File file) throws IOException {
    class FirstTrackBedHandler implements BedHandler {
      private BedTrack firstTrack;

      @Override
      public void handleTrack(ParsedBedTrack track) {
        if (firstTrack == null) {
          firstTrack = track;
        }
      }

      @Override
      public void handleAnnotation(ParsedBedAnnotation annotation, ParsedBedTrack track) {
      }

      @Override
      public boolean handleInvalid() {
        return true;
      }
    }

    FirstTrackBedHandler handler = new FirstTrackBedHandler();
    bedParser.parse(file, handler);
    return handler.firstTrack;
  }

  public int countFirstTrackData(File file) throws IOException {
    class FirstTrackBedHandler implements BedHandler {
      private BedTrack firstTrack;
      private int count;

      @Override
      public void handleTrack(ParsedBedTrack track) {
        if (firstTrack == null) {
          firstTrack = track;
        }
      }

      @Override
      public void handleAnnotation(ParsedBedAnnotation annotation, ParsedBedTrack track) {
        if (track.equals(firstTrack)) {
          count++;
        }
      }

      @Override
      public boolean handleInvalid() {
        return true;
      }
    }

    FirstTrackBedHandler handler = new FirstTrackBedHandler();
    bedParser.parse(file, handler);
    return handler.count;
  }

  public int countFirstTrackChromosomes(File file) throws IOException {
    class FirstTrackBedHandler implements BedHandler {
      private BedTrack firstTrack;
      private Set<String> chromosomes = new HashSet<>();

      @Override
      public void handleTrack(ParsedBedTrack track) {
        if (firstTrack == null) {
          firstTrack = track;
        }
      }

      @Override
      public void handleAnnotation(ParsedBedAnnotation annotation, ParsedBedTrack track) {
        if (track.equals(firstTrack) && annotation.getChromosome() != null) {
          chromosomes.add(annotation.getChromosome());
        }
      }

      @Override
      public boolean handleInvalid() {
        return true;
      }
    }

    FirstTrackBedHandler handler = new FirstTrackBedHandler();
    bedParser.parse(file, handler);
    return handler.chromosomes.size();
  }
}
