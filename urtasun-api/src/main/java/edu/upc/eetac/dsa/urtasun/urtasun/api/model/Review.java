package edu.upc.eetac.dsa.urtasun.urtasun.api.model;

public class Review {
	
	private int idReview;
	private String reviewtext;
	private String usernameReviewer;
	private String nameReviewer;
	private long lastModified;
	private int idLibro;
	public int getIdReview() {
		return idReview;
	}
	public void setIdReview(int idReview) {
		this.idReview = idReview;
	}
	public String getReviewtext() {
		return reviewtext;
	}
	public void setReviewtext(String reviewtext) {
		this.reviewtext = reviewtext;
	}
	public String getUsernameReviewer() {
		return usernameReviewer;
	}
	public void setUsernameReviewer(String usernameReviewer) {
		this.usernameReviewer = usernameReviewer;
	}
	public String getNameReviewer() {
		return nameReviewer;
	}
	public void setNameReviewer(String nameReviewer) {
		this.nameReviewer = nameReviewer;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public int getIdLibro() {
		return idLibro;
	}
	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}
}