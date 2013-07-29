package com.rekkit.wordextractor;

import java.io.File;

/**
 * Package: PACKAGE_NAME
 * Made by Rekkit on 7/7/13 12:21 PM
 */
public class HtmlUtils
{
    public final static String html = "html";
    public final static String htm = "htm";

    /*
     * Get the extension of a file.
     */
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
