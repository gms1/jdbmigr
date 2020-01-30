/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.io.File;

/**
 * The Class FileUtil.
 */
public class FileUtil {

    /**
     * Delete.
     *
     * @param in file to delete
     * @return true if file/directory is successfully deleted
     */
    public static boolean delete(File in) {

        try {
            if (in == null)
                return false;
            return in.delete();
        } catch (Exception e) {
        }
        return false;
    }
}
