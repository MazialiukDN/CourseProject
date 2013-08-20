package by.bsu.courseproject.project;

public enum ProjectCategory {
  HOTEL_AND_TOURISM_BUSINESS("Гостиничный и туристический бизнес"),
  GOVERNMENTAL_ORGANIZATION("Государственные организации"),
  SHOW_BUSINESS("Индустрия развлечений"),
  INFORMATION_SERVICE("Информационные услуги"),
  CONSULTING_SERVICE("Консультационные услуги"),
  OIL_AND_GAS_INDUSTRY("Консультационные услуги"),
  EDUCATION("Образование"),
  FOOD_INDUSTRY("Пищевая промышленность"),
  DESIGN_ORGANIZATION("Проектные организации"),
  TELECOMMUNICATIONS("Проектные организации"),
  TRADE_ORGANIZATION("Проектные организации"),
  FINANCE("Финансы"),
  TRANSPORT("Транспорт");

  private ProjectCategory(String idName) {
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
