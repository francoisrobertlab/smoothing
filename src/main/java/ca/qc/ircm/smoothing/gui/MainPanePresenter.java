package ca.qc.ircm.smoothing.gui;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;

import javax.inject.Inject;

import ca.qc.ircm.smoothing.BedWithColor;
import ca.qc.ircm.smoothing.ErrorHandler;
import ca.qc.ircm.smoothing.ErrorHandlerDefault;
import ca.qc.ircm.smoothing.service.BedParser;
import ca.qc.ircm.smoothing.service.BedParser.Warning;
import ca.qc.ircm.smoothing.service.SmoothingParameters;
import ca.qc.ircm.smoothing.service.SmoothingTask;
import ca.qc.ircm.smoothing.service.SmoothingTaskFactory;
import ca.qc.ircm.util.javafx.NullOnExceptionStringConverter;
import ca.qc.ircm.util.javafx.message.MessageDialog;
import ca.qc.ircm.util.javafx.message.MessageDialog.MessageDialogType;

/**
 * Main pane presenter.
 */
public class MainPanePresenter {
    private class SmoothingParametersBean implements SmoothingParameters {
	private List<File> files;
	private int standardDeviation = standardDeviationProperty.get();
	private int rounds = roundsProperty.get();
	private int stepLength = stepLengthProperty.get();
	private boolean includeSmoothedTrack = includeSmoothedTrackProperty.get();
	private boolean includeMinimumTrack = includeMinimumTrackProperty.get();
	private Double minimumThreshold = minimumThresholdProperty.get();
	private boolean includeMaximumTrack = includeMaximumTrackProperty.get();
	private Double maximumThreshold = maximumThresholdProperty.get();
	private Map<File, Color> colors;

	private SmoothingParametersBean() {
	    List<BedWithColor> beds = bedTable.getItems();
	    files = new ArrayList<File>(beds.size());
	    colors = new HashMap<File, Color>();
	    for (BedWithColor bed : beds) {
		files.add(bed.getFile());
		colors.put(bed.getFile(), bed.getColor());
	    }
	}

	@Override
	public List<File> getFiles() {
	    return files;
	}

	@Override
	public int getStandardDeviation() {
	    return standardDeviation;
	}

	@Override
	public int getRounds() {
	    return rounds;
	}

	@Override
	public int getStepLength() {
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
	    return colors.get(file);
	}
    }

    private class BedWarningHandler extends ErrorHandlerDefault implements BedParser.WarningHandler {
	@Override
	public void handleWarning(Warning warning) {
	    this.handleError(warning.getMessage(locale));
	}
    }

    private final IntegerProperty standardDeviationProperty = new SimpleIntegerProperty();
    private final IntegerProperty roundsProperty = new SimpleIntegerProperty();
    private final IntegerProperty stepLengthProperty = new SimpleIntegerProperty();
    private final BooleanProperty includeSmoothedTrackProperty = new SimpleBooleanProperty();
    private final BooleanProperty includeMaximumTrackProperty = new SimpleBooleanProperty();
    private final DoubleProperty maximumThresholdProperty = new SimpleDoubleProperty();
    private final BooleanProperty includeMinimumTrackProperty = new SimpleBooleanProperty();
    private final DoubleProperty minimumThresholdProperty = new SimpleDoubleProperty();
    @FXML
    private ResourceBundle resources;
    @FXML
    private Pane view;
    @FXML
    private Pane filePane;
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
    private Pane includeSmoothedTrackPane;
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
    private BedTablePresenter bedTable;
    private FileChooser fileChooser = new FileChooser();
    private Locale locale = Locale.getDefault();
    @Inject
    private BedParser bedParser;
    @Inject
    private SmoothingTaskFactory smoothingTaskFactory;

