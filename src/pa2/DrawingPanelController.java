package pa2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import pa2.util.LStack;
import pa2.util.Stack;


/**
 * The controller for the drawing canvas.
 *
 * @author Martin Quezada My work complies to JMU Honor code
 */
public class DrawingPanelController {

  // TODO Get rid of bImage and replace it with a stack:
  LStack<BufferedImage> editStack;
  LStack<BufferedImage> redoStack;


  DrawingPanel dPanel;
  DrawingTool currentTool;
  Color drawingColor;

  public DrawingPanelController(DrawingPanel dPanel) {
    editStack = new LStack<BufferedImage>(16);
    redoStack = new LStack<BufferedImage>(16);
    init(dPanel);
  }

  /**
   * @return The BufferedImage that is currently at the top of the edit stack.
   */
  public BufferedImage getCurrentImage() {
    // TODO This should return the image that is at the top of the stack.
    return editStack.topValue();
  }

  /**
   * This method is called by the drawing tools when an update has been made by the image and needs
   * to be added to the undo stack.
   */
  private void updateImage(BufferedImage image) {
    // TODO Currently this code just replaces bImage with a copy of image.
    // You need to make it work with the stack.
    editStack.push(copyImage(image));
    redoStack.clear();
  }

  /**
   * Undoes the last edit made by the user.
   */
  public void undo() {
    // TODO Implement this method.
    if (editStack.length() > 1) {
      redoStack.push(editStack.pop());
      DrawingPanelController.this.dPanel.repaint();
    }
  }

  /**
   * Redoes the last undone edit made by the user.
   */
  public void redo() {
    // TODO Implement this method.
    if (redoStack.length() > 0) {
      editStack.push(redoStack.pop());
      DrawingPanelController.this.dPanel.repaint();
    }
  }

  /**
   * A tool for drawing wherever the user drags the mouse, like a pen.
   */
  private class FillTool extends DrawingTool {

    BufferedImage tmpImage = null;

    @Override
    public void mousePressed(MouseEvent e) {
      tmpImage = DrawingPanelController.this.copyImage(getCurrentImage());
    }


    @Override
    public void mouseReleased(MouseEvent e) {
      fillFromPoint(e.getPoint());

      DrawingPanelController.this.updateImage(tmpImage);
      tmpImage = null;
      DrawingPanelController.this.dPanel.repaint();
    }

    /**
     * Flood fills tmpImage with the current drawingColor starting at the startPoint.
     * 
     * @param startPoint A point in the image.
     */
    public void fillFromPoint(Point startPoint) {
      // TODO This code should do nothing if the current drawingColor
      // and the color of tmpImage at startPoint are the same.
      // Fill in the necessary code to start at the startPoint in
      // tmpImage and flood fill with the drawingColor.
      LStack<Point> pStack = new LStack<Point>();
      int originalColor = tmpImage.getRGB(startPoint.x, startPoint.y);
      pStack.push(startPoint);
      while (pStack.length() != 0) {
        Point p = pStack.pop();
        tmpImage.setRGB(p.x, p.y, drawingColor.getRGB());
        if ((p.x + 1 >= 0) && (p.x + 1 < tmpImage.getWidth()) && (p.y >= 0)
            && (p.y < tmpImage.getHeight())) {
          if (tmpImage.getRGB(p.x + 1, p.y) == originalColor) {
            pStack.push(new Point(p.x + 1, p.y));
          }
        }

        if ((p.x - 1 >= 0) && (p.x - 1 < tmpImage.getWidth()) && (p.y >= 0)
            && (p.y < (int) tmpImage.getHeight())) {
          if (tmpImage.getRGB(p.x - 1, p.y) == originalColor) {
            pStack.push(new Point(p.x - 1, p.y));
          }
        }

        if ((p.x >= 0) && (p.y + 1 >= 0) && (p.y + 1 < tmpImage.getHeight())
            && (p.x < tmpImage.getWidth())) {
          if (tmpImage.getRGB(p.x, p.y + 1) == originalColor) {
            pStack.push(new Point(p.x, p.y + 1));
          }
        }

        if ((p.x >= 0) && (p.y - 1 >= 0) && (p.y - 1 < tmpImage.getHeight())
            && (p.x < tmpImage.getWidth())) {
          if (tmpImage.getRGB(p.x, p.y - 1) == originalColor) {
            pStack.push(new Point(p.x, p.y - 1));
          }
        }
      }
    }


