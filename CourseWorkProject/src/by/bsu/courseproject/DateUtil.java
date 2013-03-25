package by.bsu.courseproject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Artyom Strok
 * Date: 25.03.13
 * Time: 22:49
 */
public class DateUtil {
  public static final String DATE_FORMAT = "yyyy-MM-dd";

  public static Date stringToDate(String date){
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    Date result = null;
    try {
      result = dateFormat.parse(date);
    } catch (ParseException e) {
      System.out.println("Parse date error");
    }
    return result;
  }

  public static String dateToString(Date date){
    String result = null;
    return result;
  }

}
