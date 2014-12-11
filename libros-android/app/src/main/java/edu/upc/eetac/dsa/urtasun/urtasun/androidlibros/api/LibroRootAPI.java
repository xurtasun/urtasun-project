package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xavi on 7/12/14.
 */
public class LibroRootAPI {


    private Map<String, Link> links;

    public LibroRootAPI() {
        links = new HashMap<String, Link>();
    }

    public Map<String, Link> getLinks() {
        return links;
    }
}
