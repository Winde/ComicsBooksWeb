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
    private String read = "false";

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
    private Integer lastreadPage = null;

    public Integer getLastReadPage() {
        return lastreadPage;
    }

    public void setLastReadPage(Integer lastreadPage) {
        this.lastreadPage = lastreadPage;
    }

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

    public String getIsRead() {
        return read;
    }

    public void setIsRead(String read) {
        this.read = read;
    }

    public Content() {
    }

    public Content(String filename, String contentType, String read) {
        this.filename = filename;
        this.contentType = contentType;
        this.read = read;
    }

    public Content(String filename, String contentType, String read, Integer lastread) {
        this.filename = filename;
        this.contentType = contentType;
        this.read = read;
        this.lastreadPage = lastread;
    }
}
