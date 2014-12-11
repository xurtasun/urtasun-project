package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xavi on 7/12/14.
 */
public class Libro {

    private int libroid;
    private String title;
    private int idautor;
    private String autor;
    private String language;
    private String edition;
    private String eTag;
    private long dateCreation;
    private long dateImpresion;
    private String editorial;
    private Map<String, Link> links = new HashMap<String, Link>();
    private List<Review> reviews;


    public Libro() {
        super();
        reviews = new ArrayList<Review>();
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }
    public int getLibroid() {
        return libroid;
    }

    public void setLibroid(int libroid) {
        this.libroid = libroid;
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public long getDateImpresion() {
        return dateImpresion;
    }

    public void setDateImpresion(long dateImpresion) {
        this.dateImpresion = dateImpresion;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getIdautor() {
        return idautor;
    }

    public void setIdautor(int idautor) {
        this.idautor = idautor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(long dateCreation) {
        this.dateCreation = dateCreation;
    }




}
