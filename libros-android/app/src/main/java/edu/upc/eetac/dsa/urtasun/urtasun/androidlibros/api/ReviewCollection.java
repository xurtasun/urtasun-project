package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by xavi on 11/12/14.
 */
public class ReviewCollection {


    private List<Review> reviews;
    private Map<String, Link> links = new HashMap<String, Link>();


    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }


    public ReviewCollection(){
        super();
        reviews = new ArrayList<Review>();
    }

    public void addReview(Review review) {
        reviews.add(review);
    }
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