    @Override
    public void paint(Graphics2D g2d) {
      if (tmpImage != null) {
        g2d.drawImage(tmpImage, null, 0, 0);
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  //
  // YOU SHOULD NOT NEED TO EDIT ANY CODE PAST THIS POINT
  //
  ////////////////////////////////////////////////////////////////////////////



  /**
   * Creates a copy of the BufferedImage image by creating a copy and drawing image into using
   * Graphics2D
   * 
   * @param image
   * @return A copy of (the visible part of) image.
   */
  private BufferedImage copyImage(BufferedImage image) {
    BufferedImage copy =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);

    Graphics2D g2d = copy.createGraphics();
    g2d.drawImage(image, null, 0, 0);
    g2d.dispose();

    return copy;
  }

  /**
   * Sets up the basic controller and the mouse listener controls.
   * 
   * @param dPanel
   */
  private void init(DrawingPanel dPanel) {

    this.dPanel = dPanel;
    this.dPanel.setController(this);

    this.setToolType(DrawingToolType.Pen);

    drawingColor = Color.black;

    dPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if (DrawingPanelController.this.currentTool != null) {
          DrawingPanelController.this.currentTool.mousePressed(e);
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (DrawingPanelController.this.currentTool != null) {
          DrawingPanelController.this.currentTool.mouseReleased(e);
        }
      }
    });

