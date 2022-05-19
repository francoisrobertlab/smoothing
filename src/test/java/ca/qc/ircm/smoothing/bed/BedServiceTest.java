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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.qc.ircm.smoothing.test.config.ServiceTestAnnotations;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@ServiceTestAnnotations
public class BedServiceTest {
  private BedService bedServiceBean;
  @Mock
  private BedParser bedParser;
  @Mock
  private ParsedBedTrack track1;
  @Mock
  private ParsedBedTrack track2;
  @Mock
  private ParsedBedAnnotation annotation1;
  @Mock
  private ParsedBedAnnotation annotation2;
  @Mock
  private ParsedBedAnnotation annotation3;

  @BeforeEach
  public void beforeTest() {
    bedServiceBean = new BedService(bedParser);
  }

  @Test
  public void parseFirstTrack_MultipleTracks() throws Throwable {
    File file = new File("bed.bed");
    doAnswer(invocation -> {
      BedHandler handler = (BedHandler) invocation.getArguments()[1];
      handler.handleTrack(track1);
      handler.handleAnnotation(annotation1, track1);
      handler.handleTrack(track2);
      handler.handleAnnotation(annotation2, track2);
      return null;
    }).when(bedParser).parse(any(), any());

    BedTrack track = bedServiceBean.parseFirstTrack(file);

    verify(bedParser).parse(eq(file), any());
    assertSame(track1, track);
  }

  @Test
  public void parseFirstTrack_SingleTrack() throws Throwable {
    File file = new File("bed.bed");
    doAnswer(invocation -> {
      BedHandler handler = (BedHandler) invocation.getArguments()[1];
      handler.handleTrack(track1);
      handler.handleAnnotation(annotation1, track1);
      return null;
    }).when(bedParser).parse(any(), any());

    BedTrack track = bedServiceBean.parseFirstTrack(file);

    verify(bedParser).parse(eq(file), any());
    assertSame(track1, track);
  }

  @Test
  public void parseFirstTrack_SingleTrack_NoAnnotation() throws Throwable {
    File file = new File("bed.bed");
    doAnswer(invocation -> {
      BedHandler handler = (BedHandler) invocation.getArguments()[1];
      handler.handleTrack(track1);
      return null;
    }).when(bedParser).parse(any(), any());

    BedTrack track = bedServiceBean.parseFirstTrack(file);

    verify(bedParser).parse(eq(file), any());
    assertSame(track1, track);
  }

  @Test
  public void parseFirstTrack_NoTrack() throws Throwable {
    File file = new File("bed.bed");

    BedTrack track = bedServiceBean.parseFirstTrack(file);

    verify(bedParser).parse(eq(file), any());
    assertNull(track);
  }

  @Test
  public void countFirstTrackData_MultipleTracks() throws Throwable {
    File file = new File("bed.bed");
    doAnswer(invocation -> {
      BedHandler handler = (BedHandler) invocation.getArguments()[1];
      handler.handleTrack(track1);
      handler.handleAnnotation(annotation1, track1);
      handler.handleTrack(track2);
      handler.handleAnnotation(annotation2, track2);
      return null;
    }).when(bedParser).parse(any(), any());

    int count = bedServiceBean.countFirstTrackData(file);

    verify(bedParser).parse(eq(file), any());
    assertEquals(1, count);
  }

  @Test
  public void countFirstTrackData_SingleTrack() throws Throwable {
    File file = new File("bed.bed");
    doAnswer(invocation -> {
      BedHandler handler = (BedHandler) invocation.getArguments()[1];
      handler.handleTrack(track1);
      handler.handleAnnotation(annotation1, track1);
      handler.handleAnnotation(annotation2, track1);
      return null;
    }).when(bedParser).parse(any(), any());

    int count = bedServiceBean.countFirstTrackData(file);

    verify(bedParser).parse(eq(file), any());
    assertEquals(2, count);
  }

  @Test
  public void countFirstTrackData_SingleTrack_NoAnnotation() throws Throwable {
    File file = new File("bed.bed");
    doAnswer(invocation -> {
      BedHandler handler = (BedHandler) invocation.getArguments()[1];
      handler.handleTrack(track1);
      return null;
    }).when(bedParser).parse(any(), any());

    int count = bedServiceBean.countFirstTrackData(file);

    verify(bedParser).parse(eq(file), any());
    assertEquals(0, count);
  }

  @Test
  public void countFirstTrackData_NoTrack() throws Throwable {
    File file = new File("bed.bed");

    int count = bedServiceBean.countFirstTrackData(file);

    verify(bedParser).parse(eq(file), any());
    assertEquals(0, count);
  }

  @Test
  public void countFirstTrackChromosomes_MultipleTracks() throws Throwable {
    File file = new File("bed.bed");
    when(annotation1.getChromosome()).thenReturn("chr1");
    when(annotation2.getChromosome()).thenReturn("chr2");
    doAnswer(invocation -> {
      BedHandler handler = (BedHandler) invocation.getArguments()[1];
      handler.handleTrack(track1);
      handler.handleAnnotation(annotation1, track1);
      handler.handleTrack(track2);
      handler.handleAnnotation(annotation2, track2);
      return null;
    }).when(bedParser).parse(any(), any());

    int count = bedServiceBean.countFirstTrackChromosomes(file);

    verify(bedParser).parse(eq(file), any());
    assertEquals(1, count);
  }

  @Test
  public void countFirstTrackChromosomes_SingleTrack() throws Throwable {
    File file = new File("bed.bed");
    when(annotation1.getChromosome()).thenReturn("chr1");
    when(annotation2.getChromosome()).thenReturn("chr2");
    when(annotation3.getChromosome()).thenReturn("chr2");
    doAnswer(invocation -> {
      BedHandler handler = (BedHandler) invocation.getArguments()[1];
      handler.handleTrack(track1);
      handler.handleAnnotation(annotation1, track1);
      handler.handleAnnotation(annotation2, track1);
      handler.handleAnnotation(annotation3, track1);
      return null;
    }).when(bedParser).parse(any(), any());

    int count = bedServiceBean.countFirstTrackChromosomes(file);

    verify(bedParser).parse(eq(file), any());
    assertEquals(2, count);
  }

  @Test
  public void countFirstTrackChromosomes_SingleTrack_NoAnnotation() throws Throwable {
    File file = new File("bed.bed");
    doAnswer(invocation -> {
      BedHandler handler = (BedHandler) invocation.getArguments()[1];
      handler.handleTrack(track1);
      return null;
    }).when(bedParser).parse(any(), any());

    int count = bedServiceBean.countFirstTrackChromosomes(file);

    verify(bedParser).parse(eq(file), any());
    assertEquals(0, count);
  }

  @Test
  public void countFirstTrackChromosomes_NoTrack() throws Throwable {
    File file = new File("bed.bed");

    int count = bedServiceBean.countFirstTrackChromosomes(file);

    verify(bedParser).parse(eq(file), any());
    assertEquals(0, count);
  }
}
