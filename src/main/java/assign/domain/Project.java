package assign.domain;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project")
public class Project {
	private String name;
	private String des;
	private ArrayList<Meeting> meetings;
	private int id;
	
	public Project() {}
	
	public Project(String name, String des, ArrayList<Meeting> meetings, int id) {
		this.name = new String(name);
		this.des = new String(des);
		this.meetings = meetings;
		this.id = id;
	}
	@XmlElement
	public String getName() {
		return name;
	}
	@XmlElement(name="description")
	public String getDes() {
		return des;
	}
	@XmlElement
	public ArrayList<Meeting> getmeetings() {
		return meetings;
	}
	@XmlElement
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
