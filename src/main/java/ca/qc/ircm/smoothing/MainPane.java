package ca.qc.ircm.smoothing;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;

import javax.inject.Inject;

import ca.qc.ircm.smoothing.service.BedParser;
import ca.qc.ircm.smoothing.service.BedParser.Warning;
import ca.qc.ircm.smoothing.service.SmoothingParameters;
import ca.qc.ircm.smoothing.service.SmoothingTask;
import ca.qc.ircm.smoothing.service.SmoothingTaskFactory;
import ca.qc.ircm.smoothing.util.javafx.FxmlResources;
import ca.qc.ircm.smoothing.util.javafx.MessageDialog;
import ca.qc.ircm.smoothing.util.javafx.MultipleFilesPane;
import ca.qc.ircm.smoothing.util.javafx.NullOnExceptionConverter;

/**
 * Main pane.
 */
public class MainPane extends BorderPane {
	private class SmoothingParametersBean implements SmoothingParameters {
		private List<File> files = multipleFiles.getFiles();
		private Integer standardDeviation = standardDeviationProperty.get();
		private Integer rounds = roundsProperty.get();
		private Integer stepLength = stepLengthProperty.get();
		private boolean includeSmoothedTrack = includeSmoothedTrackProperty.get();
		private boolean includeMinimumTrack = includeMinimumTrackProperty.get();
		private Double minimumThreshold = minimumThresholdProperty.get();
		private boolean includeMaximumTrack = includeMaximumTrackProperty.get();
		private Double maximumThreshold = maximumThresholdProperty.get();

		private SmoothingParametersBean() {
		}

		@Override
		public List<File> getFiles() {
			return files;
		}

		@Override
		public Integer getStandardDeviation() {
			return standardDeviation;
		}

		@Override
		public Integer getRounds() {
			return rounds;
		}

		@Override
		public Integer getStepLength() {
			return stepLength;
		}

		@Override
		public boolean isIncludeSmoothedTrack() {
			return includeSmoothedTrack;
		}

		@Override
		public boolean isIncludeMinimumTrack() {
			return includeMinimumTrack;
		}

		@Override
		public Double getMinimumThreshold() {
			return minimumThreshold;
		}

		@Override
		public boolean isIncludeMaximumTrack() {
			return includeMaximumTrack;
		}

		@Override
		public Double getMaximumThreshold() {
			return maximumThreshold;
		}

		@Override
		public Color getColor(File file) {
			return null;
		}
	}

	private class BedWarningHandler extends ErrorHandlerDefault implements BedParser.WarningHandler {
		@Override
		public void handleWarning(Warning warning) {
			this.handleError(warning.getMessage(locale));
		}
	}

	private final ResourceBundle bundle;
	private final IntegerProperty standardDeviationProperty = new SimpleIntegerProperty();
	private final IntegerProperty roundsProperty = new SimpleIntegerProperty();
	private final IntegerProperty stepLengthProperty = new SimpleIntegerProperty();
	private final BooleanProperty includeSmoothedTrackProperty = new SimpleBooleanProperty();
	private final BooleanProperty includeMaximumTrackProperty = new SimpleBooleanProperty();
	private final DoubleProperty maximumThresholdProperty = new SimpleDoubleProperty();
	private final BooleanProperty includeMinimumTrackProperty = new SimpleBooleanProperty();
	private final DoubleProperty minimumThresholdProperty = new SimpleDoubleProperty();
	@FXML
	private MultipleFilesPane multipleFiles;
	@FXML
	private Pane standardDeviationPane;
	@FXML
	private TextField standardDeviation;
	@FXML
	private Pane roundsPane;
	@FXML
	private TextField rounds;
	@FXML
	private Pane stepLengthPane;
	@FXML
	private TextField stepLength;
	@FXML
	private CheckBox includeSmoothedTrack;
	@FXML
	private Pane includeMaximumTrackPane;
	@FXML
	private CheckBox includeMaximumTrack;
	@FXML
	private TextField maximumThreshold;
	@FXML
	private Pane includeMinimumTrackPane;
	@FXML
	private CheckBox includeMinimumTrack;
	@FXML
	private TextField minimumThreshold;
	private FileChooser fileChooser = new FileChooser();
	private BedParser bedParser;
	private SmoothingTaskFactory smoothingTaskFactory;
	private Locale locale = Locale.getDefault();

