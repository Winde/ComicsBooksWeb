/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Winde
 */
public class ProcessFileZip extends ProcessFile {

    ZipFile zipPointer;
    File fichero;
    List<ZipEntryComparable> entriesList;

    private ProcessFileZip(File fichero, ZipFile zipPointer) {
        this.zipPointer = zipPointer;
        this.fichero = fichero;
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipPointer.entries();
        this.entriesList = new ArrayList<ZipEntryComparable>();
        ZipEntry temp = null;
        while (entries.hasMoreElements()) {
            temp = entries.nextElement();
            if (getExtension(temp.getName()) != null) {
                if (ProcessFile.imgExtensions.contains(getExtension(temp.getName().toLowerCase()))) {
                    entriesList.add(new ZipEntryComparable(temp));
                }
            }
        }
        Collections.sort(entriesList);
    }

    public static ProcessFile createProcesFile(File fichero) {
        ZipFile zipPointer;
        try {
            zipPointer = new ZipFile(fichero);
        } catch (ZipException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
        return new ProcessFileZip(fichero, zipPointer);
    }

    @Override
    public boolean hasNext() {
        return !entriesList.isEmpty();
    }

    private byte[] processZipEntry(ZipEntry zipEntry) {
        InputStream streamData;
        try {
            streamData = zipPointer.getInputStream(zipEntry);
        } catch (IOException ex) {
            return null;
        }
        byte[] streamBytes;
        try {
            streamBytes = IOUtils.toByteArray(streamData);
            streamData.close();
        } catch (IOException ex) {
            return null;
        }
        return streamBytes;
    }

    @Override
    public String next() {
        if (this.hasNext()) {
            ZipEntry temp = entriesList.get(0).getZipEntry();
            byte[] bytes = processZipEntry(temp);
            if (bytes != null) {
                return Base64.encodeToString(bytes, false);
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getNextExtension() {
        if (this.hasNext()) {
            ZipEntry temp = entriesList.get(0).getZipEntry();
            return getExtension(temp.getName());
        } else {
            return null;
        }
    }

    @Override
    public String getImg64At(int index) {
        if ((entriesList != null) && (index >= 0)) {
            if (entriesList.size() >= index + 1) {
                byte[] bytes = processZipEntry(entriesList.get(index).getZipEntry());
                if (bytes != null) {
                    return Base64.encodeToString(bytes, false);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public byte[] getImgBytesAt(int index) {
        if ((entriesList != null) && (index >= 0)) {
            if (entriesList.size() >= index + 1) {
                byte[] bytes = processZipEntry(entriesList.get(index).getZipEntry());
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
    public String getExtensionAt(int index) {
        if ((entriesList != null) && (index >= 0)) {
            if (entriesList.size() >= index + 1) {
                return getExtension(entriesList.get(index).getZipEntry().getName());
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return entriesList.size();
    }

    @Override
    public String getFileName() {
        return fichero.getAbsolutePath();
    }

    @Override
    public void close() {
        try {
            zipPointer.close();
        } catch (IOException ex) {
            Logger.getLogger(ProcessFileZip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public BufferedImage getImageAt(int index) {
        if ((entriesList != null) && (index >= 0)) {
            if (entriesList.size() >= index + 1) {
                byte[] bytes = processZipEntry(entriesList.get(index).getZipEntry());
                String extension = this.getExtensionAt(index);
               // if ("jpeg".equals(extension) || "jpg".equals(extension)) {
                    return ProcessFile.imageFromBytesAlternate(bytes);
               // } else {
               //     return ProcessFile.imageFromBytes(bytes);
               // }
            }
        }
        return null;
    }
}
