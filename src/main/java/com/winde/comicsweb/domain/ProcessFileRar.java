/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import java.awt.image.BufferedImage;
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
                if (!fh.isDirectory() && ProcessFile.imgExtensions.contains(getExtension(fh.getFileNameString().toLowerCase()))) {
                    ficheros.add(fh);
                }
                fh = a.nextFileHeader();
            }
            Collections.sort(ficheros);
        }
    }

    public static ProcessFile createProcesFile(File fichero) {
        Archive rarPointer = null;
        try {
            rarPointer = Archive.createArchive(fichero);
        } catch (RarException ex) {
            Logger.getLogger(ProcessFileRar.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(ProcessFileRar.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NullPointerException e) {
            System.out.println("ProcessFileRar: error");
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
            System.out.println("RarException " + ex.getMessage());
        }
        byte[] bytes = out.toByteArray();
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(ProcessFileRar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bytes;

    }

    @Override
    public String next() {
        if (ficheros != null) {
            if (!ficheros.isEmpty()) {
                FileHeader ficheroActual = ficheros.get(0);
                byte[] bytes = processFileHeader(ficheroActual);
                if (bytes != null) {
                    String salida = Base64.encodeToString(bytes, false);
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
            if ((ficheros.size() >= index + 1) && (index >= 0)) {

                FileHeader ficheroActual = ficheros.get(index);
                byte[] bytes = processFileHeader(ficheroActual);

                if (bytes != null) {
                    String salida = Base64.encodeToString(bytes, false);
                    return salida;
                } else {
                    System.out.println(fichero.getName() + " bytes nulos");
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public byte[] getImgBytesAt(int index) {
        if (ficheros != null) {
            if ((ficheros.size() >= index + 1) && (index >= 0)) {
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
    public BufferedImage getImageAt(int index) {
        if (ficheros != null) {
            if ((ficheros.size() >= index + 1) && (index >= 0)) {
                FileHeader ficheroActual = ficheros.get(index);
                byte[] bytes = processFileHeader(ficheroActual);
                String extension = this.getExtensionAt(index);
                if ("jpeg".equals(extension) || "jpg".equals(extension)) {
                    return ProcessFile.imageFromBytesJpeg(bytes);
                } else {
                    return ProcessFile.imageFromBytes(bytes);
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
            if (ficheros.size() >= index + 1) {
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
