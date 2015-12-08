/** An instance of this class represents an event that occurred on some
**  particular day and involved some particular principal (e.g. person or
**  organization).
**
**  Authors: Jackowitz & McCloskey (not to be confused with Lennon & McCartney)
*/
public class Event {

   // class constants (public)
   // ------------------------
   public static final String DELIMITER   = ",";
   public static final String REPLACEMENT = "_";

   // instance variables
   // ------------------
   private CalendarDate date;  // the date on which the event occurred
   private String principal;   // the entity involved in the event
   private String description; // a description of the event 
                               // (e.g., "birth", "wedding")

   // constructors
   // ------------

   /** Initializes this event to have the specified date, principal, and
   **  description. 
   */
   public Event(CalendarDate theDate, String thePrincipal, String theDescription) {
      date = theDate;  
      principal = thePrincipal;  
      description = theDescription;
   }

   /** Initializes this event to be that described by the specified string,
   **  which is interpreted to be in three parts, separated by occurrences of
   **  the DELIMITER character.  The first part describes the date of the event
   **  (and should be in "canonical" form (as produced by CalendarDate's
   **  toString() method) and the second and third parts provide the principal
   **  and description of the event.  
   **
   **  This alternate constructor method is intended to be given previous results
   **  obtained from the toString() method.
   **
   */
   public Event(String delimited) {
      String[] field = delimited.split(DELIMITER);
      if(field.length != 3) {
         throw new IllegalArgumentException("invalid format");      
      } 
      else {
         date = new CalendarDate(field[0]);
         principal = replaceAll(field[1],REPLACEMENT,DELIMITER);
         description = replaceAll(field[2],REPLACEMENT,DELIMITER);
      }
   }

   /* Returns the date (in the form of a CalendarDate object) of this event. */
   public CalendarDate dateOf() { 
      return date; }

   /* Returns the principal of this event. */
   public String principalOf() { 
      return principal; }

   /* Returns the description of this event. */
   public String descriptionOf() { 
      return description; }

   /** Returns a three-part string, with the parts separated by the DELIMITER
   **  character, identifying this event.  First is the event's date, followed
   **  by its principal and description.  All instances of the DELIMITER in
   **  the parts are replaced by the REPLACEMENT character.
   **  (Significantly, if the resulting string is fed to the constructor, 
   **  an object identical to this one will emerge.)
   */
   public String toString() {
      return toString(REPLACEMENT);
   }

   /** A specialized variant method that allows the client to specify the 
       character to be used to replace the DELIMTIER character in the
       resultant string.
   */
   public String toString(String delimiterReplacement) {
      return dateOf() + DELIMITER + 
             replaceAll(principalOf(),DELIMITER,delimiterReplacement) + DELIMITER + 
             replaceAll(descriptionOf(),DELIMITER,delimiterReplacement);
   }


   // mutators
   // --------

   // There are no mutator methods; instances of Event are immutable!


   // private method
   // --------------
   
   /* Returns a string that is a duplicate of the specified string, except that
   ** every occurrence of the delimiter character has been replaced by an
   ** occurrence of the replacement character.
   */
   private String replaceAll(String s, String delimiter, String replacement) {
      return s.replaceAll(delimiter,replacement);
   }
}
