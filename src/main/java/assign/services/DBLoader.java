package assign.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		//System.out.println(sessionFactory.);
		
        sessionFactory = new Configuration().configure().buildSessionFactory();
        
        logger = Logger.getLogger("EavesdropReader");
	}
	public void loadData(Map<String, List<String>> data) {
		logger.info("Inside loadData.");
	}

	public Project addProject(Project p) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Project project = null;
		try {
			tx = session.beginTransaction();
			project = p.copy();
			session.save(project);
		   
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
		return project;
	}
	public Meeting addMeeting(Meeting m, int projectId) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Meeting meeting = null;
		try {
			tx = session.beginTransaction();
			meeting = m.copy(); 
			session.save(meeting);
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
		return meeting;
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
	public int addMeetingsToProject(String name, String des, Set<Meeting> meetings) throws Exception {
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
	public Project getProject(int projectId) throws Exception {
		System.out.println("getProject(int projectId) where projectId == " + projectId);
		Session session = sessionFactory.openSession();
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Project.class).add(Restrictions.eq("", projectId ));	
			
			List<Project> projects = criteria.list();
			if (projects.size() > 0) 
				return projects.get(0);	
		}
		catch (org.hibernate.QueryException e){
			return null;
		}
		finally{
			session.close();
		}
		
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
