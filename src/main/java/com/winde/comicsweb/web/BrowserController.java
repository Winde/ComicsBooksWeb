/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.web;

import com.winde.comicsweb.domain.Content;
import com.winde.comicsweb.domain.ListDirectoryContent;
import com.winde.comicsweb.domain.ProcessFile;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class BrowserController implements Controller {

    protected final Log logger = LogFactory.getLog(getClass());
    private String contentType;
    @Autowired
    private ResourceBundleMessageSource config;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ResourceBundleMessageSource getConfig() {
        return config;
    }

    public void setConfig(ResourceBundleMessageSource config) {
        this.config = config;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        if (contentType != null) {
            String path = "";

            if (contentType.equals("comics")) {
                path = config.getMessage("pathComics", null, Locale.getDefault());
            }
            if (contentType.equals("libros")) {
                path = config.getMessage("pathLibros", null, Locale.getDefault());
            }

            String ruta = request.getParameter("ruta");
            if (ruta != null) {
                path = path + "\\" + ruta;
            }
            ListDirectoryContent listaFicheros = new ListDirectoryContent(path);
            File[] directoriosArray = listaFicheros.listDirectories();
            List<String> directorios = new ArrayList<String>();
            for (File f : directoriosArray) {
                directorios.add(f.getName());
            }

            if (contentType.equals("comics")) {
                listaFicheros.addExtensiontoFilter("cbr");
                listaFicheros.addExtensiontoFilter("cbz");
            }
            if (contentType.equals("libros")) {
                listaFicheros.addExtensiontoFilter("pdf");
            }
            List<Content> ficheros = new ArrayList<Content>();
            
            
            File[] ficherosArray = listaFicheros.listFilteredFiles();
            listaFicheros.removeExtensionFilters();
            listaFicheros.addExtensiontoFilter("jpg");
            File[] thumbsArray = listaFicheros.listFilteredFiles();
            for (File f : ficherosArray) {
                ficheros.add(new Content(f.getName(), contentType));
                
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
            ModelAndView vista = new ModelAndView("browser", "model", myModel);
            return vista;
        }
        return null;
    }
}
