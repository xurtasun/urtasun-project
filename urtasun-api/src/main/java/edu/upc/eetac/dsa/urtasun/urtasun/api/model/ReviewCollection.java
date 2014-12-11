package edu.upc.eetac.dsa.urtasun.urtasun.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.urtasun.urtasun.api.MediaType;

public class ReviewCollection {
	
	@InjectLinks({
		@InjectLink(value = "/libros/reviews", style = Style.ABSOLUTE, rel = "focus", title = "Reviews Libro", type = MediaType.URTASUN_API_LIBROS) })//$-->toda {}--> valor deseado
	private List<Link> links;
	
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}




	private List<Review> reviews;

	public ReviewCollection() {
		super();
		reviews = new ArrayList<>();
	}
	
	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}




	public void addReview(Review review) {
		reviews.add(review);
		
	}

}
