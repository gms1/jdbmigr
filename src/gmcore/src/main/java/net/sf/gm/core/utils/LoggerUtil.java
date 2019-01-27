/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.io.File;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.sf.gm.core.config.ConfigurationFile;
import net.sf.gm.core.properties.AppProperties;
import net.sf.gm.core.properties.GMCOREProperties;

//
/**
 * This class contains utility functions for logging.
 */
public class LoggerUtil {

  /**
   * Load default config.
   */
  public static void loadDefaultConfig() {

    LoggerUtil.loadConfig(
        AppProperties.getAppProperty(GMCOREProperties.propName_LogConfigFile));
  }

  /**
   * initialize the logging using a configuration file.
   *
   * @param configFile the config file
   */
  public static void loadConfig(final String configFile) {

    final File cfgfile = new File(configFile);
    final File sourcecfgfile = new File(AppProperties.getAppProperty(
        GMCOREProperties.propName_LogConfigSourceFile));

    final ConfigurationFile cfgFile =
        new ConfigurationFile(cfgfile, new ConfigurationFile(sourcecfgfile));

    final InputStream is =
        cfgFile.load() ? cfgFile.getPropertiesStream("/") : null;
    if (is != null) {
      Logger log;
      log = Logger.getLogger(LoggerUtil.class.getName());
      log.fine("logging configuration: read from " + configFile);
      final LogManager lm = LogManager.getLogManager();
      lm.reset();
      try {
        lm.readConfiguration(is);
        is.close();
        log = Logger.getLogger(LoggerUtil.class.getName());
        log.finest("logging configuration: set from " + configFile);
      } catch (final Exception e) {
        try {
          lm.readConfiguration();
          log.config(
              "logging configuration: failed to set configuration from " +
              configFile);
        } catch (final Exception e2) {
        }
      }
    }
  }

  /**
   * add a file to the logfile rdh.
   *
   * @param level    the level
   * @param fileName the file name
   *
   * @return the file rdh
   */
  public static FileHandler addLogFileHandler(final String fileName,
                                              final int level) {

    return LoggerUtil.addLogFileHandler(fileName,
                                        LoggerUtil.getLevelFromInt(level));
  }

  /**
   * add a file to the logfile rdh.
   *
   * @param level    the level
   * @param fileName the file name
   *
   * @return the file rdh
   */
  public static FileHandler addLogFileHandler(final String fileName,
                                              final String level) {

    return LoggerUtil.addLogFileHandler(fileName,
                                        LoggerUtil.getLevelFromString(level));
  }

  /**
   * add a file to the logfile rdh.
   *
   * @param level    the level
   * @param fileName the file name
   *
   * @return the file rdh
   */
  public static FileHandler addLogFileHandler(final String fileName,
                                              final Level level) {

    FileHandler fh = null;
    try {
      fh = new FileHandler(fileName);
    } catch (final Exception e) {
      return null;
    }
    final Logger log = Logger.getLogger("");
    log.addHandler(fh);
    log.setLevel(level);
    return fh;
  }

  /**
   * remove a logfile rdh from logger.
   *
   * @param fh the fh
   */
  public static void remLogFileHandler(final FileHandler fh) {

    final Logger log = Logger.getLogger("");
    log.removeHandler(fh);
  }

  /**
   * helper function to convert java.lang.int to java.logging.Level
   *
   * @param level the level
   *
   * @return the level from int
   */
  public static Level getLevelFromInt(final int level) {

    switch (level) {
    case 0:
      return Level.OFF;
    case 1:
      return Level.SEVERE;
    case 2:
      return Level.WARNING;
    case 3:
      return Level.INFO;
    case 4:
      return Level.CONFIG;
    case 5:
      return Level.FINE;
    case 6:
      return Level.FINER;
    case 7:
      return Level.FINEST;
    default:
      return Level.ALL;
    }
  }

  /**
   * helper function to convert java.lang.String to java.logging.Level
   *
   * @param level the level
   *
   * @return the level from string
   */
  public static Level getLevelFromString(final String level) {

    final String lv = level.toUpperCase();
    if (lv.equals("OFF"))
      return Level.OFF;
    if (lv.equals("SEVERE"))
      return Level.SEVERE;
    if (lv.equals("WARNING"))
      return Level.WARNING;
    if (lv.equals("INFO"))
      return Level.INFO;
    if (lv.equals("CONFIG"))
      return Level.CONFIG;
    if (lv.equals("FINE"))
      return Level.FINE;
    if (lv.equals("FINER"))
      return Level.FINER;
    if (lv.equals("FINEST"))
      return Level.FINEST;
    return Level.ALL;
  }
}
