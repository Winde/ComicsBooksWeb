/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import java.io.Serializable;

/**
 *
 * @author Winde
 */
public class Content implements Serializable {

    private String contentType = "";
    private String filename = "";

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Content() {
    }

    public Content(String filename, String contentType) {
        this.filename = filename;
        this.contentType = contentType;

    }
}
