package ca.qc.ircm.smoothing.bed;

import java.awt.Color;
import java.util.List;

import ca.qc.ircm.smoothing.bio.Annotation;

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
