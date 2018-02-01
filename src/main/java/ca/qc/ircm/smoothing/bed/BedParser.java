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

package ca.qc.ircm.smoothing.bed;

import ca.qc.ircm.smoothing.bio.Strand;
import ca.qc.ircm.smoothing.validation.WarningHandler;
import ca.qc.ircm.smoothing.validation.WarningHandlerNoter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses annotations present in a file.
 */
@Service
public class BedParser {
  private static class ParsedBedTrackDefault extends BedTrackDefault implements ParsedBedTrack {
    private boolean valid;

    @Override
    public boolean isValid() {
      return valid;
    }
  }

  private static class ParsedBedAnnotationDefault extends BedAnnotationDefault
      implements ParsedBedAnnotation {
    private boolean valid;

    @Override
    public boolean isValid() {
      return valid;
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return super.equals(obj);
    }
  }

  /**
   * BED file columns.
   */
  public static enum Column {
    CHOMOSOME, START, END, NAME, SCORE, STRAND, THICK_START, THICK_END, ITEM_RGB, BLOCK_COUNT,
    BLOCK_SIZES, BLOCK_STARTS, DATA_VALUE;
  }

  public static enum Warning {
    INVALID_BROWSER, INVALID_TRACK, INVALID_TRACK_TYPE, INVALID_COLUMN_NUMBER, EMPTY_CHROMOSOME,
    INVALID_CHROMOSOME, EMPTY_START, INVALID_START, EMPTY_END, INVALID_END, INVALID_SCORE,
    INVALID_STRAND, INVALID_THICK_START, THICK_START_OUTSIDE_LOCATION, INVALID_THICK_END,
    THICK_END_OUTSIDE_LOCATION, INVALID_ITEM_RGB, INVALID_BLOCK_COUNT, INVALID_BLOCK_SIZES,
    /**
     * Number of block sizes doesn't match block count.
     */
    MISSMATCH_BLOCK_SIZES_COUNT, INVALID_BLOCK_STARTS,
    /**
     * Number of block starts doesn't match block count.
     */
    MISSMATCH_BLOCK_STARTS_COUNT,
    /**
     * First block start must be equal to 0.
     */
    INVALID_FIRST_BLOCK_START,
    /**
     * For all annotations that have blocks, this must be true: start + last block start + last
     * block size == end.
     */
    INVALID_LAST_BLOCK, EMPTY_DATA_VALUE, INVALID_DATA_VALUE;
  }

  private static interface Line {
    public String getContent();

    public int getLineNumber();

    public int getColumnCount();

    public List<String> getColumns();

    public String getColumn(int index);

    public String getColumn(Column column);
  }

  private static class LineDefault implements Line {
    private final String rawLine;
    private final List<String> columns;
    private final int lineNumber;
    private final int columnsCount;

    private LineDefault(String rawLine, int lineNumber) {
      this(rawLine, lineNumber, null);
    }

    private LineDefault(String rawLine, int lineNumber, BedTrack track) {
      this.rawLine = rawLine;
      this.lineNumber = lineNumber;
      String[] rawColumns = rawLine.split("\\s+");
      columnsCount = rawColumns.length;
      if (rawColumns.length == 0) {
        rawColumns = new String[] { "" };
      }
      columns = new ArrayList<>();
      // Pad optional columns.
      for (int i = 0; i < Math.max(Column.values().length, rawColumns.length); i++) {
        columns.add("");
      }
      // Copy actual columns.
      for (int i = 0; i < rawColumns.length; i++) {
        columns.set(i, rawColumns[i]);
      }
      if (track != null && track.getType() != null) {
        switch (track.getType()) {
          case BED:
            break;
          case WIGGLE:
            // In wiggle tracks, data value replaces name column.
            columns.set(Column.DATA_VALUE.ordinal(), columns.get(Column.NAME.ordinal()));
            columns.set(Column.NAME.ordinal(), "");
            break;
          default:
        }
      }
    }

