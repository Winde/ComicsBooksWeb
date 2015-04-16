/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.web;

import com.winde.comicsweb.domain.Config;
import com.winde.comicsweb.domain.ListDirectoryContent;
import com.winde.comicsweb.domain.ProcessFile;
import com.winde.comicsweb.domain.XMLContentRead;
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
public class ContentController {

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

    public ContentController() {
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
                return listador.listFilteredFiles(false);
            }
        }
        return null;
    }

    @RequestMapping(value = {"/lastreadfunction.htm"}, method = RequestMethod.GET)
    public ModelAndView setReadFunction(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException, ParserConfigurationException {
        String ruta = request.getParameter("ruta");
        setContentType(request);
        if (contentType != null) {
            String path = getPath(ruta);
            String lastread = request.getParameter("lastread");
            if (lastread != null) {
                if (lastread.equals("true") || lastread.equals("false")) {
                    config.setConfigValue("keepLastRead", lastread);
                }
                if (lastread.equals("wipe")) {
                    File fichero = new File(path);
                    if (fichero.isDirectory()) {
                        File ficheroXML = new File(path + "/" + BrowserController.getReadFilename());
                        if (ficheroXML.exists()) {
                            XMLContentRead readfileXML = XMLContentRead.createXMLContentRead(ficheroXML.getAbsolutePath());
                            if (readfileXML != null) {
                                File[] files = getFiles(path);
                                for (File file : files) {
                                    readfileXML.setFileLastPage(file.getName(), 0);
                                    readfileXML.flush();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (ruta != null) {
            return new ModelAndView("redirect:" + htmExitRoute + "?ruta=" + URLEncoder.encode(ruta, "UTF-8"));
        } else {
            return new ModelAndView("redirect:" + htmExitRoute);
        }
    }

    @RequestMapping(value = {"readpage.htm"}, method = RequestMethod.GET)
    public void setReadPage(HttpServletRequest request, HttpServletResponse response, Pagina command, BindingResult bindingResult, HttpSession session) throws Exception {
        if (isLastPageReadFunctionalityActivated()) {
            String page = request.getParameter("pagina");
            if (page != null) {
                Integer pageNum = null;
                try {
                    pageNum = Integer.parseInt(page);
                } catch (NumberFormatException e) {
                }
                if (pageNum != null) {
                    setContentType(request);
                    String path = getPath(request.getParameter("ruta"));
                    String parentDirectory = getParentDirectory(request.getParameter("ruta"));
                    String parentDirectoryFullPath = getParentDirectory(path);
                    String comicName = getContentName(path);
                    XMLContentRead readfileXML = XMLContentRead.createXMLContentRead(parentDirectoryFullPath + "/" + BrowserController.getReadFilename());
                    if (readfileXML != null) {
                        Integer lastReadPage = readfileXML.getLastReadPage(comicName);
                        if (lastReadPage == null) {
                            readfileXML.setFileLastPage(comicName, pageNum);
                            readfileXML.flush();
                        } else {
                            readfileXML.setFileLastPage(comicName, pageNum);
                            readfileXML.flush();
                        }
                    }
                }

            }
        }
    }

    @RequestMapping(value = {"/read.htm"}, method = RequestMethod.GET)
    public ModelAndView setRead(HttpServletRequest request, HttpServletResponse response, Pagina command, BindingResult bindingResult, HttpSession session) throws Exception {

        setContentType(request);
        String path = getPath(request.getParameter("ruta"));
        String parentDirectory = getParentDirectory(request.getParameter("ruta"));
        String parentDirectoryFullPath = getParentDirectory(path);
        String comicName = getContentName(path);

        String leido = request.getParameter("read");

        if (leido != null) {
            Boolean leidoBoolean = null;
            if (leido.equals("true")) {
                leidoBoolean = Boolean.TRUE;
            }
            if (leido.equals("false")) {
                leidoBoolean = Boolean.FALSE;
            }
            File fichero = new File(path);
            if (leidoBoolean != null) {
                if (fichero.isDirectory()) {
                    File readfile = new File(path + "/" + BrowserController.getReadFilename());
                    if (readfile.exists()) {
                        XMLContentRead readfileXML = XMLContentRead.createXMLContentRead(readfile.getAbsolutePath());
                        if (readfileXML != null) {
                            File[] files = getFiles(path);
                            for (File f : files) {
                                readfileXML.setFileRead(f.getName(), leidoBoolean);
                            }
                            readfileXML.flush();
                        }
                    }
                    String ruta = request.getParameter("ruta");
                    if ((ruta == null) || ruta.equals("")) {
                        return new ModelAndView("redirect:" + htmExitRoute);
                    } else {
                        return new ModelAndView("redirect:" + htmExitRoute + "?ruta=" + URLEncoder.encode(ruta, "UTF-8"));
                    }
                } else {
                    File readfile = new File(parentDirectoryFullPath + "/" + BrowserController.getReadFilename());
                    if (readfile.exists()) {
                        XMLContentRead readfileXML = XMLContentRead.createXMLContentRead(readfile.getAbsolutePath());
                        if (readfileXML != null) {
                            readfileXML.setFileRead(comicName, leidoBoolean);
                            readfileXML.flush();
                        }
                    }
                }
            }
        }
        return new ModelAndView("redirect:" + htmExitRoute + "?ruta=" + URLEncoder.encode(parentDirectory, "UTF-8"));
    }

    @RequestMapping(value = "/image.htm", method = RequestMethod.GET)
    public void getImage(HttpServletRequest request, final HttpServletResponse response, HttpSession session) {

        setContentType(request);
        if (contentType != null) {

            String ruta = request.getParameter("ruta");
            String path = getPath(ruta);

            File fichero = new File(path);
            ProcessFile procesadorArchivo = getProcessFile(fichero, session);
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

    @RequestMapping(value = {"/comic.htm", "/libro.htm"}, method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, Pagina command, BindingResult bindingResult, HttpSession session) throws Exception {

        setContentType(request);
        String path = getPath(request.getParameter("ruta"));
        String parentDirectoryFullPath = getParentDirectory(path);
        File fichero = new File(path);
        Map<String, Object> myModel = new HashMap<String, Object>();
        ProcessFile procesadorArchivo = getProcessFile(fichero, session);
        String pagina = request.getParameter("pagina");
        int paginaNum = 0;
        if (pagina != null) {
            try {
                paginaNum = Integer.parseInt(pagina);
            } catch (NumberFormatException e) {
                paginaNum = 0;
            }
        }
        if (isLastPageReadFunctionalityActivated()) {
            if (request.getParameter("json") == null) {
                XMLContentRead readfileXML = XMLContentRead.createXMLContentRead(parentDirectoryFullPath + "/" + BrowserController.getReadFilename());
                if (readfileXML != null) {
                    Integer lastPageRead = readfileXML.getLastReadPage(fichero.getName());
                    if (lastPageRead == null) {
                        readfileXML.setFileLastPage(fichero.getName(), paginaNum);
                        readfileXML.flush();
                    } else {
                        if (pagina == null) {
                            if (procesadorArchivo.getCount() > lastPageRead.intValue()) {
                                paginaNum = lastPageRead;
                            } else {
                                readfileXML.setFileLastPage(fichero.getName(), 0);
                                readfileXML.flush();
                            }
                        } else {
                            if (paginaNum > lastPageRead.intValue()) {
                                readfileXML.setFileLastPage(fichero.getName(), paginaNum);
                                readfileXML.flush();
                            }
                        }
                    }
                }
            }
        }


        command.setNumero(paginaNum + 1);
        String extension = procesadorArchivo.getExtensionAt(paginaNum);
        if (extension
                != null) {
            if (extension.equals("jpg")) {
                extension = "jpeg";
            }
        }
        String parentDirectory = getParentDirectory(request.getParameter("ruta"));
        String comicName = getContentName(request.getParameter("ruta"));
        String imagen = procesadorArchivo.getImg64At(paginaNum);

        if (imagen == null) {
            System.out.println("Could not render image from file " + comicName + " page " + paginaNum);
        }


        System.out.println(path  + " " + procesadorArchivo);
        if (procesadorArchivo
                != null) {
            myModel.put("extension", extension);
            myModel.put("imagen", imagen);
            myModel.put("parentDirectory", parentDirectory);
            myModel.put("contentName", comicName);
            myModel.put("exit", htmExitRoute);
            myModel.put("command", command);
            myModel.put("pagina",paginaNum);
            myModel.put("totalPages", procesadorArchivo.getCount());
            myModel.put("contentType", contentType);
        }

        if (request.getParameter("json") != null) {
            if (request.getParameter("json").equals("true")) {
                return JsonView.Render(myModel, response);
            }
        }
        System.out.println(paginaNum);
        if ((imagen == null) || (paginaNum < 0)) {
            if (procesadorArchivo != null) {
                procesadorArchivo.close();
            }
            session.setAttribute("procesador", null);
            return new ModelAndView("redirect:" + htmExitRoute + "?ruta=" + URLEncoder.encode(fichero.getParent().replace(config.getConfigValue(pathAlias),""),"UTF-8"));
        }
        ModelAndView vista = new ModelAndView("content", "model", myModel);
        return vista;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Pagina command, BindingResult bindingResult) throws Exception {        
        Integer pagina = null;
        String referrer = request.getHeader("referer");
        String paginaString = request.getParameter("numero");
        if (paginaString != null){             
            try {
                pagina = Integer.parseInt(paginaString);
            } catch (NumberFormatException ex) {}        
        }
        for (String s : request.getParameterMap().keySet()) {
            System.out.println(s + " : " + request.getParameter(s));
        }
        if (pagina == null) {
            return new ModelAndView("redirect:" +referrer);
        }
        if (request.getParameter("ruta") == null) {
            return new ModelAndView("redirect:" + request.getServletPath() + "&pagina=" + (Integer.parseInt(request.getParameter("numero")) - 1));

        }
        return new ModelAndView("redirect:" + request.getServletPath() + "?ruta=" + URLEncoder.encode(request.getParameter("ruta"), "UTF-8") + "&pagina=" + (pagina - 1));
    }
}
