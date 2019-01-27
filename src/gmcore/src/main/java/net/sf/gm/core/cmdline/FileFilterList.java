/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.cmdline;

import java.io.File;
import java.util.ArrayList;

import net.sf.gm.core.cmdline.CmdLineException.*;
import net.sf.gm.core.utils.FileFilterUtil;
import net.sf.gm.core.utils.FileUtil;

/**
 * The Class FileFilterList.
 */
public class FileFilterList extends ArrayList<FileFilterItem> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -312674890097636757L;

  /**
   * The Constructor.
   *
   * @param newFileExtension the new file extension
   * @param recursive        the recursive
   * @param args             the args
   * @param forceOverwrite   the force overwrite
   *
   * @throws CmdLineException the cmd line exception
   */
  public FileFilterList(final String[] args, String newFileExtension,
                        final boolean forceOverwrite, final boolean recursive)
      throws CmdLineException {

    super();
    getList(this, args, newFileExtension, forceOverwrite, recursive);
  }

  /**
   * Gets the list.
   *
   * @param newFileExtension the new file extension
   * @param recursive        the recursive
   * @param args             the args
   * @param forceOverwrite   the force overwrite
   *
   * @return list of filter items (pairs of source and target files)
   *
   * @throws CmdLineException the cmd line exception
   */
  public static ArrayList<FileFilterItem>
  getList(final String[] args, String newFileExtension,
          final boolean forceOverwrite, final boolean recursive)
      throws CmdLineException {

    ArrayList<FileFilterItem> list = new ArrayList<FileFilterItem>();
    return getList(list, args, newFileExtension, forceOverwrite, recursive);
  }

  /**
   * Gets the list.
   *
   * @param list           the list
   * @param recursive      the recursive
   * @param args           the args
   * @param forceOverwrite the force overwrite
   * @param newExtension   the new extension
   *
   * @return list of filter items (pairs of source and target files)
   *
   * @throws CmdLineException the cmd line exception
   */
  public static ArrayList<FileFilterItem>
  getList(ArrayList<FileFilterItem> list, final String[] args,
          String newExtension, final boolean forceOverwrite,
          final boolean recursive) throws CmdLineException {

    if (args.length == 0) {
      list.add(new FileFilterItem(null, null));
      return list;
    }
    if (args.length == 1) {
      ArrayList<File> filelist =
          FileFilterUtil.filter(args[0], recursive, "*.*");
      for (File file : filelist)
        list.add(new FileFilterItem(file, null));
      return list;
    }

    String trgFilePath = args[args.length - 1];
    File trgFile = new File(trgFilePath);
    boolean trgIsDir = trgFile.exists() && trgFile.isDirectory();

    if (args.length > 2 && !trgIsDir)
      throw new InvalidFileArgument(trgFilePath, "not a directory");

    ArrayList<File> srcFileList = new ArrayList<File>();
    for (int i = 0; i < args.length - 1; i++) {
      ArrayList<File> argFileList =
          FileFilterUtil.filter(args[i], recursive, "*.*");
      if (argFileList.size() < 1)
        throw new InvalidFileArgument(args[i], "not found");
      srcFileList.addAll(argFileList);
    }
    if (srcFileList.size() < 1)
      throw new InvalidFileArgument("source file(s) not found");

    if (srcFileList.size() >= 2 && !trgIsDir)
      throw new InvalidFileArgument(trgFilePath, "not a directory");

    if (trgIsDir) {
      for (File srcFile : srcFileList) {
        String oldFileName = srcFile.getName();
        int pos = oldFileName.lastIndexOf('.');
        StringBuilder sb = new StringBuilder();
        if (pos > 1)
          sb.append(oldFileName.substring(0, pos));
        else
          sb.append(oldFileName);
        sb.append(newExtension);
        File newTrgFile = new File(trgFile, sb.toString());
        if (newTrgFile.exists()) {
          if (!forceOverwrite)
            throw new InvalidFileArgument(newTrgFile.getPath(), "exist");
          FileUtil.delete(newTrgFile);
          if (newTrgFile.exists())
            throw new InvalidFileArgument(newTrgFile.getPath(),
                                          "delete failed");
        }

        list.add(new FileFilterItem(srcFile, newTrgFile));
      }
    } else {
      if (trgFile.exists()) {
        if (!forceOverwrite)
          throw new InvalidFileArgument(trgFilePath, "exist");
        FileUtil.delete(trgFile);
        if (trgFile.exists())
          throw new InvalidFileArgument(trgFile.getPath(), "delete failed");
      } else {
        final File parent = trgFile.getParentFile();
        if (parent != null && !parent.exists())
          throw new InvalidFileArgument(trgFilePath,
                                        "parent directory not found");
      }
      for (File srcFile : srcFileList) {
        list.add(new FileFilterItem(srcFile, trgFile));
      }
    }

    return list;
  }
}
