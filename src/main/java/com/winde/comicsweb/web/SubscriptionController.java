package com.winde.comicsweb.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.winde.comicsweb.domain.Config;
import com.winde.comicsweb.web.supporting.AjaxSignal;

@Controller
public class SubscriptionController {

	@Autowired
    private Config config;
	
	
	@RequestMapping(value = "/addsub.json")
	@ResponseBody
    public AjaxSignal addSubscription(HttpServletRequest request, HttpServletResponse response, HttpSession session){
	 
		 String searchString = request.getParameter("searchString");
		 
		 AjaxSignal result = new AjaxSignal();
		 result.setSuccess();
		 		 
		 if (searchString==null || searchString.trim().length()<=0){
			 result.setFailure();
		 }else {
			 			 
			boolean isInserted = config.addFavorite(searchString);
			 if (!isInserted){
				 result.setFailure();
			 }			 			 
		 }
		 
		 return result;
 	}
	
	@RequestMapping(value = "/removesub.json")	
	@ResponseBody
    public AjaxSignal removeSubscription(HttpServletRequest request, HttpServletResponse response, HttpSession session){
	 
		 String searchString = request.getParameter("searchString");
		 
		 AjaxSignal result = new AjaxSignal();
		 result.setSuccess();
		 		 
		 if (searchString==null || searchString.trim().length()<=0){
			 result.setFailure();
		 }else {
			 			 
			boolean isDeleted = config.deleteFavorite(searchString);
			 if (!isDeleted){
				 result.setFailure();
			 }			 			 
		 }
		 
		 return result;
 	}
	
	@RequestMapping(value = "/subs.json")	
	@ResponseBody
    public List<String> subscriptions(HttpServletRequest request, HttpServletResponse response, HttpSession session){

		 List<String> result = config.getFavorites();
		 
		 return result;
 	}
	
	@RequestMapping(value = {"/managesubs.htm"})
    public ModelAndView manageSubs(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		List<String> subs = config.getFavorites();
		
		 Map<String, Object> myModel = new HashMap<String, Object>();
         myModel.put("subs", subs);         
         ModelAndView vista = new ModelAndView("subs", "model", myModel);
         return vista;
	}
	 
	    
	    
	
}
