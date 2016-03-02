package ca.qc.ircm.smoothing.bed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import ca.qc.ircm.smoothing.bio.Strand;
import ca.qc.ircm.smoothing.test.config.Rules;
import ca.qc.ircm.smoothing.validation.WarningHandler;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.File;
import java.util.Locale;

public class BedParserBeanTest {
  private BedParserBean bedParser;
  @Mock
  private BedHandler bedHandler;
  @Mock
  private WarningHandler warningHandler;
  @Captor
  private ArgumentCaptor<ParsedBedTrack> tracksCaptor;
  @Captor
  private ArgumentCaptor<ParsedBedAnnotation> annotationsCaptor;
  @Captor
  private ArgumentCaptor<ParsedBedTrack> annotationsTrackCaptor;
  @Captor
  private ArgumentCaptor<String> warningCaptor;
  @Rule
  public RuleChain rules = Rules.defaultRules(this);

  @Before
  public void beforeTest() {
    bedParser = new BedParserBean();
    when(bedHandler.handleInvalid()).thenReturn(true);
  }

  @Test
  public void validate_NoErrors() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_NoErrors_Reporter() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_reporter.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_NoErrors_Spaces() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_spaces.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_NoErrors_2Spaces() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_2spaces.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_Minimal() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_minimal.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_MultipleTracks() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_multiple_tracks.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_SingleColor() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_single_color.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_NoTrack() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_track.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_MultipleTracksAndNoTrack() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_multiple_track_and_no_track.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_Browser() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_browser.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_EmptyBed() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_empty.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_EmptyTrack() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_empty_track.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_EmptyLines() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_empty_lines.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_InvalidBrowser() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_browser.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_InvalidTrack() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_track.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(1)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getValue());
  }

  @Test
  public void validate_InvalidTrackType() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_track_type.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(1)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getValue());
  }

  @Test
  public void validate_InvalidColumnNumber() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalidColumnNumber.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(1)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getValue());
  }

  @Test
  public void validate_NoChromosome() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_chromosome.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_InvalidChromosome() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_chromosome.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_NoStart() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_start_no_end.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(4)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
    assertNotNull(warningCaptor.getAllValues().get(2));
    assertNotNull(warningCaptor.getAllValues().get(3));
  }

  @Test
  public void validate_InvalidStart() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_start.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_NoEnd() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_end.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_InvalidEnd() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_end.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_NoName() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_name.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_NoScore() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_score.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_InvalidScore() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_score.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_NoStrand() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_strand.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_InvalidStrand() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_strand.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_NoThickStart() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_thickStart.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_InvalidThickStart() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_thickStart.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_ThickStartOutsideLocation() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_thick_start_outside_location.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_NoThickEnd() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_thickEnd.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_InvalidThickEnd() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_thickEnd.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_ThickEndOutsideLocation() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_thick_end_outside_location.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_NoItemRgb() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_itemRgb.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_InvalidItemRgb() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_itemRgb.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_ItemRgb_MissingBlue() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_invalid_itemRgb_missingBlue.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_NoBlock() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_block.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_InvalidBlockCount() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_blockCount.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_InvalidBlockSizes() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_blockSizes.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_MissMatchBlockSizesCount() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_missmatch_blockSizesCount.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_InvalidBlockStarts() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_blockStarts.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_MissMatchBlockStartsCount() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_missmatch_blockStartsCount.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_InvalidFirstBlockStart() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_invalid_first_block_start.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_InvalidLastBlock() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_last_block.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_Wiggle() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/wiggle.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_EmptyDataValue() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/wiggle_empty_datavalue.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_InvalidDataValue() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/wiggle_invalid_datavalue.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verify(warningHandler, times(2)).handle(warningCaptor.capture());
    assertNotNull(warningCaptor.getAllValues().get(0));
    assertNotNull(warningCaptor.getAllValues().get(1));
  }

  @Test
  public void validate_DataValue_Exponent() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/wiggle_datavalue_exponent.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void validate_BedWithWiggle() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_w_wiggle.bed").toURI());

    bedParser.validate(file, Locale.CANADA, warningHandler);

    verifyZeroInteractions(warningHandler);
  }

  @Test
  public void parse_NoErrors() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_NoErrors_Reporter() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_reporter.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("Mo_19K_prom", track.getName());
    assertEquals("reporters printed on Mo_19K_prom for assembly M.musculus.May04",
        track.getDescription());
    assertEquals("0,60,120", track.getAllParameters().get("color"));
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("Mo_19K_prom", track.getName());
    assertEquals("reporters printed on Mo_19K_prom for assembly M.musculus.May04",
        track.getDescription());
    assertEquals("0,60,120", track.getAllParameters().get("color"));
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("Mo_19K_prom", track.getName());
    assertEquals("reporters printed on Mo_19K_prom for assembly M.musculus.May04",
        track.getDescription());
    assertEquals("0,60,120", track.getAllParameters().get("color"));
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_NoErrors_Spaces() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_spaces.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_NoErrors_2Spaces() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_2spaces.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_Minimal() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_minimal.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals(null, track.getDescription());
    assertEquals(1, track.getAllParameters().size());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals(null, annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals(null, annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals(null, track.getDescription());
    assertEquals(1, track.getAllParameters().size());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals(null, track.getDescription());
    assertEquals(1, track.getAllParameters().size());
  }

  @Test
  public void parse_MultipleTracks() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_multiple_tracks.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler, times(2)).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(5)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(2, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = tracksCaptor.getAllValues().get(1);
    assertEquals("ColorByStrandDemo", track.getName());
    assertEquals("Color by strand demonstration", track.getDescription());
    assertEquals("2", track.getAllParameters().get("visibility"));
    assertEquals("255,0,0 0,0,255", track.getAllParameters().get("colorByStrand"));
    assertEquals(5, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(2);
    assertEquals("7", annotation.getChromosome());
    assertEquals((Long) 127471196L, annotation.getStart());
    assertEquals((Long) 127472363L, annotation.getEnd());
    assertEquals("Pos1", annotation.getName());
    assertEquals((Integer) 0, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    annotation = annotationsCaptor.getAllValues().get(3);
    assertEquals("7", annotation.getChromosome());
    assertEquals((Long) 127472363L, annotation.getStart());
    assertEquals((Long) 127473530L, annotation.getEnd());
    assertEquals("Pos2", annotation.getName());
    assertEquals((Integer) 0, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    annotation = annotationsCaptor.getAllValues().get(4);
    assertEquals("7", annotation.getChromosome());
    assertEquals((Long) 127473530L, annotation.getStart());
    assertEquals((Long) 127474697L, annotation.getEnd());
    assertEquals("Pos3", annotation.getName());
    assertEquals((Integer) 0, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(5, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(2);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("ColorByStrandDemo", track.getName());
    assertEquals("Color by strand demonstration", track.getDescription());
    assertEquals("2", track.getAllParameters().get("visibility"));
    assertEquals("255,0,0 0,0,255", track.getAllParameters().get("colorByStrand"));
    track = annotationsTrackCaptor.getAllValues().get(3);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("ColorByStrandDemo", track.getName());
    assertEquals("Color by strand demonstration", track.getDescription());
    assertEquals("2", track.getAllParameters().get("visibility"));
    assertEquals("255,0,0 0,0,255", track.getAllParameters().get("colorByStrand"));
    track = annotationsTrackCaptor.getAllValues().get(4);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("ColorByStrandDemo", track.getName());
    assertEquals("Color by strand demonstration", track.getDescription());
    assertEquals("2", track.getAllParameters().get("visibility"));
    assertEquals("255,0,0 0,0,255", track.getAllParameters().get("colorByStrand"));
  }

  @Test
  public void parse_SingleColor() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_single_color.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(0, annotation.getItemRgb().getGreen());
    assertEquals(0, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(0, annotation.getItemRgb().getGreen());
    assertEquals(0, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_NoTrack() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_track.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler, never()).handleTrack(any(ParsedBedTrack.class));
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    assertEquals(null, annotationsTrackCaptor.getAllValues().get(0));
    assertEquals(null, annotationsTrackCaptor.getAllValues().get(1));
  }

  @Test
  public void parse_MultipleTracksAndNoTrack() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_multiple_track_and_no_track.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(5)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("ColorByStrandDemo", track.getName());
    assertEquals("Color by strand demonstration", track.getDescription());
    assertEquals("2", track.getAllParameters().get("visibility"));
    assertEquals("255,0,0 0,0,255", track.getAllParameters().get("colorByStrand"));
    assertEquals(5, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(2);
    assertEquals("7", annotation.getChromosome());
    assertEquals((Long) 127471196L, annotation.getStart());
    assertEquals((Long) 127472363L, annotation.getEnd());
    assertEquals("Pos1", annotation.getName());
    assertEquals((Integer) 0, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    annotation = annotationsCaptor.getAllValues().get(3);
    assertEquals("7", annotation.getChromosome());
    assertEquals((Long) 127472363L, annotation.getStart());
    assertEquals((Long) 127473530L, annotation.getEnd());
    assertEquals("Pos2", annotation.getName());
    assertEquals((Integer) 0, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    annotation = annotationsCaptor.getAllValues().get(4);
    assertEquals("7", annotation.getChromosome());
    assertEquals((Long) 127473530L, annotation.getStart());
    assertEquals((Long) 127474697L, annotation.getEnd());
    assertEquals("Pos3", annotation.getName());
    assertEquals((Integer) 0, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(5, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(null, track);
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(null, track);
    track = annotationsTrackCaptor.getAllValues().get(2);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("ColorByStrandDemo", track.getName());
    assertEquals("Color by strand demonstration", track.getDescription());
    assertEquals("2", track.getAllParameters().get("visibility"));
    assertEquals("255,0,0 0,0,255", track.getAllParameters().get("colorByStrand"));
    track = annotationsTrackCaptor.getAllValues().get(3);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("ColorByStrandDemo", track.getName());
    assertEquals("Color by strand demonstration", track.getDescription());
    assertEquals("2", track.getAllParameters().get("visibility"));
    assertEquals("255,0,0 0,0,255", track.getAllParameters().get("colorByStrand"));
    track = annotationsTrackCaptor.getAllValues().get(4);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("ColorByStrandDemo", track.getName());
    assertEquals("Color by strand demonstration", track.getDescription());
    assertEquals("2", track.getAllParameters().get("visibility"));
    assertEquals("255,0,0 0,0,255", track.getAllParameters().get("colorByStrand"));
  }

  @Test
  public void parse_Browser() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_browser.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_EmptyBed() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_empty.bed").toURI());

    bedParser.parse(file, bedHandler);

    verifyZeroInteractions(bedHandler);
  }

  @Test
  public void parse_EmptyTrack() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_empty_track.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, never()).handleAnnotation(any(ParsedBedAnnotation.class),
        any(ParsedBedTrack.class));
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_EmptyLines() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_empty_lines.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_InvalidBrowser() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_browser.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_ValidOnlyHandler_InvalidTrack() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_track.bed").toURI());
    when(bedHandler.handleInvalid()).thenReturn(false);

    bedParser.parse(file, bedHandler);

    verify(bedHandler, never()).handleTrack(any(ParsedBedTrack.class));
    verify(bedHandler, never()).handleAnnotation(any(ParsedBedAnnotation.class),
        any(ParsedBedTrack.class));
  }

  @Test
  public void parse_ValidOnlyHandler_InvalidColumnNumber() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalidColumnNumber.bed").toURI());
    when(bedHandler.handleInvalid()).thenReturn(false);

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(1)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(1, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
  }

  @Test
  public void parse_InvalidTrack() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_track.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(false, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(true, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    assertEquals(true, annotation.isValid());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(false, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(false, track.isValid());
  }

  @Test
  public void parse_InvalidTrackType() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_track_type.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(false, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(true, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(true, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(false, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(false, track.isValid());
  }

  @Test
  public void parse_InvalidColumnNumber() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalidColumnNumber.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(true, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_NoChromosome() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_chromosome.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals(null, annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals(null, annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidChromosome() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_chromosome.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals(null, annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals(null, annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_NoStart() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_start_no_end.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals(null, annotation.getStart());
    assertEquals(null, annotation.getEnd());
    assertEquals(null, annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals(null, annotation.getStart());
    assertEquals(null, annotation.getEnd());
    assertEquals(null, annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidStart() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_start.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals(null, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals(null, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_NoEnd() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_end.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals(null, annotation.getEnd());
    assertEquals(null, annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals(null, annotation.getEnd());
    assertEquals(null, annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidEnd() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_end.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals(null, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals(null, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_NoName() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_name.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals(null, annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals(null, annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_NoScore() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_score.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidScore() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_score.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals(null, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_NoStrand() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_strand.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidStrand() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_strand.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(null, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_NoThickStart() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_thickStart.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidThickStart() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_thickStart.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals(null, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_ThickStartOutsideLocation() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_thick_start_outside_location.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 900L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 1900L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_NoThickEnd() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_thickEnd.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidThickEnd() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_thickEnd.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals(null, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_ThickEndOutsideLocation() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_thick_end_outside_location.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 5005L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 6005L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_NoItemRgb() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_itemRgb.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidItemRgb() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_itemRgb.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_ItemRgb_MissingBlue() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_invalid_itemRgb_missingBlue.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(null, annotation.getItemRgb());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_NoBlock() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_no_block.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(null, annotation.getBlockSizes());
    assertEquals(null, annotation.getBlockStarts());
    assertEquals(true, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidBlockCount() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_blockCount.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals(null, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidBlockSizes() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_blockSizes.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(0, annotation.getBlockSizes().size());
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(0, annotation.getBlockSizes().size());
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_MissMatchBlockSizesCount() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_missmatch_blockSizesCount.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(1, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(3, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals((Long) 380L, annotation.getBlockSizes().get(2));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidBlockStarts() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_blockStarts.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(0, annotation.getBlockStarts().size());
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(0, annotation.getBlockStarts().size());
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_MissMatchBlockStartsCount() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_missmatch_blockStartsCount.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(1, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(3, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals((Long) 3854L, annotation.getBlockStarts().get(2));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidFirstBlockStart() throws Throwable {
    File file =
        new File(this.getClass().getResource("/bed/bed_invalid_first_block_start.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 10L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 10L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidLastBlock() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_invalid_last_block.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3502L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3591L, annotation.getBlockStarts().get(1));
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_Wiggle() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/wiggle.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals((Double) 0.25, annotation.getDataValue());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals(new Double(-0.15), annotation.getDataValue());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_EmptyDataValue() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/wiggle_empty_datavalue.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals(null, annotation.getDataValue());
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals(null, annotation.getDataValue());
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_InvalidDataValue() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/wiggle_invalid_datavalue.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    ParsedBedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    assertEquals(2, annotationsCaptor.getAllValues().size());
    ParsedBedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals(null, annotation.getDataValue());
    assertEquals(false, annotation.isValid());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals(null, annotation.getDataValue());
    assertEquals(false, annotation.isValid());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(true, track.isValid());
  }

  @Test
  public void parse_DataValue_Exponent() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/wiggle_datavalue_exponent.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(2)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(1, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(2, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals((Double) 0.25, annotation.getDataValue());
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals(new Double(-0.15), annotation.getDataValue());
    assertEquals(2, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }

  @Test
  public void parse_BedWithWiggle() throws Throwable {
    File file = new File(this.getClass().getResource("/bed/bed_w_wiggle.bed").toURI());

    bedParser.parse(file, bedHandler);

    verify(bedHandler, times(2)).handleTrack(tracksCaptor.capture());
    verify(bedHandler, times(4)).handleAnnotation(annotationsCaptor.capture(),
        annotationsTrackCaptor.capture());
    assertEquals(2, tracksCaptor.getAllValues().size());
    BedTrack track = tracksCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = tracksCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("wpairedReads", track.getName());
    assertEquals("Wiggle Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    assertEquals(4, annotationsCaptor.getAllValues().size());
    BedAnnotation annotation = annotationsCaptor.getAllValues().get(0);
    assertEquals("21", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals("cloneA", annotation.getName());
    assertEquals((Integer) 960, annotation.getScore());
    assertEquals(Strand.PLUS, annotation.getStrand());
    assertEquals((Long) 1005L, annotation.getThickStart());
    assertEquals((Long) 4995L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 567L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 488L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3512L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(1);
    assertEquals("22", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals("cloneB", annotation.getName());
    assertEquals((Integer) 900, annotation.getScore());
    assertEquals(Strand.MINUS, annotation.getStrand());
    assertEquals((Long) 2006L, annotation.getThickStart());
    assertEquals((Long) 5994L, annotation.getThickEnd());
    assertEquals(0, annotation.getItemRgb().getRed());
    assertEquals(1, annotation.getItemRgb().getGreen());
    assertEquals(2, annotation.getItemRgb().getBlue());
    assertEquals((Integer) 2, annotation.getBlockCount());
    assertEquals(2, annotation.getBlockSizes().size());
    assertEquals((Long) 433L, annotation.getBlockSizes().get(0));
    assertEquals((Long) 399L, annotation.getBlockSizes().get(1));
    assertEquals(2, annotation.getBlockStarts().size());
    assertEquals((Long) 0L, annotation.getBlockStarts().get(0));
    assertEquals((Long) 3601L, annotation.getBlockStarts().get(1));
    annotation = annotationsCaptor.getAllValues().get(2);
    assertEquals("11", annotation.getChromosome());
    assertEquals((Long) 1000L, annotation.getStart());
    assertEquals((Long) 5000L, annotation.getEnd());
    assertEquals((Double) 0.25, annotation.getDataValue());
    annotation = annotationsCaptor.getAllValues().get(3);
    assertEquals("12", annotation.getChromosome());
    assertEquals((Long) 2000L, annotation.getStart());
    assertEquals((Long) 6000L, annotation.getEnd());
    assertEquals(new Double(-0.15), annotation.getDataValue());
    assertEquals(4, annotationsTrackCaptor.getAllValues().size());
    track = annotationsTrackCaptor.getAllValues().get(0);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(1);
    assertEquals(BedTrack.Type.BED, track.getType());
    assertEquals("pairedReads", track.getName());
    assertEquals("Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(2);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("wpairedReads", track.getName());
    assertEquals("Wiggle Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
    track = annotationsTrackCaptor.getAllValues().get(3);
    assertEquals(BedTrack.Type.WIGGLE, track.getType());
    assertEquals("wpairedReads", track.getName());
    assertEquals("Wiggle Clone Paired Reads", track.getDescription());
    assertEquals("1", track.getAllParameters().get("useScore"));
  }
}
