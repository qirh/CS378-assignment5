package assign.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import assign.domain.Project;
import assign.services.ProjectService;
import assign.services.ProjectServiceImpl;

@Path("/myeavesdrop")
public class ProjectResource {
	
	ProjectService projectService;
	String password;
	String username;
	String dbhost;	
	
	public ProjectResource(@Context ServletContext servletContext) {
		dbhost = servletContext.getInitParameter("DBHOST");
		username = servletContext.getInitParameter("DBUSERNAME");
		password = servletContext.getInitParameter("DBPASSWORD");
		this.projectService = new ProjectServiceImpl(dbhost, username, password);		
	}
	
	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		System.out.println("Inside helloworld");
		System.out.println("DB creds are:");
		System.out.println("DBURL:" + dbhost);
		System.out.println("DBUsername:" + username);
		System.out.println("DBPassword:" + password);		
		return "Hello world\n DBHOST:" + dbhost + "\nDBUSERNAME: " + username + "\nDBPASSWORD: " + password + "\n\n" + "jdbc:mysql://" + dbhost + "/cs378_" +username;	
		
	}
	
	//1) Create project
	// POST http://localhost:8080/assignment5/myeavesdrop/projects/
	@POST
	@Path("/projects")
	@Consumes("application/xml")
	public Response postProject(InputStream input) throws Exception {
				
		Project p = new Project();
		
		p = projectService.readProject(input);
		
		try {
			if(p.getName().equals("") || p.getDes().equals(""))
				throw new SQLException();
			p = projectService.addProject(p);
		}
		catch(SQLException e) {
			return Response.status(404).build();
		}
		p.printProject();
		
		Response res = Response.created(URI.create("myeavesdrop/projects/" + p.getId())).build();
		
		return res;
			
	}
	//2) Create a meeting for a project
	// POST http://localhost:8080/assignment5/myeavesdrop/projects/<projectId>/meetings
	@POST
	@Path("/projects/{project_id}/Meetings")
	@Consumes("application/xml")
	public Response postMeeting(InputStream input) throws Exception {
				
		return null;
			
	}
	//3) Get project details
	// GET http://localhost:8080/assignment5/myeavesdrop/projects/<projectId>
	@GET
	@Path("/projects/{project_id}")
	@Produces("application/xml")
	public Response getProjects(@PathParam("project_id") Integer project_id) throws Exception {
			
		Project p = projectService.getProject(project_id);
			
		if(p == null)
			return Response.status(404).build();
		
		System.out.println("HERE3");
		try {
			return Response.ok(projectXML(p), "application/xml").encoding("UTF-8").build();
		}
		catch(JAXBException e) {
			return Response.status(404).build();
		}
	}	
	//4) Update meeting
	// PUT http://localhost:8080/assignment5/myeavesdrop/projects/<projectId>/meetings/<m1>
	@PUT
	@Path("/projects/{project_id}/Meetings/{meeting_id}")
	@Consumes("application/xml")
	public Response putProject(@PathParam("project_id") Integer project_id, @PathParam("meeting_id") Integer meeting_id,InputStream input) throws Exception {
			
		System.out.println("HERE1");
		Project p = projectService.getProject(project_id);
		System.out.println("HERE2");
		if(p == null)
			return Response.status(400).build(); 
		try {
			if(p.getName().equals("") || p.getDes().equals(""))
				throw new SQLException();
			p = projectService.updateProject(p, projectService.readDes(input));
		}
		catch(SQLException e) {
			return Response.status(400).build();
		}
		System.out.println(p.getId());
		return Response.ok().build();
		
			
	}
	//5) Delete a project
	// DELETE http://localhost:8080/assignment5/myeavesdrop/projects/<projectId>
	@DELETE
	@Path("/projects/{project_id}")
	@Consumes("application/xml")
	public Response deleteProject(@PathParam("project_id") Integer project_id) throws Exception {
				
		
		Project p = projectService.getProject(project_id);
		
		if(p == null)
			return Response.status(404).build(); 
		try {
			p = projectService.deleteProject(p);
		}
		catch(SQLException e) {
			e.printStackTrace();
			return Response.status(400).build();
		}
		
		return Response.ok().build();
			
	}
	private String projectXML(Project p) throws JAXBException  {
		StringWriter sw = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.marshal(p, sw);
		return sw.toString();
	}
}
