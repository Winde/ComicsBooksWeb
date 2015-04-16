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
import java.util.Comparator;
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

    public boolean pathExists() {
        return (dir.exists() && dir.isDirectory());
    }
    
    public File[] listFiles() {
        return dir.listFiles();
    }
    
    public File[] listExeFiles(boolean orderedByDate) {
        Collection<String> extensionLnk = new ArrayList<String>();
        extensionLnk.add("exe");
        return listFilteredFiles(extensionLnk,orderedByDate);
    }

    public File[] listLnkFiles(boolean orderedByDate) {
        Collection<String> extensionLnk = new ArrayList<String>();
        extensionLnk.add("lnk");
        return listFilteredFiles(extensionLnk,orderedByDate);
    }
    
    public void addExtensiontoFilter(String extension) {
        filter.addExtension(extension);
    }

    public File[] listFilteredFiles(boolean orderedByDate) {
        File [] files = dir.listFiles(filter);
        if (orderedByDate){
        Arrays.sort(files, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                int value = Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                if (value == 0 && !f1.equals(f2)){
                    return 1;
                }
                return -1*value;
            } });
        }
        return files;
    }
    
    public File[] listFilteredFiles(Collection<String> extensions, boolean orderedByDate) {
        FileFilterExtensions extensionsFilter = new FileFilterExtensions(extensions);
        File [] files = dir.listFiles(extensionsFilter);
        if (orderedByDate){
        Arrays.sort(files, new Comparator<File>(){
            public int compare(File f1, File f2)
            {              
                int value = Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                if (value == 0 && !f1.equals(f2)){
                    return 1;
                }
                return -1*value;
            } });
        }
        return files;
    }

    public File[] listFilteredFiles(Collection<String> extensions, Collection<String> dontMatchThese, boolean orderedByDate) {
        File[] initialMatches = this.listFilteredFiles(extensions,orderedByDate);
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
    
    public void removeExtensionFilters() {
        filter.empty();
    }
}
