/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

//


/**
 * class for accessing property files and to filter properties.
 */
public class PropertiesUtil {

    /**
     * create property object from property file.
     *
     * @param props        the props
     * @param propertyFile the property file
     * @param isXML        the is XML
     * @return true, if create properties from file
     */
    public static boolean createPropertiesFromFile(final Properties props, final String propertyFile,
        final boolean isXML) {

        if (propertyFile == null)
            return false;
        if (props == null)
            return false;
        return PropertiesUtil.createProperties(props, new File(propertyFile), isXML);
    }

    /**
     * create property object from resource (search classpath using ClassLoader).
     *
     * @param props        the props
     * @param isXML        the is XML
     * @param resourceName the resource name
     * @return true, if create properties from resource
     */
    public static boolean createPropertiesFromResource(final Properties props, final String resourceName,
        final boolean isXML) {

        if (resourceName == null)
            return false;
        if (props == null)
            return false;
        return PropertiesUtil.createProperties(props, LocationUtil.getResourceUrl(resourceName, PropertiesUtil.class),
            isXML);
    }

    /**
     * create property object from resource (using ClassLoader).
     *
     * @param props         the props
     * @param resourceClass the resource class
     * @param isXML         the is XML
     * @param resourceName  the resource name
     * @return true, if create properties from resource
     */
    public static boolean createPropertiesFromResource(final Properties props, final String resourceName,
        final Class<?> resourceClass, final boolean isXML) {

        if (resourceName == null)
            return false;
        if (props == null)
            return false;
        return PropertiesUtil.createProperties(props,
            LocationUtil.getResourceUrl(resourceName, resourceClass == null ? PropertiesUtil.class : resourceClass), isXML);
    }

