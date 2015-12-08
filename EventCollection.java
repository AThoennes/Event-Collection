/** An instance of this class represents a collection of events (i.e., instances
**  of the Event class).  Initially, a collection is empty; events are placed 
**  into a collection via calls to its insert() method.  Events in a collection
**  are accessed via iteration.  A client can iterate over the events in a
**  collection in any of four orders:
**
**  --the order in which the events were inserted into the collection
**  --chronological order (i.e., the order in which the events occurred, 
**    as based upon the calendar dates associated to them)
**  --alphabetical order by principal
**  --alphabetical order by description
**
**  Each of these corresponds to an iteration mode, the distinct values of which
**  are named by public class constants.  The client, when wishing to begin an 
**  iteration, must call the reset() method and indicate the desired mode via
**  a parameter.  (The default mode is ITERATE_BY_INSERTION.)
**
**  Inserting an event into a collection aborts any active iteration over that
**  collection, making the iteration inactive.  To begin a new iteration, one
**  of the reset() methods must be called.
**
**  An event collection has a fixed capacity (meaning the maximum number of
**  events that can be inserted into it) that is established at creation.
**  This capacity can be chosen by the client (by using the one-argument
**  constructor) or it can be set to a default value (by using the no-argument
**  constructor). 
*
* By: Alex Thoennes
*/
public class EventCollection {

   // class constants (for iteration modes)
   // -------------------------------------
   private static final int ITERATE_INACTIVE = 0;      // private
   public static final int ITERATE_BY_INSERTION = 1;   // the rest are public
   public static final int ITERATE_BY_DATE = 2;
   public static final int ITERATE_BY_PRINCIPAL = 3;
   public static final int ITERATE_BY_DESCRIPTION = 4;


   // class constant (for default collection capacity)
   // ------------------------------------------------
   private static final int DEFAULT_CAPACITY = 16;


   // instance variables
   // ------------------
   private int size;       // # of events in this collection

   private Event[] events; // Array holding the events in this collection.
                           // They are stored in event[0..size-1].

   private boolean[] marked;   // marked[k] is true iff events[k] has already
                               // been returned during the current iteration.

   private int markedCount;    // # of marked events

   private int iterationMode;  // the current iteration mode
   

   // constructors
   // ------------

   /** Initializes this collection to be empty and to have the default capacity.
   */
   public EventCollection() { this(DEFAULT_CAPACITY); }


   /** Initializes this collection to be empty and to have the specified 
   **  capacity.
   */
   public EventCollection(int capacity) {
      size = 0;
      events = new Event[capacity];

      marked = new boolean[capacity];

      iterationMode = ITERATE_INACTIVE;
   }

   // observers
   // ---------

   /** Returns the number of events in the collection.
   */
   public int sizeOf() 
   {
	   int num = 0;
	   for (int i = 0; i < events.length; i++)
	   {
		   if (events[i] != null)
		   {
			   num ++;
		   }
	   }
	   size = num;
	   return size; 
   }


   /** Returns the capacity of this collection, i.e., the maximum
   **  number of elements that can be inserted into it.
   */
   public int capacityOf() { return events.length; }


   // mutator
   // -------

   /** Inserts the given event into this collection.
   **  If an iteration is active, it becomes inactive.
   **  
   */
   public void insert(Event e) {
      if (sizeOf() == capacityOf()) {
         throw new IllegalStateException("event collection already full");
      }
      else {
    	  events[size] = e;
    	  size = size + 1;
      }
   }  


   // iteration-related methods
   // -------------------------

   /** Resets iteration to begin afresh, using the default iteration mode.
   */
   public void reset() { 
      reset(ITERATE_BY_INSERTION);   // Call the other reset() method!
   }

   /** Resets iteration to begin afresh, with the parameter specifying the
   **  iteration mode.  (An exception is thrown if the parameter value does
   **  not correspond to any of the four iteration mode values as defined by
   **  the relevant class constants.)
   */
   public void reset(int iterMode) {
      if (iterMode < 0 || iterMode > 4) 
      {
         throw new IllegalArgumentException("Illegal iteration mode value");
      }
      else {
         iterationMode = iterMode;
         markedCount = 0;
         fill(marked, 0, size, false); // Fill marked[0..size) with false's
      }
   }

   
   /** Returns true if and only if there is an active iteration that has
   **  at least one more element to iterate over.
   */
   public boolean hasNext()
   {
	   if (markedCount != size)
	   {
		   return true;
	   }
	   else
	   {
		   return false;
	   }
   }


