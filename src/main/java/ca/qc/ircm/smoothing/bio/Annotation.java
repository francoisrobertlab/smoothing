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
 * Annotation / gene.
 */
public interface Annotation {
  /**
   * Returns annotation's name.
   * 
   * @return Annotation's name.
   */
  public String getName();

  /**
   * Returns chromosome on which annotatino is located.
   * 
   * @return Chromosome on which annotatino is located.
   */
  public String getChromosome();

  /**
   * Returns start position on chromosome.
   * 
   * @return Start position.
   */
  public Long getStart();

  /**
   * Returns end position on chromosome.
   * 
   * @return Start position.
   */
  public Long getEnd();

  /**
   * Returns strand on chromosome.
   * 
   * @return strand on chromosome.
   */
  public Strand getStrand();

  /**
   * Returns annotation's description.
   * 
   * @return Annotation's description.
   */
  public String getDescription();

  /**
   * Returns true if this annotation overlaps annotation, false otherwise.
   * 
   * @param annotation
   *          annotation
   * @return true if this annotation overlaps annotation, false otherwise
   */
  public boolean overlap(Annotation annotation);
}
