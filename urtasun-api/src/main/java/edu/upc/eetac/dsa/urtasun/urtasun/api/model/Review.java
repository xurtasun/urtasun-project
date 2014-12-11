package edu.upc.eetac.dsa.urtasun.urtasun.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.urtasun.urtasun.api.MediaType;

public class Review {
	
	
	@InjectLinks({
		@InjectLink(value = "/libros/reviews/{idlibro}", style = Style.ABSOLUTE, rel = "focus", title = "Reviews Libro", type = MediaType.URTASUN_API_LIBROS, bindings = { @Binding(name = "idlibro", value = "${instance.idLibro}") }) })//$-->toda {}--> valor deseado
	private List<Link> links;
	

	private int idReview;
	private String reviewtext;
	private String usernameReviewer;
	private String nameReviewer;
	private long lastModified;
	private int idLibro;
	
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
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