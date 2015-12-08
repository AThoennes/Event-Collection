/* Java class that presents a collection of static methods, each of which is
** capable of converting a calendar date expressed by a string of a particular
** form into an equivalent calendar date value expressed in the form YYYYMMDD.
** 
** The following forms are supported:
**
**   ID           Form          Examples
** ---------    ----------    ----------------------------------------------------------------
** Month_d_y    Month_d,_y    December 25, 1978  July 4, 1776    June 06,1942    April 01, 123
** d_Month y    d_Month_y     25 December 1978   4 July 1776     06 June 1942    01 April 123 
** d_Mon_y      d-Mon-y       25-Dec-1878        4-Jul-1776      06-Jun-1944     01-Apr-123   
** m_d_y        m/d/y         12/25/1978         7/4/1776        06/06/1944      04/01/123    
** y_m_d        y-m-d         1978-12-25         1776-7-4        1944-06-06      123-04-01    
** yMMDD        yMMDD         19781225           17760704        19440606        1230401     
** YYYYMMDD     YYYYMMDD      19781225           17760704        19440606        01230401
**
** Note the following in the above forms:
**    - y, m and d each represent numerals with one or more digits, allowing for leading zeroes
**    - each Y, M and D represents exactly one digit
**    - Mon represents one of the three character abbreviations for a month name
**    - Month represents the full name of a month
**
** Authors: R.W.M. and P.M.J. 
** Date: Oct. 24, 2015
*/

public class CalendarDateParsers {

   public static String INVALID_RESULT = "00000000";

   private static char SPACE = ' ';
   private static char DASH = '-';
   private static char SLASH = '/';
   private static char COMMA = ',';
   private static int  MONTHS_IN_YEAR = 12;

   /* Given a calendar date as described by a string in YYYYMMDD form,
   ** returns the same date as described in the YYYYMMDD form.
   ** If the given string is not in the required form, or if the
   ** calendar date it describes is not semantically valid (e.g., April 31
   ** of some year), the value returned is INVALID_RESULT (as defined above).
   **
   ** Examples:  "19620413" maps to "19620413"
   **            "18561207" maps to "18561207"
   **            "12340230" maps to "00000000" 
   */
   public static String parse_YYYYMMDD(String dateStr) {
      String result = INVALID_RESULT;
      if(dateStr.length() == 8) {
         String yearStr  = dateStr.substring(0,4);
         String monthStr = dateStr.substring(4,6);
         String dayStr   = dateStr.substring(6,8);
         
         if (isUnsignedIntNumeral(yearStr) &&
             isUnsignedIntNumeral(monthStr) &&
             isUnsignedIntNumeral(dayStr)) { 
            // The given calendar date string is syntactically valid!
            // Now check for logical validity.
            // Compute the integers corresponding to year, month, and day
            int year = Integer.parseInt(yearStr);
            int month = numberOfMonth(monthStr);
            int day = Integer.parseInt(dayStr);
            result = canonicalFormOf(year, month, day);
         }
      }
      return result;
   }

   /* Given a calendar date as described by a string in Month_d_y form,
   ** returns the same date as described in the YYYYMMDD form.
   ** If the given string is not in the required form, or if the
   ** calendar date it describes is not semantically valid (e.g., April 31
   ** of some year), the value returned is INVALID_RESULT (as defined above).
   **
   ** Examples:  "April 13, 1962" maps to "19620413"
   **            "December 7,1856" maps to "18561207" 
   */
   public static String parse_Month_d_y(String dateStr) {
      String result = INVALID_RESULT;
   
      int posOfSpace = dateStr.indexOf(SPACE);
      int posOfComma = dateStr.indexOf(COMMA);
   
      if ((posOfSpace != -1)  &&  (posOfComma != -1) && (posOfSpace < posOfComma) ) {
         // There is a space before a comma, so extract the substrings to the
         // left of the space, to the right of the comma, and in between the two.
         String dayStr = dateStr.substring(posOfSpace+1, posOfComma);
         String monthStr = dateStr.substring(0, posOfSpace);
         String yearStr = dateStr.substring(posOfComma+1).trim();  //NOTE the trim here!
      
         if (isUnsignedIntNumeral(yearStr) &&
             isValidMonth(monthStr) &&
             isUnsignedIntNumeral(dayStr)) { 
            // The given calendar date string is syntactically valid!
            // Now check for logical validity.
         
            // Compute the integers corresponding to year, month, and day
            int year = Integer.parseInt(yearStr);
            int month = numberOfMonth(monthStr);
            int day = Integer.parseInt(dayStr);
            result = canonicalFormOf(year, month, day);
         }
      }
      return result;
   }

