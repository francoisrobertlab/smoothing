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
     *            annotation
     * @return true if this annotation overlaps annotation, false otherwise
     */
    public boolean overlap(Annotation annotation);
}
