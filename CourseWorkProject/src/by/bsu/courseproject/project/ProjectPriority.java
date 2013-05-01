package by.bsu.courseproject.project;

/**
 * User: Artyom Strok
 * Date: 17.02.13
 * Time: 18:56
 */
public enum ProjectPriority {
  LOWEST("Самый низкий"),
  LOW("Низкий"),
  NORMAL("Нормальный"),
  HIGH("Высокий"),
  HIGHEST("Самый высокий");

  private ProjectPriority(String idName) {
    this.idName = idName;
  }

  private String idName;

  public String getIdName() {
    return idName;
  }


  @Override
  public String toString() {
    return idName;
  }
}
