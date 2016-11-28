package assign.resources;

import java.io.InputStream;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import assign.domain.Project;
import assign.domain.Meeting;
import assign.services.DBLoader;

@Path("/myeavesdrop")
public class ProjectResource {
	
	DBLoader db;
	String password;
	String username;
	String dbhost;	
	
	public ProjectResource(@Context ServletContext servletContext) {
		dbhost = servletContext.getInitParameter("DBHOST");
		username = servletContext.getInitParameter("DBUSERNAME");
		password = servletContext.getInitParameter("DBPASSWORD");
		this.db = new DBLoader();		
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
		//System.out.println("1) Create project");		
		Project p = new Project();
		
		p = readProject(input);
		
		try {
			if(p == null || p.getProjectName().equals("") || p.getProjectName().trim().length() == 0 || p.getProjectDescription().equals("") || p.getProjectDescription().trim().length() == 0)
				throw new SQLException();
			p = db.addProject(p);
		}
		catch(SQLException e) {
			return Response.status(404).build();
		}
		p.printProject();
		
		Response res = Response.created(URI.create("myeavesdrop/projects/" + p.getProjectId())).build();
		
		return res;
			
	}
	//2) Create a meeting for a project
	// POST http://localhost:8080/assignment5/myeavesdrop/projects/<projectId>/meetings
	@POST
	@Path("/projects/{projectId}/meetings")
	@Consumes("application/xml")
	public Response postMeeting(InputStream input, @PathParam("projectId") Integer projectId) throws Exception {
		//System.out.println("2) Create a meeting for a project");	
		
		Meeting m = new Meeting();
		m = readMeeting(input);
		
		try {
			if(m == null || m.getMeetingName().equals("") || m.getMeetingName().trim().length() == 0 || m.getMeetingYear().equals("") || m.getMeetingYear().trim().length() == 0)
				throw new SQLException();
			m = db.addMeeting(m, projectId);
			if(m == null)
				throw new SQLException();
		}
		catch(SQLException e) {
			return Response.status(404).build();
		}
		m.printMeeting();
		
		return Response.created(URI.create("myeavesdrop/projects/" + projectId + "/meetings/" + m.getMeetingName())).build();

			
	}
	//3) Get project details
	// GET http://localhost:8080/assignment5/myeavesdrop/projects/<projectId>
	@GET
	@Path("/projects/{projectId}")
	@Produces("application/xml")
	public Response getProject(@PathParam("projectId") Integer projectId) throws Exception {
		//System.out.println("3) Get project details");	
		Project p = db.getProject(projectId);
			
		if(p == null)
			return Response.status(404).build();
		
		try {
			return Response.ok(projectXML(p), "application/xml").encoding("UTF-8").build();
		}
		catch(JAXBException e) {
			System.out.println("Exception");
			e.printStackTrace();
			return Response.status(404).build();
		}
	}
	//3x) Get meeting details
	// GET http://localhost:8080/assignment5/myeavesdrop/projects/{projectId}/meetings/<m1>
	@GET
	@Path("/projects/{projectId}/meetings/{meetingName}")
	@Produces("application/xml")
	public Response getMeeting(@PathParam("projectId") Integer projectId, @PathParam("meetingName") String meetingName) throws Exception {
		//System.out.println("3x) Get project details");	
		Meeting m = db.getMeeting(meetingName);
			
		if(m == null)
			return Response.status(404).build();
		
		try {
			return Response.ok(meetingXML(m), "application/xml").encoding("UTF-8").build();
		}
		catch(JAXBException e) {
			System.out.println("Exception");
			e.printStackTrace();
			return Response.status(404).build();
		}
	}
	//4x) Update Project
	// PUT http://localhost:8080/assignment5/myeavesdrop/projects/<projectId>
	/*
	@PUT
	@Path("/projects/{projectId}/meetings/{meetingName}")
	@Consumes("application/xml")
	public Response putProject(@PathParam("projectId") Integer projectId, @PathParam("meetingName") Integer meetingName, InputStream input) throws Exception {
		System.out.println("4x) Update project");	
		Project p = db.getProject(projectId);
		if(p == null)
			return Response.status(400).build(); 
		try {
			if(p.getProjectName().equals("") || p.getProjectDescription().equals(""))
				throw new SQLException();
			//p = projectService.updateProject(p, projectService.readDes(input));
		}
		catch(SQLException e) {
			return Response.status(400).build();
		}
		System.out.println(p.getProjectId());
		return Response.ok().build();	
	}
	*/
	//4) Update Meeting
	// PUT http://localhost:8080/assignment5/myeavesdrop/projects/<projectId>/meetings/<m1>
	@PUT
	@Path("/projects/{projectId}/meetings/{meetingName}")
	@Consumes("application/xml")
	public Response putMeeting(@PathParam("projectId") Integer projectId, @PathParam("meetingName") String meetingName, InputStream input) throws Exception {
		//System.out.println("4) Update meeting");	
		Meeting m = db.getMeeting(meetingName);
		Meeting newMeeting = readMeeting(input);
		
		if(m == null || newMeeting == null)
			return Response.status(400).build(); 
		else if(m.getMeetingName().equals("") || m.getMeetingName().trim().length() == 0 || m.getMeetingYear().equals("") || m.getMeetingYear().trim().length() == 0)
			return Response.status(400).build(); 
		else if(newMeeting.getMeetingName().equals("") || newMeeting.getMeetingName().trim().length() == 0 || newMeeting.getMeetingYear().equals("") || newMeeting.getMeetingYear().trim().length() == 0)
			return Response.status(400).build(); 
		if(getProject(projectId) == null)
			return Response.status(404).build();
		
		try {
			if(m.getMeetingName().equals("") || m.getMeetingYear().equals(""))
				throw new SQLException();
			db.updateMeeting(newMeeting, m.getMeetingId());
		}
		catch(SQLException e) {
			return Response.status(400).build();
		}
		return Response.ok().build();	
	}
	//5) Delete Project
	// DELETE http://localhost:8080/assignment5/myeavesdrop/projects/<projectId>
	@DELETE
	@Path("/projects/{projectId}")
	@Consumes("application/xml")
	public Response deleteProject(@PathParam("projectId") Integer projectId) throws Exception {
		//System.out.println("5) Delete a project");	
		Project p = db.getProject(projectId);
		
		if(p == null)
			return Response.status(404).build(); 
		try {
			db.deleteProject(p);
		}
		catch(SQLException e) {
			e.printStackTrace();
			return Response.status(400).build();
		}
		return Response.ok().build();	
	}
	public Project readProject(InputStream is) {
		try {
			 DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			 Document doc = builder.parse(is);
			 Element root = doc.getDocumentElement();
			 Project p = new Project();
			 NodeList nodes = root.getChildNodes();
			 if (root.getAttribute("id") != null && !root.getAttribute("id").trim().equals(""))
				 p.setProjectId(Integer.valueOf(root.getAttribute("id")));
		 
			 for (int i = 0; i < nodes.getLength(); i++) {
				 Element element = (Element) nodes.item(i);
				 if (element.getTagName().equals("name")) {
					 p.setProjectName(element.getTextContent());
				 }
				 else if (element.getTagName().equals("description")) {
					 p.setProjectDescription(element.getTextContent());
				 }
				 else if (element.getTagName().equals("id")) {
			           p.setProjectId(Integer.parseInt(element.getTextContent()));
			     }
			 }
			 return p;
		}
		catch (Exception e) {
		     throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}
	public Meeting readMeeting(InputStream is) {
		try {
			 DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			 Document doc = builder.parse(is);
			 Element root = doc.getDocumentElement();
			 Meeting m = new Meeting();
			 NodeList nodes = root.getChildNodes();
			 if (root.getAttribute("id") != null && !root.getAttribute("id").trim().equals(""))
				 m.setMeetingId(Integer.valueOf(root.getAttribute("id")));
		 
			 for (int i = 0; i < nodes.getLength(); i++) {
				 Element element = (Element) nodes.item(i);
				 if (element.getTagName().equalsIgnoreCase("name")) {
					 m.setMeetingName(element.getTextContent());
				 }
				 else if (element.getTagName().equalsIgnoreCase("year")) {
					 m.setMeetingYear(element.getTextContent());
				 }
				 else if (element.getTagName().equalsIgnoreCase("id")) {
			           m.setMeetingId(Integer.parseInt(element.getTextContent()));
			     }
			 }
			 return m;
		}
		catch (Exception e) {
		     throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}
	private String projectXML(Project p) throws JAXBException  {
		StringWriter sw = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.marshal(p, sw);
		return sw.toString();
	}
	private String meetingXML(Meeting m) throws JAXBException  {
		StringWriter sw = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(Meeting.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.marshal(m, sw);
		return sw.toString();
	}
}
