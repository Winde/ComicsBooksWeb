/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Winde
 */
public class ProcessFileRar extends ProcessFile {

    private File fichero;
    private Archive a = null;
    private List<FileHeader> ficheros;

    private ProcessFileRar(File fichero, Archive rarPointer) {
        this.fichero = fichero;
        this.a = rarPointer;
        ficheros = new ArrayList<FileHeader>();
        if (a != null) {
            FileHeader fh = a.nextFileHeader();
            while (fh != null) {
                if (!fh.isDirectory() && ProcessFile.imgExtensions.contains(getExtension(fh.getFileNameString()))) {
                    ficheros.add(fh);
                }
                fh = a.nextFileHeader();
            }
            Collections.sort(ficheros);
        }
    }

    public static ProcessFile createProcesFile(File fichero) {
        Archive rarPointer = null;
        System.out.println(fichero.getName());
        try {
            rarPointer = new Archive(fichero);
        } catch (RarException e) {
            System.out.println("No es un archivo rar");
            return null;
        } catch (IOException ex) {
            System.out.println("Excepcion IOException");
            return null;
        } catch (NullPointerException ex) {
            return null;
        }
        return new ProcessFileRar(fichero, rarPointer);
    }

    @Override
    public boolean hasNext() {
        if (ficheros != null) {
            if (!ficheros.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private byte[] processFileHeader(FileHeader ficheroActual) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            a.extractFile(ficheroActual, out);
        } catch (RarException ex) {
            System.out.println("RarException " +ex.getMessage());
        }
        byte[] bytes = out.toByteArray();
        return bytes;

    }

    @Override
    public String next() {
        if (ficheros != null) {
            if (!ficheros.isEmpty()) {
                FileHeader ficheroActual = ficheros.get(0);
                byte[] bytes = processFileHeader(ficheroActual);
                if (bytes != null) {
                    String salida = Base64.encode(bytes);
                    ficheros.remove(ficheroActual);
                    return salida;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public String getImg64At(int index) {
        if (ficheros != null) {
            if ((ficheros.size() > index + 1) && (index >= 0)) {
                FileHeader ficheroActual = ficheros.get(index);
                byte[] bytes = processFileHeader(ficheroActual);
                System.out.println(index + " " + bytes.length);
                if (bytes != null) {
                    String salida = Base64.encode(bytes);
                    return salida;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public byte[] getImgBytesAt(int index) {
        if (ficheros != null) {
            if ((ficheros.size() > index + 1) && (index >= 0)) {
                FileHeader ficheroActual = ficheros.get(index);
                byte[] bytes = processFileHeader(ficheroActual);
                if (bytes != null) {
                    return bytes;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public String getNextExtension() {
        if (!this.hasNext()) {
            return null;
        } else {
            return getExtension(ficheros.get(0).getFileNameString());
        }
    }

    @Override
    public String getExtensionAt(int index) {
        if ((ficheros != null) && (index >= 0)) {
            if (ficheros.size() > index + 1) {
                FileHeader ficheroActual = ficheros.get(index);
                return getExtension(ficheroActual.getFileNameString());
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return ficheros.size();
    }

    @Override
    public String getFileName() {
        return fichero.getAbsolutePath();
    }

    @Override
    public void close() {
    }
}
