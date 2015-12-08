import javax.swing.JOptionPane;
	
public class DialogBox {
    
//===========================================================================================                      
// C o n s t a n t s   /   D e f a u l t s
//===========================================================================================                      
   private int DEFAULT_INT         = 0;
   private float DEFAULT_FLOAT     = (float)0.0;
   private double DEFAULT_DOUBLE   = 0.0;
   private String DEFAULT_STRING   = "";

   private int defaultInt         = DEFAULT_INT;
   private float defaultFloat     = DEFAULT_FLOAT;
   private double defaultDouble   = DEFAULT_DOUBLE;
   private String defaultString   = DEFAULT_STRING;
   
   private static String DEFAULT_PROMPT = "";

//===========================================================================================                      
// I n s t a n c e   V a r i a b l e s
//===========================================================================================                      

   private String lastprompt = DEFAULT_PROMPT;
   	
//===========================================================================================                      
// C o n s t r u c t o r s
//===========================================================================================                      

   public DialogBox() {
   }
   
   public DialogBox(String Prompt) {
      lastprompt = Prompt;
   }
      
//===========================================================================================                      
// A c t i o n   M e t h o d s
//===========================================================================================                      

   public String nextLine() {
      String result;
      result = JOptionPane.showInputDialog(null,lastprompt);
      if(result == null) {
         result = defaultString;
      }
      return result;
   }
   
   public String nextLine(String Prompt) {
      lastprompt = Prompt;
      return nextLine();
   }
   	
   public int nextInt () {
      String input;
      boolean valid = false;
      int result = defaultInt;
      String savedprompt = lastprompt;
      do {
         input = nextLine();
         try {
            if(input != defaultString) result = Integer.parseInt(input);
            valid = true;
         }
         catch(Exception e) {
            lastprompt = "REENTER: " + lastprompt;
         } 
      } while (!valid);
      lastprompt = savedprompt;
      return result;
   }
   
   public int nextInt (String Prompt) {
      lastprompt = Prompt;
      return nextInt();
   }
   
   public float nextFloat () {
      String input;
      boolean valid = false;
      float result = defaultFloat;
      String savedprompt = lastprompt;
      do {
         input = nextLine();
         try {
            if(input != defaultString) result = Float.parseFloat(input);
            valid = true;
         }
         catch(Exception e) {
            lastprompt = "REENTER: " + lastprompt;
         } 
      } while (!valid);
      lastprompt = savedprompt;
      return result;
   }
   
   public float nextFloat (String Prompt) {
      lastprompt = Prompt;
      return nextFloat();
   }
   
   public double nextDouble () {
      String input;
      boolean valid = false;
      double result = defaultDouble;
      String savedprompt = lastprompt;
      do {
         input = nextLine();
         try {
            if(input != defaultString) result = Double.parseDouble(input);
            valid = true;
         }
         catch(Exception e) {
            lastprompt = "REENTER: " + lastprompt;
         } 
      } while (!valid);
      lastprompt = savedprompt;
      return result;
   }
   
   public double nextDouble (String Prompt) {
      lastprompt = Prompt;
      return nextDouble();
   }
         
//===========================================================================================                      
// A c c e s s o r s
//===========================================================================================                      

   public int getDefaultInt() {
      return defaultInt;
   }
   	
   public float getDefaultFloat() {
      return defaultFloat;
   }
   	
   public double getDefaultDouble() {
      return defaultDouble;
   }
   	
   public String getDefaultString() {
      return defaultString;
   }
   	
//===========================================================================================                      
//  M u t a t o r s
//===========================================================================================                      

   public void setDefaultInt(int I) {
      defaultInt = I;
   }
   
   public void setDefaultFloat(float F) {
      defaultFloat = F;
   }
   
   public void setDefaultDouble(double D) {
      defaultDouble = D;
   }
   
   public void setDefaultString(String S) {
      defaultString = S;
   }
  
}
