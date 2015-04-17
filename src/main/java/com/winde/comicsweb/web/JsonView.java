/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;

public class JsonView {

    public static ModelAndView Render(Object model, HttpServletResponse response) {
        
          MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
          jsonConverter.setPrefixJson(false);
         
          MediaType jsonMimeType = MediaType.APPLICATION_JSON;
         
         try { 
        	 response.setContentType("application/json");
        	 jsonConverter.write(model, jsonMimeType, new ServletServerHttpResponse(response)); 
          } catch(HttpMessageNotWritableException e) {
        		e.printStackTrace();        	  
          } catch (IOException e) { 
        	  e.printStackTrace();
          } catch (Exception e) { 
        	  e.printStackTrace();
          }
         
    	
    	/*
        JSONObject json = new JSONObject((Map) model);
        try {
            response.setContentType("application/json");
            json.write(response.getWriter());

        } catch (IOException ex) {
            Logger.getLogger(JsonView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(JsonView.class.getName()).log(Level.SEVERE, null, ex);
        }
    	 */
        return null;
    }
}