package assign.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Projects")
public class Project {
	
	private String name;
	private String des;
	private Set<Meeting> meetings = new HashSet<Meeting>();
	private int id;
	
	public Project() {}
	public Project(String name, String des) {
		this.name = new String(name);
		this.des = new String(des);
	}
	public Project(String name, String des, Set<Meeting> meetings) {
		this.name = new String(name);
		this.des = new String(des);
		this.meetings = meetings;
	}
	public Project(String name, String des, Set<Meeting> meetings, int id) {
		this.name = new String(name);
		this.des = new String(des);
		this.meetings = meetings;
		this.id = id;
	}

	@Column(name="projectName")
	public String getName() {
		return name;
	}
	@Column(name="projectDescription")
	public String getDes() {
		return des;
	}
	@OneToMany(mappedBy="project")
    @Cascade({CascadeType.DELETE})
	public Set<Meeting> getmeetings() {
		return meetings;
	}
	@Id
	@GeneratedValue//(generator="increment")
	//@GenericGenerator(name="increment", strategy = "increment")
	@Column(name="projectId")
	public int getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public void setMeetings(Set<Meeting> meetings) {
		this.meetings = meetings;
	}
    public void setId(int id) {
        this.id = id;
    }
    public void addMeeting(Meeting meeting) {
		meetings.add(meeting);
	}
    private Set<Meeting> copyMeetings(Set<Meeting> meetings) {
    	Set<Meeting> tmp = new HashSet<Meeting>();
    	for(Meeting m : meetings)
    		tmp.add(m.copy());
    	return tmp;
	}
    private void printMeetings() {
    	System.out.println("printMeeting:");
    	for(Meeting m : meetings)
    		System.out.println("\t" + m);
    	System.out.println();
	}
    public void printProject() {
    	System.out.println(this.getName());
		System.out.println(this.getDes());
		this.printMeetings();
		System.out.println(this.getId());
	}
    public Project copy() {
    	return new Project(this.name, this.des, copyMeetings(this.meetings), this.id);
    }
}