   /** Returns the next event in the iteration.
   **  pre-condition: hasNext()
   */
   public Event next() {
	  int k;
      if (iterationMode == ITERATE_BY_INSERTION) {
         k = markedCount;
      }
      else if (iterationMode == ITERATE_BY_DATE) {
         k = indexOfNextByDate();
      }
      else if (iterationMode == ITERATE_BY_PRINCIPAL) {
         k = indexOfNextByPrincipal();
      } 
      else {  // (iterationMode == ITERATE_BY_DESCRIPTION) 
         k = indexOfNextByDescription();
      }

      marked[k] = true;
      markedCount++;

      return events[k];
   }


   // private methods
   // ---------------


   /* Returns the location, within events[], of an unmarked event having the
   ** earliest calendar date among all unmarked events in the collection.
   ** More formally:
   ** Returns k satisfying 0<=k<size && !marked[k] &&
   ** (forall m | 0<=m<size && !marked[m] : 
   **             events[k].dateOf().compareTo(events[m].dateOf()) <= 0)
   ** 
   ** pre-condition: hasNext()
   */
   private int indexOfNextByDate()
   {
	   int j = 0;
	   
	   while (marked[j])
	   {
		   j++;
	   }
	   
	   int current = j;

	   for (int i = j; i < size; i++)
	   {
		   if ((events[current].dateOf().isLaterThan(events[i].dateOf())) && !marked[i])
		   {
			   current = i;
		   }
	   }
	   return current;
   }

   /* Returns the location, within events[], of an unmarked event having the
   ** alphabetically smallest principal among all unmarked events in the 
   ** collection.  More formally:
   ** Returns k satisfying 0<=k<size && !marked[k] &&
   ** (forall m | 0<=m<size && !marked[m] : 
   **             events[k].principalOf().compareTo(events[m].principalOf()) <= 0)
   **
   ** pre-condition: hasNext()
   */
   private int indexOfNextByPrincipal()
   {
	   int j = 0;
	   
	   while (marked[j])
	   {
		   j++;
	   }
	   
	   int current = j;
	   
	   for (int i = j; i < size; i++)
	   {
		   /* if events[current] compared to events[i] < 0......current precedes the argument
		    * else if events[current] compared to events[i] > 0.......current follows the argument
		    * else if events[current] compared to events[i] = 0........they are equal
		    */
		   
		   if (events[i].principalOf().compareTo(events[current].principalOf()) <= 0 && !marked[i])
		   {
			   current = i;
		   }		   
	   }
	   return current;
   }

   /* Returns the location, within events[], of an unmarked event having the
   ** alphabetically smallest description among all unmarked events in the 
   ** collection.  More formally:
   ** Returns k satisfying 0<=k<size && !marked[k] &&
   ** (for all m | 0<=m<size && !marked[m] : 
   **             events[k].descriptionOf().compareTo(events[m].descriptionOf()) <= 0)
   **
   ** pre-condition: hasNext()
   */
   private int indexOfNextByDescription()
   {
	   int j = 0;
	   
	   while (marked[j])
	   {
		   j++;
	   }
	   
	   int current = j;
	   
	   for (int i = j; i < size; i++)
	   {
		   /* if events[current] compared to events[i] < 0......current precedes the argument
		    * else if events[current] compared to events[i] > 0.......current follows the argument
		    * else if events[current] compared to events[i] = 0........they are equal
		    */
		   if (events[i].descriptionOf().compareTo(events[current].descriptionOf()) <= 0 && !marked[i])
		   {
			   current = i;
		   }
	   }
	   return current;
   }

   /* Returns the lowest-numbered location in b[] containing an element equal
   ** to val, or, if val does not occur in b[], returns b.length. 
   ** More formally:
   ** Returns the smallest k such that 0 <= k <= b.length and either
   ** k == b.length or b[k] == val.
   */
   private int indexOf(boolean[] b, boolean val)
   {
      int i = 0;
      while (i != b.length  &&  b[i] != val) { 
         i++;
      }
      return i;
   }

   /* Places the specified value into each element of b[lowIndex..highIndex-1].
   */
   private void fill(boolean[] b, int lowIndex, int highIndex, boolean value) {
      for (int i = lowIndex; i < highIndex; i++) 
         { b[i] = value; }
   }

}
