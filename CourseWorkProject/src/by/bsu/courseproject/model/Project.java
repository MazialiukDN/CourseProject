package by.bsu.courseproject.model;

import by.bsu.courseproject.project.ProjectCategory;
import by.bsu.courseproject.project.ProjectPriority;
import by.bsu.courseproject.project.ProjectStatus;

import java.util.Date;

/**
 * User: Artyom Strok
 * Date: 17.02.13
 * Time: 18:38
 */
public class Project extends BasicEntity {

  private String name;
  private Date dueDate;
  private ProjectCategory category;
  private ProjectStatus status;
  private ProjectPriority priority;
  private Investor investor;
  private Customer customer;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public ProjectCategory getCategory() {
    return category;
  }

  public void setCategory(ProjectCategory category) {
    this.category = category;
  }

  public ProjectStatus getStatus() {
    return status;
  }

  public void setStatus(ProjectStatus status) {
    this.status = status;
  }

  public ProjectPriority getPriority() {
    return priority;
  }

  public void setPriority(ProjectPriority priority) {
    this.priority = priority;
  }

  public Investor getInvestor() {
    return investor;
  }

  public void setInvestor(Investor investor) {
    this.investor = investor;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
}
