/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.web;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;

public class JsonView {

    public static ModelAndView Render(Object model, HttpServletResponse response) {
        /*
         * MappingJacksonHttpMessageConverter jsonConverter = new
         * MappingJacksonHttpMessageConverter();
         * jsonConverter.setPrefixJson(false);
         *
         * MediaType jsonMimeType = MediaType.APPLICATION_JSON;
         *
         * try { jsonConverter.write(model, jsonMimeType, new
         * ServletServerHttpResponse(response)); } catch
         * (HttpMessageNotWritableException e) { } catch (IOException e) { }
         */
        JSONObject json = new JSONObject((Map) model);
        try {
            response.setContentType("application/json");
            json.write(response.getWriter());

        } catch (IOException ex) {
            Logger.getLogger(JsonView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(JsonView.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}