/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

//


/**
 * This class is used to dynamically load a class using a modified classpath.
 */
public class DynamicClassLoader {

    /**
     * The urls.
     */
    private URL[] urls = null;

    /**
     * The Constant af.
     */
    private final static ArchiveFilter af = new ArchiveFilter();

    /**
     * The log.
     */
    static Logger log = Logger.getLogger(DynamicClassLoader.class.getName());

    /**
     * The parent map.
     */
    private static Map<ClassLoader, Map<String, ClassLoader>> parentMap = null;

    /**
     * add classpath to classpath.
     *
     * @param addPath the add path
     * @return true, if add class path
     */
    public synchronized boolean addClassPath(final String addPath) {

        final String[] paths = LocationUtil.splitClassPath(addPath);
        boolean res = true;

        for (String element : paths)
            if (!addSinglePath(element))
                res = false;
        return res;
    }

    /**
     * add path to classpath.
     *
     * @param addPath the add path
     * @return true, if add single path
     */
    public boolean addSinglePath(final String addPath) {

        URL newUrl = null;
        final File f = new File(addPath);
        if (!f.exists()) {
            DynamicClassLoader.log.fine("failed to add \"" + addPath + "\" to classpath (not found)");
            return false;
        }
        try {
            newUrl = f.toURI().toURL();
        } catch (final IOException e) {
            DynamicClassLoader.log
                .fine("failed to add \"" + addPath + "\" to classpath (" + StringUtil.getShortExceptionMessage(e) + ")");
            return false;
        }
        int i = 0;
        if (urls != null) {
            boolean add = true;
            for (URL element : urls)
                if (element.equals(newUrl))
                    add = false;
            if (add) {
                final URL[] newUrls = new URL[urls == null ? 1 : urls.length + 1];
                if (urls != null)
                    for (i = 0; i < urls.length; i++)
                        newUrls[i] = urls[i];
                newUrls[i] = newUrl;
                urls = newUrls;
            }
        } else {
            urls = new URL[1];
            urls[i] = newUrl;
        }
        DynamicClassLoader.log.finest("added \"" + addPath + "\" to classpath (url=" + newUrl.toString() + ")");
        return true;
    }

    /**
     * search a directory for jar and zip files and add them to the classpath.
     *
     * @param dir the dir
     * @return true, if add archives from dir
     */
    public boolean addArchivesFromDir(final File dir) {

        if (dir.isDirectory() == false) {
            DynamicClassLoader.log
                .fine("failed to add archives in \"" + dir.toString() + "\" to classpath (directory not found)");
            return false;
        }

        boolean r = true;
        final File[] archives = dir.listFiles(DynamicClassLoader.af);
        for (int j = 0; archives != null && j < archives.length; j++)
            if (!addSinglePath(archives[j].getAbsolutePath()))
                r = false;
        return r;
    }

    /**
     * load a class and return a new instance of this class.
     *
     * @param className the class name
     * @return the object
     * @throws IllegalAccessException    the illegal access exception
     * @throws ClassNotFoundException    the class not found exception
     * @throws InstantiationException    the instantiation exception
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public synchronized Object loadClass(final String className)
        throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException,
        InvocationTargetException, NoSuchMethodException, SecurityException {

        final ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        final ClassLoader newCL = getClassLoader(oldCL);

        Object res = null;
        res = Class.forName(className, true, newCL).getConstructor().newInstance();
        return res;
    }

    /**
     * get new classloader.
     *
     * @param parentClassLoader the parent class loader
     * @return the class loader
     */
    private ClassLoader getClassLoader(final ClassLoader parentClassLoader) {

        if (DynamicClassLoader.parentMap == null)
            DynamicClassLoader.parentMap = new HashMap<ClassLoader, Map<String, ClassLoader>>();
        Map<String, ClassLoader> childMap = DynamicClassLoader.parentMap.get(parentClassLoader);
        if (childMap == null) {
            childMap = new HashMap<String, ClassLoader>();
            DynamicClassLoader.parentMap.put(parentClassLoader, childMap);
        }
        final String urlsString = urlsToString(urls);
        ClassLoader childClassLoader = childMap.get(urlsString);
        if (childClassLoader == null) {
            childClassLoader = urls == null ? parentClassLoader : new URLClassLoader(urls, parentClassLoader);
            childMap.put(urlsString, childClassLoader);
        }
        return childClassLoader;
    }

    /*
     *
     */

    /**
     * Urls to string.
     *
     * @param urls the urls
     * @return the string
     */
    private final String urlsToString(final URL[] urls) {

        if (urls == null)
            return "";
        final SortedSet<String> urlSet = new TreeSet<String>();
        for (final URL url : urls)
            urlSet.add(url.toString());
        return urlSet.toString();
    }

    /**
     * helper class to filter jar and zip files.
     */
    static class ArchiveFilter implements java.io.FileFilter {

        /**
         * Accept.
         *
         * @param file the file
         * @return true, if accept
         */
        public boolean accept(final File file) {

            if (!file.isFile())
                return false;
            final String uc = file.getName().toUpperCase();
            if (uc.endsWith(".JAR") || uc.endsWith(".ZIP"))
                return true;
            return false;
        }
    }
}
