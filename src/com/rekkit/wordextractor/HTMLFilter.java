package com.rekkit.wordextractor;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Package: PACKAGE_NAME
 * Made by Rekkit on 7/7/13 12:18 PM
 */
public class HTMLFilter extends FileFilter
{
    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = HtmlUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(HtmlUtils.html) || extension.equals(HtmlUtils.htm)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "HTML Files";
    }
}
