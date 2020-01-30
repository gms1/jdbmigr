/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.datasource.config;

import net.sf.gm.core.properties.AppProperties;
import net.sf.gm.core.utils.DynamicClassLoader;
import net.sf.gm.jdbc.common.JdbcException;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import java.sql.Driver;

//


/**
 * The Class DriverClassInfo.
 */
public class DriverClassInfo {

    /**
     * The name.
     */
    private String name;

    /**
     * The class name.
     */
    private String className;

    /**
     * The class path.
     */
    private String classPath;

    /**
     * The class type.
     */
    private String classType;

    /**
     * The description.
     */
    private String description;

    /**
     * The url template.
     */
    private String urlTemplate;

    /**
     * The class instance.
     */
    private Object classInstance;

    /**
     * The Constructor.
     *
     * @param description the description
     * @param urlTemplate the url template
     * @param className   the class name
     * @param classType   the class type
     * @param name        the name
     * @param classPath   the class path
     */
    public DriverClassInfo(final String name, final String className,
        final String classPath, final String classType,
        final String urlTemplate, final String description) {

        this.name = name;
        this.className = className;
        this.classPath = classPath;
        this.classType = classType;
        this.urlTemplate = urlTemplate;
        this.description = description;
        this.classInstance = null;
    }

    /**
     * The Constructor.
     */
    public DriverClassInfo() {

        this.name = null;
        this.className = null;
        this.classPath = null;
        this.classType = null;
        this.urlTemplate = null;
        this.description = null;
        this.classInstance = null;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Gets the class path.
     *
     * @return the class path
     */
    public String getClassPath() {
        return classPath;
    }

    /**
     * Gets the class type.
     *
     * @return the class type
     */
    public String getClassType() {
        return classType;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the url template.
     *
     * @return the url template
     */
    public String getUrlTemplate() {
        return urlTemplate;
    }

    /**
     * Sets the class name.
     *
     * @param className the class name
     */
    public void setClassName(final String className) {

        this.className = className;
    }

    /**
     * Sets the class path.
     *
     * @param classPath the class path
     */
    public void setClassPath(final String classPath) {

        this.classPath = classPath;
    }

    /**
     * Sets the class type.
     *
     * @param classType the class type
     */
    public void setClassType(final String classType) {

        this.classType = classType;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(final String description) {

        this.description = description;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the url template.
     *
     * @param urlTemplate the url template
     */
    public void setUrlTemplate(final String urlTemplate) {

        this.urlTemplate = urlTemplate;
    }

    /**
     * Removes the class instance.
     */
    public void removeClassInstance() {
        classInstance = null;
    }

    /**
     * Gets the class instance.
     *
     * @return the class instance
     * @throws JdbcException the jdbc exception
     */
    public Object getClassInstance() throws JdbcException {

        if (classInstance != null)
            return classInstance;
        classInstance = getNewClassInstance();
        return classInstance;
    }

    /**
     * Gets the new class instance.
     *
     * @return the new class instance
     * @throws JdbcException the jdbc exception
     */
    public Object getNewClassInstance() throws JdbcException {

        final DynamicClassLoader dcl = new DynamicClassLoader();
        Object res;

        String classPath = null;
        if (this.classPath != null)
            classPath = AppProperties.expandApplicationProperties(this.classPath);

        String className = null;
        if (this.className != null)
            className = AppProperties.expandApplicationProperties(this.className);

        String classType = null;
        if (this.classType != null)
            classType = AppProperties.expandApplicationProperties(this.classType);

        if (classPath != null && classPath.length() > 0)
            dcl.addClassPath(classPath);
        try {
            if (className == null)
                throw new JdbcException("className is NULL");
            final Object obj = dcl.loadClass(className);
            if (classType == null)
                throw new JdbcException("interface for class \"" + className +
                    "\" not specified");
            else if (classType.equals("Driver")) {
                if (!Driver.class.isInstance(obj))
                    throw new JdbcException(
                        "the class \"" + className +
                            "\" is not an implementation of the Driver interface");
            } else if (classType.equals("DataSource")) {
                if (!DataSource.class.isInstance(obj))
                    throw new JdbcException(
                        "the class \"" + className +
                            "\" is not an implementation of the DataSource interface");
            } else if (classType.equals("ConnectionPoolDataSource")) {
                if (!ConnectionPoolDataSource.class.isInstance(obj))
                    throw new JdbcException(
                        "the class \"" + className +
                            "\" is not an implementation of the ConnectionPoolDataSource interface");
            } else
                throw new JdbcException("unknown interface type \"" + classType +
                    "\" of class \"" + className + "\"");
            res = obj;

        } catch (final Exception e) {
            throw new JdbcException(e);
        }
        return res;
    }

    /**
     * Clone.
     *
     * @return the object
     */
    @Override
    public Object clone() {

        return new DriverClassInfo(name, className, classPath, classType,
            urlTemplate, description);
    }
}