   /* Given a calendar date as described by a string in d_Month_y form,
   ** returns the same date as described in the YYYYMMDD form.
   ** If the given string is not in the required form, or if the
   ** calendar date it describes is not semantically valid (e.g., April 31
   ** of some year), the value returned is INVALID_RESULT (as defined above).
   **
   ** Examples:  "15 April 2013" maps to "20130415"
   **            "8 March 896" maps to "08960408" 
   **            "5 November 10213" maps to "00000000"
   */
   public static String parse_d_Month_y(String dateStr) {
      String result = INVALID_RESULT;
   
      int posOfFirstSpace = dateStr.indexOf(SPACE);
      int posOfLastSpace = dateStr.lastIndexOf(SPACE);
   
      if ((posOfFirstSpace != -1)  &&  (posOfFirstSpace != posOfLastSpace) ) {
         // There are at least two spaces, so extract the substrings to the
         // left of the first, to the right of the last, and in between the two.
         String dayStr = dateStr.substring(0, posOfFirstSpace);
         String monthStr = dateStr.substring(posOfFirstSpace+1, posOfLastSpace);
         String yearStr = dateStr.substring(posOfLastSpace+1);
      
         if (isUnsignedIntNumeral(yearStr) &&
             isValidMonth(monthStr) &&
             isUnsignedIntNumeral(dayStr)) { 
            // The given calendar date string is syntactically valid!
            // Now check for logical validity.
         
            // Compute the integers corresponding to year, month, and day
            int year = Integer.parseInt(yearStr);
            int month = numberOfMonth(monthStr);
            int day = Integer.parseInt(dayStr);
            result = canonicalFormOf(year, month, day);
         }
      }
      return result;
   }

   /* Given a calendar date as described by a string in m_d_y form,
   ** returns the same date as described in the YYYYMMDD form.
   ** If the given string is not in the required form, or if the
   ** calendar date it describes is not semantically valid (e.g., April 31
   ** of some year), the value returned is INVALID_RESULT (as defined above).
   **
   ** Examples:  "04/15/2013" maps to "20130415"
   **            "4/8/896" maps to "08960408" 
   **            "11/5/213" maps to "2131105"
   */
   public static String parse_m_d_y(String dateStr) {
      String result = INVALID_RESULT;
   
      int posOfFirstSlash = dateStr.indexOf(SLASH);
      int posOfLastSlash  = dateStr.lastIndexOf(SLASH);
   
      if ((posOfFirstSlash != -1)  &&  (posOfFirstSlash != posOfLastSlash) ) {
         // There are at least two slashes, so extract the substrings to the
         // left of the first, to the right of the last, and in between the two.
         String yearStr = dateStr.substring(posOfLastSlash+1);
         String monthStr = dateStr.substring(0,posOfFirstSlash);
         String dayStr = dateStr.substring(posOfFirstSlash+1,posOfLastSlash);
      
         if (isUnsignedIntNumeral(yearStr) &&
             isUnsignedIntNumeral(monthStr) &&
             isUnsignedIntNumeral(dayStr)) { 
            // The given calendar date string is syntactically valid!
            // Now check for logical validity.
         
            // Compute the integers corresponding to year, month, and day
            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            int day = Integer.parseInt(dayStr);
            result = canonicalFormOf(year, month, day);
         }
      }
      return result;
   }

