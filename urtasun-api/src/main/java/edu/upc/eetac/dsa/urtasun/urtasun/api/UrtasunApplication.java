package edu.upc.eetac.dsa.urtasun.urtasun.api;


import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class UrtasunApplication extends ResourceConfig {
	public UrtasunApplication(){
		super();
		register(DeclarativeLinkingFeature.class);
	}

}
