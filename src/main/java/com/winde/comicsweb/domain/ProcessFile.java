/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

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

    public static String getNameWithoutExtension(String filename) {
        if (!filename.contains(".")) {
            return null;
        }
        return filename.substring(0, filename.lastIndexOf("."));
    }

    public static String getExtension(String filename) {
        if (!filename.contains(".")) {
            return null;
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage imageFromBytes(byte[] bytes) {
        if (bytes != null) {
            InputStream in = new ByteArrayInputStream(bytes);
            ImageInputStream iis;
            try {
                iis = ImageIO.createImageInputStream(in);
            } catch (IOException ex) {
                return null;
            }
            try {
                BufferedImage bufferedImage = ImageIO.read(iis);
                return bufferedImage;
            } catch (IOException ex) {
                Logger.getLogger(ProcessFileRar.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

        } else {
            return null;
        }
    }

    public static BufferedImage imageFromBytesJpeg(byte[] bytes) {

        int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
        ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);

        Image image = Toolkit.getDefaultToolkit().createImage(bytes);
        PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);
        try {
            pg.grabPixels();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        int width = pg.getWidth(), height = pg.getHeight();
        DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
        BufferedImage bi;
        try {
            WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
            bi = new BufferedImage(RGB_OPAQUE, raster, false, null);
        } catch (IllegalArgumentException e) {
            return null;
        }

        return bi;
    }

    public static byte[] bytesFromImage(BufferedImage bufferedImage) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageOutputStream ios;
        try {
            ios = ImageIO.createImageOutputStream(out);
        } catch (IOException ex) {
            return null;
        }
        try {

            Iterator iter = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = (ImageWriter) iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();

            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            float quality = (float) 0.9;
            iwp.setCompressionQuality(quality);

            writer.setOutput(ios);
            if (bufferedImage != null) {
                writer.write(bufferedImage);
            } else {
                return null;
            }
            //ImageIO.write(bufferedImage, "jpg", out);            
            writer.dispose();
        } catch (IOException ex) {
            return null;
        }
        byte[] salida = out.toByteArray();
        try {
            out.close();



        } catch (IOException ex) {
            Logger.getLogger(ProcessFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salida;
    }

    public static ProcessFile createProcesFile(File fichero) {
        if (fichero == null) {
            return null;
        }
        String filename = fichero.getName();
        String extension = ProcessFile.getExtension(filename);
        if (extension != null) {
            extension = extension.toLowerCase();
        }
        ProcessFile procesador = null;
        if (bookExtensions.contains(extension)) {
            procesador = ProcessFileBook.createProcessFile(fichero);
            return procesador;
        }
        procesador = ProcessFileZip.createProcesFile(fichero);
        if (procesador != null) {
            return procesador;
        }
        procesador = ProcessFileRar.createProcesFile(fichero);
        if (procesador != null) {
            return procesador;
        }


        return null;
    }

    public abstract String getNextExtension();

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abstract BufferedImage getImageAt(int index);

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
