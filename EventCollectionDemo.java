public class EventCollectionDemo {

   public static void main(String[] args)
   {
      EventCollection eventColl = new EventCollection();
      doSomeInsertions(eventColl);

      System.out.println("Iterating in order of insertion:");
      System.out.println("--------------------------------");
      iterate(eventColl, EventCollection.ITERATE_BY_INSERTION);

      System.out.println("\nIterating in order by date:");
      System.out.println("---------------------------");
      iterate(eventColl, EventCollection.ITERATE_BY_DATE);

      System.out.println("\nIterating in order by principal:");
      System.out.println("--------------------------------");
      iterate(eventColl, EventCollection.ITERATE_BY_PRINCIPAL);

      System.out.println("\nIterating in order by description:");
      System.out.println("----------------------------------");
      iterate(eventColl, EventCollection.ITERATE_BY_DESCRIPTION);
   }

   /* Does a complete iteration over the specified event collection,
   ** using the specified iteration mode.  Each event is printed.
   */
   private static void iterate(EventCollection ec, int iterateMode)
   {
      ec.reset(iterateMode);
      while (ec.hasNext()) {
         Event e = ec.next();
         System.out.println(e);
      }
   }

   /* Inserts some events into the specified collection.
   */
   private static void doSomeInsertions(EventCollection ec) {
      ec.insert(new Event("1967-1-15,Green Bay Packers,Superbowl I"));
      ec.insert(new Event("6/23/1912,Alan Turing,Birth"));
      ec.insert(new Event("3/4/1801,Thomas Jefferson,Inauguration"));
      ec.insert(new Event("8 May 1945,Great Britain,VE Day"));
      ec.insert(new Event("4-Jul-1826,Thomas Jefferson,Death"));
   }
}
