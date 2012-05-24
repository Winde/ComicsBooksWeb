/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFImage;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPrintPage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 *
 * @author Winde
 */
public class ProcessFileBook extends ProcessFile {

    private File file;
    private PDFFile pdf;
//    private PDDocument pdfDocument;
    private PDFPrintPage pagesPrint;

    private ProcessFileBook(File file, PDFFile pdf/*
             * File file, PDDocument document
             */, long timeCreation) {
        /*
         *
         * this.pdfDocument = document; pages =
         * pdfDocument.getDocumentCatalog().getAllPages();
         *
         */
        this.pdf = pdf;
        System.out.println("Creating PDF Document List");
        this.file = file;
        System.out.println("Creating PDF Document List " + "DONE " + (new Date().getTime() - timeCreation));
    }

    public static ProcessFile createProcessFile(File fichero) {
        System.out.println("CREANDO");
        try {
            long time = new Date().getTime();
            System.out.println("Creating PDF Document " + fichero.getName());
            /*
             * PDDocument document = null; document = PDDocument.load(fichero);
             * document.getDocument(); System.out.println("Creating PDF Document
             * " + "DONE "+ (new Date().getTime() - time)); return new
             * ProcessFileBook(fichero, document, new Date().getTime());
             */
            RandomAccessFile raf = null;
            raf = new RandomAccessFile(fichero,
                    "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buffer = null;
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdf = new PDFFile(buffer);
            return new ProcessFileBook(fichero, pdf, time);

        } catch (IOException ex) {
            Logger.getLogger(ProcessFileBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getNextExtension() {
        return "jpg";
    }

    private byte[] getBytes(PDPage page, int index) {
        /*
         * BufferedImage bufferedImage = null; BufferedImage resizedImage; long
         * time;
         *
         * time = new Date().getTime(); System.out.println("Extracting image " +
         * index); try { //bufferedImage = page.convertToImage();
         *
         * bufferedImage = page.convertToImage(BufferedImage.TYPE_INT_RGB, 200);
         *
         * } catch (IOException ex) {
         * Logger.getLogger(ProcessFileBook.class.getName()).log(Level.SEVERE,
         * null, ex); } System.out.println("Extracting image " + index + " DONE
         * " + (new Date().getTime() - time)); time = new Date().getTime();
         * System.out.println("Resizing image " + index); //bufferedImage =
         * Scalr.resize(bufferedImage, 800, 1280); System.out.println("Resizing
         * image " + index + " DONE " + (new Date().getTime() - time));
         *
         * time = new Date().getTime(); System.out.println("Converting image to
         * Base64 " + index);
         *
         * System.out.println("Converting image to Base64 " + " DONE " + (new
         * Date().getTime() - time)); return salida;
         */
        return null;
    }

    private byte[] getBytes(BufferedImage bufferedImage) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(3000000);
        ImageOutputStream ios;
        try {
            ios = ImageIO.createImageOutputStream(out);
        } catch (IOException ex) {
            return null;
        }
        try {
            bufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, null);
            Iterator iter = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = (ImageWriter) iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();

            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            float quality = (float) 0.9;
            iwp.setCompressionQuality(quality);

            writer.setOutput(ios);
            writer.write(bufferedImage);
            //ImageIO.write(bufferedImage, "jpg", out);
        } catch (IOException ex) {
            return null;
        }
        byte[] salida = out.toByteArray();
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(ProcessFileBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salida;
    }

    private byte[] getBytes(PDFPrintPage pagesPrint, int index) {
        try {
            PDFPage page = pdf.getPage(index);
            System.out.println(page.getHeight());
            System.out.println(page.getWidth());
            BufferedImage bufferedImage = new BufferedImage(573, 789, BufferedImage.TYPE_INT_RGB);
            pagesPrint.print(bufferedImage.getGraphics(), new PageFormat(), index);
            return getBytes(bufferedImage);
        } catch (PrinterException ex) {
            Logger.getLogger(ProcessFileBook.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private byte[] getBytes(PDFPage page, int index) {
        Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());

        Image image = page.getImage(rect.width, rect.height, // width & height
                rect, // clip rect 
                null, // null for the ImageObserver
                true,// fill background with white
                true // block until drawing is done 
                );

        BufferedImageBuilder builder = new BufferedImageBuilder();
        BufferedImage bufferedImage = builder.bufferImage(image);
        return getBytes(bufferedImage);
    }

    @Override
    public String getImg64At(int index) {
        /*
         * if (index < this.getCount() && (index >= 0)) {
         *
         * PDFPage page = pdf.getPage(index); // get the width and height for
         * the doc at the default zoom Rectangle rect = new Rectangle(0, 0,
         * (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
         * // generate the image
         *
         * Image image = page.getImage(rect.width, rect.height, // width &
         * height rect, // clip rect null, // null for the ImageObserver true,
         * // fill background with white true // block until drawing is done );
         * System.out.println("WIDTH: " + image.getWidth(null));
         * System.out.println("HEIGHT: " + image.getHeight(null));
         *
         * // save it as a file BufferedImage bufferedImage =
         * toBufferedImage(image);
         *
         * long time = new Date().getTime(); System.out.println("Obtaining image
         * " + index); PDPage page = (PDPage) pages.get(index);
         * System.out.println("Obtaining image " + index + " DONE " + (new
         * Date().getTime() - time)); byte[] bytes = getBytes(page, index); if
         * (bytes != null) { return Base64.encode(bytes); } else { return null;
         * } } else { return null; }
         */
        byte[] bytes = getImgBytesAt(index);
        if (bytes != null) {
            return Base64.encode(bytes);
        } else {
            return null;
        }
    }

    @Override
    public byte[] getImgBytesAt(int index) {
        if (index < this.getCount() && (index >= 0)) {
            PDFPage page = pdf.getPage(index);
            //System.out.println("Obtaining image " + index + " DONE " + (new Date().getTime() - time));
            return getBytes(page, index);
        } else {
            return null;
        }
    }

    @Override
    public String getExtensionAt(int index) {
        if (index < this.getCount() && (index >= 0)) {
            return "jpg";
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        //return pdf.getNumPages();
        return pdf.getNumPages();
    }

    @Override
    public boolean hasNext() {
        /*f (pages != null) {
            return !pages.isEmpty();
        } else {
            return false;
        }*/return false;
    }

    @Override
    public String next() {
        /*PDFPage page = pages.get(0);
        pages.remove(0);
        byte[] bytes = getBytes(page, 0);
        if (bytes != null) {
            return Base64.encode(bytes);
        } else {
            return null;
        }*/return null;
    }

    @Override
    public String getFileName() {
        return file.getAbsolutePath();
    }

    @Override
    public void close() {
        /*
         * try { pdfDocument.close(); } catch (IOException ex) {
         * Logger.getLogger(ProcessFileBook.class.getName()).log(Level.SEVERE,
         * null, ex); }
         *
         */
    }
}
