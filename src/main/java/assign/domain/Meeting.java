package assign.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;




@XmlRootElement (name = "Meeting")
@Entity
@Table( name = "Meetings" )
public class Meeting {
	
	private String meetingName;
	private String meetingYear;
	private int meetingId;
	private Project project;
	
	public Meeting() {}
	public Meeting(String name, String year) {
		this.meetingName = new String(name);
		this.meetingYear = new String(year);
	}
	public Meeting(String name, String year, int id) {
		this.meetingName = new String(name);
		this.meetingYear = new String(year);
		this.meetingId = id;
	}
	@XmlElement
	@Column(name = "meetingName")
	public String getMeetingName() {
		return meetingName;
	}
	@XmlElement
	@Column(name = "meetingYear")
	public String getMeetingYear() {
		return meetingYear;
	}
	//@XmlElement
	@Id
	@Column(name = "meetingId")
	@GeneratedValue//(strategy=GenerationType.AUTO)
	public int getMeetingId() {
		return meetingId;
	}
	@ManyToOne
    public Project getProject() { 
    	return this.project;
    }
	
	public void setMeetingName(String name) {
		this.meetingName = name;
	}
	public void setMeetingYear(String year) {
		this.meetingYear = year;
	}
    public void setMeetingId(int id) {
        this.meetingId = id;
    }
    public void setProject(Project p) {
        this.project = p;
    }
    public void printMeeting() {
    	System.out.println(this.getMeetingId());
    	System.out.println(this.getMeetingName());
		System.out.println(this.getMeetingYear());
		this.project.printProject();
	}
}
