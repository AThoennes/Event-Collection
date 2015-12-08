import java.awt.*;
import javax.swing.*;

public class InputForm {

//===========================================================================================                      
// C o n s t a n t s   /   D e f a u l t s
//===========================================================================================                      

   public static final int TEXT_FIELD_LIMIT = 8;
    
//===========================================================================================                      
// I n s t a n c e   V a r i a b l e s
//===========================================================================================                      

   private String title;
   private JPanel panel;
   private JTextField[] textField;
   private int nextTextFieldIndex = 0;
   
//===========================================================================================                      
// C o n s t r u c t o r s
//===========================================================================================                      

   public InputForm(String title) {
      this.title = title;
      panel = new JPanel(new GridLayout(0,1));
      panel.setPreferredSize(new Dimension(640,256));
      textField = new JTextField[TEXT_FIELD_LIMIT];
   }

//===========================================================================================                      
//  M u t a t o r s
//===========================================================================================                      

   public void addTextField(String label) {
      if(nextTextFieldIndex < TEXT_FIELD_LIMIT) {
         panel.add(new JLabel(label));
         textField[nextTextFieldIndex] = new JTextField("");
         panel.add(textField[nextTextFieldIndex]);
         nextTextFieldIndex = nextTextFieldIndex + 1;
      } 
      else {
         throw new IllegalStateException("InputForm limit reached");   
      }
   }
 
//===========================================================================================                      
// A c c e s s o r s
//===========================================================================================                      

   public boolean hasInput() {
      boolean result = false;
      boolean returnable;
      // Clear each of the text fields
      for(int index=0; index<nextTextFieldIndex; index++) {
         textField[index].setText("");         
      }
      // Loop until Cancelled or all of the text fields are completed
      do {
         result = (JOptionPane.showConfirmDialog(null,panel,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE) 
             == JOptionPane.OK_OPTION);
         returnable = !result || allTextFieldsCompleted();
      } while (!returnable);
      return result;
   }
   
   public String getTextField(int fieldIndex) {
      String result;
      if((0 <= fieldIndex) && (fieldIndex < nextTextFieldIndex)) {
         result = textField[fieldIndex].getText();
      } 
      else {
         throw new IllegalArgumentException("invalid fieldIndex passed to getTextField()");         
      }
      return result;
   }
   
//===========================================================================================                      
// P r i v a t e
//===========================================================================================                      

   private boolean allTextFieldsCompleted() {
      boolean result = true;
      for(int index = 0; result && (index < nextTextFieldIndex); index++) {
         result = (getTextField(index).length() > 0);
      }
      return result;
   }
   
}
