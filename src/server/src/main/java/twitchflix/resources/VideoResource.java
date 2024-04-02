package twitchflix.resources;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import twitchflix.models.VideoData;
import twitchflix.Data;

@Path("videos")
public class VideoResource {
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<VideoData> getCatalog() {	
	return new ArrayList<VideoData>(Data.videos.values());
    }

    @GET
    @Path("/{videoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public VideoData getVideo(@QueryParam("ext") String extension, @PathParam("videoId") String id) {
	VideoData data = new VideoData();
	data.setMessage(getURI(extension, id));
	return data;
    }

    private String getURI(String ext, String id) {
    	String uri = null;
    	String serverIP = "127.0.0.1";
    	String keyspace = "twitchflix";

    	Cluster cluster = Cluster.builder()
    			.addContactPoints(serverIP)
    			.build();
    	Session session = cluster.connect(keyspace);
    	String cqlStatement = "SELECT * FROM " + ext + " WHERE id = " + id;
    	for (Row row : session.execute(cqlStatement)) {
    		uri = row.getString("uri");
    	}
    	session.close();
    	cluster.close();
    	return uri;
    }
}
