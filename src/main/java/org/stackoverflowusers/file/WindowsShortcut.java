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

package org.stackoverflowusers.file;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;

/**
 * Represents a Windows shortcut (typically visible to Java only as a '.lnk' file).
 * <p>
 * Retrieved 2011-09-23 from http://stackoverflow.com/questions/309495/windows-shortcut
 * -lnk-parser-in-java/672775#672775 Originally called LnkParser
 * </p>
 * <p>
 * Written by: (the stack overflow users, obviously!) Headerified, refactored and commented by Code
 * Bling http://stackoverflow.com/users/675721/code-bling Network file support added by Stefan
 * Cordes http://stackoverflow.com/users/81330/stefan-cordes Adapted by Sam Brightman
 * http://stackoverflow.com/users/2492/sam-brightman Based on information in 'The Windows Shortcut
 * File Format' by Jesse Hager &lt;jessehager@iname.com&gt; And somewhat based on code from the book
 * 'Swing Hacks: Tips and Tools for Killer GUIs' by Joshua Marinacci and Chris Adamson ISBN:
 * 0-596-00907-0 http://www.oreilly.com/catalog/swinghks/
 * </p>
 */
@SuppressWarnings("checkstyle:all")
@SuppressFBWarnings(value = "DM_DEFAULT_ENCODING", justification = "Code copied from stackoverflow")
public class WindowsShortcut {
  private boolean isDirectory;
  private boolean isLocal;
  private String real_file;

  /**
   * Provides a quick test to see if this could be a valid link ! If you try to instantiate a new
   * WindowShortcut and the link is not valid, Exceptions may be thrown and Exceptions are extremely
   * slow to generate, therefore any code needing to loop through several files should first check
   * this.
   *
   * @param file
   *          the potential link
   * @return true if may be a link, false otherwise
   * @throws IOException
   *           if an IOException is thrown while reading from the file
   */
  public static boolean isPotentialValidLink(FileObject file) throws IOException {
    final int minimum_length = 0x64;
    InputStream fis = file.getContent().getInputStream();
    boolean isPotentiallyValid = false;
    try {
      isPotentiallyValid = file.getType().equals(FileType.FILE)
          && file.getName().getExtension().toLowerCase().equals("lnk")
          && fis.available() >= minimum_length && isMagicPresent(getBytes(fis, 32));
    } finally {
      fis.close();
    }
    return isPotentiallyValid;
  }

  public WindowsShortcut(File file) throws IOException, ParseException {
    InputStream in = new FileInputStream(file);
    try {
      parseLink(getBytes(in));
    } finally {
      in.close();
    }
  }

  public WindowsShortcut(FileObject file) throws IOException, ParseException {
    InputStream in = file.getContent().getInputStream();
    try {
      parseLink(getBytes(in));
    } finally {
      in.close();
    }
  }

  /**
   * @return the name of the filesystem object pointed to by this shortcut
   */
  public String getRealFilename() {
    return real_file;
  }

  /**
   * Tests if the shortcut points to a local resource.
   *
   * @return true if the 'local' bit is set in this shortcut, false otherwise
   */
  public boolean isLocal() {
    return isLocal;
  }

  /**
   * Tests if the shortcut points to a directory.
   *
   * @return true if the 'directory' bit is set in this shortcut, false otherwise
   */
  public boolean isDirectory() {
    return isDirectory;
  }

  /**
   * Gets all the bytes from an InputStream
   *
   * @param in
   *          the InputStream from which to read bytes
   * @return array of all the bytes contained in 'in'
   * @throws IOException
   *           if an IOException is encountered while reading the data from the InputStream
   */
  private static byte[] getBytes(InputStream in) throws IOException {
    return getBytes(in, null);
  }

  /**
   * Gets up to max bytes from an InputStream
   *
   * @param in
   *          the InputStream from which to read bytes
   * @param max
   *          maximum number of bytes to read
   * @return array of all the bytes contained in 'in'
   * @throws IOException
   *           if an IOException is encountered while reading the data from the InputStream
   */
  private static byte[] getBytes(InputStream in, Integer max) throws IOException {
    // read the entire file into a byte buffer
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    byte[] buff = new byte[256];
    while (max == null || max > 0) {
      int n = in.read(buff);
      if (n == -1) {
        break;
      }
      bout.write(buff, 0, n);
      if (max != null)
        max -= n;
    }
    in.close();
    return bout.toByteArray();
  }

  private static boolean isMagicPresent(byte[] link) {
    final int magic = 0x0000004C;
    final int magic_offset = 0x00;
    return link.length >= 32 && bytesToDword(link, magic_offset) == magic;
  }

  /**
   * Gobbles up link data by parsing it and storing info in member fields
   *
   * @param link
   *          all the bytes from the .lnk file
   */
  private void parseLink(byte[] link) throws ParseException {
    try {
      if (!isMagicPresent(link))
        throw new ParseException("Invalid shortcut; magic is missing", 0);

      // get the flags byte
      byte flags = link[0x14];

      // get the file attributes byte
      final int file_atts_offset = 0x18;
      byte file_atts = link[file_atts_offset];
      byte is_dir_mask = (byte) 0x10;
      if ((file_atts & is_dir_mask) > 0) {
        isDirectory = true;
      } else {
        isDirectory = false;
      }

      // if the shell settings are present, skip them
      final int shell_offset = 0x4c;
      final byte has_shell_mask = (byte) 0x01;
      int shell_len = 0;
      if ((flags & has_shell_mask) > 0) {
        // the plus 2 accounts for the length marker itself
        shell_len = bytesToWord(link, shell_offset) + 2;
      }

      // get to the file settings
      int file_start = 0x4c + shell_len;

      final int file_location_info_flag_offset_offset = 0x08;
      int file_location_info_flag = link[file_start + file_location_info_flag_offset_offset];
      isLocal = (file_location_info_flag & 2) == 0;
      // get the local volume and local system values
      // final int localVolumeTable_offset_offset = 0x0C;
      final int basename_offset_offset = 0x10;
      final int networkVolumeTable_offset_offset = 0x14;
      final int finalname_offset_offset = 0x18;
      int finalname_offset = link[file_start + finalname_offset_offset] + file_start;
      String finalname = getNullDelimitedString(link, finalname_offset);
      if (isLocal) {
        int basename_offset = link[file_start + basename_offset_offset] + file_start;
        String basename = getNullDelimitedString(link, basename_offset);
        real_file = basename + finalname;
      } else {
        int networkVolumeTable_offset =
            link[file_start + networkVolumeTable_offset_offset] + file_start;
        int shareName_offset_offset = 0x08;
        int shareName_offset =
            link[networkVolumeTable_offset + shareName_offset_offset] + networkVolumeTable_offset;
        String shareName = getNullDelimitedString(link, shareName_offset);
        real_file = shareName + "\\" + finalname;
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new ParseException("Could not be parsed, probably not a valid WindowsShortcut", 0);
    }
  }

  private static String getNullDelimitedString(byte[] bytes, int off) {
    int len = 0;
    // count bytes until the null character (0)
    while (true) {
      if (bytes[off + len] == 0) {
        break;
      }
      len++;
    }
    return new String(bytes, off, len);
  }

  /*
   * convert two bytes into a short note, this is little endian because it's
   * for an Intel only OS.
   */
  private static int bytesToWord(byte[] bytes, int off) {
    return ((bytes[off + 1] & 0xff) << 8) | (bytes[off] & 0xff);
  }

  private static int bytesToDword(byte[] bytes, int off) {
    return (bytesToWord(bytes, off + 2) << 16) | bytesToWord(bytes, off);
  }

}