	public MainPane() {
		bundle = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());

		FxmlResources.loadFxml(this, bundle);

		fileChooser.setTitle(bundle.getString("fileChooser.title"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter(bundle.getString("fileChooser.description"), "*"));
		multipleFiles.setFileChooser(fileChooser);
		standardDeviation.textProperty().bindBidirectional(standardDeviationProperty,
				new NullOnExceptionConverter<Number>(new NumberStringConverter()));
		rounds.textProperty().bindBidirectional(roundsProperty,
				new NullOnExceptionConverter<Number>(new NumberStringConverter()));
		stepLength.textProperty().bindBidirectional(stepLengthProperty,
				new NullOnExceptionConverter<Number>(new NumberStringConverter()));
		includeSmoothedTrack.selectedProperty().bindBidirectional(includeSmoothedTrackProperty);
		includeMaximumTrack.selectedProperty().bindBidirectional(includeMaximumTrackProperty);
		maximumThreshold.textProperty().bindBidirectional(maximumThresholdProperty,
				new NullOnExceptionConverter<Number>(new NumberStringConverter()));
		includeMinimumTrack.selectedProperty().bindBidirectional(includeMinimumTrackProperty);
		minimumThreshold.textProperty().bindBidirectional(minimumThresholdProperty,
				new NullOnExceptionConverter<Number>(new NumberStringConverter()));

		// Default values.
		standardDeviationProperty.set(200);
		roundsProperty.set(2);
		stepLengthProperty.set(10);
		includeSmoothedTrackProperty.set(true);
		maximumThresholdProperty.set(3);
		minimumThresholdProperty.set(-3);
	}

