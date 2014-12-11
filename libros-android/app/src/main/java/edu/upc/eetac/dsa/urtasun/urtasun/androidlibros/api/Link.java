package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xavi on 7/12/14.
 */
public class Link {

    private String target;
    private Map<String, String> parameters;

    public Link() {
        parameters = new HashMap<String, String>();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