    private String removeQuotes(String input) {
      if (input != null && input.length() >= 2 && input.startsWith("\"") && input.endsWith("\"")) {
        return input.substring(1, input.length() - 1);
      } else {
        return input;
      }
    }

    @Override
    public String getContent() {
      return rawLine;
    }

    @Override
    public int getLineNumber() {
      return lineNumber;
    }

    @Override
    public List<String> getColumns() {
      return columns;
    }

    @Override
    public String getColumn(int index) {
      return removeQuotes(columns.get(index).trim());
    }

    @Override
    public String getColumn(Column column) {
      return removeQuotes(columns.get(column.ordinal()).trim());
    }

    @Override
    public int getColumnCount() {
      return columnsCount;
    }
  }

  private final Logger logger = LoggerFactory.getLogger(BedParser.class);

  /**
   * Validates BED file.
   *
   * @param file
   *          BED file
   * @param locale
   *          user's locale
   * @param warningHandler
   *          handles warnings
   * @throws IOException
   *           could not parse BED file
   */
  public void validate(File file, Locale locale, WarningHandler warningHandler) throws IOException {
    parseAndValidate(file, null, locale, warningHandler);
  }

  /**
   * Parses BED file.
   *
   * @param file
   *          BED file
   * @param handler
   *          handles BED elements
   * @throws IOException
   *           could not parse BED file
   */
  public void parse(File file, BedHandler handler) throws IOException {
    parseAndValidate(file, handler, Locale.getDefault(), null);
  }

  private void parseAndValidate(File file, BedHandler handler, Locale locale,
      WarningHandler warningHandler) throws IOException {
    Objects.requireNonNull(file, "file parameter cannot be null.");
    ResourceBundle bundle = ResourceBundle.getBundle(BedParser.class.getName(), locale);

    final LineNumberReader lineNumberReader =
        new LineNumberReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    try {
      String rawLine;

      ParsedBedTrackDefault track = null;
      Integer expectedNumberOfColumns = null;
      while ((rawLine = lineNumberReader.readLine()) != null) {
        WarningHandlerNoter noter = new WarningHandlerNoter(warningHandler);

        // Skip empty lines.
        if (StringUtils.isBlank(rawLine)) {
          continue;
        }

        if (rawLine.startsWith("browser")) {
          Line line = new LineDefault(rawLine, lineNumberReader.getLineNumber());
          validateBrowser(line, bundle, noter);
        } else if (rawLine.startsWith("track")) {
          Line line = new LineDefault(rawLine, lineNumberReader.getLineNumber());
          track = this.parseTrack(line, bundle, noter);
          track.valid = !noter.hasWarning();
          expectedNumberOfColumns = null;
          if (handler != null && (handler.handleInvalid() || track.isValid())) {
            handler.handleTrack(track);
          }
        } else {
          Line line = new LineDefault(rawLine, lineNumberReader.getLineNumber(), track);
          if (expectedNumberOfColumns == null) {
            expectedNumberOfColumns = line.getColumnCount();
          } else if (line.getColumnCount() != expectedNumberOfColumns) {
            String message = message(bundle, Warning.INVALID_COLUMN_NUMBER, line.getLineNumber());
            logger.debug("{}", message);
            noter.handle(message);
          }
          ParsedBedAnnotationDefault annotation = parseAnnotation(line, track, bundle, noter);
          annotation.valid = !noter.hasWarning();
          if (handler != null
              && (handler.handleInvalid() || (track.isValid() && annotation.isValid()))) {
            handler.handleAnnotation(annotation, track);
          }
        }
      }
    } finally {
      lineNumberReader.close();
    }
  }

  private void validateBrowser(Line line, ResourceBundle bundle, WarningHandler warningHandler) {
    String value = line.getContent();
    if (!Pattern.matches(bundle.getString("browser.pattern"), value)) {
      String message = message(bundle, Warning.INVALID_BROWSER, line.getLineNumber());
      logger.debug("{}", message);
      warningHandler.handle(message);
    }
  }

