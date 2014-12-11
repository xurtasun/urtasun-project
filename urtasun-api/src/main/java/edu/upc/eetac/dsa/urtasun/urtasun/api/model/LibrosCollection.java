package edu.upc.eetac.dsa.urtasun.urtasun.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.urtasun.urtasun.api.MediaType;
import edu.upc.eetac.dsa.urtasun.urtasun.api.LibrosResource;

public class LibrosCollection {
	
	@InjectLinks({
		@InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "create-libros", title = "Create libro", type = MediaType.URTASUN_API_LIBROS),
		@InjectLink(value = "/libros/review", style = Style.ABSOLUTE, rel = "allReviews", title = "Show all reviews", type = MediaType.URTASUN_API_LIBROS),//$-->toda {}--> valor deseado
		@InjectLink(value = "/libros?before={before}", style = Style.ABSOLUTE, rel = "previous", title = "Previous llibros", type = MediaType.URTASUN_API_LIBROS_COLLECTION, bindings = { @Binding(name = "before", value = "${instance.oldestTimestamp}") }),//$-->toda {}--> valor deseado
		@InjectLink(value = "/libros?after={after}", style = Style.ABSOLUTE, rel = "current", title = "Newest libros", type = MediaType.URTASUN_API_LIBROS_COLLECTION, bindings = { @Binding(name = "after", value = "${instance.newestTimestamp}") }) })
private List<Link> links;
private List<Libros> libros;
private long newestTimestamp;
private long oldestTimestamp;

public LibrosCollection() {
	super();
	libros = new ArrayList<>();
}



public List<Link> getLinks() {
	return links;
}

public void setLinks(List<Link> links) {
	this.links = links;
}



public void addLibros(Libros libro){
	libros.add(libro);
}

public Libros getLibro (int idlibro){
	for( int i= 0 ;i<libros.size();i++){
		if(libros.get(i).getLibroid()==idlibro){
			return libros.get(i);
		}
		
	}
	return null;
}

public List<Libros> getLibros() {
	return libros;
}

public void setLibros(List<Libros> libros) {
	this.libros = libros;
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

}
