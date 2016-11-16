package assign.services;

import java.io.InputStream;

import assign.domain.Project;

public interface ProjectService {
	public Project getProject(int projectId) throws Exception;
	public Project addProject(Project c) throws Exception;
	public Project updateProject(Project c, String s) throws Exception;
	public Project deleteProject(Project c) throws Exception;
	public Project readProject(InputStream is);
	public String readDes(InputStream is);
}
