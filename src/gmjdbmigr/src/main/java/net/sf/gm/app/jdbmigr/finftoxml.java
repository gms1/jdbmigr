/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.app.jdbmigr;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import net.sf.gm.app.jdbmigr.common.JDbMigrApplicationBase;
import net.sf.gm.core.app.AbstractApplication;
import net.sf.gm.core.app.AppProgress;
import net.sf.gm.core.cmdline.CmdLineParser;
import net.sf.gm.core.cmdline.FileFilterItem;
import net.sf.gm.core.cmdline.FileFilterList;
import net.sf.gm.core.cmdline.OptionalFlag;
import net.sf.gm.core.ui.OutputTarget;
import net.sf.gm.core.ui.Progress;

import org.jvnet.fastinfoset.FastInfosetSource;

//
/**
 * The Class finftoxml.
 */
public class finftoxml extends JDbMigrApplicationBase {

  /** The force. */
  private boolean force;

  /** The file list. */
  private FileFilterList fileList;

  /**
   * The Constructor.
   */
  public finftoxml() { super(); }

  /**
   * Parses the cmd line.
   *
   * @param args the args
   *
   * @return the string[]
   */
  @Override
  protected String[] ParseCmdLine(final String[] args) {

    final CmdLineParser clp = new CmdLineParser();
    final OptionalFlag optHelp = new OptionalFlag(
        clp, "h?", "help", "help mode\nprint this usage information and exit");
    optHelp.forHelpUsage();
    final OptionalFlag optForce =
        new OptionalFlag(clp, "f", "force", "force overwrite");

    clp.setArgumentDescription(
        "[[sourcefile [destinationfile]]\n[sourcefile... targetdirectory]]", -1,
        -1, null);
    final String[] argv = clp.getOptions(args);

    force = optForce.getValue(false);

    return argv;
  }

  /**
   * Inits the instance.
   *
   * @param args the args
   *
   * @return the int
   *
   * @throws Exception the exception
   */
  @Override
  protected int initInstance(final String[] args) throws Exception {

    setShowElapsed(false);
    fileList = new FileFilterList(args, ".xml", force, false);

    if (fileList.size() == 1 && fileList.get(0).isStdOut())
      AbstractApplication.setMessageLevel(OutputTarget.LEVEL_WARNING);

    return 0;
  }

  /**
   * Run instance.
   *
   * @return the int
   *
   * @throws Exception the exception
   */
  @Override
  protected int runInstance() throws Exception {

    final Transformer tx = TransformerFactory.newInstance().newTransformer();
    tx.setOutputProperty(OutputKeys.INDENT, "yes");
    int failedCount = 0;
    Progress progress = new AppProgress();
    int currentItems = 0;
    int allItems = fileList.size();
    progress.setProgress(0);
    for (FileFilterItem item : fileList) {
      currentItems++;
      try {
        progress.messageln("converting '" + item.getInputPath() + "' ...");
        InputStream is = item.getInputStream();
        OutputStream os = item.getOutputStream();
        tx.transform(new FastInfosetSource(is), new StreamResult(os));
        item.close();
        progress.messageln("  '" + item.getOutputPath() + "' created");
      } catch (Exception e) {
        failedCount++;
        item.closeAndDelete();
        progress.errorln("failed: ", e);
      }
      progress.setProgress(currentItems * 100 / allItems);
    }
    if (failedCount > 0)
      progress.errorln("convertion failed for " + failedCount + " of " +
                       fileList.size() + " file(s)");
    progress.setProgress(100);
    return failedCount;
  }

  /**
   * Exit instance.
   *
   * @param exit the exit
   *
   * @return the int
   */
  @Override
  protected int exitInstance(final int exit) {

    super.exitInstance(exit);
    fileList = null;
    return exit;
  }

  /**
   * The main method.
   *
   * @param args the args
   */
  public static void main(final String[] args) {

    AbstractApplication.runMain(null, "jdbmigr",
                                finftoxml.class.getSimpleName(), null,
                                "gmjdbmigr.version", "gm.jdbmigr.release",
                                finftoxml.class.getName(), args);
  }
}
