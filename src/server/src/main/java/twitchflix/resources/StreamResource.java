package twitchflix.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import twitchflix.models.StreamData;
import twitchflix.models.UserData;
import twitchflix.Data;

@Path("live")
public class StreamResource {
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StreamData> getCatalog() {	
	if(Data.streams.values() == null)
	    return new ArrayList<StreamData>();
	return new ArrayList<StreamData>(Data.streams.values());
    }

    @GET
    @Path("/{streamId}")
    @Produces(MediaType.APPLICATION_JSON)
    public StreamData getStream(@PathParam("streamId") String id) {
	StreamData data = new StreamData();
	data.setMessage(getURI(id));
	return data;
    }

    @POST
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    public StreamData goLive(@QueryParam("title") String title, @QueryParam("img") String img, UserData user) {
	StreamData data = new StreamData();

	if(user.getId() != null){
	    boolean isLive = false;
	    for(StreamData stream : Data.streams.values()){
		if(user.getId().equals(stream.getUserId())){
		    isLive = true;
		    data = stream;
		}
	    }
	    
	    if(!isLive){
		String id = Integer.toString(Data.streams.size() + 1);
		System.out.println(id);
		data.setId(id);
		data.setUserId(user.getId());
		data.setTitle(title);
		data.setImg("/images/cameraicon.png");
		Data.streams.put(id, data);
		addToDB(data);
	    }else{
		data.setMessage("user already live");
	    }
	}else{
	    data.setMessage("unauthorized access");
	}
	
	return data;
    }

    @POST
    @Path("/stop")
    @Produces(MediaType.APPLICATION_JSON)
    public StreamData closeStream(@QueryParam("streamId") String streamId, UserData user) {
	StreamData data = new StreamData();

	if(user.getId() != null){
	    if(Data.streams.containsKey(streamId)){
		data = Data.streams.get(streamId);
		Data.streams.remove(streamId);
		removeFromDB(streamId);
	    }else{
		data.setMessage("unable to locate live stream");
	    }
	}else{
	    data.setMessage("unauthorized access");
	}
	
	return data;
    }

    private String getURI(String id) {
	String uri = null;
	String serverIP = "127.0.0.1";
	String keyspace = "twitchflix";
	System.out.println("id: " + id);

	Cluster cluster = Cluster.builder()
	    .addContactPoints(serverIP)
	    .build();
	Session session = cluster.connect(keyspace);
	String cqlStatement = "SELECT * FROM stream "  + "WHERE id = " + id;
	for (Row row : session.execute(cqlStatement)) {
	    uri = row.getString("uri");
	}
	session.close();
	cluster.close();
	return uri;
    }

    private void removeFromDB(String id) {
	// Apagar da DB Maria
	String QUERY = "DELETE FROM STREAM WHERE StreamId = " + id;
	try (Connection connection = DriverManager
	     .getConnection("jdbc:mysql://localhost:3306/twitchflix","admin","admin");
	     Statement s = connection.createStatement();)
	    {
		s.executeUpdate(QUERY);  	
	    }
	catch(SQLException e) {
	    e.printStackTrace();
	}
    
	// Apagar da DB cassandra
	String serverIP = "127.0.0.1";
	String keyspace = "twitchflix";

	Cluster cluster = Cluster.builder()
	    .addContactPoints(serverIP)
	    .build();
	Session session = cluster.connect(keyspace);
	String cqlStatement = "DELETE FROM stream "  + "WHERE id = " + id;
	session.execute(cqlStatement);
	session.close();
	cluster.close();
    }

    private void addToDB(StreamData stream) {
	// Adicionar à DB Maria
	String QUERY = "INSERT INTO STREAM VALUES (?,?,?,?)";
	try(Connection connection = DriverManager
	    .getConnection("jdbc:mysql://localhost:3306/twitchflix","admin","admin");
	    PreparedStatement s = connection.prepareStatement(QUERY);)
	    {
		s.setString(1, stream.getId());
		s.setString(2, stream.getUserId());
		s.setString(3, stream.getTitle());
		s.setString(4, stream.getImg());
		s.executeUpdate();
	    }
	catch(SQLException e) {
	    e.printStackTrace();
	}
    
	// Adicionar à DB cassandra
	String serverIP = "127.0.0.1";
	String keyspace = "twitchflix";
	String streamURI ="'/hls/"+stream.getId()+".m3u8'";
	System.out.println(streamURI);

	Cluster cluster = Cluster.builder()
	    .addContactPoints(serverIP)
	    .build();
	Session session = cluster.connect(keyspace);
	String cqlStatement = "INSERT INTO stream(id,uri) VALUES (" + stream.getId()
	    + "," + streamURI + ")";
	session.execute(cqlStatement);
	session.close();
	cluster.close();
    }
}
