package assign.services;

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
		//System.out.println("addMeeting(Meeting m, int projectId) where projectId == " + projectId + " and meetingId == " + m.getMeetingId());
		
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Meeting meeting = null;
		try {
			tx = session.beginTransaction();
			meeting = m;
			
			if(getProject(projectId) == null){
				return null;
			}
			Project p = getProject(projectId);
			meeting.setProject(p);
			
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
	public Meeting updateMeeting(Meeting meeting, int meetingId) throws Exception {
		//System.out.println("updateMeeting(Meeting m, int meetingId) where meetingId == " + meetingId);
		
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			Meeting oldMeeting = getMeeting(meetingId);
			
			if(oldMeeting == null){
				return null;
			}
			oldMeeting.setMeetingName(meeting.getMeetingName());
			oldMeeting.setMeetingYear(meeting.getMeetingYear());
			session.update(oldMeeting);
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
	public Project getProject(int projectId) throws Exception {
		//System.out.println("getProject(int projectId) where projectId == " + projectId);
		Session session = sessionFactory.openSession();
		try{
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Project.class).add(Restrictions.eq("projectId", projectId ));	
			
			List<Project> projects = criteria.list();
			if (projects.size() > 0) 
				return projects.get(0);	
		}
		catch (org.hibernate.QueryException e){
			e.printStackTrace();
			return null;
		}
		finally{
			session.close();
		}
		return null;
	}
	
	public void deleteProject(Project project) throws Exception {
		
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		
        session.delete(project);

        session.getTransaction().commit();
        session.close();
	}
	
	public Meeting getMeeting(int meetingId) throws Exception {
		//System.out.println("getMeeting(int meetingId) where meetingId == " + meetingId);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Meeting.class).add(Restrictions.eq("id", meetingId));
		
		List<Meeting> meetings = criteria.list();
		session.close();
		return meetings.get(0);		
	}
	public Meeting getMeeting(String meetingName) throws Exception {
		//System.out.println("getMeeting(int meetingName) where meetingName == " + meetingName);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Meeting.class).add(Restrictions.eq("meetingName", meetingName));
		List<Meeting> meetings = criteria.list();
		
		session.close();
		
		if (meetings.size() > 0) 
			return meetings.get(0);			
		return null;
		
	}
}
