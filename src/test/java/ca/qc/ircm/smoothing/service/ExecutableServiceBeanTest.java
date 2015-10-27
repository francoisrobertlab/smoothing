package ca.qc.ircm.smoothing.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.Charset;

import javax.inject.Provider;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import ca.qc.ircm.smoothing.OperatingSystem;
import ca.qc.ircm.smoothing.OperatingSystemService;
import ca.qc.ircm.smoothing.service.ExecutableService.SmoothingEventListener;
import ca.qc.ircm.smoothing.test.config.TestRunnerLog4J;

/**
 * Tests for {@link ExecutableServiceBean}.
 */
@RunWith(TestRunnerLog4J.class)
public class ExecutableServiceBeanTest {
    private ExecutableServiceBean executableServiceBean;
    @Mock
    private SmoothingEventListener listener;
    @Mock
    private OperatingSystemService operatingSystemService;
    @Mock
    private Provider<Executor> executorProvider;
    @Mock
    private Executor executor;
    @Mock
    private SmoothingCoreParameters smoothingCoreParameters;
    @Captor
    private ArgumentCaptor<ExecuteStreamHandler> streamHandlerCaptor;
    @Captor
    private ArgumentCaptor<CommandLine> commandLineCaptor;

    @Before
    public void beforeTest() {
	executableServiceBean = new ExecutableServiceBean(operatingSystemService, executorProvider);
	when(operatingSystemService.currentOS()).thenReturn(OperatingSystem.WINDOWS);
	when(operatingSystemService.is64bit(any(OperatingSystem.class))).thenReturn(false);
	when(executorProvider.get()).thenReturn(executor);
    }

    private SmoothingCoreParametersBean parameters() {
	SmoothingCoreParametersBean parameters = new SmoothingCoreParametersBean();
	parameters.input(new File("input.txt"));
	parameters.output(new File("output.txt"));
	parameters.trackName("track_name");
	parameters.trackDatabase("track_database");
	parameters.standardDeviation(200);
	parameters.rounds(2);
	parameters.stepLength(10);
	parameters.includeSmoothedTrack(true);
	parameters.includeMaximumTrack(true);
	parameters.includeMinimumTrack(false);
	parameters.maximumThreshold(3.0);
	parameters.minimumThreshold(3.0);
	return parameters;
    }

    @Test
    public void smoothing() throws Throwable {
	SmoothingCoreParametersBean parameters = parameters();
	StringBuilder processOutput = new StringBuilder();
	processOutput.append("Start\n");
	processOutput.append("\t10%\n");
	processOutput.append("\t25%\n");
	processOutput.append("\t68%\n");
	processOutput.append("\t100%\n");
	processOutput.append("End\n");
	final ByteArrayInputStream input = new ByteArrayInputStream(processOutput.toString().getBytes(
		Charset.defaultCharset()));
	doAnswer(invocation -> {
	    ExecuteStreamHandler streamHandler = (ExecuteStreamHandler) invocation.getArguments()[0];
	    streamHandler.setProcessOutputStream(input);
	    streamHandler.start();
	    return null;
	}).when(executor).setStreamHandler(any(ExecuteStreamHandler.class));

	executableServiceBean.smoothing(parameters, listener);

	verify(executor).setStreamHandler(streamHandlerCaptor.capture());
	verify(executorProvider).get();
	verify(executor).setWorkingDirectory(new File(SystemUtils.getUserHome(), "smoothing"));
	verify(executor).execute(commandLineCaptor.capture());
	CommandLine commandLine = commandLineCaptor.getValue();
	File executable = new File(commandLine.getExecutable());
	assertEquals(true, executable.exists());
	assertEquals(true, executable.canExecute());
	assertEquals(parameters.getInput().getAbsolutePath(), commandLine.getArguments()[0]);
	assertEquals(parameters.getOutput().getAbsolutePath(), commandLine.getArguments()[1]);
	assertEquals(parameters.getTrackName(), commandLine.getArguments()[2]);
	assertEquals(parameters.getTrackDatabase(), commandLine.getArguments()[3]);
	assertEquals(String.valueOf(parameters.getStandardDeviation()), commandLine.getArguments()[4]);
	assertEquals(String.valueOf(parameters.getRounds()), commandLine.getArguments()[5]);
	assertEquals(String.valueOf(parameters.getStepLength()), commandLine.getArguments()[6]);
	assertEquals("1", commandLine.getArguments()[7]);
	assertEquals("1", commandLine.getArguments()[8]);
	assertEquals("0", commandLine.getArguments()[9]);
	assertEquals(String.valueOf(parameters.getMaximumThreshold()), commandLine.getArguments()[10]);
	assertEquals(String.valueOf(parameters.getMinimumThreshold()), commandLine.getArguments()[11]);
	// Validate events.
	Thread.sleep(200);
	streamHandlerCaptor.getValue().stop();
	verify(listener).processProgress(0.1);
	verify(listener).processProgress(0.25);
	verify(listener).processProgress(0.68);
	verify(listener).processProgress(1.0);
    }

    @Test
    public void smoothing_Windows32() throws Throwable {
	SmoothingCoreParametersBean parameters = parameters();

	executableServiceBean.smoothing(parameters, listener);

	verify(executor).execute(commandLineCaptor.capture());
	CommandLine commandLine = commandLineCaptor.getValue();
	File executable = new File(commandLine.getExecutable());
	assertEquals(true, executable.exists());
	assertEquals(true, executable.canExecute());
	String executableResource = "/executables/smoothing_32.exe";
	File expectedExecutable = new File(this.getClass().getResource(executableResource).toURI());
	assertEquals(FileUtils.checksumCRC32(expectedExecutable), FileUtils.checksumCRC32(executable));
    }

    @Test
    public void smoothing_Windows64() throws Throwable {
	when(operatingSystemService.is64bit(any(OperatingSystem.class))).thenReturn(true);
	SmoothingCoreParametersBean parameters = parameters();

	executableServiceBean.smoothing(parameters, listener);

	verify(executor).execute(commandLineCaptor.capture());
	CommandLine commandLine = commandLineCaptor.getValue();
	File executable = new File(commandLine.getExecutable());
	assertEquals(true, executable.exists());
	assertEquals(true, executable.canExecute());
	String executableResource = "/executables/smoothing_64.exe";
	File expectedExecutable = new File(this.getClass().getResource(executableResource).toURI());
	assertEquals(FileUtils.checksumCRC32(expectedExecutable), FileUtils.checksumCRC32(executable));
    }

    @Test
    public void smoothing_Mac64() throws Throwable {
	when(operatingSystemService.currentOS()).thenReturn(OperatingSystem.MAC);
	when(operatingSystemService.is64bit(any(OperatingSystem.class))).thenReturn(true);
	SmoothingCoreParametersBean parameters = parameters();

	executableServiceBean.smoothing(parameters, listener);

	verify(executor).execute(commandLineCaptor.capture());
	CommandLine commandLine = commandLineCaptor.getValue();
	File executable = new File(commandLine.getExecutable());
	assertEquals(true, executable.exists());
	assertEquals(true, executable.canExecute());
	String executableResource = "/executables/smoothing_mac_64";
	File expectedExecutable = new File(this.getClass().getResource(executableResource).toURI());
	assertEquals(FileUtils.checksumCRC32(expectedExecutable), FileUtils.checksumCRC32(executable));
    }
}
