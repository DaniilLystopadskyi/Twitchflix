package twitchflix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;

import twitchflix.models.VideoData;
import twitchflix.models.StreamData;
import twitchflix.models.UserData;

public class Data {
    public static HashMap<String, VideoData> videos;
    public static HashMap<String, StreamData> streams;
    public static HashMap<String, UserData> users;

    public static void getVideos() {
	videos = new HashMap<String, VideoData>();
	String QUERY = "SELECT * FROM MOVIE";
	
	try (Connection connection = DriverManager
    			.getConnection("jdbc:mysql://localhost:3306/twitchflix","admin","admin");
    			Statement s = connection.createStatement();)
    	{
    		ResultSet rs = s.executeQuery(QUERY);
    		while (rs.next()) {
		    VideoData data = new VideoData(rs.getString(1),rs.getString(2),rs.getString(3),null);
		    videos.put(rs.getString(1), data);
    		}
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    }

    public static void getStreams() {
	streams = new HashMap<String, StreamData>();
	String QUERY = "SELECT * FROM STREAM";
	try (Connection connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/twitchflix","admin","admin");
			Statement s = connection.createStatement();)
	{
		ResultSet rs = s.executeQuery(QUERY);
		while (rs.next()) {
	    StreamData data = new StreamData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),null);
	    streams.put(rs.getString(1), data);
		}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
    }

    public static void getUsers() {
	users = new HashMap<String, UserData>();
	String QUERY = "SELECT * FROM USER";
	try (Connection connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/twitchflix","admin","admin");
			Statement s = connection.createStatement();)
	{
		ResultSet rs = s.executeQuery(QUERY);
		while (rs.next()) {
	    UserData data = new UserData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),null);
	    users.put(rs.getString(1), data);
		}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
    }
}
