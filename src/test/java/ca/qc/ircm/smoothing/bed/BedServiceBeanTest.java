package ca.qc.ircm.smoothing.bed;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ca.qc.ircm.smoothing.test.config.TestRunnerLog4J;

@RunWith(TestRunnerLog4J.class)
public class BedServiceBeanTest {
    private BedServiceBean bedServiceBean;
    @Mock
    private BedParser bedParser;
    @Mock
    private ParsedBedTrack track1;
    @Mock
    private ParsedBedAnnotation annotation1;
    @Mock
    private ParsedBedTrack track2;
    @Mock
    private ParsedBedAnnotation annotation2;

    @Before
    public void beforeTest() {
	bedServiceBean = new BedServiceBean(bedParser);
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
}
