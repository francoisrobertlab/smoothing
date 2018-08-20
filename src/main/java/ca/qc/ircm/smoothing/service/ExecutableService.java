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

package ca.qc.ircm.smoothing.service;

import ca.qc.ircm.smoothing.OperatingSystem;
import ca.qc.ircm.smoothing.OperatingSystemService;
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
import org.springframework.stereotype.Service;

/**
 * Launch external programs.
 */
@Service
public class ExecutableService {
  /**
   * Listens for events coming from vap program.
   */
  public interface SmoothingEventListener {
    /**
     * Program's progression changed.
     *
     * @param progress
     *          new progression
     */
    public void processProgress(double progress);

    public void setRawDataCount(int count);

    public void setChromosomeCount(int count);
  }

  private static final Pattern PROGRESS_PATTERN = Pattern.compile("\\s+(\\d+)%");
  private static final Pattern RAW_DATA_COUNT_PATTERN = Pattern.compile("nbRawData=(\\d+)");
  private static final Pattern CHROMOSOME_COUNT_PATTERN = Pattern.compile("nbrChrom=(\\d+)");

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
      matcher = RAW_DATA_COUNT_PATTERN.matcher(line);
      if (matcher.find()) {
        listener.setRawDataCount(Integer.parseInt(matcher.group(1)));
      }
      matcher = CHROMOSOME_COUNT_PATTERN.matcher(line);
      if (matcher.find()) {
        listener.setChromosomeCount(Integer.parseInt(matcher.group(1)));
      }
    }
  }

  private final Logger logger = LoggerFactory.getLogger(ExecutableService.class);
  private final ResourceBundle bundle = ResourceBundle.getBundle(ExecutableService.class.getName());
  private final OperatingSystemService operatingSystemService;
  private Provider<Executor> executorProvider;

  @Inject
  protected ExecutableService(OperatingSystemService operatingSystemService,
      Provider<Executor> executorProvider) {
    this.operatingSystemService = operatingSystemService;
    this.executorProvider = executorProvider;
  }

  /**
   * Executes native smoothing program.
   * 
   * @param parameters
   *          parameters for smoothing
   * @param listener
   *          listener for events
   * @throws IOException
   *           could not execute smoothing program
   */
  public void smoothing(SmoothingCoreParameters parameters, SmoothingEventListener listener)
      throws IOException {
    File directory = new File(SystemUtils.getUserHome(), "smoothing");
    if (!directory.exists() && !directory.mkdir()) {
      throw new IOException("Could not create directory " + directory);
    }
    File executable = extractExecutable(directory);
    CommandLine commandLine = new CommandLine(executable);
    commandLine.addArgument(parameters.getInput().getAbsolutePath());
    commandLine.addArgument(parameters.getOutput().getAbsolutePath());
    commandLine.addArgument(parameters.getTrackName());
    commandLine.addArgument(parameters.getTrackDatabase());
    commandLine.addArgument(String.valueOf(parameters.getStandardDeviation()));
    commandLine.addArgument(String.valueOf(parameters.getRounds()));
    commandLine.addArgument(String.valueOf(parameters.getStepLength()));
    commandLine.addArgument(parameters.isIncludeSmoothedTrack() ? "1" : "0");
    commandLine.addArgument(parameters.isIncludeMaximumTrack() ? "1" : "0");
    commandLine.addArgument(parameters.isIncludeMinimumTrack() ? "1" : "0");
    commandLine.addArgument(
        parameters.getMaximumThreshold() != null ? String.valueOf(parameters.getMaximumThreshold())
            : "");
    commandLine.addArgument(
        parameters.getMinimumThreshold() != null ? String.valueOf(parameters.getMinimumThreshold())
            : "");
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
    OperatingSystem operatingSystem = operatingSystemService.currentOs();
    boolean is64bits = operatingSystemService.is64bit(operatingSystem);
    String resource = "/executables/"
        + bundle.getString("smoothing." + operatingSystem.name() + "." + (is64bits ? "64" : "32"));
    logger.trace("Extracting {} to {}", resource, executable);
    try (InputStream input = new BufferedInputStream(getClass().getResourceAsStream(resource));
        OutputStream output = new BufferedOutputStream(new FileOutputStream(executable))) {
      IOUtils.copy(input, output);
    }
    executable.setExecutable(true);
    return executable;
  }
}
