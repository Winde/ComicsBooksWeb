/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.web;

import com.winde.comicsweb.domain.*;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Winde
 */
@Controller
public class ChangePathController {

    private String contentType;
    @Autowired
    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
    private String htmExitRoute;
    private String pathAlias;
    private String basePath;

    public ChangePathController() {
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private String getParentDirectory(String ruta) {
        String parentDirectory = "";
        if (ruta != null) {
            int lastSlash = ruta.lastIndexOf("/");
            if (lastSlash <= 0) {
                parentDirectory = "";
            } else {
                parentDirectory = ruta.substring(0, lastSlash);
            }
        }
        return parentDirectory;
    }

    private String getContentName(String ruta) {
        String contentName = "";
        if (ruta != null) {
            int lastSlash = ruta.lastIndexOf("/");
            if (lastSlash <= 0) {
                if ((lastSlash == 0) && (ruta.length() > 1)) {
                    contentName = ruta.substring(lastSlash + 1);
                }
            } else {
                contentName = ruta.substring(lastSlash + 1);
            }
        }
        return contentName;
    }

    private String getPath(String ruta) {

        if (contentType != null) {
            htmExitRoute = "";
            if (contentType.equals("comics")) {
                pathAlias = "pathComics";
                htmExitRoute = "comics.htm";

            }
            if (contentType.equals("libros")) {
                pathAlias = "pathLibros";
                htmExitRoute = "libros.htm";
            }
            String path = config.getConfigValue(pathAlias);
            basePath = path;
            if (ruta != null) {
                if (!ruta.trim().equals("")) {
                    path = path + "\\" + ruta;
                }
            } else {
                //ERROR
            }
            return path;
        }
        return null;
    }

    private void setContentType(HttpServletRequest request) {
        String type = request.getParameter("type");
        if (type != null) {
            if (request.getParameter("type").contains("comic")) {
                contentType = "comics";
            } else if (request.getParameter("type").contains("libro")) {
                contentType = "libros";
            } else {
                contentType = null;
            }
        } else {
            if (request.getServletPath().contains("comic")) {
                contentType = "comics";
            } else if (request.getServletPath().contains("libro")) {
                contentType = "libros";
            } else {
                contentType = null;
            }
        }
    }

    private boolean isLastPageReadFunctionalityActivated() {
        String readFunctionality = config.getConfigValue("keepLastRead");
        if (readFunctionality != null) {
            return readFunctionality.equals("true");
        } else {
            return true;
        }
    }

    private ProcessFile getProcessFile(File fichero, HttpSession session) {
        ProcessFile procesadorArchivo = (ProcessFile) session.getAttribute("procesador");
        if (procesadorArchivo == null) {
            procesadorArchivo = ProcessFile.createProcesFile(fichero);
            System.out.println("Nuevo Procesador: Sesion es null - " + fichero.getName() + " " + procesadorArchivo.getClass());
        } else {
            if (!fichero.getAbsolutePath().equals(procesadorArchivo.getFileName())) {
                procesadorArchivo.close();
                procesadorArchivo = ProcessFile.createProcesFile(fichero);
                System.out.println("Nuevo Procesador: Procesador de diferente archivo - " + fichero.getName() + " " + procesadorArchivo.getClass());
            } else {
            }
        }
        if (procesadorArchivo == null) {
            System.out.println("PROCESADOR:" + procesadorArchivo);
        }
        return procesadorArchivo;
    }

    private File[] getFiles(String directory) {
        if (contentType == null) {
            return null;
        } else {
            ListDirectoryContent listador = new ListDirectoryContent(directory);
            String[] extensions = null;
            if (contentType.contains("comic")) {
                extensions = BrowserController.getComicExtensions();
            }
            if (contentType.contains("libro")) {
                extensions = BrowserController.getBookExtensions();
            }
            if (extensions != null) {
                for (String s : extensions) {
                    listador.addExtensiontoFilter(s);
                }
                return listador.listFilteredFiles();
            }
        }
        return null;
    }

    @RequestMapping(value = {"/changepath.htm"}, method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, Paths command, BindingResult bindingResult, HttpSession session) throws Exception {

        String path = getPath(request.getParameter("ruta"));
        String parentDirectoryFullPath = getParentDirectory(path);

        command.setPathComics(config.getConfigValue("pathComics"));
        command.setPathLibros(config.getConfigValue("pathLibros"));

        String parentDirectory = getParentDirectory(request.getParameter("ruta"));

        Map<String, Object> myModel = new HashMap<String, Object>();


        myModel.put("parentDirectory", parentDirectory);
        myModel.put("exit", htmExitRoute);
        myModel.put("command", command);
        myModel.put("contentType", contentType);

       
        ModelAndView vista = new ModelAndView("changepath", "model", myModel);
        return vista;
    }

    @RequestMapping(value = "/changepath.htm", method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Paths command, BindingResult bindingResult) throws Exception {        
        
        config.setConfigValue("pathComics",command.getPathComics());
        config.setConfigValue("pathLibros",command.getPathLibros());
        
        
        contentType = request.getParameter("type");
        if (contentType.contains("libros")) {
            htmExitRoute="/libros.htm";
        } else {
            htmExitRoute="/comics.htm";
        }
        if (request.getParameter("ruta") == null || "".equals(request.getParameter("ruta"))) {
            return new ModelAndView("redirect:" + htmExitRoute );

        }
        
        return new ModelAndView("redirect:" + htmExitRoute + "?ruta=" + URLEncoder.encode(request.getParameter("ruta"), "UTF-8") );
    }
}
