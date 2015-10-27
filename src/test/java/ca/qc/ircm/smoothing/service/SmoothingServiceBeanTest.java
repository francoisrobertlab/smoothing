package ca.qc.ircm.smoothing.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashMap;

import javafx.scene.paint.Color;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.qc.ircm.progress_bar.ProgressBar;
import ca.qc.ircm.smoothing.bed.BedService;
import ca.qc.ircm.smoothing.bed.BedTrackDefault;
import ca.qc.ircm.smoothing.service.ExecutableService.SmoothingEventListener;
import ca.qc.ircm.smoothing.test.config.TestRunnerLog4J;

/**
 * Tests for {@link SmoothingServiceBean}.
 */
@RunWith(TestRunnerLog4J.class)
public class SmoothingServiceBeanTest {
    private SmoothingServiceBean smoothingServiceBean;
    @Mock
    private ExecutableService executableService;
    @Mock
    private BedService bedService;
    @Mock
    private ProgressBar progressBar;
    @Captor
    private ArgumentCaptor<File> fileCaptor;
    @Captor
    private ArgumentCaptor<SmoothingCoreParameters> smoothingCoreParametersCaptor;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private SmoothingParametersBean parameters;
    private BedTrackDefault track;

    @Before
    public void beforeTest() throws Throwable {
	smoothingServiceBean = new SmoothingServiceBean(executableService, bedService);
	parameters = new SmoothingParametersBean();
	parameters.setStandardDeviation(200);
	parameters.setRounds(2);
	parameters.setStepLength(10);
	parameters.setIncludeSmoothedTrack(true);
	parameters.setIncludeMaximumTrack(false);
	parameters.setIncludeMinimumTrack(false);
	parameters.setMaximumThreshold(3.0);
	parameters.setMinimumThreshold(-3.0);
	parameters.setColors(new HashMap<File, Color>());
	track = new BedTrackDefault();
	track.setName("unit_track");
	track.setDatabase("unit_database");
	when(bedService.parseFirstTrack(any(File.class))).thenReturn(track);
	when(progressBar.step(any(Double.class))).thenReturn(progressBar);
    }

    private File smoothedBed(File bed) {
	String name = FilenameUtils.getBaseName(bed.getName());
	StringBuilder smoothedName = new StringBuilder(name);
	smoothedName.append("_smoothed");
	String extension = FilenameUtils.getExtension(bed.getName());
	if (!extension.equals("")) {
	    smoothedName.append(".");
	    smoothedName.append(extension);
	}
	if (bed.getParentFile() != null) {
	    return new File(bed.getParentFile(), smoothedName.toString());
	} else {
	    return new File(smoothedName.toString());
	}
    }

    @Test
    public void smoothing() throws Throwable {
	final File bed = new File("abc.bed");
	doAnswer(invocation -> {
	    SmoothingEventListener listener = (SmoothingEventListener) invocation.getArguments()[1];
	    listener.processProgress(0.3);
	    listener.processProgress(0.6);
	    listener.processProgress(1.0);
	    return null;
	}).when(executableService).smoothing(any(SmoothingCoreParameters.class), any(SmoothingEventListener.class));
	parameters.setFiles(Collections.nCopies(1, bed));

	smoothingServiceBean.smoothing(parameters, progressBar);

	verify(bedService).parseFirstTrack(bed);
	verify(executableService).smoothing(smoothingCoreParametersCaptor.capture(), any(SmoothingEventListener.class));
	SmoothingCoreParameters coreParameters = smoothingCoreParametersCaptor.getValue();
	assertEquals(bed, coreParameters.getInput());
	assertEquals(smoothedBed(bed), coreParameters.getOutput());
	assertEquals(track.getName(), coreParameters.getTrackName());
	assertEquals(track.getDatabase(), coreParameters.getTrackDatabase());
	assertEquals(parameters.getStandardDeviation(), coreParameters.getStandardDeviation());
	assertEquals(parameters.getRounds(), coreParameters.getRounds());
	assertEquals(parameters.getStepLength(), coreParameters.getStepLength());
	assertEquals(parameters.isIncludeSmoothedTrack(), coreParameters.isIncludeSmoothedTrack());
	assertEquals(parameters.isIncludeMaximumTrack(), coreParameters.isIncludeMaximumTrack());
	assertEquals(parameters.isIncludeMinimumTrack(), coreParameters.isIncludeMinimumTrack());
	assertEquals(parameters.getMaximumThreshold(), coreParameters.getMaximumThreshold(), 0.01);
	assertEquals(parameters.getMinimumThreshold(), coreParameters.getMinimumThreshold(), 0.01);
	verify(progressBar).setMessage(any());
	verify(progressBar, atLeastOnce()).setProgress(any(Double.class));
    }

