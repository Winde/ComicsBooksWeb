/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.web;

import com.winde.comicsweb.domain.Config;
import com.winde.comicsweb.domain.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BrowserController {

    private static String readFileName = "read.xml";
    protected final Log logger = LogFactory.getLog(getClass());
    private String contentType;
    private String pathAlias;
    private String htmExitRoute;
    @Autowired
    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public BrowserController() {
        ImageIO.scanForPlugins();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public static String getReadFilename() {
        return readFileName;
    }

    private void setContentType(HttpServletRequest request) {

        if (request.getServletPath().contains("comic")) {
            contentType = "comics";
        } else if (request.getServletPath().contains("libro")) {
            contentType = "libros";
        } else {
            contentType = null;
        }
    }

    public static String[] getComicExtensions() {
        return new String[]{"cbr", "cbz"};
    }

    public static String[] getBookExtensions() {
        return new String[]{"pdf"};
    }

    @RequestMapping(value = {"/comics.htm", "/libros.htm"}, method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException, ParserConfigurationException {
        ProcessFile procesadorArchivo = (ProcessFile) session.getAttribute("procesador");
        if (procesadorArchivo != null) {
            procesadorArchivo.close();
            session.setAttribute("procesador", null);
        }

        String [] extensions = null;
        
        setContentType(request);
        if (contentType != null) {
            String path = "";

            if (contentType.equals("comics")) {
                path = config.getConfigValue("pathComics");
            }
            if (contentType.equals("libros")) {
                path = config.getConfigValue("pathLibros");
            }

            String ruta = request.getParameter("ruta");
            if (ruta != null) {
                path = path + "\\" + ruta;
            }
            ListDirectoryContent listaFicheros = new ListDirectoryContent(path);
            if (!listaFicheros.pathExists()) {
                Map<String, Object> myModel = new HashMap<String, Object>();

                myModel.put("parentDirectory", null);
                myModel.put("contentType", contentType);
                String readlastFunctionality = config.getConfigValue("keepLastRead");
                if (readlastFunctionality == null) {
                    readlastFunctionality = "true";
                }
                myModel.put("keepLastRead", readlastFunctionality);
                ModelAndView vista = new ModelAndView("browser", "model", myModel);
                return vista;
            }
            File[] directoriosArray = listaFicheros.listDirectories();
            List<String> directorios = new ArrayList<String>();
            for (File f : directoriosArray) {
                directorios.add(f.getName());
            }

            if (contentType.equals("comics")) {
                for (String s : getComicExtensions()) {
                    listaFicheros.addExtensiontoFilter(s);
                }
            }
            if (contentType.equals("libros")) {
                for (String s : getBookExtensions()) {
                    listaFicheros.addExtensiontoFilter(s);
                }
            }
            List<Content> ficheros = new ArrayList<Content>();

            boolean orderedByDate = false;
            if (ruta!=null && ruta.toLowerCase().contains("0-day")){
                orderedByDate=true;
            }
            
            File[] ficherosArray = listaFicheros.listFilteredFiles(orderedByDate);
            listaFicheros.removeExtensionFilters();
            listaFicheros.addExtensiontoFilter("jpg");
            File[] thumbsArray = listaFicheros.listFilteredFiles(orderedByDate);
            boolean directorioLeido = true;
            if (ficherosArray.length > 0) {
                File leidos = new File(path + "/" + readFileName);
                XMLContentRead readfile = XMLContentRead.createXMLContentRead(leidos.getAbsolutePath());
                if (readfile != null) {
                    for (File f : ficherosArray) {
                        Boolean readValue = readfile.getReadValue(f.getName());
                        Integer lastreadPage = readfile.getLastReadPage(f.getName());
                        if (readValue != null) {
                            if (!readValue.booleanValue()) {
                                directorioLeido = false;
                            }
                            ficheros.add(new Content(f.getName(), contentType, readValue.toString(), lastreadPage));
                        } else {
                            directorioLeido = false;
                            readfile.setFileNotRead(f.getName());
                            ficheros.add(new Content(f.getName(), contentType, Boolean.FALSE.toString(), lastreadPage));
                        }
                    }
                    readfile.flush();
                } else {
                    for (File f : ficherosArray) {
                        ficheros.add(new Content(f.getName(), contentType, Boolean.FALSE.toString(), null));
                    }
                }
            }

            String parentDirectory = "";
            if (ruta != null) {

                int lastSlash = ruta.lastIndexOf("/");
                if (lastSlash <= 0) {
                    parentDirectory = "";
                } else {

                    parentDirectory = ruta.substring(0, lastSlash);
                }
            }
            Map<String, Object> myModel = new HashMap<String, Object>();
            myModel.put("directorios", directorios);
            myModel.put("ficheros", ficheros);
            myModel.put("parentDirectory", parentDirectory);
            myModel.put("contentType", contentType);
            String readlastFunctionality = config.getConfigValue("keepLastRead");
            if (readlastFunctionality == null) {
                readlastFunctionality = "true";
            }
            myModel.put("keepLastRead", readlastFunctionality);
            ModelAndView vista = new ModelAndView("browser", "model", myModel);
            return vista;
        }
        return null;
    }
}
