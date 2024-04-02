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

import twitchflix.models.UserData;
import twitchflix.Data;

@Path("user")
public class UserResource {
	
    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public UserData addUser(UserData user) {
	UserData data = new UserData();

	if(!userExists(user.getEmail())){
	    data = user;
	    String id = Integer.toString(Data.users.size() + 1);
	    data.setId(id);
	    Data.users.put(id, data);
	    addToDB(data);
	}else{
	    data.setMessage("this email is already in use");
	}
	
	return data;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public UserData deleteUser(UserData user) {
	UserData data = new UserData();

	if(Data.users.containsKey(user.getId())){
	    data = user;
	    Data.users.remove(user.getId());
	    removeFromDB(user.getId());
	}else{
	    data.setMessage("user doesn't exist");
	}
	
	return data;
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public UserData loginUser(UserData user) {
	UserData data = new UserData();
	String userId = checkUser(user);

	if(userId != null){
	    data = Data.users.get(userId);
	}else{
	    data.setMessage("wrong email/password");
	}
	
	return data;
    }

    public boolean userExists(String email) {
	for(UserData user : Data.users.values())
	    if(user.getEmail().equals(email))
		return true;

	return false;
    }

    public static String checkUser(UserData user) {
	for(Map.Entry<String,UserData> entry : Data.users.entrySet())
	    if(entry.getValue().getEmail().equals(user.getEmail()) && entry.getValue().getPassword().equals(user.getPassword()))
		return entry.getKey();

	return null;
    }

    private void removeFromDB(String id) {
    String QUERY = "DELETE FROM USER WHERE UserId = " + id;
    try (Connection connection = DriverManager
    			.getConnection("jdbc:mysql://localhost:3306/twitchflix","admin","admin");
    			Statement s = connection.createStatement();)
    {
    	s.executeUpdate(QUERY);  	
    }
    catch(SQLException e) {
		e.printStackTrace();
	}
    }

    private void addToDB(UserData user) {
    String QUERY = "INSERT INTO USER VALUES (?,?,?,?)";
    try(Connection connection = DriverManager
    			.getConnection("jdbc:mysql://localhost:3306/twitchflix","admin","admin");
    			PreparedStatement s = connection.prepareStatement(QUERY);)
    {
    	s.setString(1, user.getId());
    	s.setString(2, user.getName());
    	s.setString(3, user.getEmail());
    	s.setString(4, user.getPassword());
    	s.executeUpdate();
    }
    catch(SQLException e) {
		e.printStackTrace();
	}
    }

}

