/*
 * Copyright (c) 2015 Institut de recherches cliniques de Montreal (IRCM)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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

  /**
   * Parses first track.
   *
   * @param file
   *          BED file
   * @return first track
   * @throws IOException
   *           could not parse BED file
   */
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

  /**
   * Counts number of annotations in first track.
   *
   * @param file
   *          BED file
   * @return number of annotations in first track
   * @throws IOException
   *           could not parse BED file
   */
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

  /**
   * Counts number of chromosomes in first track.
   *
   * @param file
   *          BED file
   * @return number of chromosomes in first track
   * @throws IOException
   *           could not parse BED file
   */
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
