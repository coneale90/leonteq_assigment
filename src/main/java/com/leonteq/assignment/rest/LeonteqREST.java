package com.leonteq.assignment.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.leonteq.assignment.service.Converter;

import java.net.MalformedURLException;
import java.security.Timestamp;
import java.util.LinkedList;

/**
 * Created by Conese on 01/12/2016.
 */
@Path("/leonteq")
public class LeonteqREST {

    @GET
    @Path("/test")
    public String test(){
        
        return "test";
    }
    
    @GET
    @Path("/short")
    @Produces(MediaType.APPLICATION_JSON)
    public Response shortUrl(@QueryParam("uri") String uri){
    	JSONObject obj = new JSONObject();
    	try {
    		String shortUrl = Converter.shortUrl(uri);
    		obj.put("data",shortUrl);
            obj.put("result","success");
		} catch (JSONException e) {
			try {
				obj.put("result","error");
				obj.put("data","Impossible to load short URL");
			} catch (JSONException e1) {
				return Response.noContent().build();
			}
			
		}
        return Response.ok().entity(obj.toString()).build();
    }
    
    @GET
    @Path("/unshort")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unshortUrl(@QueryParam("uri") String uri){
    	JSONObject obj = new JSONObject();
    	try {
    		String longUrl = Converter.unshortUrl(uri);
    		if (longUrl != null){
	    		obj.put("data",longUrl);
	            obj.put("result","success");
            }else{
            	obj.put("result","error");
				obj.put("data","URL not found");
            }
		} catch (JSONException e) {
			try {
				obj.put("result","error");
				obj.put("data","Impossible to load unshort URL");
			} catch (JSONException e1) {
				return Response.noContent().build();
			}
			
		}
        return Response.ok().entity(obj.toString()).build();
    }
}
