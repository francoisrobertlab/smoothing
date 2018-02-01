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

import ca.qc.ircm.smoothing.bio.AnnotationDefault;

import java.awt.Color;
import java.util.List;

/**
 * Default implementation of {@link BedAnnotation}.
 */
public class BedAnnotationDefault extends AnnotationDefault implements BedAnnotation {
  public Integer score;
  public Long thickStart;
  public Long thickEnd;
  public Color itemRgb;
  public Integer blockCount;
  public List<Long> blockSizes;
  public List<Long> blockStarts;
  public Double dataValue;

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  @Override
  public Long getThickStart() {
    return thickStart;
  }

  public void setThickStart(Long thickStart) {
    this.thickStart = thickStart;
  }

  @Override
  public Long getThickEnd() {
    return thickEnd;
  }

  public void setThickEnd(Long thickEnd) {
    this.thickEnd = thickEnd;
  }

  @Override
  public Color getItemRgb() {
    return itemRgb;
  }

  public void setItemRgb(Color itemRgb) {
    this.itemRgb = itemRgb;
  }

  @Override
  public Integer getBlockCount() {
    return blockCount;
  }

  public void setBlockCount(Integer blockCount) {
    this.blockCount = blockCount;
  }

  @Override
  public List<Long> getBlockSizes() {
    return blockSizes;
  }

  public void setBlockSizes(List<Long> blockSizes) {
    this.blockSizes = blockSizes;
  }

  @Override
  public List<Long> getBlockStarts() {
    return blockStarts;
  }

  public void setBlockStarts(List<Long> blockStarts) {
    this.blockStarts = blockStarts;
  }

  @Override
  public Double getDataValue() {
    return dataValue;
  }

  public void setDataValue(Double dataValue) {
    this.dataValue = dataValue;
  }

  @Override
  public Long getMiddle() {
    return (this.getEnd() - this.getStart()) / 2 + this.getStart();
  }

  @Override
  public Long getLength() {
    return this.getEnd() - this.getStart();
  }
}
