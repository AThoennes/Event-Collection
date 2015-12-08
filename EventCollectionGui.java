/* Java application that is for testing the EventCollection class.
*/
import java.util.Scanner;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EventCollectionGUI {
   static final String TITLE = "PMJ's EventCollectionGUI...";
   
   static DialogBox db = new DialogBox(TITLE); 
   static InputForm eventInputForm = new InputForm(TITLE);
   static InputForm iterationModeSelectionForm = new InputForm(TITLE);

   static final String LOAD_EVENTS  = "Load Events";
   static final String INSERT_EVENT = "Insert Event";
   static final String LIST_EVENTS_INSERTION   = "List Events by insertion";
   static final String LIST_EVENTS_DATE        = "List Events by date";
   static final String LIST_EVENTS_PRINCIPAL   = "List Events by principal";
   static final String LIST_EVENTS_DESCRIPTION = "List Events by description";
   static final String SAVE_EVENTS  = "Save Events";
   static final String QUIT         = "QUIT";

   static final String[] COMMANDS = {LOAD_EVENTS,INSERT_EVENT,
                                     LIST_EVENTS_INSERTION,LIST_EVENTS_DATE,
                                     LIST_EVENTS_PRINCIPAL,LIST_EVENTS_DESCRIPTION,
                                     SAVE_EVENTS,QUIT};

   static EventCollection ec = new EventCollection(256);

//===========================================================================================                      
// Local classes, each implementing the ActionListener interface
//===========================================================================================                      

//-------------------------------------------------------------------------------------------  	    
   public static class LOAD_EVENTS_Listener implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         try{
            String fileName = db.nextLine("Enter filename:");
            Scanner input = new Scanner(new File(fileName));
            while (input.hasNextLine()) {
               String eventDescriptor = input.nextLine();
               Event e = new Event(eventDescriptor);
               ec.insert(e);
            }
            System.out.println("Events loaded from " + fileName + "\n");
         } 
         catch (IOException e){
            System.out.println("==> CAUGHT IOException; no such file");
         }
      }
   }
   static LOAD_EVENTS_Listener LOAD_EVENTS_Handler = new LOAD_EVENTS_Listener();   
//-------------------------------------------------------------------------------------------                      
   public static class INSERT_EVENT_Listener implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         if(eventInputForm.hasInput()) {
            try{
               ec.insert(new Event(new CalendarDate(eventInputForm.getTextField(0).trim()),
                                   eventInputForm.getTextField(1).trim(),
                                   eventInputForm.getTextField(2).trim()
                                  )
                        );
               System.out.println("Event inserted.\n");
            } 
            catch (IllegalStateException e){
               System.out.println("==> CAUGHT IllegalStateException; unable to insert event");
            }
         }       
      }
   }
   static INSERT_EVENT_Listener INSERT_EVENT_Handler = new INSERT_EVENT_Listener();   
//-------------------------------------------------------------------------------------------                      
   public static class LIST_EVENTS_INSERTION_Listener implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         System.out.println("Listing by order of insertion:");
         ec.reset();
         iterate(ec," | ");
      }
   }
   static LIST_EVENTS_INSERTION_Listener LIST_EVENTS_INSERTION_Handler = new LIST_EVENTS_INSERTION_Listener();   
//-------------------------------------------------------------------------------------------                      
   public static class LIST_EVENTS_DATE_Listener implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         System.out.println("Listing by order of date:");
         ec.reset(EventCollection.ITERATE_BY_DATE);
         iterate(ec," | ");
      }
   }
   static LIST_EVENTS_DATE_Listener LIST_EVENTS_DATE_Handler = new LIST_EVENTS_DATE_Listener();   
//-------------------------------------------------------------------------------------------                      
   public static class LIST_EVENTS_PRINCIPAL_Listener implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         System.out.println("Listing by order of pricipal:");
         ec.reset(EventCollection.ITERATE_BY_PRINCIPAL);
         iterate(ec," | ");
      }
   }
   static LIST_EVENTS_PRINCIPAL_Listener LIST_EVENTS_PRINCIPAL_Handler = new LIST_EVENTS_PRINCIPAL_Listener();   
//-------------------------------------------------------------------------------------------                      
   public static class LIST_EVENTS_DESCRIPTION_Listener implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         System.out.println("Listing by order of decription:");
         ec.reset(EventCollection.ITERATE_BY_DESCRIPTION);
         iterate(ec," | ");
      }
   }
   static LIST_EVENTS_DESCRIPTION_Listener LIST_EVENTS_DESCRIPTION_Handler = new LIST_EVENTS_DESCRIPTION_Listener();   
//-------------------------------------------------------------------------------------------                      

//**
   public static class SAVE_EVENTS_Listener implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         try{
            String fileName = db.nextLine("Enter filename:");
            iterateToFile(ec,fileName);
            System.out.println("Events saved to " + fileName + "\n");
         } 
         catch (IOException e){
            System.out.println("==> CAUGHT IOException; not saved");
         }
      }
   }
   static SAVE_EVENTS_Listener SAVE_EVENTS_Handler = new SAVE_EVENTS_Listener();   
//------------------------------------------------------------------------------------------- 
   public static class QUIT_Listener implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         System.exit(0);
      }
   }
   static QUIT_Listener QUIT_Handler = new QUIT_Listener();   
//-------------------------------------------------------------------------------------------                      

//===========================================================================================                      
// Declaration and construction of Command Buttons object
//===========================================================================================                      
   static CommandButtons CBs = new CommandButtons("Select Command:", COMMANDS, 
      												LOAD_EVENTS_Handler, INSERT_EVENT_Handler, 
                                          LIST_EVENTS_INSERTION_Handler,
                                          LIST_EVENTS_DATE_Handler,
                                          LIST_EVENTS_PRINCIPAL_Handler,
                                          LIST_EVENTS_DESCRIPTION_Handler,
                                          SAVE_EVENTS_Handler,
                                          QUIT_Handler);
//===========================================================================================                      

   public static void main(String[] args) throws IOException {
      System.out.println(TITLE);
      eventInputForm.addTextField("Date:");
      eventInputForm.addTextField("Principal:");
      eventInputForm.addTextField("Description:");
      CBs.Show(800,200);      	
   }


   /* Iterates over all events in the given collection, using whatever mode
   ** is in force.  For each event, prints the result of using the individual
   ** observer methods.
   */
   private static void iterate(EventCollection eventColl, String fieldSeparator) {
      while (eventColl.hasNext()) {
         Event e = eventColl.next();
         System.out.println(e.dateOf().toString() + fieldSeparator +
                            e.principalOf() + fieldSeparator +
                            e.descriptionOf()
                           );
      }
      System.out.println("Listing Done.\n");
   }

   /* Iterates over all events in the given collection, by order of inserting, and
   ** writing each event to the file with the given name.  For each event, prints 
   ** the result of its toString() method.
   */
   private static void iterateToFile(EventCollection eventColl, String filename) throws IOException {
      FileWriter fw = new FileWriter(filename);
      eventColl.reset();
      while (eventColl.hasNext()) {
         Event e = eventColl.next();
         fw.write(e.toString()+"\n");
      }
      fw.close();
   }


}
