package assign.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "Projects")
public class Project {
	
	private String projectName;
	private String projectDescription;
	private Set<Meeting> meetings = new HashSet<Meeting>();
	private int projectId;
	
	public Project() {}
	public Project(String name, String des) {
		this.projectName = new String(name);
		this.projectDescription = new String(des);
	}
	public Project(String name, String des, Set<Meeting> meetings) {
		this.projectName = new String(name);
		this.projectDescription = new String(des);
		this.meetings = meetings;
	}
	public Project(String name, String des, Set<Meeting> meetings, int projectId) {
		this.projectName = new String(name);
		this.projectDescription = new String(des);
		this.meetings = meetings;
		this.projectId = projectId;
	}

	@Column(name="projectName")
	public String getName() {
		return projectName;
	}
	@Column(name="projectDescription")
	public String getDes() {
		return projectDescription;
	}
	@OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    @Cascade({CascadeType.DELETE})
	public Set<Meeting> getmeetings() {
		return meetings;
	}
	@Id
	@GeneratedValue//(generator="increment")
	//@GenericGenerator(name="increment", strategy = "increment")
	@Column(name="projectId")
	public int getId() {
		return projectId;
	}
	public void setName(String name) {
		this.projectName = name;
	}
	public void setDes(String des) {
		this.projectDescription = des;
	}
	public void setMeetings(Set<Meeting> meetings) {
		this.meetings = meetings;
	}
    public void setId(int id) {
        this.projectId = id;
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
    	return new Project(this.projectName, this.projectDescription, copyMeetings(this.meetings), this.projectId);
    }
}
