package edu.upc.eetac.dsa.urtasun.urtasun.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import edu.upc.eetac.dsa.urtasun.urtasun.api.model.UrtasunRootAPI;

@Path("/")
public class UrtasunRootAPIResource {
	@GET
	public UrtasunRootAPI getRootAPI() {
		UrtasunRootAPI api = new UrtasunRootAPI();
		return api;
	}
}