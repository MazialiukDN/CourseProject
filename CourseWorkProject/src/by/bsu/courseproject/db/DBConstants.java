package by.bsu.courseproject.db;

import android.provider.BaseColumns;

/**
 * User: Artyom Strok
 * Date: 23.03.13
 * Time: 17:00
 */
public class DBConstants {
  public static final class Columns implements BaseColumns {

    public static final String PROJECT_PROJECTNAME = "PROJECTNAME";
    public static final String PROJECT_PROJECTDUEDATE = "PROJECTDUEDATE";
    public static final String PROJECT_CATEGORY = "CATEGORY";
    public static final String PROJECT_STATUS = "STATUS";
    public static final String PROJECT_PRIORITY = "PRIORITY";
    public static final String PROJECT_INVESTOR_ID = "INVESTOR_ID";
    public static final String PROJECT_CUSTOMER_ID = "CUSTOMER_ID";

    public static final String LOGIN = "LOGIN";
    public static final String PASSWORD = "PASSWORD";
    public static final String SALT = "SALT";

    public static final String PERSON_FIRSTNAME = "FIRSTNAME";
    public static final String PERSON_MIDDLENAME = "MIDDLENAME";
    public static final String PERSON_LASTNAME = "LASTNAME";
    public static final String PERSON_EMAIL = "EMAIL";
    public static final String PERSON_SKYPE = "SKYPE";
    public static final String PERSON_PHONE = "PHONE";
    public static final String PERSON_BIRTHDAY = "BIRTHDAY";
    public static final String PERSON_INFO = "INFO";
    public static final String PERSON_DISCRIMINATOR = "DISCRIMINATOR";
    public static final String EMPLOYEE_EXPERIENCE = "experience";
    public static final String EMPLOYEE_EDUCATION = "education";
    public static final String CUSTOMER_INVESTOR_COMPANY = "COMPANY";


    public static final String STAGE_MANAGER = "MANAGER_ID";
    public static final String STAGE_ID = "STAGE_ID";
    public static final String EMPLOYEE_ID = "EMPL_ID";

    public static final String STAGE_TYPE = "STAGE_TYPE";
    public static final String STAGE_PROJECT_ID = "PROJECT_ID";

    public static final String SORT_ORDER_ASC = "_id ASC";
    public static final String SORT_ORDER_EMP = "LASTNAME ASC";
    public static final String SORT_ORDER_DESC = "_id DESC";
    public static final String SORT_ORDER_PROJECT = "PRIORITY DESC";

    public static final String INVESTOR_FIRSTNAME = "INVESTOR_FIRSTNAME";
    public static final String INVESTOR_MIDDLENAME = "INVESTOR_MIDDLENAME";
    public static final String INVESTOR_LASTNAME = "INVESTOR_LASTNAME";

    public static final String CUSTOMER_FIRSTNAME = "CUSTOMER_FIRSTNAME";
    public static final String CUSTOMER_MIDDLENAME = "CUSTOMER_MIDDLENAME";
    public static final String CUSTOMER_LASTNAME = "CUSTOMER_LASTNAME";

  }

  public static final class Tables {

    public static final String PROJECT = "project";
    public static final String PERSON = "person";
    public static final String STAGE_EMPLOYEE = "stage_person";
    public static final String STAGE = "stage";
    public static final String AUTHORIZATION = "authorization";
    public static final String PROJECT_VIEW = "project_view";
  }
}
