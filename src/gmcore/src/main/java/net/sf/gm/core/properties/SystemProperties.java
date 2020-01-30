/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.properties;

import net.sf.gm.core.utils.LocationUtil;

import java.io.File;

//


/**
 * contains the static operating system dependend properties like:
 * pathseperator...
 */
final public class SystemProperties {

    /**
     * The Constant isWindows.
     */
    private final static boolean isWindows =
        System.getProperty("os.name").startsWith("Windows") ? true : false;

    /**
     * The Constant persistentPathSeperator.
     */
    private final static String persistentPathSeperator = ";";

    /**
     * The Constant pathSeperator.
     */
    private final static String pathSeperator = System.getProperty(
        "path.separator", SystemProperties.persistentPathSeperator);

    /**
     * The Constant splitPathExpression.
     */
    private final static String splitPathExpression =
        SystemProperties.pathSeperator.equals(
            SystemProperties.persistentPathSeperator)
            ? "[\\" + SystemProperties.pathSeperator + "]+"
            : "[\\" + SystemProperties.pathSeperator + "\\" +
            SystemProperties.persistentPathSeperator + "]+";

    /**
     * The Constant fileSeperator.
     */
    private final static String fileSeperator =
        System.getProperty("file.separator");

    /**
     * The Constant classPath.
     */
    private final static String classPath =
        System.getProperty("java.class.path", ".");

    /**
     * The Constant hiddenFilePrefix.
     */
    private final static String hiddenFilePrefix =
        SystemProperties.isWindows() ? "" : ".";

    /**
     * get the os dependend path seperator.
     *
     * @return the path seperator
     */
    public static String getPathSeperator() {

        return SystemProperties.pathSeperator;
    }

    /**
     * get an os independend path seperator (used for persistence).
     *
     * @return the persistent path seperator
     */
    public static String getPersistentPathSeperator() {

        return SystemProperties.persistentPathSeperator;
    }

    /**
     * get the expression for splitting a path.
     *
     * @return the split path expression
     */
    public static String getSplitPathExpression() {

        return SystemProperties.splitPathExpression;
    }

    /**
     * get the file seperator.
     *
     * @return the file seperator
     */
    public static String getFileSeperator() {

        return SystemProperties.fileSeperator;
    }

    /**
     * get te classpath.
     *
     * @return the class path
     */
    public static String getClassPath() {
        return SystemProperties.classPath;
    }

    /**
     * test if running on windows.
     *
     * @return true, if is windows
     */
    public static boolean isWindows() {
        return SystemProperties.isWindows;
    }

    /**
     * Gets the hidden file prefix.
     *
     * @return the hidden file prefix
     */
    public static String getHiddenFilePrefix() {

        return SystemProperties.hiddenFilePrefix;
    }

    /**
     * get the machine configuration directory.
     *
     * @return the etc dir
     */
    public static String getEtcDir() {

        String dir = null;
        if (!SystemProperties.isWindows())
            dir = "/etc/";
        else {
            dir = System.getenv("ALLUSERSPROFILE");
            if (dir != null)
                if ((dir = LocationUtil.getChildPath(new File(dir),
                    "Application Data")) != null)
                    dir = dir + SystemProperties.getFileSeperator();
        }
        return dir;
    }

    /**
     * get the default user-home directory.
     *
     * @return the home dir
     */
    public static String getHomeDir() {

        String dir = null;
        if (!SystemProperties.isWindows()) {
            if ((dir = System.getProperty("user.home")) != null)
                return dir + SystemProperties.getFileSeperator();
            if ((dir = System.getenv("HOME")) != null)
                return dir = dir + SystemProperties.getFileSeperator();
        } else if ((dir = System.getenv("APPDATA")) != null)
            return dir = dir + SystemProperties.getFileSeperator();
        return dir;
    }

    /**
     * get the library directory * e.g. class: packagename.classname
     * <p>
     * LIBDIR/packagename/classname.class LIBDIR/jarname.jar
     *
     * @param libraryClass the library class
     * @return the library dir
     */
    public static String getLibraryDir(final Class<?> libraryClass) {

        final File file = LocationUtil.getClassInstallDir(libraryClass);
        return file.getAbsolutePath() + SystemProperties.getFileSeperator();
    }
}
