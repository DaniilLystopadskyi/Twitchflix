package twitchflix;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import twitchflix.resources.VideoResource;
import twitchflix.resources.StreamResource;
import twitchflix.resources.UserResource;


public class Main {     	
    public static void main(String[] args) throws Exception {    
	ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(80);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
             org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
           "jersey.config.server.provider.packages",
           "twitchflix.resources");

	Data.getVideos();
	Data.getStreams();
	Data.getUsers();

        jettyServer.start();
        jettyServer.join();
    }
    
}
