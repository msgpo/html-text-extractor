package com.rekkit.wordextractor;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Package: PACKAGE_NAME
 * Made by Rekkit on 7/7/13 11:04 AM
 */
public class CsvFilter extends FileFilter{
    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = CsvUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(CsvUtils.csv)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "CSV (Comma-Separated Values) Files";
    }
}
