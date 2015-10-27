package ca.qc.ircm.smoothing.bio;

/**
 * Strand on chromosome.
 */
public enum Strand {
    /**
     * '+' strand.
     */
    PLUS("+"),
    /**
     * '-' strand.
     */
    MINUS("-");
    /**
     * Strand value in database / files.
     */
    public final String databaseValue;

    Strand(String databaseValue) {
	this.databaseValue = databaseValue;
    }
}