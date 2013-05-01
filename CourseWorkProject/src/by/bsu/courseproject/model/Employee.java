package by.bsu.courseproject.model;

/**
 * User: Artyom Strok
 * Date: 17.02.13
 * Time: 19:09
 */
public class Employee extends Person {
  private String experience;
  private String education;

  public Employee() {
    setDiscriminator("E");
  }

  public String getExperience() {
    return experience;
  }

  public void setExperience(String experience) {
    this.experience = experience;
  }

  public String getEducation() {
    return education;
  }

  public void setEducation(String education) {
    this.education = education;
  }
}
