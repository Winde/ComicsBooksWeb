/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.web;

import com.winde.comicsweb.domain.Pagina;
import com.winde.comicsweb.domain.ProcessFile;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
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
public class ContentController {

    private String contentType;
    @Autowired
    private ResourceBundleMessageSource config;

    public ResourceBundleMessageSource getConfig() {
        return config;
    }

    public void setConfig(ResourceBundleMessageSource config) {
        this.config = config;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @RequestMapping(value = "/image.htm", method = RequestMethod.GET)
    public void getImage(HttpServletRequest request, final HttpServletResponse response, HttpSession session) {
        if (request.getParameter("type") != null) {

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
                String path = config.getMessage(pathAlias, null, Locale.getDefault());

                String ruta = request.getParameter("ruta");
                if (ruta != null) {
                    if (!ruta.trim().equals("")) {
                        path = path + "\\" + ruta;
                    }
                } else {
                    //ERROR
                }
                File fichero = new File(path);
                ProcessFile procesadorArchivo = (ProcessFile) session.getAttribute("procesador");
                if (procesadorArchivo == null) {
                    procesadorArchivo = ProcessFile.createProcesFile(fichero);
                    session.setAttribute("procesador", procesadorArchivo);
                    System.out.println("Nuevo Procesador: Sesion es null - " + fichero.getName());
                } else {
                    if (!fichero.getAbsolutePath().equals(procesadorArchivo.getFileName())) {
                        System.out.println("Fichero: " + fichero.getAbsolutePath());
                        System.out.println(procesadorArchivo.getFileName());
                        procesadorArchivo.close();
                        procesadorArchivo = ProcessFile.createProcesFile(fichero);
                        System.out.println("Nuevo Procesador: Procesador de diferente archivo - " + fichero.getName());
                    } else {
                        System.out.println("Reusando Procesador - " + fichero.getName());
                    }
                }
                if (procesadorArchivo == null) {
                    System.out.println("PROCESADOR:" + procesadorArchivo);
                }
                String pagina = request.getParameter("pagina");
                int paginaNum = 0;
                if (pagina != null) {
                    try {
                        paginaNum = Integer.parseInt(pagina);
                    } catch (NumberFormatException e) {
                        paginaNum = 0;
                    }
                }
                String extension = procesadorArchivo.getExtensionAt(paginaNum);
                if (extension != null) {
                    if (extension.equals("jpg")) {
                        extension = "jpeg";
                    }
                }
                byte imgBytes[] = procesadorArchivo.getImgBytesAt(paginaNum);

                try {

                    response.setContentType("image/" + extension);
                    response.setContentLength(imgBytes.length);
                    response.addHeader("Content-Disposition", "attachment; filename=\"" + fichero.getName() + "-" + paginaNum + "." + extension + '"');

                    response.getOutputStream().write(imgBytes);
                    response.getOutputStream().flush();
                } catch (IOException ex) {
                    Logger.getLogger(ContentController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    @RequestMapping(value = {"/comic.htm", "/libro.htm"}, method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, Pagina command, BindingResult bindingResult, HttpSession session) throws Exception {
        if (request.getServletPath().contains("comic")) {
            contentType = "comics";
        } else if (request.getServletPath().contains("libro")) {
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
            String path = config.getMessage(pathAlias, null, Locale.getDefault());

            String ruta = request.getParameter("ruta");
            if (ruta != null) {
                if (!ruta.trim().equals("")) {
                    path = path + "\\" + ruta;
                }
            } else {
                //ERROR
            }

            File fichero = new File(path);

            Map<String, Object> myModel = new HashMap<String, Object>();
            ProcessFile procesadorArchivo = (ProcessFile) session.getAttribute("procesador");
            if (procesadorArchivo == null) {
                procesadorArchivo = ProcessFile.createProcesFile(fichero);
                session.setAttribute("procesador", procesadorArchivo);
                System.out.println("Nuevo Procesador: Sesion es null - " + fichero.getName());
            } else {
                if (!fichero.getAbsolutePath().equals(procesadorArchivo.getFileName())) {
                    System.out.println("Fichero: " + fichero.getAbsolutePath());
                    System.out.println(procesadorArchivo.getFileName());
                    procesadorArchivo.close();
                    procesadorArchivo = ProcessFile.createProcesFile(fichero);
                    session.setAttribute("procesador", procesadorArchivo);
                    System.out.println("Nuevo Procesador: Procesador de diferente archivo - " + fichero.getName());
                } else {
                    System.out.println("Reusando Procesador - " + fichero.getName());
                }
            }
            if (procesadorArchivo == null) {
                System.out.println("PROCESADOR:" + procesadorArchivo);
                return null;
            }
            String pagina = request.getParameter("pagina");
            int paginaNum = 0;
            if (pagina != null) {
                try {
                    paginaNum = Integer.parseInt(pagina);
                } catch (NumberFormatException e) {
                    paginaNum = 0;
                }
            }
            command.setNumero(paginaNum + 1);
            String extension = procesadorArchivo.getExtensionAt(paginaNum);
            if (extension != null) {
                if (extension.equals("jpg")) {
                    extension = "jpeg";
                }
            }
            String imagen = procesadorArchivo.getImg64At(paginaNum);


            String parentDirectory = "";
            String comicName = "";
            if (ruta != null) {

                int lastSlash = ruta.lastIndexOf("/");
                if (lastSlash <= 0) {
                    parentDirectory = "";
                    if ((lastSlash == 0) && (ruta.length() > 1)) {
                        comicName = ruta.substring(lastSlash + 1);
                    }
                } else {

                    parentDirectory = ruta.substring(0, lastSlash);
                    comicName = ruta.substring(lastSlash + 1);
                }
            }


            if (procesadorArchivo != null) {
                myModel.put("extension", extension);
                myModel.put("imagen", imagen);
                myModel.put("parentDirectory", parentDirectory);
                myModel.put("contentName", comicName);
                myModel.put("exit", htmExitRoute);
                myModel.put("command", command);
                myModel.put("totalPages", procesadorArchivo.getCount());
                myModel.put("contentType", contentType);

            }

            if (request.getParameter("json") != null) {
                if (request.getParameter("json").equals("true")) {
                    return JsonView.Render(myModel, response);
                }
            }
            if ((imagen == null) || (paginaNum < 0)) {
                if (procesadorArchivo != null) {
                    procesadorArchivo.close();
                }
                session.setAttribute("procesador", null);
                return new ModelAndView("redirect:" + htmExitRoute + "?ruta=" + URLEncoder.encode(fichero.getParent().replace(config.getMessage(pathAlias, null, Locale.getDefault()), ""), "UTF-8"));
            }
            ModelAndView vista = new ModelAndView("content", "model", myModel);
            return vista;
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Pagina command, BindingResult bindingResult) throws Exception {
        System.out.println(request.getServletPath());
        for (String s : request.getParameterMap().keySet()) {
            System.out.println(s + " : " + request.getParameter(s));
        }

        return new ModelAndView("redirect:" + request.getServletPath() + "?ruta=" + URLEncoder.encode(request.getParameter("ruta"), "UTF-8") + "&pagina=" + (Integer.parseInt(request.getParameter("numero")) - 1));
    }
}
