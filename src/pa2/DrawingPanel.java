package pa2;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * The drawing canvas.
 *
 * @author John C. Bowers <bowersjc@jmu.edu>
 * @version V1, 09/21/16
 */
public class DrawingPanel extends JPanel {

  private DrawingPanelController controller;

  public DrawingPanel() {}

  public void setController(DrawingPanelController controller) {
    this.controller = controller;
  }

  @Override
  public void paint(Graphics g) {

    controller.initImageIfNecessary();

    // Cast g as a Graphics2D for extra functionality:
    Graphics2D g2d = (Graphics2D) g;

    // Draw the saved image buffer:
    g2d.drawImage(controller.getCurrentImage(), null, 0, 0);

    // The current tool may need additional painting:
    controller.paintTool(g2d);
  }

}
