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

package ca.qc.ircm.smoothing.bio;

/**
 * Default implementation of {@link Annotation}.
 */
public class AnnotationDefault implements Annotation {
  public String name;
  public String chromosome;
  public Long start;
  public Long end;
  public Strand strand;
  public String description;

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getChromosome() {
    return chromosome;
  }

  public void setChromosome(String chromosome) {
    this.chromosome = chromosome;
  }

  @Override
  public Long getStart() {
    return start;
  }

  public void setStart(Long start) {
    this.start = start;
  }

  @Override
  public Long getEnd() {
    return end;
  }

  public void setEnd(Long end) {
    this.end = end;
  }

  @Override
  public Strand getStrand() {
    return strand;
  }

  public void setStrand(Strand strand) {
    this.strand = strand;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean overlap(Annotation annotation) {
    if (this.getChromosome().equals(annotation.getChromosome())) {
      // If chromosomes are equal, check overlap.
      if (this.getStart() < annotation.getEnd() && annotation.getStart() < this.getEnd()) {
        return true;
      } else {
        return false;
      }
    } else {
      // Chromosomes are not equal, so no overlap.
      return false;
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.getChromosome());
    builder.append(":");
    builder.append(this.getStart());
    builder.append("-");
    builder.append(this.getEnd());
    if (this.getName() != null) {
      builder.append("[");
      builder.append(this.getName());
      builder.append("]");
    }
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((chromosome == null) ? 0 : chromosome.hashCode());
    result = prime * result + ((end == null) ? 0 : end.hashCode());
    result = prime * result + ((start == null) ? 0 : start.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof AnnotationDefault)) {
      return false;
    }
    AnnotationDefault other = (AnnotationDefault) obj;
    if (chromosome == null) {
      if (other.chromosome != null) {
        return false;
      }
    } else if (!chromosome.equals(other.chromosome)) {
      return false;
    }
    if (end == null) {
      if (other.end != null) {
        return false;
      }
    } else if (!end.equals(other.end)) {
      return false;
    }
    if (start == null) {
      if (other.start != null) {
        return false;
      }
    } else if (!start.equals(other.start)) {
      return false;
    }
    return true;
  }
}