    @Test
    public void smoothing_NoDatasetName_NoAssemblyName() throws Throwable {
	final File bed = new File("abc.bed");
	track.setName(null);
	track.setDatabase(null);
	doAnswer(invocation -> {
	    SmoothingEventListener listener = (SmoothingEventListener) invocation.getArguments()[1];
	    listener.processProgress(0.3);
	    listener.processProgress(0.6);
	    listener.processProgress(1.0);
	    return null;
	}).when(executableService).smoothing(any(SmoothingCoreParameters.class), any(SmoothingEventListener.class));
	parameters.setFiles(Collections.nCopies(1, bed));

	smoothingServiceBean.smoothing(parameters, progressBar);

	verify(bedService).parseFirstTrack(bed);
	verify(executableService).smoothing(smoothingCoreParametersCaptor.capture(), any(SmoothingEventListener.class));
	SmoothingCoreParameters coreParameters = smoothingCoreParametersCaptor.getValue();
	assertEquals(bed, coreParameters.getInput());
	assertEquals(smoothedBed(bed), coreParameters.getOutput());
	assertEquals("", coreParameters.getTrackName());
	assertEquals("", coreParameters.getTrackDatabase());
	assertEquals(parameters.getStandardDeviation(), coreParameters.getStandardDeviation());
	assertEquals(parameters.getRounds(), coreParameters.getRounds());
	assertEquals(parameters.getStepLength(), coreParameters.getStepLength());
	assertEquals(parameters.isIncludeSmoothedTrack(), coreParameters.isIncludeSmoothedTrack());
	assertEquals(parameters.isIncludeMaximumTrack(), coreParameters.isIncludeMaximumTrack());
	assertEquals(parameters.isIncludeMinimumTrack(), coreParameters.isIncludeMinimumTrack());
	assertEquals(parameters.getMaximumThreshold(), coreParameters.getMaximumThreshold(), 0.01);
	assertEquals(parameters.getMinimumThreshold(), coreParameters.getMinimumThreshold(), 0.01);
	verify(progressBar).setMessage(any());
	verify(progressBar, atLeastOnce()).setProgress(any(Double.class));
    }

    @Test
    public void smoothing_Color() throws Throwable {
	final File bed = temporaryFolder.newFile("abc.bed");
	try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bed), "UTF-8"))) {
	    writer.write("track color=128,0,128");
	}
	final File smoothedBed = temporaryFolder.newFile("abc_smoothed.bed");
	doAnswer(new Answer<Void>() {
	    @Override
	    public Void answer(InvocationOnMock invocation) throws Throwable {
		FileUtils.copyFile(bed, smoothedBed);
		return null;
	    }
	}).when(executableService).smoothing(any(SmoothingCoreParameters.class), any(SmoothingEventListener.class));
	parameters.setFiles(Collections.nCopies(1, bed));
	parameters.getColors().put(bed, Color.AQUA);

	smoothingServiceBean.smoothing(parameters, progressBar);

	verify(executableService).smoothing(any(SmoothingCoreParameters.class), any(SmoothingEventListener.class));
	try (BufferedReader reader = new BufferedReader(
		new InputStreamReader(new FileInputStream(smoothedBed), "UTF-8"))) {
	    String trackLine = reader.readLine();
	    assertEquals("track color=0,255,255", trackLine);
	}
    }
}