	@FXML
	private void run(ActionEvent event) {
		ErrorHandlerDefault errorHandler = new ErrorHandlerDefault();
		validate(errorHandler);
		if (errorHandler.hasErrors()) {
			new MessageDialog(getScene().getWindow(), MessageDialog.Type.ERROR,
					bundle.getString("validationError.title"), errorHandler.messages());
			return;
		} else {
			List<File> files = multipleFiles.getFiles();
			BedWarningHandler bedWarningHandler = new BedWarningHandler();
			for (File file : files) {
				try {
					bedParser.validateFirstTrack(file, bedWarningHandler);
				} catch (IOException e) {
					bedWarningHandler.handleError(MessageFormat.format(bundle.getString("error.files.IOException"),
							file));
				}
			}
			if (bedWarningHandler.hasErrors()) {
				new MessageDialog(getScene().getWindow(), MessageDialog.Type.ERROR,
						bundle.getString("validationError.title"), errorHandler.messages());
				return;
			} else {
				final Window window = this.getScene().getWindow();
				SmoothingParameters parameters = new SmoothingParametersBean();
				final SmoothingTask task = smoothingTaskFactory.create(parameters);
				final ProgressDialog progressDialog = new ProgressDialog(this.getScene().getWindow(), task);
				task.stateProperty().addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
						if (newValue == State.FAILED || newValue == State.SUCCEEDED || newValue == State.CANCELLED) {
							progressDialog.close();
						}
						if (newValue == State.FAILED) {
							// Show error message.
							Throwable error = task.getException();
							new MessageDialog(window, MessageDialog.Type.ERROR, bundle.getString("failed.title"),
									bundle.getString("failed.message"), error.getMessage());
						} else if (newValue == State.SUCCEEDED) {
							// Show confirm message.
							new MessageDialog(window, MessageDialog.Type.INFORMATION, bundle
									.getString("succeeded.title"), bundle.getString("succeeded.message"));
						}
					}
				});
				Thread thread = new Thread(task);
				thread.setDaemon(true);
				thread.start();
			}
		}
	}

	public void validate(ErrorHandler errorHandler) {
		validateFiles(errorHandler);
		validateStandardDeviation(errorHandler);
		validateRounds(errorHandler);
		validateStepLength(errorHandler);
		validateTracks(errorHandler);
		validateMaximumThreshold(errorHandler);
		validateMinimumThreshold(errorHandler);
	}

	public void validateFiles(ErrorHandler errorHandler) {
		multipleFiles.getStyleClass().removeAll("error");
		boolean error = false;
		multipleFiles.remvoveStyleClassFromCells("error");
		Collection<File> files = multipleFiles.getFiles();
		if (files == null || files.isEmpty()) {
			errorHandler.handleError(bundle.getString("error.files.required"));
			error = true;
		} else {
			for (File file : files) {
				if (!file.isFile()) {
					errorHandler.handleError(
							MessageFormat.format(bundle.getString("error.files.notExists"), file.getName()),
							file.getPath());
					error = true;
					multipleFiles.addStyleClass(file, "error");
				}
			}
		}
		if (error) {
			multipleFiles.getStyleClass().add("error");
		}
	}

	public void validateStandardDeviation(ErrorHandler errorHandler) {
		standardDeviationPane.getStyleClass().remove("error");
		try {
			NumberStringConverter converter = new NumberStringConverter();
			long value = converter.fromString(standardDeviation.getText()).longValue();
			if (value < 1) {
				errorHandler.handleError(bundle.getString("error.standardDeviation.underMinimum"));
				standardDeviationPane.getStyleClass().add("error");
			}
			if (value > 1000000) {
				errorHandler.handleError(bundle.getString("error.standardDeviation.overMaximum"));
				standardDeviationPane.getStyleClass().add("error");
			}
		} catch (Exception e) {
			errorHandler.handleError(bundle.getString("error.standardDeviation.invalidNumber"));
			standardDeviationPane.getStyleClass().add("error");
		}
	}

	public void validateRounds(ErrorHandler errorHandler) {
		roundsPane.getStyleClass().remove("error");
		try {
			NumberStringConverter converter = new NumberStringConverter();
			long value = converter.fromString(rounds.getText()).longValue();
			if (value < 1) {
				errorHandler.handleError(bundle.getString("error.rounds.underMinimum"));
				roundsPane.getStyleClass().add("error");
			}
			if (value > 10) {
				errorHandler.handleError(bundle.getString("error.rounds.overMaximum"));
				roundsPane.getStyleClass().add("error");
			}
		} catch (Exception e) {
			errorHandler.handleError(bundle.getString("error.rounds.invalidNumber"));
			roundsPane.getStyleClass().add("error");
		}
	}

	public void validateStepLength(ErrorHandler errorHandler) {
		stepLengthPane.getStyleClass().remove("error");
		try {
			NumberStringConverter converter = new NumberStringConverter();
			long value = converter.fromString(stepLength.getText()).longValue();
			if (value < 1) {
				errorHandler.handleError(bundle.getString("error.stepLength.underMinimum"));
				stepLengthPane.getStyleClass().add("error");
			}
			if (value > 10000) {
				errorHandler.handleError(bundle.getString("error.stepLength.overMaximum"));
				stepLengthPane.getStyleClass().add("error");
			}
		} catch (Exception e) {
			errorHandler.handleError(bundle.getString("error.stepLength.invalidNumber"));
			stepLengthPane.getStyleClass().add("error");
		}
	}

	public void validateTracks(ErrorHandler errorHandler) {
		if (!includeSmoothedTrackProperty.get() && !includeMaximumTrackProperty.get()
				&& !includeMinimumTrackProperty.get()) {
			errorHandler.handleError(bundle.getString("error.track.noTrack"));
		}
	}

	public void validateMaximumThreshold(ErrorHandler errorHandler) {
		// Nothing to validate.
	}

	public void validateMinimumThreshold(ErrorHandler errorHandler) {
		// Nothing to validate.
	}

	@Inject
	public void setBedParser(BedParser bedParser) {
		this.bedParser = bedParser;
	}

	@Inject
	public void setSmoothingTaskFactory(SmoothingTaskFactory smoothingTaskFactory) {
		this.smoothingTaskFactory = smoothingTaskFactory;
	}
}
