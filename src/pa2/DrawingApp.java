package pa2;

/*
 * Main app for starting the drawing application.
 * 
 * @author John C. Bowers <bowersjc@jmu.edu>
 * 
 * @version V1, 09/21/16
 */
public class DrawingApp {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    /* Create and display the app's main form: */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new DrawingFrame().setVisible(true);
      }
    });
  }

}
