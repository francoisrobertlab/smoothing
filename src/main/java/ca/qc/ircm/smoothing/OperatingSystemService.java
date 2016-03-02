package ca.qc.ircm.smoothing;

/**
 * Service for operating system.
 */
public interface OperatingSystemService {
  /**
   * Returns current operating system.
   *
   * @return current operating system
   */
  public OperatingSystem currentOS();

  /**
   * Returns true if underling OS is 64 bits, false otherwise.
   * <p>
   * There is no easy way to determine if OS is 64 bits in java. See
   * http://stackoverflow.com/questions/1856565/how-do-you-determine-32-or-64-bit-architecture-of-
   * windows-using-java
   * </p>
   *
   * @param os
   *          operating system
   * @return true if underling OS is 64 bits, false otherwise.
   */
  public boolean is64bit(OperatingSystem os);
}
