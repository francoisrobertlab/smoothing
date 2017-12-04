package ca.qc.ircm.smoothing;

import static ca.qc.ircm.smoothing.OperatingSystem.MAC;
import static ca.qc.ircm.smoothing.OperatingSystem.OTHER;
import static ca.qc.ircm.smoothing.OperatingSystem.UNIX;
import static ca.qc.ircm.smoothing.OperatingSystem.WINDOWS;
import static ca.qc.ircm.smoothing.OperatingSystem.WINDOWS_XP_OLDER;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link OperatingSystemService}.
 */
@Service
public class OperatingSystemService {
  /**
   * Returns current operating system.
   *
   * @return current operating system
   */
  public OperatingSystem currentOs() {
    if (SystemUtils.IS_OS_WINDOWS) {
      try {
        Double version = Double.parseDouble(SystemUtils.OS_VERSION);
        if (version < 6.0) {
          return WINDOWS_XP_OLDER;
        }
      } catch (Throwable e) {
        // Assume WINDOWS.
      }
      return WINDOWS;
    } else if (SystemUtils.IS_OS_MAC) {
      return MAC;
    } else if (SystemUtils.IS_OS_UNIX) {
      return UNIX;
    }
    return OTHER;
  }

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
  public boolean is64bit(OperatingSystem os) {
    boolean is64bit = System.getProperty("os.arch").indexOf("64") != -1;
    switch (os) {
      case WINDOWS:
      case WINDOWS_XP_OLDER:
        return System.getenv("ProgramFiles(x86)") != null || is64bit;
      case MAC:
        return is64bit;
      case UNIX:
        return is64bit;
      case OTHER:
        return is64bit;
      default:
        throw new AssertionError("");
    }
  }
}
