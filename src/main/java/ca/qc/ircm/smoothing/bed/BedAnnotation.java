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

import ca.qc.ircm.smoothing.bio.Annotation;
import java.awt.Color;
import java.util.List;

/**
 * Single annotation in a BED file.
 */
public interface BedAnnotation extends Annotation {
  public Integer getScore();

  public Long getThickStart();

  public Long getThickEnd();

  public Color getItemRgb();

  public Integer getBlockCount();

  public List<Long> getBlockSizes();

  public List<Long> getBlockStarts();

  /**
   * Annotation's value/score. This is valid only if track type is {@link BedTrack.Type#WIGGLE}.
   * 
   * @return annotation's value/score
   */
  public Double getDataValue();

  /**
   * Returns annotation's middle position on chromosome.
   * <p>
   * Middle position is equal to <code>(start + end) / 2</code>
   * </p>
   *
   * @return annotation's middle position on chromosome
   */
  public Long getMiddle();

  /**
   * Returns annotation's length.
   * <p>
   * This value cannot be lower than 1.
   * </p>
   *
   * @return annotation's length
   */
  public Long getLength();
}
