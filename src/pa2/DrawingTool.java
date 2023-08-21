package pa2;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;

/**
 * A class representing a drawing tool. It is essentially the mouse adapter for the tool plus an
 * extra paint method that allows additional drawing to be performed while the user is interacting
 * with the tool.
 * 
 * @author John C. Bowers <bowersjc@jmu.edu>
 * @version V1, 09/21/16
 */
public abstract class DrawingTool extends MouseAdapter {

  /**
   * Paints any added drawing the tool needs to perform that has not been rendered to the underlying
   * bImage. For instance, while the user is editing a line segment, a line segment drawing tool
   * would paint the segment that will be drawn once the user releases the mouse button.
   * 
   * @param g2d The Graphics2D context to paint into.
   */
  public abstract void paint(Graphics2D g2d);
}
