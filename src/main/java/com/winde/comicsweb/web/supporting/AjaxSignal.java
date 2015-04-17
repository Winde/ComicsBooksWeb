package com.winde.comicsweb.web.supporting;

public class AjaxSignal {

	public String result;

	public void setSuccess(){
		result = "success";
	}
	
	public void setFailure(){
		result = "error";
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
