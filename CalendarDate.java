import java.text.DecimalFormat;  // for formatting numbers

/** An instance of this Java class represents a calendar date, such as
**  March 15, 1967.  A client gives a String as the actual argument to the
**  constructor to specify the date value, and this date value must be in
**  one of the following forms:
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
**
**  Observer methods exist by which a client can obtain a date's month (either
**  as an integer, its name as a String or its abbreviation as a String), day, and year.
**  Other observers provide a description of the date in any of the forms listed above.
**
**  Two dates can be compared for equality or to determine whether one is 
**  "earlier than" another.  Also, two dates can be compared to determine the number 
**  of days separating these two dates.
**
*/

public class CalendarDate {

   // instance variables 
   private int year;    // e.g., 1979
   private int month;   // e.g., 10 for October
   private int day;     // e.g., 14 for the 14th day of the month

   // global symbolic constants
   private static final char COMMA = ',';
   private static final char DASH = '-';
   private static final char SLASH = '/';
   private static final char SPACE = ' ';

   private static final int MONTHS_IN_YEAR = 12;
   
   // For producing numerals (Strings of digit characters) of lengths two
   // and four, with leading zeros when appropriate.
   private static DecimalFormat twoDigits = new DecimalFormat("00");
   private static DecimalFormat fourDigits = new DecimalFormat("0000");

   private final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "Invalid date value";

   private final String DEFAULT_VALUE_IN_CANONICAL_FORM = "00010101";    // January  1, 1
   

// -------------------------------------------------------------------------------------
// C o n s t r u c t o r    M e t h o d s
// -------------------------------------------------------------------------------------

   /** Initializes this object to the date expressed by the given string.
   *** If the given string does not describe a valid date, an
   *** IllegalArgumentException is thrown.
   */
   public CalendarDate(String dateValue) {
      String canonicalForm = parseDateValue(dateValue);
      setDate(canonicalForm); 
   }


   public void reset() {
      year = 1; month = 1; day = 1;
   
   }

// -------------------------------------------------------------------------------------
// O b s e r v e r   M e t h o d s
// -------------------------------------------------------------------------------------

   /** Returns this date's year number.
   **/
   public int getYear() { 
      return year; }

   /** Returns this date's month number (e.g., 1 for January, 2 for February).
   **/
   public int getMonth() { 
      return month; }

   /** Returns this date's day of the month number.
   **/
   public int getDay() { 
      return day; }

   /** Returns this date's month name (e.g., "JANUARY", "NOVEMBER").
   **/
   public String getMonthName() {
      return CalendarDateOperations.nameOfMonthNumber(getMonth());
   }

   /** Returns this date's month abbreviation (e.g., "JAN", "NOV").
   **/
   public String getMonthAbbreviation() {
      return CalendarDateOperations.abbreviationOfMonthNumber(getMonth());
   }

   /** Returns the string in the default format, YYYYMMDD) describing this date.
   *** For example, "20100514" is such a string (describing May 14, 2010).
   **/
   public String toString() { 
      return toString_YYYYMMDD(); 
   }

   /** Returns the string in the YYYYMMDD format describing this date.
   *** For example, "2010-05-14" is such a string (describing May 14, 2010).
   **/
   public String toString_YYYYMMDD() {
      String yearStr = fourDigits.format(getYear());
      String monthStr = twoDigits.format(getMonth());
      String dayStr = twoDigits.format(getDay());
      return yearStr + monthStr + dayStr;
   }

   /** Returns the string in the y-mm-dd format describing this date.
   *** For example, "123-05-14" is such a string (describing May 14, 123).
   **/
   public String toString_yMMDD() {
      String yearStr = "" + getYear();
      String monthStr = twoDigits.format(getMonth());
      String dayStr = twoDigits.format(getDay());
      return yearStr + monthStr + dayStr;
   }

   /** Returns the calendar date as a string in the form Month_d_y.
   *** For example, "May 4, 2010" is such a string.
   **/
   public String toString_Month_d_y() {
      return CalendarDateOperations.nameOfMonthNumber(getMonth()) + SPACE + getDay() + COMMA + SPACE + getYear();
   }

   /** Returns the calendar date as a string in the form d_Month_y.
   *** For example, "4 August 2010" is such a string.
   **/
   public String toString_d_Month_y() {
      return "" + getDay() + SPACE +CalendarDateOperations.nameOfMonthNumber(getMonth()) + SPACE + getYear();
   }

   /** Returns the calendar date as a string in the form m/d/y.
   *** For example, "11/4/2010" is such a string.
   **/
   public String toString_m_d_y() {
      return "" + getMonth() + SLASH + getDay() + SLASH + getYear();
   }

   /** Returns the calendar date as a string in the form d-Mon-y.
   *** For example, "4-Aug-2010" is such a string.
   **/
   public String toString_d_Mon_y() {
      return "" + getDay() + DASH + CalendarDateOperations.abbreviationOfMonthNumber(getMonth()) + DASH + getYear();
   }

   /** Returns the calendar date as a string in the form DD-Mon-YYYY.
   *** For example, "04-Aug-2010" is such a string.
   **/
   public String toString_DD_Mon_YYYY() {
      String yearStr = "" + fourDigits.format(getYear());
      String dayStr = twoDigits.format(getDay());
      return "" + dayStr + DASH + CalendarDateOperations.abbreviationOfMonthNumber(getMonth()) + DASH + yearStr;
   }

   /** Returns the calendar date as a string in the form y-m-d.
   *** For example, "2010-4-11" is such a string.
   **/
   public String toString_y_m_d() {
      return "" + getYear() + DASH + getMonth() + DASH + getDay();
   }

