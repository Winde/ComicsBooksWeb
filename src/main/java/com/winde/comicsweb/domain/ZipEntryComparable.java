/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import java.util.zip.ZipEntry;

/**
 *
 * @author Winde
 */
public class ZipEntryComparable implements Comparable{

    ZipEntry zipEntry;
    
    public ZipEntryComparable(ZipEntry zipEntry) {
        this.zipEntry=zipEntry;
    }
    
    public ZipEntry getZipEntry() {
        return zipEntry;
    }
    
    @Override
    public int compareTo(Object o) {
        if (o instanceof ZipEntryComparable) {
            ZipEntryComparable comparar = (ZipEntryComparable) o;
            return this.getZipEntry().getName().toLowerCase().compareTo(comparar.getZipEntry().getName().toLowerCase());
        } else{
            return 0;
        }
    }
    
}
