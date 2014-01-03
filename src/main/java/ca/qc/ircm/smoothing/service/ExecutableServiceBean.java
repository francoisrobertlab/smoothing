package ca.qc.ircm.smoothing.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.qc.ircm.smoothing.OperatingSystem;
import ca.qc.ircm.smoothing.OperatingSystemService;

/**
 * Default implementation of {@link ExecutableService}.
 */
public class ExecutableServiceBean implements ExecutableService {
	private final static Pattern PROGRESS_PATTERN = Pattern.compile("\\s+(\\d+)%");

	private static class SmoothingEventGenerator extends LogOutputStream {
		private final SmoothingEventListener listener;

		public SmoothingEventGenerator(SmoothingEventListener listener) {
			this.listener = listener;
		}

		@Override
		protected void processLine(String line, int level) {
			Matcher matcher = PROGRESS_PATTERN.matcher(line);
			if (matcher.matches()) {
				listener.processProgress(Double.valueOf(matcher.group(1)) / 100);
			}
		}
	}

	private final Logger logger = LoggerFactory.getLogger(ExecutableServiceBean.class);
	private final ResourceBundle bundle = ResourceBundle.getBundle(ExecutableService.class.getName());
	private final OperatingSystemService operatingSystemService;
	private Provider<Executor> executorProvider;

	@Inject
	protected ExecutableServiceBean(OperatingSystemService operatingSystemService, Provider<Executor> executorProvider) {
		this.operatingSystemService = operatingSystemService;
		this.executorProvider = executorProvider;
	}

	@Override
	public void smoothing(File parameters, SmoothingEventListener listener) throws IOException {
		File directory = new File(SystemUtils.getUserHome(), "smoothing");
		if (!directory.exists() && !directory.mkdir()) {
			throw new IOException("Could not create directory " + directory);
		}
		File executable = extractExecutable(directory);
		CommandLine commandLine = new CommandLine(executable);
		commandLine.addArgument(parameters.getAbsolutePath());
		Executor executor = executorProvider.get();
		if (listener != null) {
			OutputStream output = new TeeOutputStream(System.out, new SmoothingEventGenerator(listener));
			executor.setStreamHandler(new PumpStreamHandler(output, System.err));
		}
		executor.setWorkingDirectory(directory);
		logger.info("Executing: {}", commandLine);
		int exitValue = executor.execute(commandLine);
		if (executor.isFailure(exitValue)) {
			throw new IOException("Incorrect return value " + exitValue);
		}
	}

	private File extractExecutable(File directory) throws IOException {
		File executable = new File(directory, "smoothing.exe");
		OperatingSystem operatingSystem = operatingSystemService.currentOS();
		boolean is64bits = operatingSystemService.is64bit(operatingSystem);
		String resource = "/executables/"
				+ bundle.getString("smoothing." + operatingSystem.name() + "." + (is64bits ? "64" : "32"));
		logger.trace("Extracting {} to {}", resource, executable);
		try (InputStream input = new BufferedInputStream(getClass().getResourceAsStream(resource));
				OutputStream output = new BufferedOutputStream(new FileOutputStream(executable))) {
			IOUtils.copy(input, output);
		}
		return executable;
	}
}
