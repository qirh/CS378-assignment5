package assign.services;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.dbcp.BasicDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import assign.domain.Project;


public class ProjectServiceImpl implements ProjectService {

	String dbURL;
	String dbHost;
	String dbUsername = "";
	String dbPassword = "";
	DataSource ds;

	// DB connection information would typically be read from a config file.
	public ProjectServiceImpl(String dbHost, String dbUsername, String dbPassword) {
		
		this.dbHost = dbHost;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		
		this.dbURL = "jdbc:mysql://" + 
				 		this.dbHost + "/cs378_" +
				 		this.dbUsername;
		ds = setupDataSource();
	}
	public DataSource setupDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername(this.dbUsername);
        ds.setPassword(this.dbPassword);
        ds.setUrl(this.dbURL);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        return ds;
    }
	
	public Project getProject(int id) throws Exception {
		String query = "select * from Projects where id=?";
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		s.setString(1, String.valueOf(id));
		ResultSet r = s.executeQuery();
		System.out.print("getProject()");
		if (!r.next()) {
			System.out.println();
		    return null;
		}
		Project p = new Project();
		p.setDes(r.getString("des"));
		p.setName(r.getString("name"));
		p.setId(r.getInt("id"));
		System.out.println(", project name = " + p.getName());
		return p;
    }
	public Project addProject(Project p) throws Exception {
		Connection conn = ds.getConnection();
		
		String query = "INSERT INTO Projects(name, des) VALUES(?, ?)";
		PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, p.getName());
		stmt.setString(2, p.getDes());
		
		int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) 
            throw new SQLException("Creating project failed, no rows affected.");
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        
        if (generatedKeys.next())
        	p.setId(generatedKeys.getInt(1));
        else 
            throw new SQLException("Creating project failed, no ID obtained.");
        
        conn.close();
		return p;
	}
	public Project updateProject(Project p, String des) throws Exception {
		Connection conn = ds.getConnection();
		String query = "update Projects set des=? where id = ?";
		PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, des);
		stmt.setString(2, ""+String.valueOf(p.getId()));
		int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) 
            throw new SQLException("Updating project failed, no rows affected.");
        stmt.close();
        conn.close();
		p.setDes(des);
		
		return p;
	}
	public Project deleteProject(Project p) throws Exception {
		Connection conn = ds.getConnection();
		String query = "delete from Projects where id = ?";
		PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, ""+String.valueOf(p.getId()));
		int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) 
            throw new SQLException("Updating project failed, no rows affected.");
        stmt.close();
        conn.close();
		
		return p;
	}
	public Project readProject(InputStream is) {
	      try {
	         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	         Document doc = builder.parse(is);
	         Element root = doc.getDocumentElement();
	         Project p = new Project();
	         NodeList nodes = root.getChildNodes();
	         if (root.getAttribute("id") != null && !root.getAttribute("id").trim().equals(""))
	            p.setId(Integer.valueOf(root.getAttribute("id")));
	         
	         for (int i = 0; i < nodes.getLength(); i++) {
	            Element element = (Element) nodes.item(i);
	            if (element.getTagName().equals("name")) {
	               p.setName(element.getTextContent());
	            }
	            else if (element.getTagName().equals("description")) {
	               p.setDes(element.getTextContent());
	            }
	            else if (element.getTagName().equals("id")) {
	               p.setId(Integer.parseInt(element.getTextContent()));
	            }
	         }
	         return p;
	      }
	      catch (Exception e) {
	         throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
	      }
	 }
	public String readDes(InputStream is) {
	      try {
	         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	         Document doc = builder.parse(is);
	         Element root = doc.getDocumentElement();
	         NodeList nodes = root.getChildNodes();
	         
	         String des = new String();
	         
	         for (int i = 0; i < nodes.getLength(); i++) {
	            Element element = (Element) nodes.item(i);
	            if (element.getTagName().equals("description")) {
	               des = element.getTextContent();
	            }
	         }
	         return des;
	      }
	      catch (Exception e) {
	         throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
	      }
	 }
	public Project addMeeting(Project c) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    

}
