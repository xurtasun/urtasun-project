package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xavi on 7/12/14.
 */
public class LibroCollection {

    private Map<String, Link> links = new HashMap<String, Link>();
    private List<Libro> libros;
    private long newestTimestamp;
    private long oldestTimestamp;


    public LibroCollection() {
        super();
        libros = new ArrayList<Libro>();
    }

    public void addLibro(Libro libro) {
        libros.add(libro);
    }
    public long getNewestTimestamp() {
        return newestTimestamp;
    }

    public void setNewestTimestamp(long newestTimestamp) {
        this.newestTimestamp = newestTimestamp;
    }

    public long getOldestTimestamp() {
        return oldestTimestamp;
    }

    public void setOldestTimestamp(long oldestTimestamp) {
        this.oldestTimestamp = oldestTimestamp;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

}
