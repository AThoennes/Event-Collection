/* Java class that presents a collection of static methods, each one being related to
** calendar dates.  This class is essentially a "toolkit" of useful methods.
** 
** Authors: R.W.M. and P.M.J. 
** Date: Oct. 23, 2015
*/

public class CalendarDateOperations {

   private static char SPACE = ' ';
   private static char DASH = '-';
   private static char SLASH = '/';
   private static char COMMA = ',';
   private static int  MONTHS_IN_YEAR = 12;

   /* Returns true if the given integer values for month, and year are valid, and  
   ** false otherwise.  To be valid, the month must be in the range 1..12 and the 
   ** year must be in the range 1..9999.
   */
   public static boolean isValidMonthYear(int month, int year) {
      return (0 < month)  &&  (month <= MONTHS_IN_YEAR)  &&  
             (0 < year)   &&  (year <= 9999);
   }

   /* Returns true if the given integer values for month, day, and year describe a valid 
   ** calendar date, and false otherwise.  To be valid, the month must be in the range 
   ** 1..MONTHS_IN_YEAR and the day must be in the range 1..monthDays(m,y) and the year 
   ** must be in the range 1..9999.
   */
   public static boolean isValidMonthDayYear(int month, int day, int year) {
      return isValidMonthYear(month, year)  &&  
             ((0 < day) &&  (day <= monthDays(month,year)));
   }

   /* Given a month number (1 is Jan, 2 is Feb, etc.) and a year, returns 
   ** the number of days in that month of that year.
   */
   public static int monthDays(int month, int year) {
      int result = 0;
      if(isValidMonthYear(month,year)) {
         if (month == 2) {   // February
            if (isLeapYear(year)) { 
               result = 29; 
            }
            else { 
               result = 28; 
            }
         }
         else if (month == 4 || month == 6 || month == 9 || month == 11) {
         // April, June, September, or November
            result = 30;
         }
         else {   // month is one of 1, 3, 5, 7, 8, 10, or 12 (a 31-day month)
            result = 31;
         }
      }
      return result;
   }

   /* Returns true if the given year is a leap year, false otherwise.
   ** A year is a leap year if it is divisible by 400, or if it is
   ** divisible by 4 but not by 100.
   */
   public static boolean isLeapYear(int year) {
      return (year % 400 == 0) || (year % 4 == 0  &&  year % 100 != 0);
   }

   private static final String MONTH_NAMES = 
   " JANUARY FEBRUARY MARCH APRIL MAY JUNE JULY AUGUST SEPTEMBER OCTOBER NOVEMBER DECEMBER "; 

   /* Returns true if and ony if the given string is one of the twelve
   ** abbreviations for a month.
   */
   public static boolean isValidMonthAbbreviation(String monthAbbreviation) {
      int monthNumber = numberOfMonthAbbreviation(monthAbbreviation);
      return (monthNumber >= 1) && (monthNumber <= 12);   
   }

   /* Returns the month number corresponding to the abbreviation for the month
   ** whose abbreviation is equivalent to the given string.  If the string does
   ** not match any of the twelve abbreviations then zero is returned.
   */
   public static int numberOfMonthAbbreviation(String monthAbbreviation) {
      int result = 0;
      int index = MONTH_NAMES.indexOf(SPACE+monthAbbreviation.toUpperCase());
      for(int start=0; start<=index; start=MONTH_NAMES.indexOf(SPACE,start+1)) {
         result = result + 1;
      }
      return result;
   }

   /* Returns true if and only if the given string is one of the twelve
   ** names for a month.
   */
   public static boolean isValidMonthName(String monthName) {
      int monthNumber = numberOfMonthName(monthName);
      return (monthNumber >= 1) && (monthNumber <= 12);
   }

   /* Returns the month number corresponding to the month name that is equivalent to 
   ** the given string.  If the string does not match any of the twelve month names
   ** then zero is returned.
   */
   public static int numberOfMonthName(String monthName) {
      int result = 0;
      int index = MONTH_NAMES.indexOf(SPACE+monthName.toUpperCase()+SPACE);
      for(int start=0; start<=index; start=MONTH_NAMES.indexOf(SPACE,start+1)) {
         result = result + 1;
      }
      return result;
   }
   
   /* Returns the name of the month indicated by the specified month number.
   ** For example, month 11 is "November".
   */
   public static String nameOfMonthNumber(int monthNumber) {
      monthNumber = monthNumber - 1;
      String result = "???";
      int index;
      for(index=0; 
          (index < MONTH_NAMES.length()) && (monthNumber > 0); 
          index=MONTH_NAMES.indexOf(SPACE,(index+1))) {
         monthNumber = monthNumber - 1;
      }
      index = index + 1;
      if(index < MONTH_NAMES.length()) {
         result = MONTH_NAMES.substring(index,MONTH_NAMES.indexOf(SPACE,index));
      }
      return result;
   }

   /* Returns the 3-letter abbreviation of the month indicated by the 
   ** specified month number.  For example, month 11 is "NOV".
   */
   public static String abbreviationOfMonthNumber(int monthNumber) {
      return nameOfMonthNumber(monthNumber).substring(0,3).toUpperCase();
   }

}
