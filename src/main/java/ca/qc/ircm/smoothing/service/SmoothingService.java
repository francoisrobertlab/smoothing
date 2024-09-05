package ca.qc.ircm.smoothing.service;

import ca.qc.ircm.progressbar.ProgressBar;
import ca.qc.ircm.smoothing.bed.BedService;
import ca.qc.ircm.smoothing.bed.BedTrack;
import ca.qc.ircm.smoothing.service.ExecutableService.SmoothingEventListener;
import ca.qc.ircm.smoothing.validation.WarningHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.paint.Color;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Services for smoothing BED files.
 */
@Service
public class SmoothingService {
  private static class SmoothingCoreParametersDelegate implements SmoothingCoreParameters {
    private final SmoothingParameters parameters;
    private File input;
    private File output;
    private String trackName;
    private String trackDatabase;

    private SmoothingCoreParametersDelegate(SmoothingParameters parameters, File input, File output,
        String trackName, String trackDatabase) {
      this.parameters = parameters;
      this.input = input;
      this.output = output;
      this.trackName = trackName;
      this.trackDatabase = trackDatabase;
    }

    @Override
    public File getInput() {
      return input;
    }

    @Override
    public File getOutput() {
      return output;
    }

    @Override
    public String getTrackName() {
      return trackName;
    }

    @Override
    public String getTrackDatabase() {
      return trackDatabase;
    }

    @Override
    public int getStandardDeviation() {
      return parameters.getStandardDeviation();
    }

    @Override
    public int getRounds() {
      return parameters.getRounds();
    }

    @Override
    public int getStepLength() {
      return parameters.getStepLength();
    }

    @Override
    public boolean isIncludeSmoothedTrack() {
      return parameters.isIncludeSmoothedTrack();
    }

    @Override
    public boolean isIncludeMinimumTrack() {
      return parameters.isIncludeMinimumTrack();
    }

    @Override
    public Double getMinimumThreshold() {
      return parameters.getMinimumThreshold();
    }

    @Override
    public boolean isIncludeMaximumTrack() {
      return parameters.isIncludeMaximumTrack();
    }

    @Override
    public Double getMaximumThreshold() {
      return parameters.getMaximumThreshold();
    }
  }

  private static class SmoothingEventListenerBean implements SmoothingEventListener {
    private final ProgressBar progressBar;
    private Integer rawDataCount;
    private Integer chromosomeCount;

    private SmoothingEventListenerBean(ProgressBar progressBar) {
      this.progressBar = progressBar;
    }

    @Override
    public void processProgress(double progress) {
      progressBar.setProgress(progress);
    }

    @Override
    public void setRawDataCount(int count) {
      rawDataCount = count;
    }

    @Override
    public void setChromosomeCount(int count) {
      chromosomeCount = count;
    }
  }

  private final Logger logger = LoggerFactory.getLogger(SmoothingService.class);
  @Inject
  private ExecutableService executableService;
  @Inject
  private BedService bedService;

  protected SmoothingService() {
  }

  protected SmoothingService(ExecutableService executableService, BedService bedService) {
    this.executableService = executableService;
    this.bedService = bedService;
  }

  /**
   * Smooth BED files.
   *
   * @param parameters
   *          smoothing parameters
   * @param progressBar
   *          records progression
   * @param warningHandler
   *          handles warnings
   * @throws IOException
   *           could not parse BED file or could not execute smoothing
   */
  public void smoothing(SmoothingParameters parameters, ProgressBar progressBar,
      WarningHandler warningHandler) throws IOException {
    List<File> files = parameters.getFiles();
    double step = 1.0 / Math.max(files.size(), 1);
    ResourceBundle resources =
        ResourceBundle.getBundle(SmoothingService.class.getName(), Locale.getDefault());
    for (File file : files) {
      progressBar.setMessage(message(resources, "file", file.getName()));
      smoothing(file, parameters, progressBar.step(step), warningHandler, resources);
    }
  }

  private void smoothing(File file, SmoothingParameters parameters, ProgressBar progressBar,
      WarningHandler warningHandler, ResourceBundle resources) throws IOException {
    BedTrack track = bedService.parseFirstTrack(file);
    int expectedChromosomeCount = bedService.countFirstTrackChromosomes(file);
    int expectedDataCount = bedService.countFirstTrackData(file);

    File smoothingOutput = smoothingOutput(file);
    File executableParameters = File.createTempFile("smoothing_parameters", ".txt");
    try {
      String trackName = track != null && track.getName() != null ? track.getName() : "";
      String trackDatabase =
          track != null && track.getDatabase() != null ? track.getDatabase() : "";
      SmoothingCoreParameters coreParameters = new SmoothingCoreParametersDelegate(parameters, file,
          smoothingOutput, trackName, trackDatabase);
      SmoothingEventListenerBean smoothingListener = new SmoothingEventListenerBean(progressBar);
      executableService.smoothing(coreParameters, smoothingListener);
      if (smoothingListener.chromosomeCount != null
          && smoothingListener.chromosomeCount != expectedChromosomeCount) {
        String message = message(resources, "chromosomeCount.mismatch", file.getName(),
            smoothingListener.chromosomeCount, expectedChromosomeCount);
        logger.debug("{}", message);
        warningHandler.handle(message);
      } else if (smoothingListener.rawDataCount != null
          && smoothingListener.rawDataCount != expectedDataCount) {
        String message = message(resources, "rawDataCount.mismatch", file.getName(),
            smoothingListener.rawDataCount, expectedDataCount);
        logger.debug("{}", message);
        warningHandler.handle(message);
      }
    } finally {
      if (!executableParameters.delete()) {
        logger.warn("Could not delete file {}", executableParameters);
      }
    }

    // Alter color.
    Color color = parameters.getColor(file);
    if (color != null) {
      File temporaryBed = File.createTempFile("alter_color", ".bed");
      try (
          BufferedReader reader = new BufferedReader(
              new InputStreamReader(new FileInputStream(smoothingOutput), "UTF-8"));
          BufferedWriter writer = new BufferedWriter(
              new OutputStreamWriter(new FileOutputStream(temporaryBed), "UTF-8"))) {
        Pattern pattern = Pattern.compile("color\\=(\\d+)\\,(\\d+)\\,(\\d+)");
        String line;
        while ((line = reader.readLine()) != null) {
          Matcher matcher = pattern.matcher(line);
          if (matcher.find()) {
            line = matcher.replaceAll("color=" + (int) (color.getRed() * 255) + ","
                + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255));
          }
          writer.write(line);
          writer.write("\n");
        }
      }
      if (smoothingOutput.delete()) {
        FileUtils.moveFile(temporaryBed, smoothingOutput);
      } else {
        logger.warn("Could not delete smoothing output {} to alter color", smoothingOutput);
      }
    }

    progressBar.setProgress(1.0);
  }

  private File smoothingOutput(File file) {
    String name = FilenameUtils.getBaseName(file.getName());
    String extension = FilenameUtils.getExtension(file.getName());
    StringBuilder smoothingOutputName = new StringBuilder(name);
    smoothingOutputName.append("_smoothed");
    if (!extension.equals("")) {
      smoothingOutputName.append(".");
      smoothingOutputName.append(extension);
    }
    if (file.getParentFile() != null) {
      return new File(file.getParentFile(), smoothingOutputName.toString());
    } else {
      return new File(smoothingOutputName.toString());
    }
  }

  private String message(ResourceBundle resources, String key, Object... replacements) {
    return MessageFormat.format(resources.getString(key), replacements);
  }
}
