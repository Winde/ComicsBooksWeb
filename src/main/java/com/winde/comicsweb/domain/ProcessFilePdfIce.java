/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.imgscalr.Scalr;

/**
 *
 * @author Winde
 */
public class ProcessFilePdfIce extends ProcessFile {

    private Document document = null;
    private File fichero;
    private int actual = 0;

    private ProcessFilePdfIce(File fichero, Document document) {
        this.document = document;
        this.fichero = fichero;
    }

    public static ProcessFile createProcessFile(File fichero) {
        Defs.setSystemProperty("org.icepdf.core.scaleImages", "false");
        //Defs.setSystemProperty("org.icepdf.core.awtFontLoading", "true");
        Document document = new Document();
        try {
            document.setFile(fichero.getAbsolutePath());
        }/* catch (PDFException ex) {
            Logger.getLogger(ProcessFilePdfIce.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (PDFSecurityException ex) {
            Logger.getLogger(ProcessFilePdfIce.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(ProcessFilePdfIce.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }*/catch (Exception ex) {
            Logger.getLogger(ProcessFilePdfIce.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ProcessFilePdfIce(fichero, document);
    }

    @Override
    public String getNextExtension() {
        return "jpg";
    }

    @Override
    public BufferedImage getImageAt(int index) {
        BufferedImage image = null;
        try {
            image = (BufferedImage) document.getPageImage(index, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0f, 2.0f);
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(ProcessFilePdfIce.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return image;
    }

    @Override
    public String getImg64At(int index) {
        byte[] bytes = getImgBytesAt(index);
        if (bytes == null) {
            return null;
        } else {
            return Base64.encodeToString(bytes, false);
        }
    }

    @Override
    public byte[] getImgBytesAt(int index) {
        BufferedImage image = getImageAt(index);
        if (image == null) {
            System.out.println("ICEPdf could not render image at index "+index);    
            return null;
        } else {
            return ProcessFile.bytesFromImage(image);
        }

    }

    @Override
    public String getExtensionAt(int index) {
        return "jpg";
    }

    @Override
    public int getCount() {
        return document.getNumberOfPages();
    }

    @Override
    public String getFileName() {
        return fichero.getName();
    }

    @Override
    public void close() {
        System.out.println("Procesador de fichero: " + fichero.getName() + " liberado");
        if (document != null) {
            document.dispose();
            document = null;
        }
    }

    @Override
    public boolean hasNext() {
        return (actual < document.getNumberOfPages());
    }

    @Override
    public String next() {
        String salida = getImg64At(actual);
        actual = actual + 1;
        return salida;
    }

    @Override
    public BufferedImage getThumbAt(int index, int dimension) {
        BufferedImage image = null;
        try {
            image = (BufferedImage) document.getPageImage(index, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX, 0f, 1.0f);
        } catch (NullPointerException ex) {
            return null;
        }
        if (image == null) {
            return null;
        } else {
            image = Scalr.resize(image, dimension);
            return image;
        }
    }
}
