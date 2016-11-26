package assign.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

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

//@XmlRootElement(name = "meeting")
@Entity
@Table( name = "Meetings" )
public class Meeting {
	private String name;
	private String year;
	private int id;
	private Project project;
	
	public Meeting() {}
	public Meeting(String name, String year) {
		this.name = new String(name);
		this.year = new String(year);
	}
	public Meeting(String name, String year, int id) {
		this.name = new String(name);
		this.year = new String(year);
		this.id = id;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	@Column(name = "year")
	public String getYear() {
		return year;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
    @ManyToOne
    @JoinColumn(name="project_id")
    public Project getProject() { 
    	return this.project;
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
    public void setProject(Project p) {
        this.project = p;
    }
    
    public Meeting copy() {
    	return new Meeting(this.name, this.year, this.id);
    }
}
