package ca.qc.ircm.smoothing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.File;
import java.util.Locale;

import javafx.scene.paint.Color;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import ca.qc.ircm.smoothing.service.BedParser.Warning;
import ca.qc.ircm.smoothing.service.BedParser.WarningHandler;
import ca.qc.ircm.smoothing.test.config.TestRunnerLog4J;

/**
 * Tests for {@link BedParserBean}.
 */
@RunWith(TestRunnerLog4J.class)
public class BedParserBeanTest {
	private BedParserBean bedParserBean;
	@Mock
	private WarningHandler warningHandler;
	@Captor
	private ArgumentCaptor<BedTrack> trackCaptor;
	@Captor
	private ArgumentCaptor<BedParser.Warning> warningCaptor;

	@Before
	public void beforeTest() {
		bedParserBean = new BedParserBean();
	}

	@Test
	public void validateFirstTrack_NoErrors() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed.bed").toURI());

		bedParserBean.validateFirstTrack(file, warningHandler);

		verifyZeroInteractions(warningHandler);
	}

	@Test
	public void parseFirstTrack_NoErrors() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed.bed").toURI());

		BedTrack track = bedParserBean.parseFirstTrack(file);

		assertEquals("pairedReads", track.getName());
		assertEquals("hg19", track.getDatabase());
		assertEquals(Color.DARKORANGE, track.getColor());
	}

	@Test
	public void validateFirstTrack_NoTrack() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_no_track.bed").toURI());

		bedParserBean.validateFirstTrack(file, warningHandler);

		verify(warningHandler).handleWarning(warningCaptor.capture());
		Warning warning = warningCaptor.getValue();
		assertEquals(Warning.Type.NO_TRACK, warning.getType());
		assertNotNull(warning.getMessage(Locale.getDefault()));
	}

	@Test
	public void parseFirstTrack_NoTrack() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_no_track.bed").toURI());

		BedTrack track = bedParserBean.parseFirstTrack(file);

		assertEquals(null, track.getName());
		assertEquals(null, track.getDatabase());
		assertEquals(null, track.getColor());
	}

	@Test
	public void validateFirstTrack_MultipleTracks() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_multiple_tracks.bed").toURI());

		bedParserBean.validateFirstTrack(file, warningHandler);

		verifyZeroInteractions(warningHandler);
	}

	@Test
	public void parseFirstTrack_MultipleTracks() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_multiple_tracks.bed").toURI());

		BedTrack track = bedParserBean.parseFirstTrack(file);

		assertEquals("pairedReads", track.getName());
		assertEquals("hg19", track.getDatabase());
		assertEquals(Color.DARKORANGE, track.getColor());
	}

	@Test
	public void validateFirstTrack_Empty() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_empty.bed").toURI());

		bedParserBean.validateFirstTrack(file, warningHandler);

		verify(warningHandler).handleWarning(warningCaptor.capture());
		Warning warning = warningCaptor.getValue();
		assertEquals(Warning.Type.NO_TRACK, warning.getType());
		assertNotNull(warning.getMessage(Locale.getDefault()));
	}

	@Test
	public void parseFirstTrack_Empty() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_empty.bed").toURI());

		BedTrack track = bedParserBean.parseFirstTrack(file);

		assertEquals(null, track.getName());
		assertEquals(null, track.getDatabase());
		assertEquals(null, track.getColor());
	}

	@Test
	public void validateFirstTrack_InvalidTrack() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_invalid_track.bed").toURI());

		bedParserBean.validateFirstTrack(file, warningHandler);

		verify(warningHandler).handleWarning(warningCaptor.capture());
		Warning warning = warningCaptor.getValue();
		assertEquals(Warning.Type.INVALID_TRACK, warning.getType());
		assertNotNull(warning.getMessage(Locale.getDefault()));
	}

	@Test
	public void parseFirstTrack_InvalidTrack() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_invalid_track.bed").toURI());

		BedTrack track = bedParserBean.parseFirstTrack(file);

		assertEquals("pairedReads", track.getName());
		assertEquals("hg19", track.getDatabase());
		assertEquals(Color.DARKORANGE, track.getColor());
	}

	@Test
	public void validateFirstTrack_NoName() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_no_name.bed").toURI());

		bedParserBean.validateFirstTrack(file, warningHandler);

		verify(warningHandler).handleWarning(warningCaptor.capture());
		Warning warning = warningCaptor.getValue();
		assertEquals(Warning.Type.NO_NAME, warning.getType());
		assertNotNull(warning.getMessage(Locale.getDefault()));
	}

	@Test
	public void parseFirstTrack_NoName() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_no_name.bed").toURI());

		BedTrack track = bedParserBean.parseFirstTrack(file);

		assertEquals(null, track.getName());
		assertEquals("hg19", track.getDatabase());
		assertEquals(Color.DARKORANGE, track.getColor());
	}

	@Test
	public void validateFirstTrack_NoDatabase() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_no_database.bed").toURI());

		bedParserBean.validateFirstTrack(file, warningHandler);

		verify(warningHandler).handleWarning(warningCaptor.capture());
		Warning warning = warningCaptor.getValue();
		assertEquals(Warning.Type.NO_DATABASE, warning.getType());
		assertNotNull(warning.getMessage(Locale.getDefault()));
	}

	@Test
	public void parseFirstTrack_NoDatabase() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_no_database.bed").toURI());

		BedTrack track = bedParserBean.parseFirstTrack(file);

		assertEquals("pairedReads", track.getName());
		assertEquals(null, track.getDatabase());
		assertEquals(Color.DARKORANGE, track.getColor());
	}

	@Test
	public void validateFirstTrack_NoColor() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_no_color.bed").toURI());

		bedParserBean.validateFirstTrack(file, warningHandler);

		verify(warningHandler, never()).handleWarning(warningCaptor.capture());
	}

	@Test
	public void parseFirstTrack_NoColor() throws Throwable {
		File file = new File(this.getClass().getResource("/bed/bed_no_color.bed").toURI());

		BedTrack track = bedParserBean.parseFirstTrack(file);

		assertEquals("pairedReads", track.getName());
		assertEquals("hg19", track.getDatabase());
		assertEquals(null, track.getColor());
	}
}
