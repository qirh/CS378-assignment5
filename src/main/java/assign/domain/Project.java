package assign.domain;

import java.util.ArrayList;
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
	private ArrayList<Meeting> meetings;
	private int id;
	
	public Project() {}
	public Project(String name, String des) {
		this.name = new String(name);
		this.des = new String(des);
	}
	public Project(String name, String des, ArrayList<Meeting> meetings) {
		this.name = new String(name);
		this.des = new String(des);
		this.meetings = meetings;
	}
	public Project(String name, String des, ArrayList<Meeting> meetings, int id) {
		this.name = new String(name);
		this.des = new String(des);
		this.meetings = meetings;
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	@Column(name="description")
	public String getDes() {
		return des;
	}
    @OneToMany(mappedBy="Project")
    @Cascade({CascadeType.DELETE})
	public ArrayList<Meeting> getmeetings() {
		return meetings;
	}
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	//@Column(name="project_id")
	public int getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public void setMeetings(ArrayList<Meeting> meetings) {
		this.meetings = meetings;
	}
    public void setId(int id) {
        this.id = id;
    }
    public void addMeeting(Meeting meeting) {
		meetings.add(meeting);
	}
    private ArrayList<Meeting> copyMeetings(ArrayList<Meeting> meetings) {
    	ArrayList<Meeting> tmp = new ArrayList<Meeting>();
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
