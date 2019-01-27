/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.io.File;
import java.net.URL;

import net.sf.gm.core.properties.SystemProperties;

//
/**
 * The Class LocationUtil.
 */
public class LocationUtil {

  /* convert something to URL */
  /**
   * convert File object to URL object (ignore all exceptions).
   *
   * @param file the file
   *
   * @return the URL
   */
  public static URL toUrl(final File file) {

    URL url = null;
    try {
      url = file.toURI().toURL();
    } catch (final Exception ignore) {
    }
    return url;
  }

  /**
   * convert String to URL object (ignore all exceptions).
   *
   * @param path the path
   *
   * @return the URL
   */
  public static URL toUrl(final String path) {

    URL url = null;
    try {
      url = new URL(path);
    } catch (final Exception e) {
      try {
        url = LocationUtil.toUrl(new File(path));
      } catch (final Exception ignore) {
      }
    }
    return url;
  }

  /**
   * search for a resource url (using the ClassLoader for this class).
   *
   * @param resourceName the resource name
   *
   * @return the resource url
   */
  public static URL getResourceUrl(final String resourceName) {

    return LocationUtil.getResourceUrl(resourceName, LocationUtil.class);
  }

  /**
   * search for a resource url (using the ClassLoader for specified class).
   *
   * @param resourceClass the resource class
   * @param resourceName  the resource name
   *
   * @return the resource url
   */
  public static URL getResourceUrl(final String resourceName,
                                   final Class<?> resourceClass) {

    final ClassLoader clsLoader = resourceClass.getClassLoader();
    return clsLoader == null ? ClassLoader.getSystemResource(resourceName)
                             : clsLoader.getResource(resourceName);
  }

  /**
   * return .class location url for specified class
   *
   * @param cls the cls
   *
   * @return the class url
   */
  public static URL getClassUrl(final Class<?> cls) {

    URL url = null;
    try {
      final URL classPathUrl = LocationUtil.getClassPathUrl(cls);
      if (classPathUrl == null)
        return null;
      url = new URL(classPathUrl, LocationUtil.getResourceNameFromClass(cls));
    } catch (final Exception ignore) {
    }
    return url;
  }

  /**
   * return classpath location url for specified class.
   *
   * @param cls the cls
   *
   * @return the class path url
   */
  public static URL getClassPathUrl(final Class<?> cls) {

    URL url = null;
    try {
      final URL codeLocation =
          cls.getProtectionDomain().getCodeSource().getLocation();
      final File codeFile = LocationUtil.toFile(codeLocation);
      if (codeFile == null || !codeFile.exists())
        return null;
      String classPathUrlString = null;
      if (codeFile.isFile())
        classPathUrlString = "jar:" + codeLocation.toExternalForm() + "!/";
      else
        classPathUrlString = codeLocation.toExternalForm();
      url = new URL(classPathUrlString);
    } catch (final Exception ignore) {
    }
    return url;
  }

  /* convert something to FILE */
  /**
   * convert url to file.
   *
   * @param url the url
   *
   * @return the file
   */
  public static File toFile(final URL url) {

    if (url == null)
      return null;
    if (!url.getProtocol().equals("file"))
      return null;
    File res = null;
    try {
      res = new File(url.toURI());
    } catch (final Exception ignore) {
    }
    return res;
  }

  /**
   * return a the installation directory for a specified class
   *
   * e.g. class: packagename.classname
   *
   * INSTALLDIR/packagename/classname.class INSTALLDIR/jarname.jar
   *
   * @param cls the cls
   *
   * @return the class install dir
   */
  public static File getClassInstallDir(final Class<?> cls) {

    File installDir = null;
    try {
      final URL codeLocation =
          cls.getProtectionDomain().getCodeSource().getLocation();
      final File codeFile = LocationUtil.toFile(codeLocation);
      if (codeFile == null || !codeFile.exists())
        return null;
      // codeLocation points to directory or jar file containing the class
      if (codeFile.isFile()) // we want the directory containing the jar file
        installDir = codeFile.getParentFile();
      else
        installDir = codeFile;
      if (!installDir.exists())
        return null;
    } catch (final Exception ignore) {
    }
    return installDir;
  }

  /* convert more */
  /**
   * Gets the resource name from class.
   *
   * @param cls the cls
   *
   * @return the resource name from class
   */
  public static String getResourceNameFromClass(final Class<?> cls) {

    return cls.getName().replace('.', '/').concat(".class");
  }

  /**
   * Gets the file name from class.
   *
   * @param cls the cls
   *
   * @return the file name from class
   */
  public static String getFileNameFromClass(final Class<?> cls) {

    return cls.getName()
        .replace(".", SystemProperties.getFileSeperator())
        .concat(".class");
  }

  /* concatenate path */
  /**
   * return File object for concatenated path.
   *
   * @param parent  the parent
   * @param subPath the sub path
   *
   * @return the child
   */
  public static File getChild(final File parent, final String... subPath) {

    if (parent == null)
      return null;
    File f = parent;
    for (final String s : subPath)
      f = new File(f, s);
    return f;
  }

  /**
   * get file path string from concatenated path count.
   *
   * @param parent  the parent
   * @param subPath the sub path
   *
   * @return the child path
   */
  public static String getChildPath(final File parent,
                                    final String... subPath) {

    return LocationUtil.getChild(parent, subPath).getAbsolutePath();
  }

  /**
   * get URL object for concatenated path.
   *
   * @param parent  the parent
   * @param subPath the sub path
   *
   * @return the child
   */
  public static URL getChild(final URL parent, final String... subPath) {

    if (parent == null)
      return null;
    try {
      URL u = parent;
      for (final String s : subPath)
        u = new URL(u, s);
      return u;
    } catch (final Exception ignore) {
    }
    return null;
  }

  /**
   * get url string from concatenated path...
   *
   * @param parent  the parent
   * @param subPath the sub path
   *
   * @return the child path
   */
  public static String getChildPath(final URL parent, final String... subPath) {

    return LocationUtil.getChild(parent, subPath).toExternalForm();
  }

  /* split classpath */
  /**
   * Split class path.
   *
   * @param path the path
   *
   * @return the string[]
   */
  public static String[] splitClassPath(final String path) {

    if (path == null)
      return null;
    if (path.contains(SystemProperties.getPathSeperator()) ||
        path.contains(SystemProperties.getPersistentPathSeperator()))
      return path.split(SystemProperties.getSplitPathExpression());
    final String[] res = new String[1];
    res[0] = path;
    return res;
  }
}
