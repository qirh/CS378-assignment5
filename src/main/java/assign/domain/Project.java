package assign.domain;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project")
//@XmlAccessorType(XmlAccessType.FIELD)
public class Project {
	private String name;
	private String des;
	private int id;
	
	public Project() {
		
	}
	public Project(String name, String des, int id) {
		this.name = new String(name);
		this.des = new String(des);
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
	public int getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDes(String des) {
		this.des = des;
	}
    public void setId(int id) {
        this.id = id;
    }
    
    public Project copy() {
    	return new Project(this.name, this.des, this.id);
    }
}
