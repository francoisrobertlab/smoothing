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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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

	@Test
	public void smoothing() throws Throwable {
		File parameters = new File("parameters.txt");
		StringBuilder processOutput = new StringBuilder();
		processOutput.append("Start\n");
		processOutput.append("\t10%\n");
		processOutput.append("\t25%\n");
		processOutput.append("\t68%\n");
		processOutput.append("\t100%\n");
		processOutput.append("End\n");
		final ByteArrayInputStream input = new ByteArrayInputStream(processOutput.toString().getBytes(
				Charset.defaultCharset()));
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				ExecuteStreamHandler streamHandler = (ExecuteStreamHandler) invocation.getArguments()[0];
				streamHandler.setProcessOutputStream(input);
				streamHandler.start();
				return null;
			}
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
		File parameters = new File("parameters.txt");

		executableServiceBean.smoothing(parameters, listener);

		verify(executor).execute(commandLineCaptor.capture());
		CommandLine commandLine = commandLineCaptor.getValue();
		File executable = new File(commandLine.getExecutable());
		assertEquals(true, executable.exists());
		assertEquals(true, executable.canExecute());
		String executableResource = "/resources/executables/smoothing_32.exe";
		File expectedExecutable = new File(this.getClass().getResource(executableResource).toURI());
		assertEquals(FileUtils.checksumCRC32(expectedExecutable), FileUtils.checksumCRC32(executable));
	}

	@Test
	public void smoothing_Windows64() throws Throwable {
		when(operatingSystemService.is64bit(any(OperatingSystem.class))).thenReturn(true);
		File parameters = new File("parameters.txt");

		executableServiceBean.smoothing(parameters, listener);

		verify(executor).execute(commandLineCaptor.capture());
		CommandLine commandLine = commandLineCaptor.getValue();
		File executable = new File(commandLine.getExecutable());
		assertEquals(true, executable.exists());
		assertEquals(true, executable.canExecute());
		String executableResource = "/resources/executables/smoothing_64.exe";
		File expectedExecutable = new File(this.getClass().getResource(executableResource).toURI());
		assertEquals(FileUtils.checksumCRC32(expectedExecutable), FileUtils.checksumCRC32(executable));
	}
}
