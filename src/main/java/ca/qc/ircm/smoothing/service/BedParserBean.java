package ca.qc.ircm.smoothing.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.paint.Color;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.qc.ircm.smoothing.validation.WarningHandler;

/**
 * Parses annotations present in a file.
 */
public class BedParserBean implements BedParser {
    private static interface Line {
	public String getContent();

	public int getLineNumber();
    }

    private static class LineDefault implements Line {
	private final String rawLine;
	private final int lineNumber;

	private LineDefault(String rawLine, int lineNumber) {
	    this.rawLine = rawLine;
	    this.lineNumber = lineNumber;
	}

	@Override
	public String getContent() {
	    return rawLine;
	}

	@Override
	public int getLineNumber() {
	    return lineNumber;
	}
    }

    private final Logger logger = LoggerFactory.getLogger(BedParserBean.class);
    /**
     * BED properties.
     */
    private final ResourceBundle bundle;

    public BedParserBean() {
	this.bundle = ResourceBundle.getBundle(BedParser.class.getName(), Locale.getDefault());
    }

    @Override
    public void validateFirstTrack(File file, WarningHandler warningHandler, Locale locale) throws IOException {
	Validate.notNull(file, "file parameter cannot be null.");
	Validate.isTrue(file.exists(), "file parameter must exists.");
	Validate.isTrue(file.canRead(), "file parameter must be readable.");

	ResourceBundle resources = ResourceBundle.getBundle(BedParser.class.getName(), locale);
	final LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(new FileInputStream(file),
		"UTF-8"));
	try {
	    boolean trackFound = false;
	    String rawLine;

	    while (!trackFound && (rawLine = lineNumberReader.readLine()) != null) {
		// Skip empty lines.
		if (StringUtils.isBlank(rawLine)) {
		    continue;
		}

		if (rawLine.startsWith("track")) {
		    Line line = new LineDefault(rawLine, lineNumberReader.getLineNumber());
		    this.validateTrack(resources, line, warningHandler, file);
		    trackFound = true;
		}
	    }
	    if (!trackFound) {
		logger.debug("File {} does not contain a track", file);
		warningHandler.handle(message(resources, WarningType.NO_TRACK, file.getName()));
	    }
	} finally {
	    lineNumberReader.close();
	}
    }

    private void validateTrack(ResourceBundle resources, Line line, WarningHandler warningHandler, File file) {
	String value = line.getContent();
	if (!Pattern.matches(this.bundle.getString("track.pattern"), value)) {
	    logger.debug("Track at line {} does not match pattern {}", line.getLineNumber(),
		    bundle.getString("track.pattern"));
	    warningHandler.handle(message(resources, WarningType.INVALID_TRACK, file.getName(), line.getLineNumber()));
	}

	Set<String> parameterNames = new HashSet<String>();
	Pattern pattern = Pattern.compile(bundle.getString("track.attribute.pattern"));
	Matcher matcher = pattern.matcher(line.getContent());
	int start = 0;
	while (matcher.find(start)) {
	    String name = matcher.group(1);
	    parameterNames.add(name);

	    start = matcher.end();
	}

	if (!parameterNames.contains("name")) {
	    logger.debug("Track at line {} does not contain a name", line.getLineNumber());
	    warningHandler.handle(message(resources, WarningType.NO_NAME, file.getName(), line.getLineNumber()));
	}
	if (!parameterNames.contains("db")) {
	    logger.debug("Track at line {} does not contain a database", line.getLineNumber());
	    warningHandler.handle(message(resources, WarningType.NO_DATABASE, file.getName(), line.getLineNumber()));
	}
    }

    @Override
    public BedTrack parseFirstTrack(File file) throws IOException {
	Validate.notNull(file, "file parameter cannot be null.");
	Validate.isTrue(file.exists(), "file parameter must exists.");
	Validate.isTrue(file.canRead(), "file parameter must be readable.");

	final LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(new FileInputStream(file),
		"UTF-8"));
	try {
	    String rawLine;

	    while ((rawLine = lineNumberReader.readLine()) != null) {
		// Skip empty lines.
		if (StringUtils.isBlank(rawLine)) {
		    continue;
		}

		if (rawLine.startsWith("track")) {
		    Line line = new LineDefault(rawLine, lineNumberReader.getLineNumber());
		    return this.parseTrack(line);
		}
	    }

	    return new BedTrackDefault();
	} finally {
	    lineNumberReader.close();
	}
    }

    private BedTrackDefault parseTrack(Line line) {
	BedTrackDefault track = new BedTrackDefault();

	String value = line.getContent();

	Pattern pattern = Pattern.compile(bundle.getString("track.attribute.pattern"));
	Matcher matcher = pattern.matcher(line.getContent());
	int start = 0;
	while (matcher.find(start)) {
	    String name = matcher.group(1);
	    if (matcher.group(2) != null) {
		value = matcher.group(2);
	    } else {
		value = matcher.group(3);
	    }
	    if (name.equals("name")) {
		track.name = value;
	    } else if (name.equals("db")) {
		track.database = value;
	    } else if (name.equals("color")) {
		track.color = parseColor(value);
	    }

	    start = matcher.end();
	}

	return track;
    }

    private Color parseColor(String colorValue) {
	Pattern pattern = Pattern.compile(bundle.getString("color.pattern"));
	Matcher matcher = pattern.matcher(colorValue);
	if (matcher.matches()) {
	    return new Color(Double.valueOf(matcher.group(1)) / 255, Double.valueOf(matcher.group(2)) / 255,
		    Double.valueOf(matcher.group(3)) / 255, 1.0);
	} else {
	    return null;
	}
    }

    private String message(ResourceBundle resources, WarningType type, Object... replacements) {
	String pattern = resources.getString("warning." + type.name());
	return MessageFormat.format(pattern, replacements);
    }
}
