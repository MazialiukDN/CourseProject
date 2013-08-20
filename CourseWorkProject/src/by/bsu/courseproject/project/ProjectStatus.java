package by.bsu.courseproject.project;

public enum ProjectStatus {
  PROPOSED("Предлагаемый"),
  ACTIVE("Действующий"),
  INTERRUPTED("Прерванный"),
  CLOSED("Закрытый"),
  DECLINED("Отклоненный");

  private ProjectStatus(String idName) {
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
