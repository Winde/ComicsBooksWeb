/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.web;

import com.winde.comicsweb.domain.Config;
import com.winde.comicsweb.domain.ListDirectoryContent;
import com.winde.comicsweb.domain.ProcessFile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Winde
 */
@Controller
public class ThumbsController {

    @Autowired
    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public ThumbsController() {
           
    }

    private byte[] createThumb(String pathFile, String pathThumb) {
        File fichero = new File(pathFile);
        if (fichero.isDirectory()) {
            return null;
        }
        ProcessFile procesador = ProcessFile.createProcesFile(fichero);
        if (procesador == null) {
            return null;
        }
        BufferedImage image = procesador.getThumbAt(0, 256);
        procesador.close();
        if (image == null) {
            return null;
        }
        return ProcessFile.bytesFromImage(image);
    }

    @RequestMapping(value = "/thumb.htm", method = RequestMethod.GET)
    public void getImage(HttpServletRequest request, final HttpServletResponse response, HttpSession session) {
        if ((request.getParameter("ruta") == null) || (request.getParameter("type") == null)) {
        } else {
            String contentType = null;
            if (request.getParameter("type").contains("comic")) {
                contentType = "comics";
            } else if (request.getParameter("type").contains("libro")) {
                contentType = "libros";
            } else {
                contentType = null;
            }
            if (contentType != null) {
                String pathAlias = "";
                String htmExitRoute = "";
                if (contentType.equals("comics")) {
                    pathAlias = "pathComics";
                    htmExitRoute = "comics.htm";

                }
                if (contentType.equals("libros")) {
                    pathAlias = "pathLibros";
                    htmExitRoute = "libros.htm";
                }
                String path = config.getConfigValue(pathAlias);

                String ruta = request.getParameter("ruta");
                if (ruta != null) {
                    if (!ruta.trim().equals("")) {
                        path = path + "\\" + ruta;
                    }
                } else {
                    //ERROR
                }
                String pathWithoutExtension = "";
                if (new File(path).isDirectory()) {
                    ListDirectoryContent listar = new ListDirectoryContent(path);
                    listar.addExtensiontoFilter("jpg");
                    File[] thumbs = listar.listFilteredFiles(false);
                    if (thumbs != null) {
                        if (thumbs.length >= 1) {
                            pathWithoutExtension = ProcessFile.getNameWithoutExtension(thumbs[0].getAbsolutePath());
                        }
                    }
                } else {
                    pathWithoutExtension = ProcessFile.getNameWithoutExtension(path);
                }
                if (pathWithoutExtension != null) {
                    String pathThumbnail = pathWithoutExtension + ".jpg";
                    File fichero = new File(pathThumbnail);
                    byte[] bytes = null;
                    if (fichero.exists()) {

                        FileInputStream fin;

                        try {
                            fin = new FileInputStream(fichero);
                            bytes = new byte[(int) fichero.length()];
                            fin.read(bytes);
                            fin.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ThumbsController.class.getName()).log(Level.SEVERE, null, ex);

                        }

                    } else {

                        bytes = createThumb(path, pathThumbnail);
                        if (bytes != null) {
                            FileOutputStream fout;
                            try {
                                fout = new FileOutputStream(fichero);
                                fout.write(bytes);
                                fout.close();
                            } catch (IOException ex) {
                                Logger.getLogger(ThumbsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    if (bytes != null) {
                        try {

                            response.setContentType("image/" + "jpeg");
                            response.setContentLength(bytes.length);
                            response.addHeader("Content-Disposition", "attachment; filename=\"" + pathThumbnail + '"');
                            response.getOutputStream().write(bytes);
                            response.getOutputStream().flush();
                        } catch (IOException ex) {
                            Logger.getLogger(ContentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        response.setContentType("image/" + "jpg");
                        response.setContentLength(0);
                        response.addHeader("Content-Disposition", "attachment; filename=\"" + pathThumbnail + '"');
                    }
                }
            }

        }
    }
}
