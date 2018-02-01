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

import java.awt.Color;
import java.util.Map;

/**
 * Track inside a BED file.
 */
public interface BedTrack {
  /**
   * Track's type.
   */
  public static enum Type {
    /**
     * Default.
     */
    BED("wiggle_0"),
    /**
     * Equivalent to "type=wiggle_0" at the UCSC
     * (http://genome.ucsc.edu/goldenPath/help/wiggle.html). Note that we handle Wiggle tracks as
     * BedGraph tracks are defined. See http://genome.ucsc.edu/goldenPath/help/bedgraph.html.
     */
    WIGGLE("wiggle_0");
    public final String value;

    Type(String value) {
      this.value = value;
    }
  }

  public static enum BedTrackParameters {
    TYPE("type"), NAME("name"), DESCRIPTION("description"), DATABASE("db"), COLOR("color");
    public final String name;

    BedTrackParameters(String name) {
      this.name = name;
    }
  }

  /**
   * Returns track's name.
   *
   * @return track's name
   */
  public Type getType();

  /**
   * Returns track's name.
   *
   * @return track's name
   */
  public String getName();

  /**
   * Returns track's description.
   *
   * @return track's description
   */
  public String getDescription();

  /**
   * Returns track's database.
   *
   * @return track's database
   */
  public String getDatabase();

  /**
   * Returns track's color.
   *
   * @return track's color
   */
  public Color getColor();

  /**
   * Returns track's other parameters.
   *
   * @return track's other parameters
   */
  public Map<String, String> getAllParameters();
}