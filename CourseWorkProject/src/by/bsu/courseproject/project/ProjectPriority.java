package by.bsu.courseproject.project;

/**
 * User: Artyom Strok
 * Date: 17.02.13
 * Time: 18:56
 */
public enum ProjectPriority {
  HIGHEST("Самый высокий"),
  HIGH("Высокий"),
  NORMAL("Нормальный"),
  LOW("Низкий"),
  LOWEST("Самый низкий");


  private ProjectPriority(String idName) {
    this.idName = idName;
  }

  private final String idName;

  public String getIdName() {
    return idName;
  }


  @Override
  public String toString() {
    return idName;
  }
}