   /* Given a calendar date as described by a string in d_Mon_y form,
   ** returns the same date as described in the YYYYMMDD form.
   ** If the given string is not in the required form, or if the
   ** calendar date it describes is not semantically valid (e.g., April 31
   ** of some year), the value returned is INVALID_RESULT (as defined above).
   **
   ** Examples:  "13-Apr-1962" maps to "19620413"
   **            "7-Dec-1856" maps to "18561207" 
   */
   public static String parse_d_Mon_y(String dateStr) {
      String result = INVALID_RESULT;
   
      int posOfFirstDash = dateStr.indexOf(DASH);
      int posOfLastDash = dateStr.lastIndexOf(DASH);
   
      if ((posOfFirstDash != -1)  &&  (posOfFirstDash != posOfLastDash) ) {
         // There are at least two dashes, so extract the substrings to the
         // left of the first, to the right of the last, and in between the two.
         String dayStr = dateStr.substring(0, posOfFirstDash);
         String monthStr = dateStr.substring(posOfFirstDash+1, posOfLastDash);
         String yearStr = dateStr.substring(posOfLastDash+1);
      
         if (isUnsignedIntNumeral(yearStr) &&
             isValidMon(monthStr) &&
             isUnsignedIntNumeral(dayStr)) { 
            // The given calendar date string is syntactically valid!
            // Now check for logical validity.
         
            // Compute the integers corresponding to year, month, and day
            int year = Integer.parseInt(yearStr);
            int month = numberOfMon(monthStr);
            int day = Integer.parseInt(dayStr);
            result = canonicalFormOf(year, month, day);
         }
      }
      return result;
   }

   /* Given a calendar date as described by a string in y-m-d form,
   ** returns the same date as described in the YYYYMMDD form.
   ** If the given string is not in the required form, or if the
   ** calendar date it describes is not semantically valid (e.g., April 31
   ** of some year), the value returned is INVALID_RESULT (as defined above).
   **
   ** Examples:  "1978-11-05" maps to "19781105"
   **            "2013-3-7" maps to "20130307"
   */
   public static String parse_y_m_d(String dateStr) {
      String result = INVALID_RESULT;
   
      int posOfFirstDash = dateStr.indexOf(DASH);
      int posOfLastDash = dateStr.lastIndexOf(DASH);
   
      if ((posOfFirstDash != -1)  &&  (posOfFirstDash != posOfLastDash) ) {
         // There are at least two dashes, so extract the substrings to the
         // left of the first, to the right of the last, and in between the two.
         String yearStr = dateStr.substring(0, posOfFirstDash);
         String monthStr = dateStr.substring(posOfFirstDash+1, posOfLastDash);
         String dayStr = dateStr.substring(posOfLastDash+1);
      
         if (isUnsignedIntNumeral(yearStr) &&
             isUnsignedIntNumeral(monthStr) &&
             isUnsignedIntNumeral(dayStr)) { 
            // The given calendar date string is syntactically valid!
            // Now check for logical validity.
         
            // Compute the integers corresponding to year, month, and day
            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            int day = Integer.parseInt(dayStr);
            result = canonicalFormOf(year, month, day);
         }
      }
      return result;
   }

   /* Given a calendar date as described by a string in yMMDD form,
   ** returns the same date as described in the YYYYMMDD form.
   ** If the given string is not in the required form, or if the
   ** calendar date it describes is not semantically valid (e.g., April 31
   ** of some year), the value returned is INVALID_RESULT (as defined above).
   **
   ** Examples:  "19781105" maps to "19781105"
   **            "5470317" maps to "05470317"
   */
   public static String parse_yMMDD(String dateStr) {  
      String result = INVALID_RESULT;
      int length = dateStr.length();
      if(dateStr.length() >= 5) {
         String yearStr = dateStr.substring(0,(length-4));
         String monthStr = dateStr.substring((length-4),(length-2));
         String dayStr = dateStr.substring((length-2),length);
      
         if (isUnsignedIntNumeral(yearStr) && 
             isUnsignedIntNumeral(monthStr) &&
             isUnsignedIntNumeral(dayStr)) { 
            // The given calendar date string is syntactically valid!
            // Now check for logical validity.
         
            // Compute the integers corresponding to year, month, and day
            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            int day = Integer.parseInt(dayStr);
            result = canonicalFormOf(year, month, day);
         }
      }
      return result;
   }

// -------------------------------------------------------------------------------------
// P r i v a t e   M e t h o d s
// -------------------------------------------------------------------------------------

