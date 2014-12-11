package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xavi on 11/12/14.
 */
public class Review {

    private int idReview;
    private int idLibro;
    private String username;
    private String name;
    private String reviewText;
    private long lastmodified;
    private Map<String, Link> links = new HashMap<String, Link>();


    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }


    public int getIdReview() {
        return idReview;
    }

    public void setIdReview(int idReview) {
        this.idReview = idReview;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(long lastmodified) {
        this.lastmodified = lastmodified;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }


}
