/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFParseException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imgscalr.Scalr;

/**
 *
 * @author Winde
 */
public class ProcessFilePdfSun extends ProcessFile {

    private File file;
    private PDFFile pdf;
    int index = 0;

    private ProcessFilePdfSun(File file, PDFFile pdf, long timeCreation) {
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
            RandomAccessFile raf = null;
            raf = new RandomAccessFile(fichero,
                    "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buffer = null;
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdf = null;
            try {
                pdf = new PDFFile(buffer);
            } catch (PDFParseException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
            channel.close();
            raf.close();
            return new ProcessFilePdfSun(fichero, pdf, time);

        } catch (IOException ex) {
            Logger.getLogger(ProcessFilePdfSun.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getNextExtension() {
        return "jpg";
    }

    private byte[] getBytes(BufferedImage bufferedImage) {
        return ProcessFile.bytesFromImage(bufferedImage);
    }

    private BufferedImage getImageAt(PDFPage page, int index) {
        if (page == null) {
            return null;
        }
        Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
        Image image = null;
        try {
            image = page.getImage(rect.width, rect.height, // width & height
                    rect, // clip rect 
                    null, // null for the ImageObserver
                    true,// fill background with white
                    true // block until drawing is done 
                    );
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
            return null;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        BufferedImageBuilder builder = new BufferedImageBuilder();
        BufferedImage bufferedImage = builder.bufferImage(image);
        return bufferedImage;
    }

    private byte[] getBytes(PDFPage page, int index) {
        return getBytes(getImageAt(page, index));
    }

    @Override
    public String getImg64At(int index) {
        byte[] bytes = getImgBytesAt(index);
        if (bytes != null) {
            return Base64.encodeToString(bytes, false);
        } else {
            return null;
        }
    }

    @Override
    public BufferedImage getImageAt(int index) {
        if (index < this.getCount() && (index >= 0)) {
            PDFPage page = pdf.getPage(index);
            return getImageAt(page, index);
        } else {
            return null;
        }
    }

    @Override
    public byte[] getImgBytesAt(int index) {
        if (index < this.getCount() && (index >= 0)) {
            PDFPage page = pdf.getPage(index);
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
        return pdf.getNumPages();
    }

    @Override
    public boolean hasNext() {
        return index < this.getCount();
    }

    @Override
    public String next() {
        if (this.hasNext()) {
            String salida = getImg64At(index);
            index = index + 1;
            return salida;
        } else {
            return null;
        }
    }

    @Override
    public String getFileName() {
        return file.getAbsolutePath();
    }

    @Override
    public void close() {
    }
}