    /**
     * Creates property object from url.
     *
     * @param props the props
     * @param url   the url
     * @param isXML the is XML
     * @return true, if create properties
     */
    public static boolean createProperties(final Properties props, final URL url, final boolean isXML) {

        if (url == null)
            return false;
        if (props == null)
            return false;
        try {
            final InputStream is = url.openStream();
            if (isXML)
                props.loadFromXML(is);
            else
                props.load(is);
            is.close();
            return true;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * Creates property object from file.
     *
     * @param props the props
     * @param file  the file
     * @param isXML the is XML
     * @return true, if create properties
     */
    public static boolean createProperties(final Properties props, final File file, final boolean isXML) {

        if (file == null || !file.exists() || !file.isFile())
            return false;
        if (props == null)
            return false;
        try {
            final InputStream is = new FileInputStream(file);
            if (isXML)
                props.loadFromXML(is);
            else
                props.load(is);
            is.close();
            return true;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * save properties to file.
     *
     * @param fileName the file name
     * @param props    the props
     * @param isXML    the is XML
     * @return true, if save properties to file
     */
    public static boolean savePropertiesToFile(final Properties props, final String fileName, final boolean isXML) {

        if (fileName == null)
            return false;
        return PropertiesUtil.saveProperties(props, new File(fileName), isXML);
    }

    /**
     * save properties to resource (search classpath using ClassLoader).
     *
     * @param props        the props
     * @param isXML        the is XML
     * @param resourceName the resource name
     * @return true, if save properties to resource
     */
    public static boolean savePropertiesToResource(final Properties props, final String resourceName,
        final boolean isXML) {

        if (resourceName == null)
            return false;
        return PropertiesUtil.saveProperties(props, LocationUtil.getResourceUrl(resourceName, PropertiesUtil.class), isXML);
    }

    /**
     * save properties to resource (using ClassLoader).
     *
     * @param props         the props
     * @param resourceClass the resource class
     * @param isXML         the is XML
     * @param resourceName  the resource name
     * @return true, if save properties to resource
     */
    public static boolean savePropertiesToResource(final Properties props, final String resourceName,
        final Class<?> resourceClass, final boolean isXML) {

        if (resourceName == null)
            return false;
        return PropertiesUtil.saveProperties(props,
            LocationUtil.getResourceUrl(resourceName, resourceClass == null ? PropertiesUtil.class : resourceClass), isXML);
    }

    /**
     * save properties to file.
     *
     * @param props the props
     * @param file  the file
     * @param isXML the is XML
     * @return true, if save properties
     */
    public static boolean saveProperties(final Properties props, final File file, final boolean isXML) {

        if (file == null)
            return false;
        final File dir = file.getParentFile();
        if (!dir.exists())
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        if (!dir.exists())
            return false;
        try {
            final FileOutputStream fos = new FileOutputStream(file);
            if (isXML)
                props.storeToXML(fos, null);
            else
                props.store(fos, null);
            fos.close();
        } catch (final IOException e) {
            return false;
        }
        return true;
    }

    /**
     * save properties to url.
     *
     * @param props the props
     * @param url   the url
     * @param isXML the is XML
     * @return true, if save properties
     */
    public static boolean saveProperties(final Properties props, final URL url, final boolean isXML) {

        final File file = LocationUtil.toFile(url);
        if (file != null)
            return PropertiesUtil.saveProperties(props, file, isXML);
        return false;
    }

    /**
     * sort and print properties one per line ( key = value ).
     *
     * @param p the p
     * @return the string
     */
    public static String toString(final Properties p) {

        final StringBuilder buf = new StringBuilder();
        final String[] keys = PropertiesUtil.getSortedKeys(p);
        for (String element : keys)
            buf.append(element).append(" = ").append(p.getProperty(element)).append("\n");
        return buf.toString();
    }

    /**
     * Filters input properties for keys starting with prefix.
     *
     * @param prefix the prefix
     * @param in     the in
     * @return the properties
     */
    public static Properties filterOnPrefix(final String prefix, final Properties in) {

        final Properties out = new Properties();
        final Enumeration<Object> keys = in.keys();
        while (keys.hasMoreElements()) {
            final String key = (String) keys.nextElement();
            if (key.startsWith(prefix))
                out.put(key, in.getProperty(key));
        }
        return out;
    }

    /**
     * remove the prefix from all keys starting with prefix.
     *
     * @param prefix the prefix
     * @param in     the in
     * @return the properties
     */
    public static Properties removeKeyPrefix(final String prefix, final Properties in) {

        final Properties out = new Properties();
        final int prefixLen = prefix.length();
        final Enumeration<Object> keys = in.keys();
        while (keys.hasMoreElements()) {
            final String key = (String) keys.nextElement();
            if (key.startsWith(prefix))
                out.put(key.substring(prefixLen), in.getProperty(key));
            else
                out.put(key, in.getProperty(key));
        }
        return out;
    }

    /**
     * get property value as string.
     *
     * @param defaultValue the default value
     * @param propertyName the property name
     * @param properties   the properties
     * @return the property
     */
    public static String getProperty(final Properties properties, final String propertyName, final String defaultValue) {

        return properties.getProperty(propertyName, defaultValue);
    }

    /**
     * set string property value.
     *
     * @param value        the value
     * @param propertyName the property name
     * @param properties   the properties
     * @return true, if set property
     */
    public static boolean setProperty(final Properties properties, final String propertyName, final String value) {

        final String oldValue = PropertiesUtil.getProperty(properties, propertyName, null);
        if (value.equals(oldValue))
            return false;
        properties.setProperty(propertyName, value);
        return true;
    }

    /**
     * get property value and convert it to boolean.
     *
     * @param defaultValue the default value
     * @param propertyName the property name
     * @param properties   the properties
     * @return the property bool
     */
    public static boolean getPropertyBool(final Properties properties, final String propertyName,
        final boolean defaultValue) {

        final String v = PropertiesUtil.getProperty(properties, propertyName, null);
        if (v == null)
            return defaultValue;
        if (v.equalsIgnoreCase("true") || v.equals("1"))
            return true;
        if (v.equalsIgnoreCase("false") || v.equals("0"))
            return false;
        return defaultValue;
    }

    /**
     * set boolean property value.
     *
     * @param value        the value
     * @param propertyName the property name
     * @param properties   the properties
     * @return true, if set property bool
     */
    public static boolean setPropertyBool(final Properties properties, final String propertyName, final boolean value) {

        final String strValue = value ? "true" : "false";
        return PropertiesUtil.setProperty(properties, propertyName, strValue);
    }

    /**
     * get property value and convert it to int.
     *
     * @param defaultValue the default value
     * @param propertyName the property name
     * @param properties   the properties
     * @return the property integer
     */
    public static int getPropertyInteger(final Properties properties, final String propertyName, final int defaultValue) {

        final String v = PropertiesUtil.getProperty(properties, propertyName, null);
        if (v == null)
            return defaultValue;
        try {
            return Integer.parseInt(v);
        } catch (final Exception ignored) {
        }
        return defaultValue;
    }

    /**
     * set integer property value.
     *
     * @param value        the value
     * @param propertyName the property name
     * @param properties   the properties
     * @return true, if set property integer
     */
    public static boolean setPropertyInteger(final Properties properties, final String propertyName, final int value) {

        final String strValue = Integer.toString(value);
        return PropertiesUtil.setProperty(properties, propertyName, strValue);
    }

    /**
     * get property value and convert it to double.
     *
     * @param defaultValue the default value
     * @param propertyName the property name
     * @param properties   the properties
     * @return the property double
     */
    public static double getPropertyDouble(final Properties properties, final String propertyName,
        final double defaultValue) {

        final String v = PropertiesUtil.getProperty(properties, propertyName, null);
        if (v == null)
            return defaultValue;
        try {
            return Double.parseDouble(v);
        } catch (final Exception ignored) {
        }
        return defaultValue;
    }

    /**
     * set double property value.
     *
     * @param value        the value
     * @param propertyName the property name
     * @param properties   the properties
     * @return true, if set property double
     */
    public static boolean setPropertyDouble(final Properties properties, final String propertyName, final double value) {

        final String strValue = Double.toString(value);
        return PropertiesUtil.setProperty(properties, propertyName, strValue);
    }

    /**
     * Gets the sorted keys.
     *
     * @param properties the properties
     * @return the sorted keys
     */
    public static String[] getSortedKeys(final Properties properties) {

        final Map<Object, Object> map = new TreeMap<Object, Object>(properties);
        final Object[] keys = map.keySet().toArray();
        final String[] res = new String[keys.length];
        for (int i = 0; i < keys.length; i++)
            res[i] = keys[i].toString();
        return res;
    }
}
