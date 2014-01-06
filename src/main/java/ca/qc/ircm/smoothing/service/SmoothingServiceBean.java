package ca.qc.ircm.smoothing.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.paint.Color;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.qc.ircm.progress_bar.ProgressBar;
import ca.qc.ircm.progress_bar.StepProgressBar;
import ca.qc.ircm.smoothing.service.ExecutableService.SmoothingEventListener;

/**
 * Default implementation of {@link SmoothingService}.
 */
public class SmoothingServiceBean implements SmoothingService {
	private static class SmoothingEventListenerBean implements SmoothingEventListener {
		private final ProgressBar progressBar;

		private SmoothingEventListenerBean(ProgressBar progressBar) {
			this.progressBar = progressBar;
		}

		@Override
		public void processProgress(double progress) {
			progressBar.setProgress(progress);
		}
	}

	private final Logger logger = LoggerFactory.getLogger(SmoothingServiceBean.class);
	private final ExecutableService executableService;
	private final BedParser bedParser;

	@Inject
	protected SmoothingServiceBean(ExecutableService executableService, BedParser bedParser) {
		this.executableService = executableService;
		this.bedParser = bedParser;
	}

	@Override
	public void smoothing(SmoothingParameters parameters, SmoothingProgressBar progressBar) throws IOException {
		List<File> files = parameters.getFiles();
		double step = 1.0 / Math.max(files.size(), 1);
		for (File file : files) {
			progressBar.setFile(file);
			smoothing(file, parameters, new StepProgressBar(progressBar, step));
		}
	}

	private void smoothing(File file, SmoothingParameters parameters, ProgressBar progressBar) throws IOException {
		BedTrack track = bedParser.parseFirstTrack(file);

		File smoothingOutput = smoothingOutput(file);
		File executableParameters = File.createTempFile("smoothing_parameters", ".txt");
		try {
			writeParameters(executableParameters, file, smoothingOutput, parameters, track);
			executableService.smoothing(executableParameters, new SmoothingEventListenerBean(progressBar));
		} finally {
			if (!executableParameters.delete()) {
				logger.warn("Could not delete file {}", executableParameters);
			}
		}

		// Alter color.
		Color color = parameters.getColor(file);
		if (color != null) {
			File temporaryBed = File.createTempFile("alter_color", ".bed");
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(smoothingOutput),
					"UTF-8"));
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(temporaryBed), "UTF-8"))) {
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

	private void writeParameters(File output, File file, File smoothingOutput, SmoothingParameters parameters,
			BedTrack track) throws IOException {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"))) {
			writer.write(file.getAbsolutePath());
			writer.write("\n");
			writer.write(smoothingOutput.getAbsolutePath());
			writer.write("\n");
			writer.write(track != null && track.getName() != null ? track.getName() : "");
			writer.write("\n");
			writer.write(track != null && track.getDatabase() != null ? track.getDatabase() : "");
			writer.write("\n");
			writer.write(String.valueOf(parameters.getStandardDeviation()));
			writer.write("\n");
			writer.write(String.valueOf(parameters.getRounds()));
			writer.write("\n");
			writer.write(String.valueOf(parameters.getStepLength()));
			writer.write("\n");
			writer.write(parameters.isIncludeSmoothedTrack() ? "1" : "0");
			writer.write("\n");
			writer.write(parameters.isIncludeMaximumTrack() ? "1" : "0");
			writer.write("\n");
			writer.write(parameters.isIncludeMinimumTrack() ? "1" : "0");
			writer.write("\n");
			writer.write(parameters.getMaximumThreshold() != null ? String.valueOf(parameters.getMaximumThreshold())
					: "");
			writer.write("\n");
			writer.write(parameters.getMinimumThreshold() != null ? String.valueOf(parameters.getMinimumThreshold())
					: "");
			writer.write("\n");
		}
	}
}
