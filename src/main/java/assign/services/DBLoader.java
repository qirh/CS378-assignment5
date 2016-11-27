package assign.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import assign.domain.Meeting;
import assign.domain.Project;

import java.util.logging.*;

public class DBLoader {
	private SessionFactory sessionFactory;
	
	Logger logger;
	
	@SuppressWarnings("deprecation")
	public DBLoader() {
		// A SessionFactory is set up once for an application
        sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
        
        logger = Logger.getLogger("EavesdropReader");
	}
	public void loadData(Map<String, List<String>> data) {
		logger.info("Inside loadData.");
	}
	public int addMeeting(String name, String year) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		int meetingId = -1;
		try {
			tx = session.beginTransaction();
			Meeting meeting = new Meeting(name, year); 
			session.save(meeting);
		    meetingId = meeting.getId();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
		return meetingId;
	}
	public int addProject(Project p) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		int meetingId = -1;
		try {
			tx = session.beginTransaction();
			Project project = p.copy();
			session.save(project);
		    meetingId = project.getId();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
		return meetingId;
	}
	/*
	public Long addAssignmentAndCourse(String title, String courseTitle) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long assignmentId = null;
		try {
			tx = session.beginTransaction();
			Assignment newAssignment = new Assignment( title, new Date() );
			UTCourse course = new UTCourse(courseTitle);
			newAssignment.setCourse(course);
			session.save(course);
			session.save(newAssignment);
		    assignmentId = newAssignment.getId();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
		return assignmentId;
	}
	*/
	public int addMeetingsToProject(String name, String des, ArrayList<Meeting> meetings) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		int projectId = -1;
		try {
			tx = session.beginTransaction();
			Project project = new Project(name, des, meetings);
			session.save(project);
			projectId = project.getId();
			for(Meeting m : meetings) {
				Meeting newmeeting = m.copy();		//could be problematic, because of the gen
				newmeeting.setProject(project);
				session.save(newmeeting);
			}
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
		return projectId;
	}
	// BAD PRACTICE
	public ArrayList<Meeting> getMeetingsForProject(int project_id) throws Exception {
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Meeting where project_id = " + project_id; 
		List<Meeting> meetings = session.createQuery(query).list();
		session.close();
		return new ArrayList<Meeting>(meetings);
	}
	public List<Object[]> getMeetingsForProject(String projectName) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		String query = "from Meeting m join m.project_id x where x.projectName = :pid";		
				
		List<Object[]> meetings = session.createQuery(query).setParameter("pid", projectName).list();
		session.close();
		
		return meetings;
	}
	public Meeting getMeeting(String meetingName) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Meeting.class).add(Restrictions.eq("meetingName", meetingName));
		List<Meeting> meetings = criteria.list();
		
		session.close();
		
		if (meetings.size() > 0) 
			return meetings.get(0);			
		return null;
		
	}
	public Project getProject(String projectName) throws Exception {

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Project.class).add(Restrictions.eq("projectName", projectName));	
		List<Project> projects = criteria.list();
		
		session.close();
		if (projects.size() > 0) 
			return projects.get(0);	
		return null;
	}
	public Project getProject(int projectId) throws Exception {

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Project.class).add(Restrictions.eq("projectId", projectId));	
		List<Project> projects = criteria.list();
		
		session.close();
		if (projects.size() > 0) 
			return projects.get(0);	
		return null;
	}
	public void deleteProject(String projectName) throws Exception {
		
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Projects p where p.projectName = :projectName";		
				
		Project p = (Project)session.createQuery(query).setParameter("projectName", projectName).list().get(0);
        session.delete(p);

        session.getTransaction().commit();
        session.close();
	}
	public Meeting getMeeting(int meetingId) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Meeting.class).add(Restrictions.eq("id", meetingId));
		
		List<Meeting> meetings = criteria.list();
		session.close();
		return meetings.get(0);		
	}
}
