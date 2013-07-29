package com.rekkit.wordextractor;

import java.io.File;

/**
 * Package: PACKAGE_NAME
 * Made by Rekkit on 7/7/13 11:07 AM
 */
public class CsvUtils {
    public final static String csv = "csv";

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

}
