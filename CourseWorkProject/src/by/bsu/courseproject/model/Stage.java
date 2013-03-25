package by.bsu.courseproject.model;

import by.bsu.courseproject.stage.StageType;

import java.util.Set;

/**
 * User: Artyom Strok
 * Date: 17.02.13
 * Time: 19:08
 */
public class Stage extends BasicEntity {
  private Employee manager;
  private Set<Employee> employees;
  private StageType type;
  private Project project;

  public Employee getManager() {
    return manager;
  }

  public void setManager(Employee manager) {
    this.manager = manager;
  }

  public Set<Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(Set<Employee> employees) {
    this.employees = employees;
  }

  public StageType getType() {
    return type;
  }

  public void setType(StageType type) {
    this.type = type;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }
}