   /** Returns true if this date and the specified date are equal, false
   *** otherwise.  (Two dates are equal if, and only if, they represent the
   *** same calendar date (such as April 25, 1956).)
   */
   public boolean equals(CalendarDate that) {
      return getYear() == that.getYear() &&
             getMonth() == that.getMonth() &&
             getDay() == that.getDay();
   }

   /** Returns true if this date occurs earlier than the specified date,
   *** false otherwise.  For example, April 25, 1956 is earlier than
   *** January 2, 1895 but is not earlier than October 3, 2010.
   */
   public boolean isEarlierThan(CalendarDate date) {
      return getYear() < date.getYear()  ||
             (getYear() == date.getYear() &&
              (getMonth() < date.getMonth() ||
               (getMonth() == date.getMonth() && getDay() < date.getDay())
              )
             );
   
      //alternative method body:
      /*
        int thisYear = getYear(), dateYear = date.getYear();
        if (thisYear != dateYear) { return thisYear < dateYear; }
        else {
           int thisMonth = getMonth(), dateMonth = date.getMonth();
           if (thisMonth != dateMonth) { return thisMonth < dateMonth; }
           else { return getDay() < date.getDay(); }
        }
      */   
   }

   /** Returns true if this date occurs later than the specified date,
   *** false otherwise.  For example, April 25, 1956 is later than
   *** January 2, 1895 but is not later than October 3, 2010.
   */
   public boolean isLaterThan(CalendarDate date) {
      return getYear() > date.getYear()  ||
             (getYear() == date.getYear() &&
              (getMonth() > date.getMonth() ||
               (getMonth() == date.getMonth() && getDay() > date.getDay())
              )
             );
   }

   /** Returns the difference, measured in days, between this date and the
   *** specified date.  The magnitude of the difference is the number of
   *** days one would have to go forward, or backwards, respectively, from
   *** this date in order to reach the specified date, depending upon whether
   *** this date was earlier than, or not earlier than, respectively, the
   *** specified date.  The sign of the difference is negative if this date
   *** is earlier than the specified one, but positive otherwise.
   *** Example: The difference between April 10, 2002 and March 31, 2001
   *** is 375 days.  The difference between March 31, 2001 and April 10, 2002
   *** is -375 days.
   **/
   public int compareTo(CalendarDate date)
   {
      if (isEarlierThan(date)) { 
         return -date.daysDifference(this);
      }
      else { 
         return daysDifference(date);
      }
   }

// -------------------------------------------------------------------------------------
// P r i v a t e   M e t h o d s
// -------------------------------------------------------------------------------------

   /** Tries each of the "parser" methods available in the CalendarDateParsers class in
   **  turn attempting to recognize the given date value.  If one of these methods
   **  succeeds then the "canonical form" of the date value is returned as the result.
   **  If the given date value can not be recognized then the default value is returned.
   */
   private String parseDateValue(String dateValue) {
      String result = CalendarDateParsers.parse_YYYYMMDD(dateValue);
      if(!isValidDateInCanonicalForm(result)) {   
         result = CalendarDateParsers.parse_yMMDD(dateValue);
         if(!isValidDateInCanonicalForm(result)) {
            result = CalendarDateParsers.parse_Month_d_y(dateValue);
            if(!isValidDateInCanonicalForm(result)) {
               result = CalendarDateParsers.parse_d_Month_y(dateValue);
               if(!isValidDateInCanonicalForm(result)) {
                  result = CalendarDateParsers.parse_m_d_y(dateValue);
                  if(!isValidDateInCanonicalForm(result)) {
                     result = CalendarDateParsers.parse_d_Mon_y(dateValue);
                     if(!isValidDateInCanonicalForm(result)) {
                        result = CalendarDateParsers.parse_y_m_d(dateValue);
                        if(!isValidDateInCanonicalForm(result)) {
                           result = DEFAULT_VALUE_IN_CANONICAL_FORM;
                           //throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
                        }
                     }
                  }
               }
            }
         }
      }
      return result;
   }

   /* Sets this date to that described by the three arguments for month (m),
   ** day (d), and year (y), which are assumed to describe a valid date.
   */
   private void setDateTo(int m, int d, int y) { 
      month = m; day = d; year = y;
   }

   private boolean isValidDateInCanonicalForm(String canonicalForm) {
      setDate(canonicalForm);
      return CalendarDateOperations.isValidMonthDayYear(month,day,year);
   }

   private void setDate(String canonicalForm) {
      year  = Integer.parseInt(canonicalForm.substring(0,4));
      month = Integer.parseInt(canonicalForm.substring(4,6));
      day   = Integer.parseInt(canonicalForm.substring(6,8));
   }

   /** Returns the difference, measured in days, between this date and the
   **  specified date.  It is assumed that the former is not earlier than
   **  the latter.
   */
   private int daysDifference(CalendarDate date) {
      int result = 0;
      CalendarDate dateCopy = new CalendarDate(date.toString());
      while (!this.equals(dateCopy)) { 
         dateCopy.advanceDateValue(); 
         result = result + 1;
      }
      return result;
   }

   /** Moves this date forward by one day (e.g., from April 30, 1956 to
   *** May 1, 1956).
   **/
   private void advanceDateValue() {
      int monthOf = getMonth();  // store values of getMonth(), getDay() and 
      int dayOf = getDay();      // getYear() to avoid repeated calls to
      int yearOf = getYear();    // those methods.
   
      if (dayOf < CalendarDateOperations.monthDays(monthOf, yearOf)) {   // usual case
         setDateTo(monthOf, dayOf+1, yearOf);
      }
      else if (monthOf == MONTHS_IN_YEAR) {     // case of Dec. 31
         setDateTo(1, 1, yearOf+1);
      }
      else {  // last day of month, but not December
         setDateTo(monthOf+1, 1, yearOf);
      }
   }

}
