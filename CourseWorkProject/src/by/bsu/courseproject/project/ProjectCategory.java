package by.bsu.courseproject.project;

public enum ProjectCategory {
	HOTEL_AND_TOURISM_BUSINESS("Гостиничный и туристический бизнес"),
	GOVERNMENTAL_ORGANIZATION("Государственные организации"),
	SHOW_BUSINESS("Индустрия развлечений"),
	INFORMATION_SERVICE("Информационные услуги"),
	CONSULTING_SERVICE(""),
	OIL_AND_GAS_INDUSTRY(""),
	EDUCATION(""),
	FOOD_INDUSTRY(""),
	DESIGN_ORGANIZATION(""),
	TELECOMMUNICATIONS(""),
	TRADE_ORGANIZATION(""),
	FINANCE(""),
	TRANSPORT("");
	
	private ProjectCategory(String idName) {
		this.idName = idName;
	}

	private String idName;

	public String getIdName(){
		return idName;
	}
	
	
}
