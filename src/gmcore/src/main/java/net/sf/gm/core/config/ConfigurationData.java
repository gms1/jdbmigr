/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.config;

import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

//


/**
 * The Class ConfigurationData.
 */
public class ConfigurationData {

    /**
     * The name.
     */
    public final String name;

    /**
     * The child nodes.
     */
    public HashMap<String, ConfigurationData> childNodes;

    /**
     * The props.
     */
    public Properties props;

    /**
     * The Constructor.
     *
     * @param name the name
     */
    public ConfigurationData(final String name) {

        super();
        this.name = name;
        props = new Properties();
        childNodes = new HashMap<String, ConfigurationData>();
    }

    /**
     * Find node.
     *
     * @param nodeName the node name
     * @return the configuration data
     */
    public ConfigurationData findNode(final String nodeName) {

        String name = nodeName;
        if (name == null)
            return null;
        if (name.startsWith("/"))
            name = name.substring(1);
        if (name.length() == 0)
            return this;
        ConfigurationData actParent = this;
        ConfigurationData actChild = this;
        final StringTokenizer paths = new StringTokenizer(name, "/");
        while (paths.hasMoreTokens()) {
            final String childName = paths.nextToken();
            if (childName.length() == 0)
                continue;
            actChild = actParent.childNodes.get(childName);
            if (actChild == null)
                return null;
            actParent = actChild;
        }
        return actChild;
    }

    /**
     * Adds the node.
     *
     * @param nodeName the node name
     * @return the configuration data
     */
    public ConfigurationData addNode(final String nodeName) {

        String name = nodeName;
        if (name == null)
            return null;
        if (name.startsWith("/"))
            name = name.substring(1);
        if (name.length() == 0)
            return this;
        ConfigurationData actParent = this;
        ConfigurationData actChild = this;
        final StringTokenizer paths = new StringTokenizer(name, "/");
        while (paths.hasMoreTokens()) {
            final String childName = paths.nextToken();
            if (childName.length() == 0)
                continue;
            actChild = actParent.childNodes.get(childName);
            if (actChild == null) {
                actChild = new ConfigurationData(childName);
                actParent.childNodes.put(childName, actChild);
            }
            actParent = actChild;
        }
        return actChild;
    }

    /**
     * Find node properties.
     *
     * @param nodeName the node name
     * @return the properties
     */
    public Properties findNodeProperties(final String nodeName) {

        final ConfigurationData data = findNode(nodeName);
        if (data == null)
            return null;
        return data.props;
    }

    /**
     * Find node child nodes.
     *
     * @param nodeName the node name
     * @return the string[]
     */
    public String[] findNodeChildNodes(final String nodeName) {

        final ConfigurationData data = findNode(nodeName);
        if (data == null)
            return null;
        return data.childNodes.keySet().toArray(new String[0]);
    }

    /**
     * Adds the node proprties.
     *
     * @param nodeName the node name
     * @return the properties
     */
    protected Properties addNodeProprties(final String nodeName) { // insert node

        // if

        // node do not
        // exist
        final ConfigurationData data = addNode(nodeName);
        if (data == null)
            return null;
        return data.props;
    }
}
