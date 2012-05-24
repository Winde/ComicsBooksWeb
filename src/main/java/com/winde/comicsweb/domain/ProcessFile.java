/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Winde
 */
public abstract class ProcessFile implements Iterator<String> {

    public static List rarExtensions = new ArrayList() {

        {
            add("cbr");
            add("rar");
        }
    };
    public static List zipExtensions = new ArrayList() {

        {
            add("cbz");
            add("zip");
        }
    };
    public static List imgExtensions = new ArrayList() {

        {
            add("jpg");
            add("png");
            add("gif");
        }
    };
    public static List bookExtensions = new ArrayList() {

        {
            add("pdf");
        }
    };

    public static String getExtension(String filename) {
        if (!filename.contains(".")) {
            return null;
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public static ProcessFile createProcesFile(File fichero) {
        if (fichero == null) {
            return null;
        }
        String filename = fichero.getName();
        String extension = ProcessFile.getExtension(filename);
        ProcessFile procesador = null;
        if (rarExtensions.contains(extension)) {
            procesador = ProcessFileRar.createProcesFile(fichero);
            if (procesador == null) {
                return ProcessFileZip.createProcesFile(fichero);
            } else {
                return procesador;
            }
        }
        if (zipExtensions.contains(extension)) {
            procesador = ProcessFileZip.createProcesFile(fichero);
            if (procesador == null) {
                return ProcessFileRar.createProcesFile(fichero);
            } else {
                return procesador;
            }
        }
        if (bookExtensions.contains(extension)) {
            procesador = ProcessFileBook.createProcessFile(fichero);
            return procesador;
        }
        return null;
    }

    public abstract String getNextExtension();

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abstract String getImg64At(int index);

    public abstract byte[] getImgBytesAt(int index);
    
    public abstract String getExtensionAt(int index);

    public abstract int getCount();

    public abstract String getFileName();

    public abstract void close();

    @Override
    public int hashCode() {
        return this.getFileName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof ProcessFile) {
            ProcessFile temp = (ProcessFile) o;
            return temp.getFileName().equals(this.getFileName());
        } else {
            return false;
        }
    }
}