  private ParsedBedTrackDefault parseTrack(Line line, ResourceBundle bundle,
      WarningHandler warningHandler) {
    ParsedBedTrackDefault track = new ParsedBedTrackDefault();
    track.allParameters = new HashMap<>();

    String value = line.getContent();
    if (!Pattern.matches(bundle.getString("track.pattern"), value)) {
      String message = message(bundle, Warning.INVALID_TRACK, line.getLineNumber());
      logger.debug("{}", message);
      warningHandler.handle(message);
    }

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
      track.allParameters.put(name, value);
      if (name.equals("type")) {
        BedTrack.Type trackType = null;
        for (BedTrack.Type type : BedTrack.Type.values()) {
          if (value.equals(bundle.getString("track.type." + type.name()))) {
            trackType = type;
          }
        }
        if (trackType == null) {
          String message = message(bundle, Warning.INVALID_TRACK_TYPE, line.getLineNumber());
          logger.debug("{}", message);
          warningHandler.handle(message);
        }
      }

      start = matcher.end();
    }

    return track;
  }

  private Color parseColor(ResourceBundle bundle, String colorValue) {
    Pattern pattern = Pattern.compile(bundle.getString("color.pattern"));
    Matcher matcher = pattern.matcher(colorValue);
    if (matcher.matches()) {
      int red = Integer.parseInt(matcher.group(1));
      int green;
      if (matcher.group(2) == null) {
        green = Integer.parseInt(matcher.group(1));
      } else {
        green = Integer.parseInt(matcher.group(2));
      }
      int blue;
      if (matcher.group(3) == null) {
        blue = Integer.parseInt(matcher.group(1));
      } else {
        blue = Integer.parseInt(matcher.group(3));
      }
      try {
        return new Color(red, green, blue);
      } catch (IllegalArgumentException e) {
        return null;
      }
    } else {
      return null;
    }
  }

  private ParsedBedAnnotationDefault parseAnnotation(Line line, BedTrack track,
      ResourceBundle bundle, WarningHandler warningHandler) {
    ParsedBedAnnotationDefault annotation = new ParsedBedAnnotationDefault();
    String value;

    // Chromosome.
    value = line.getColumns().get(Column.CHOMOSOME.ordinal());
    if (value.equals("")) {
      String message = message(bundle, Warning.EMPTY_CHROMOSOME, line.getLineNumber());
      logger.debug("{}", message);
      warningHandler.handle(message);
    } else {
      Pattern pattern = Pattern.compile(bundle.getString("chromosome.pattern"));
      Matcher matcher = pattern.matcher(value);
      if (!matcher.matches()) {
        String message = message(bundle, Warning.INVALID_CHROMOSOME, line.getLineNumber(), value);
        logger.debug("{}", message);
        warningHandler.handle(message);
      } else {
        annotation.chromosome = matcher.group(1);
      }
    }

    // Start.
    value = line.getColumns().get(Column.START.ordinal());
    if (value.equals("")) {
      String message = message(bundle, Warning.EMPTY_START, line.getLineNumber());
      logger.debug("{}", message);
      warningHandler.handle(message);
    } else {
      try {
        annotation.start = Long.parseLong(value);
      } catch (NumberFormatException e) {
        String message = message(bundle, Warning.INVALID_START, line.getLineNumber(), value);
        logger.debug("{}", message);
        warningHandler.handle(message);
      }
    }

    // End.
    value = line.getColumns().get(Column.END.ordinal());
    if (value.equals("")) {
      String message = message(bundle, Warning.EMPTY_END, line.getLineNumber());
      logger.debug("{}", message);
      warningHandler.handle(message);
    } else {
      try {
        annotation.end = Long.parseLong(value);
      } catch (NumberFormatException e) {
        String message = message(bundle, Warning.INVALID_END, line.getLineNumber(), value);
        logger.debug("{}", message);
        warningHandler.handle(message);
      }
    }

    if (track == null || track.getType() == null || track.getType() == BedTrack.Type.BED) {
      // Name.
      if (!line.getColumns().get(Column.NAME.ordinal()).equals("")) {
        annotation.name = line.getColumns().get(Column.NAME.ordinal());
      }

      // Score.
      value = line.getColumns().get(Column.SCORE.ordinal());
      if (!value.equals("")) {
        try {
          annotation.score = new Integer(value);
        } catch (NumberFormatException e) {
          String message = message(bundle, Warning.INVALID_SCORE, line.getLineNumber(), value);
          logger.debug("{}", message);
          warningHandler.handle(message);
        }
      }

      // Strand.
      value = line.getColumns().get(Column.STRAND.ordinal());
      if (!value.equals("")) {
        // Find matching strand.
        Strand strand = null;
        for (Strand strandEnum : Strand.values()) {
          if (value.equals(bundle.getString("strand." + strandEnum.name()))) {
            strand = strandEnum;
          }
        }

        if (strand == null) {
          // No match was found.
          String message = message(bundle, Warning.INVALID_STRAND, line.getLineNumber(), value);
          logger.debug("{}", message);
          warningHandler.handle(message);
        } else {
          annotation.strand = strand;
        }
      }

      // Thick start.
      value = line.getColumns().get(Column.THICK_START.ordinal());
      if (!value.equals("")) {
        try {
          annotation.thickStart = new Long(value);
        } catch (NumberFormatException e) {
          String message =
              message(bundle, Warning.INVALID_THICK_START, line.getLineNumber(), value);
          logger.debug("{}", message);
          warningHandler.handle(message);
        }
        if (annotation.start != null && annotation.end != null && annotation.thickStart != null
            && (annotation.thickStart < annotation.start
                || annotation.thickStart > annotation.end)) {
          String message =
              message(bundle, Warning.THICK_START_OUTSIDE_LOCATION, line.getLineNumber(), value);
          logger.debug("{}", message);
          warningHandler.handle(message);
        }
      }

      // Thick end.
      value = line.getColumns().get(Column.THICK_END.ordinal());
      if (!value.equals("")) {
        try {
          annotation.thickEnd = new Long(value);
        } catch (NumberFormatException e) {
          String message = message(bundle, Warning.INVALID_THICK_END, line.getLineNumber(), value);
          logger.debug("{}", message);
          warningHandler.handle(message);
        }
        if (annotation.start != null && annotation.end != null && annotation.thickEnd != null
            && (annotation.thickEnd < annotation.start || annotation.thickEnd > annotation.end)) {
          String message =
              message(bundle, Warning.THICK_END_OUTSIDE_LOCATION, line.getLineNumber(), value);
          logger.debug("{}", message);
          warningHandler.handle(message);
        }
      }

      // Item RGB.
      value = line.getColumns().get(Column.ITEM_RGB.ordinal());
      if (!value.equals("")) {
        annotation.itemRgb = parseColor(bundle, value);
        if (annotation.itemRgb == null) {
          String message = message(bundle, Warning.INVALID_ITEM_RGB, line.getLineNumber(), value);
          logger.debug("{}", message);
          warningHandler.handle(message);
        }
      }

      // Block count.
      boolean blockError = false;
      value = line.getColumns().get(Column.BLOCK_COUNT.ordinal());
      if (!value.equals("")) {
        try {
          annotation.blockCount = new Integer(value);
        } catch (NumberFormatException e) {
          String message =
              message(bundle, Warning.INVALID_BLOCK_COUNT, line.getLineNumber(), value);
          logger.debug("{}", message);
          warningHandler.handle(message);
          blockError = true;
        }
      }

      // Block sizes.
      value = line.getColumns().get(Column.BLOCK_SIZES.ordinal());
      if (!value.equals("")) {
        String[] sizesAsString = value.split(",");
        if (annotation.blockCount != null && sizesAsString.length != annotation.blockCount) {
          String message = message(bundle, Warning.MISSMATCH_BLOCK_SIZES_COUNT,
              line.getLineNumber(), value, annotation.blockCount);
          logger.debug("{}", message);
          warningHandler.handle(message);
          blockError = true;
        }
        annotation.blockSizes = new ArrayList<>();
        boolean formatException = false;
        for (String size : sizesAsString) {
          try {
            annotation.blockSizes.add(new Long(size));
          } catch (NumberFormatException e) {
            if (!formatException) {
              String message =
                  message(bundle, Warning.INVALID_BLOCK_SIZES, line.getLineNumber(), value);
              logger.debug("{}", message);
              warningHandler.handle(message);
              formatException = true;
              blockError = true;
            }
          }
        }
      }

      // Block starts.
      value = line.getColumns().get(Column.BLOCK_STARTS.ordinal());
      if (!value.equals("")) {
        String[] startsAsString = value.split(",");
        if (annotation.blockCount != null && startsAsString.length != annotation.blockCount) {
          String message = message(bundle, Warning.MISSMATCH_BLOCK_STARTS_COUNT,
              line.getLineNumber(), value, annotation.blockCount);
          logger.debug("{}", message);
          warningHandler.handle(message);
          blockError = true;
        }

        annotation.blockStarts = new ArrayList<>();
        boolean formatException = false;
        for (String start : startsAsString) {
          try {
            annotation.blockStarts.add(new Long(start));
          } catch (NumberFormatException e) {
            if (!formatException) {
              String message =
                  message(bundle, Warning.INVALID_BLOCK_STARTS, line.getLineNumber(), value);
              logger.debug("{}", message);
              warningHandler.handle(message);
              formatException = true;
              blockError = true;
            }
          }
        }
      }

      if (!blockError) {
        // Validate first block.
        if (annotation.blockStarts != null && !annotation.blockStarts.isEmpty()
            && annotation.blockStarts.get(0) != 0L) {
          String message = message(bundle, Warning.INVALID_FIRST_BLOCK_START, line.getLineNumber());
          logger.debug("{}", message);
          warningHandler.handle(message);
        }

        // Validate last block.
        if (annotation.start != null && annotation.end != null && annotation.blockCount != null
            && annotation.blockSizes != null && !annotation.blockSizes.isEmpty()
            && annotation.blockStarts != null && !annotation.blockStarts.isEmpty()
            && (annotation.blockCount > 1 || annotation.blockStarts.get(0) == 0L)) {
          long blockCoverage = annotation.start;
          blockCoverage += annotation.blockStarts.get(annotation.blockStarts.size() - 1);
          blockCoverage += annotation.blockSizes.get(annotation.blockSizes.size() - 1);
          if (blockCoverage != annotation.end) {
            String message = message(bundle, Warning.INVALID_LAST_BLOCK, line.getLineNumber());
            logger.debug("{}", message);
            warningHandler.handle(message);
          }
        }
      }
    } else if (track.getType() == BedTrack.Type.WIGGLE) {
      // Data value.
      value = line.getColumns().get(Column.DATA_VALUE.ordinal());
      if (value.equals("")) {
        String message = message(bundle, Warning.EMPTY_DATA_VALUE, line.getLineNumber());
        logger.debug("{}", message);
        warningHandler.handle(message);
      } else {
        try {
          annotation.dataValue = new Double(value);
        } catch (NumberFormatException e) {
          String message = message(bundle, Warning.INVALID_DATA_VALUE, line.getLineNumber(), value);
          logger.debug("{}", message);
          warningHandler.handle(message);
        }
      }
    }

    return annotation;
  }

  private String message(ResourceBundle bundle, Warning warning, Object... replacements) {
    String message = bundle.getString("error." + warning.name());
    return MessageFormat.format(message, replacements);
  }
}
