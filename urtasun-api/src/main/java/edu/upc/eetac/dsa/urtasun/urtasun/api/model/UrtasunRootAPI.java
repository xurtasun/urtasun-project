package edu.upc.eetac.dsa.urtasun.urtasun.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.urtasun.urtasun.api.UrtasunRootAPIResource;
import edu.upc.eetac.dsa.urtasun.urtasun.api.MediaType;
import edu.upc.eetac.dsa.urtasun.urtasun.api.LibrosResource;

public class UrtasunRootAPI {
	@InjectLinks({//(1)al ser absoluta veremos la uri completa;
		@InjectLink(resource = UrtasunRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Beeter Root API", method = "getRootAPI"),
		@InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "stings", title = "Latest stings", type = MediaType.URTASUN_API_LIBROS_COLLECTION),
		@InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "create-stings", title = "Latest stings", type = MediaType.URTASUN_API_LIBROS) })
	private List<Link> links;

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
}