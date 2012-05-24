/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Winde
 */
public class ListDirectoryContent {

    private File dir;
    private FileFilterExtensions filter;

    public ListDirectoryContent(String path) {
        dir = new File(path);
        filter = new FileFilterExtensions();
    }

    public File[] listFiles() {
        return dir.listFiles();
    }
    
    public File[] listExeFiles() {
        Collection<String> extensionLnk = new ArrayList<String>();
        extensionLnk.add("exe");
        return listFilteredFiles(extensionLnk);
    }

    public File[] listLnkFiles() {
        Collection<String> extensionLnk = new ArrayList<String>();
        extensionLnk.add("lnk");
        return listFilteredFiles(extensionLnk);
    }
    
    public void addExtensiontoFilter(String extension) {
        filter.addExtension(extension);
    }

    public File[] listFilteredFiles() {
        return dir.listFiles(filter);
    }
    
    public File[] listFilteredFiles(Collection<String> extensions) {
        FileFilterExtensions extensionsFilter = new FileFilterExtensions(extensions);
        return dir.listFiles(extensionsFilter);
    }

    public File[] listFilteredFiles(Collection<String> extensions, Collection<String> dontMatchThese) {
        File[] initialMatches = this.listFilteredFiles(extensions);
        List<File> initialMatchesCopy = Arrays.asList(initialMatches);
        for (File f : initialMatches) {
            if (dontMatchThese != null) {
                for (String dontMatchThis : dontMatchThese) {
                    if (dontMatchThis.equals(f.getName())) {
                        initialMatchesCopy.remove(f);
                    }
                }
            }
        }
        return (File[]) initialMatchesCopy.toArray();
    }

    public File[] listDirectories() {
        FileFilter directoryFilter = new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        return dir.listFiles(directoryFilter);
    }
}
