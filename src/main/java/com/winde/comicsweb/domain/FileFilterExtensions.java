/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Winde
 */
public class FileFilterExtensions implements FileFilter {

    private Collection<String> extensions = null;

    public FileFilterExtensions() {
        this.extensions = new HashSet<String>();
    } 
           
    
    public FileFilterExtensions(Collection<String> extensions) {
        this.extensions = extensions;
    }

    public void addExtension(String extension){
        extensions.add(extension);
    }
    
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return false;
        } else if (f.isFile()) {
            for (String extension : extensions) {
                if (f.getName().toLowerCase().endsWith("." + extension.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void empty() {
        extensions.clear();
    }

}
