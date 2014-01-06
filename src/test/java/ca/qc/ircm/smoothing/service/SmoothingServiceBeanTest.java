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
import java.io.IOException;
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

import ca.qc.ircm.smoothing.service.ExecutableService.SmoothingEventListener;
import ca.qc.ircm.smoothing.service.SmoothingService.SmoothingProgressBar;
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
	private BedParser bedParser;
	@Mock
	private SmoothingProgressBar progressBar;
	@Captor
	private ArgumentCaptor<File> fileCaptor;
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();
	private SmoothingParametersBean parameters;
	private BedTrackDefault track;

	@Before
	public void beforeTest() throws Throwable {
		smoothingServiceBean = new SmoothingServiceBean(executableService, bedParser);
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
		when(bedParser.parseFirstTrack(any(File.class))).thenReturn(track);
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

	private void validateParametersFile(File file, File bed, SmoothingParametersBean parameters, String datasetName,
			String assemblyName) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			assertEquals(bed.getAbsolutePath(), reader.readLine());
			assertEquals(smoothedBed(bed).getAbsolutePath(), reader.readLine());
			assertEquals(datasetName, reader.readLine());
			assertEquals(assemblyName, reader.readLine());
			assertEquals(parameters.getStandardDeviation(), Integer.valueOf(reader.readLine()));
			assertEquals(parameters.getRounds(), Integer.valueOf(reader.readLine()));
			assertEquals(parameters.getStepLength(), Integer.valueOf(reader.readLine()));
			assertEquals(parameters.isIncludeSmoothedTrack(), reader.readLine().equals("1"));
			assertEquals(parameters.isIncludeMaximumTrack(), reader.readLine().equals("1"));
			assertEquals(parameters.isIncludeMinimumTrack(), reader.readLine().equals("1"));
			assertEquals(parameters.getMaximumThreshold(), Double.valueOf(reader.readLine()), 0.1);
			assertEquals(parameters.getMinimumThreshold(), Double.valueOf(reader.readLine()), 0.1);
		}
	}

	@Test
	public void smoothing() throws Throwable {
		final File bed = new File("abc.bed");
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				validateParametersFile((File) invocation.getArguments()[0], bed, parameters, "unit_track",
						"unit_database");
				SmoothingEventListener listener = (SmoothingEventListener) invocation.getArguments()[1];
				listener.processProgress(0.3);
				listener.processProgress(0.6);
				listener.processProgress(1.0);
				return null;
			}
		}).when(executableService).smoothing(any(File.class), any(SmoothingEventListener.class));
		parameters.setFiles(Collections.nCopies(1, bed));

		smoothingServiceBean.smoothing(parameters, progressBar);

		verify(bedParser).parseFirstTrack(bed);
		verify(executableService).smoothing(any(File.class), any(SmoothingEventListener.class));
		verify(progressBar).setFile(bed);
		verify(progressBar, atLeastOnce()).setProgress(any(Double.class));
	}

	@Test
	public void smoothing_NoDatasetName_NoAssemblyName() throws Throwable {
		final File bed = new File("abc.bed");
		track.setName(null);
		track.setDatabase(null);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				validateParametersFile((File) invocation.getArguments()[0], bed, parameters, "", "");
				SmoothingEventListener listener = (SmoothingEventListener) invocation.getArguments()[1];
				listener.processProgress(0.3);
				listener.processProgress(0.6);
				listener.processProgress(1.0);
				return null;
			}
		}).when(executableService).smoothing(any(File.class), any(SmoothingEventListener.class));
		parameters.setFiles(Collections.nCopies(1, bed));

		smoothingServiceBean.smoothing(parameters, progressBar);

		verify(bedParser).parseFirstTrack(bed);
		verify(executableService).smoothing(any(File.class), any(SmoothingEventListener.class));
		verify(progressBar).setFile(bed);
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
		}).when(executableService).smoothing(any(File.class), any(SmoothingEventListener.class));
		parameters.setFiles(Collections.nCopies(1, bed));
		parameters.getColors().put(bed, Color.AQUA);

		smoothingServiceBean.smoothing(parameters, progressBar);

		verify(executableService).smoothing(any(File.class), any(SmoothingEventListener.class));
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(smoothedBed), "UTF-8"))) {
			String trackLine = reader.readLine();
			assertEquals("track color=0,255,255", trackLine);
		}
	}
}
