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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.imgscalr.Scalr;

/**
 *
 * @author Winde
 */
public class ProcessFilePdfBox extends ProcessFile {

    private PDDocument document = null;
    private File fichero;
    private int actual = 0;

    private ProcessFilePdfBox(File fichero, PDDocument document) {
        this.document = document;
        this.fichero = fichero;
    }

    public static ProcessFile createProcessFile(File fichero) {
        PDDocument document;
        try {
            document = PDDocument.load(fichero);
        } catch (IOException ex) {
            Logger.getLogger(ProcessFilePdfBox.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        if (document != null) {
            return new ProcessFilePdfBox(fichero, document);
        } else {
            return null;
        }
    }

    @Override
    public String getNextExtension() {
        return "jpg";
    }

    @Override
    public BufferedImage getImageAt(int index) {
        BufferedImage image = null;
        try {
            PDPage page = (PDPage) document.getDocumentCatalog().getAllPages().get(index);
            image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 200);
        } catch (IOException ex) {
            Logger.getLogger(ProcessFilePdfBox.class.getName()).log(Level.SEVERE, null, ex);
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
            System.out.println("PdfBOX could not render image at index " + index);
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
            try {
                document.close();
            } catch (IOException ex) {
                Logger.getLogger(ProcessFilePdfBox.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            PDPage page = (PDPage) document.getDocumentCatalog().getAllPages().get(index);
            image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 50);
        } catch (IOException ex) {
            Logger.getLogger(ProcessFilePdfBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        image = Scalr.resize(image,250);
        return image;
    }
}