    @FXML
    private void initialize() {
	BedTableView bedTableView = new BedTableView();
	bedTable = (BedTablePresenter) bedTableView.getPresenter();
	filePane.getChildren().add(bedTableView.getView());
	fileChooser.setTitle(resources.getString("fileChooser.title"));
	fileChooser.getExtensionFilters().add(new ExtensionFilter(resources.getString("fileChooser.description"), "*"));
	bedTable.initialDirectoryProperty().bindBidirectional(fileChooser.initialDirectoryProperty());
	standardDeviation.textProperty().bindBidirectional(standardDeviationProperty,
		new NullOnExceptionStringConverter<Number>(new NumberStringConverter()));
	rounds.textProperty().bindBidirectional(roundsProperty,
		new NullOnExceptionStringConverter<Number>(new NumberStringConverter()));
	stepLength.textProperty().bindBidirectional(stepLengthProperty,
		new NullOnExceptionStringConverter<Number>(new NumberStringConverter()));
	includeSmoothedTrack.selectedProperty().bindBidirectional(includeSmoothedTrackProperty);
	includeMaximumTrack.selectedProperty().bindBidirectional(includeMaximumTrackProperty);
	maximumThreshold.textProperty().bindBidirectional(maximumThresholdProperty,
		new NullOnExceptionStringConverter<Number>(new NumberStringConverter()));
	includeMinimumTrack.selectedProperty().bindBidirectional(includeMinimumTrackProperty);
	minimumThreshold.textProperty().bindBidirectional(minimumThresholdProperty,
		new NullOnExceptionStringConverter<Number>(new NumberStringConverter()));

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
	    new MessageDialog(view.getScene().getWindow(), MessageDialogType.ERROR,
		    resources.getString("validationError.title"), errorHandler.messages());
	    return;
	} else {
	    List<BedWithColor> beds = bedTable.getItems();
	    BedWarningHandler bedWarningHandler = new BedWarningHandler();
	    for (BedWithColor bed : beds) {
		File file = bed.getFile();
		try {
		    bedParser.validateFirstTrack(file, bedWarningHandler);
		} catch (IOException e) {
		    bedWarningHandler.handleError(MessageFormat.format(resources.getString("error.files.IOException"),
			    file));
		}
	    }
	    if (bedWarningHandler.hasErrors()) {
		new MessageDialog(view.getScene().getWindow(), MessageDialogType.ERROR,
			resources.getString("validationError.title"), errorHandler.messages());
		return;
	    } else {
		final Window window = view.getScene().getWindow();
		SmoothingParameters parameters = new SmoothingParametersBean();
		final SmoothingTask task = smoothingTaskFactory.create(parameters);
		final ProgressDialog progressDialog = new ProgressDialog(view.getScene().getWindow(), task);
		task.stateProperty().addListener(new ChangeListener<State>() {
		    @Override
		    public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
			if (newValue == State.FAILED || newValue == State.SUCCEEDED || newValue == State.CANCELLED) {
			    progressDialog.hide();
			}
			if (newValue == State.FAILED) {
			    // Show error message.
			    Throwable error = task.getException();
			    new MessageDialog(window, MessageDialogType.ERROR, resources.getString("failed.title"),
				    resources.getString("failed.message"), error.getMessage());
			} else if (newValue == State.SUCCEEDED) {
			    // Show confirm message.
			    new MessageDialog(window, MessageDialogType.INFORMATION, resources
				    .getString("succeeded.title"), resources.getString("succeeded.message"));
			}
		    }
		});
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	    }
	}
    }

    public void validate(ErrorHandlerDefault errorHandler) {
	validateFiles(errorHandler);
	validateStandardDeviation(errorHandler);
	validateRounds(errorHandler);
	validateStepLength(errorHandler);
	validateTracks(errorHandler);
	validateMaximumThreshold(errorHandler);
	validateMinimumThreshold(errorHandler);
    }

    public void validateFiles(ErrorHandlerDefault errorHandler) {
	boolean oldHasError = errorHandler.hasErrors();
	errorHandler.setHasError(false);
	filePane.getStyleClass().remove("error");
	bedTable.validate(errorHandler);
	if (errorHandler.hasErrors()) {
	    filePane.getStyleClass().add("error");
	}
	errorHandler.setHasError(oldHasError || errorHandler.hasErrors());
    }

    public void validateStandardDeviation(ErrorHandler errorHandler) {
	standardDeviationPane.getStyleClass().remove("error");
	try {
	    NumberStringConverter converter = new NumberStringConverter();
	    long value = converter.fromString(standardDeviation.getText()).longValue();
	    if (value < 1) {
		errorHandler.handleError(resources.getString("error.standardDeviation.underMinimum"));
		standardDeviationPane.getStyleClass().add("error");
	    }
	    if (value > 1000000) {
		errorHandler.handleError(resources.getString("error.standardDeviation.overMaximum"));
		standardDeviationPane.getStyleClass().add("error");
	    }
	} catch (Exception e) {
	    errorHandler.handleError(resources.getString("error.standardDeviation.invalidNumber"));
	    standardDeviationPane.getStyleClass().add("error");
	}
    }

    public void validateRounds(ErrorHandler errorHandler) {
	roundsPane.getStyleClass().remove("error");
	try {
	    NumberStringConverter converter = new NumberStringConverter();
	    long value = converter.fromString(rounds.getText()).longValue();
	    if (value < 1) {
		errorHandler.handleError(resources.getString("error.rounds.underMinimum"));
		roundsPane.getStyleClass().add("error");
	    }
	    if (value > 10) {
		errorHandler.handleError(resources.getString("error.rounds.overMaximum"));
		roundsPane.getStyleClass().add("error");
	    }
	} catch (Exception e) {
	    errorHandler.handleError(resources.getString("error.rounds.invalidNumber"));
	    roundsPane.getStyleClass().add("error");
	}
    }

    public void validateStepLength(ErrorHandler errorHandler) {
	stepLengthPane.getStyleClass().remove("error");
	try {
	    NumberStringConverter converter = new NumberStringConverter();
	    long value = converter.fromString(stepLength.getText()).longValue();
	    if (value < 1) {
		errorHandler.handleError(resources.getString("error.stepLength.underMinimum"));
		stepLengthPane.getStyleClass().add("error");
	    }
	    if (value > 10000) {
		errorHandler.handleError(resources.getString("error.stepLength.overMaximum"));
		stepLengthPane.getStyleClass().add("error");
	    }
	} catch (Exception e) {
	    errorHandler.handleError(resources.getString("error.stepLength.invalidNumber"));
	    stepLengthPane.getStyleClass().add("error");
	}
    }

    public void validateTracks(ErrorHandler errorHandler) {
	if (!includeSmoothedTrackProperty.get() && !includeMaximumTrackProperty.get()
		&& !includeMinimumTrackProperty.get()) {
	    errorHandler.handleError(resources.getString("error.track.noTrack"));
	}
    }

    public void validateMaximumThreshold(ErrorHandler errorHandler) {
	// Nothing to validate.
    }

    public void validateMinimumThreshold(ErrorHandler errorHandler) {
	// Nothing to validate.
    }
}
