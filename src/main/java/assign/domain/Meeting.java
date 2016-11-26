package assign.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "meeting")
public class Meeting {
	private String name;
	private String year;
	private int id;
	
	public Meeting() {
		
	}
	public Meeting(String name, String year, int id) {
		this.name = new String(name);
		this.year = new String(year);
		this.id = id;
	}
	@XmlElement
	public String getName() {
		return name;
	}
	@XmlElement
	public String getYear() {
		return year;
	}
	@XmlElement
	public int getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setYear(String year) {
		this.year = year;
	}
    public void setId(int id) {
        this.id = id;
    }
    
    public Meeting copy() {
    	return new Meeting(this.name, this.year, this.id);
    }
}