   /** If the given arguments represent a valid calendar date this method returns
    ** a string of length 8 that represents that date in "canonical form", that is
    ** in the form YYYYMMDD.  If the given calendar date is invalid then the
    ** INVALID-RESULT is returned. 
   */
   private static String canonicalFormOf(int year, int month, int day) {
      String result = INVALID_RESULT;
      if (CalendarDateOperations.isValidMonthDayYear(month, day, year)) {
         /* The date is semantically valid, so compute the YYYY, MM, and DD
         substrings to be concatenated to obtain the result. */
         String yearString = intToNumeral(year, 4);
         String monthString = intToNumeral(month, 2);
         String dayString = intToNumeral(day, 2);
         result = yearString + monthString + dayString;
      }
      return result;
   }

   /* Given a string, returns true if it has the form of an unsigned decimal 
   ** integer numeral, and false otherwise.  Specifically, such a numeral is a
   ** nonempty string of digit characters.  
   ** (The digit characters are '0', '1', ..., '9'.)
   */
   private static boolean isUnsignedIntNumeral(String str) {
      boolean result = true;
      if (str.length() == 0) { 
         result = false;
      }
      else {
         for (int i=0; i != str.length() && result; i = i+1) {
            if (!Character.isDigit(str.charAt(i))) { 
               result = false;
            }
         }
      }
      return result;
   
      // Superior way to do it?:
      // final int STR_LEN = str.length();
      // 
      // int i = 0;
      // while (i != STR_LEN  &&  Character.isDigit(str.charAt(i))) {
      //    i = i+1;
      // }
      // return STR_LEN != 0  &&  i == STR_LEN;
   }

   /* Returns a decimal numeral of at least the specified length (numDigits)
   ** that corresponds to the specified integer value (k), assumed to be
   ** nonnegative.  Leading zeros are used to pad the result up to the 
   ** desired length.
   ** Examples: k = 14 and numDigits = 3 results in "014" being returned.
   **           k = 3 and numDigits = 5 results in "00003" being returned.
   **           k = 2456 and numDigits = 2 results in "2456" being returned.
   */
   private static String intToNumeral(int k, int numDigits) {
      String result = Integer.toString(k);
      int startingLen = result.length();
      int leadingZerosNeeded = numDigits - startingLen;
      for (int i = 0; i < leadingZerosNeeded; i = i+1) {
         result = "0" + result;
      } 
      return result;
   }
   
   /* Returns true if and ony if the given string is one of the twelve
   ** abbreviations for a month.
   */
   private static boolean isValidMon(String s) {
      return CalendarDateOperations.isValidMonthAbbreviation(s);
   }

   /* Returns the month number corresponding to the abbreviation for the month
   ** whose abbreviation is equivalent to the given string.  If the string does
   ** not match any of the twelve abbreviations then zero is returned.
   */
   private static int numberOfMon(String s) {
      return CalendarDateOperations.numberOfMonthAbbreviation(s);
   }

   /* Returns true if and ony if the given string is one of the twelve
   ** names for a month.
   */
   private static boolean isValidMonth(String s) {
      return CalendarDateOperations.isValidMonthName(s);
   }

   /* Returns the month number corresponding to the month name that is equivalent to 
   ** the given string.  If the string does not match any of the twelve month names
   ** then zero is returned.
   */
   private static int numberOfMonth(String s) {
      return CalendarDateOperations.numberOfMonthName(s);
   }

}