    dPanel.addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        if (DrawingPanelController.this.currentTool != null) {
          DrawingPanelController.this.currentTool.mouseDragged(e);
        }
      }
    });
  }

  /**
   * A tool for drawing wherever the user drags the mouse, like a pen.
   */
  private class PenTool extends DrawingTool {

    BufferedImage tmpImage = null;
    Point prevPoint = null;

    private void drawPoint(Point p) {

      if (prevPoint != null) {
        Graphics2D g2d = tmpImage.createGraphics();
        g2d.setColor(DrawingPanelController.this.drawingColor);
        g2d.drawLine(prevPoint.x, prevPoint.y, p.x, p.y);
      }
      prevPoint = p;
    }

    @Override
    public void mousePressed(MouseEvent e) {
      tmpImage = DrawingPanelController.this.copyImage(getCurrentImage());
      drawPoint(e.getPoint());
      DrawingPanelController.this.dPanel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      drawPoint(e.getPoint());
      DrawingPanelController.this.updateImage(tmpImage);
      tmpImage = null;
      prevPoint = null;
      DrawingPanelController.this.dPanel.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      drawPoint(e.getPoint());
      DrawingPanelController.this.dPanel.repaint();
    }

    @Override
    public void paint(Graphics2D g2d) {
      if (tmpImage != null) {
        g2d.drawImage(tmpImage, null, 0, 0);
      }
    }
  }

  /**
   * A tool for drawing line segments.
   */
  private class LineSegmentTool extends DrawingTool {

    Point mouseDownPoint = null;
    Point mouseDraggedPoint = null;

    @Override
    public void mousePressed(MouseEvent e) {
      mouseDownPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

      if (mouseDraggedPoint != null) {
        // Draw the current segment into the bImage.
        DrawingPanelController.this.updateImage(copyImage(getCurrentImage()));
        Graphics2D g2d = getCurrentImage().createGraphics();
        this.paint(g2d);
        g2d.dispose();
      }

      mouseDownPoint = null;
      mouseDraggedPoint = null;

      DrawingPanelController.this.dPanel.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      mouseDraggedPoint = e.getPoint();
      DrawingPanelController.this.dPanel.repaint();
    }

    @Override
    public void paint(Graphics2D g2d) {
      if (mouseDownPoint != null && mouseDraggedPoint != null) {
        g2d.setColor(DrawingPanelController.this.drawingColor);
        g2d.drawLine(mouseDownPoint.x, mouseDownPoint.y, mouseDraggedPoint.x, mouseDraggedPoint.y);
      }
    }
  }

  /**
   * A tool for drawing rectangles.
   */
  private class RectangleTool extends DrawingTool {

    Point mouseDownPoint = null;
    Point mouseDraggedPoint = null;

    @Override
    public void mousePressed(MouseEvent e) {
      mouseDownPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

      if (mouseDraggedPoint != null) {
        // Draw the current segment into the bImage.
        DrawingPanelController.this.updateImage(copyImage(getCurrentImage()));
        Graphics2D g2d = getCurrentImage().createGraphics();
        this.paint(g2d);
        g2d.dispose();
      }

      mouseDownPoint = null;
      mouseDraggedPoint = null;

      DrawingPanelController.this.dPanel.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      mouseDraggedPoint = e.getPoint();
      DrawingPanelController.this.dPanel.repaint();
    }

    @Override
    public void paint(Graphics2D g2d) {
      if (mouseDownPoint != null && mouseDraggedPoint != null) {
        g2d.setColor(DrawingPanelController.this.drawingColor);
        g2d.drawRect(Math.min(mouseDownPoint.x, mouseDraggedPoint.x),
            Math.min(mouseDownPoint.y, mouseDraggedPoint.y),
            Math.abs(mouseDraggedPoint.x - mouseDownPoint.x),
            Math.abs(mouseDraggedPoint.y - mouseDownPoint.y));
      }
    }
  }

  /**
   * The available drawing tool types.
   */
  public enum DrawingToolType {
    Pen, LineSegment, Rectangle, Fill, Custom
  }
  
  ;

  /**
   * Sets which drawing tool is currently being used.
   * 
   * @param toolType
   */
  public void setToolType(DrawingToolType toolType) {
    switch (toolType) {
      case Rectangle:
        currentTool = new RectangleTool();
        break;
      case LineSegment:
        currentTool = new LineSegmentTool();
        break;
      case Pen:
        currentTool = new PenTool();
        break;
      case Fill:
        currentTool = new FillTool();
        break;
      case Custom:
      default:
        currentTool = null;
        break;
    }
  }

  /**
   * In addition to the built-in tools, custom tools extending DrawingTool may be used with this
   * DrawingPanel by setting them via setTool.
   * 
   * @param tool
   */
  public void setTool(DrawingTool tool) {
    setToolType(DrawingToolType.Custom);
    this.currentTool = tool;
  }

  public void paintTool(Graphics2D g2d) {
    if (currentTool != null) {
      currentTool.paint(g2d);
    }
  }

  /**
   * Initializes the first image and pushes it to the stack if no image has yet been created.
   */
  public void initImageIfNecessary() {
    if (this.getCurrentImage() == null) {
      BufferedImage image =
          new BufferedImage(dPanel.getWidth(), dPanel.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
      Graphics2D g2d = image.createGraphics();

      g2d.setColor(Color.white);
      g2d.fillRect(0, 0, dPanel.getWidth(), dPanel.getHeight());
      g2d.dispose();

      updateImage(image);
    }
  }


  /**
   * @return The current drawing color.
   */
  public Color getDrawingColor() {
    return new Color(this.drawingColor.getRGB());
  }

  /**
   * Sets the current drawing color for all tools.
   * 
   * @param drawingColor The new drawing color.
   */
  public void setDrawingColor(Color drawingColor) {
    this.drawingColor = drawingColor;
  }

  // Included just so you don't accidentally define another default constructor
  private DrawingPanelController() {

  }
}
