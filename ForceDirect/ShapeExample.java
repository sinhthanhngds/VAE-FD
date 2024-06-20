import javax.swing.*;   // For JPanel, etc.
import java.awt.*;      // For Graphics, etc.
import java.awt.geom.*; // For Ellipse2D, etc.

/** An example of drawing/filling shapes with Java 2D. 
 *
 *  From <a href="http://courses.coreservlets.com/Course-Materials/">the
 *  coreservlets.com tutorials on servlets, JSP, Struts, JSF, Ajax, GWT, and Java</a>.
 */

public class ShapeExample extends JPanel {
  private Ellipse2D.Double circle =
    new Ellipse2D.Double(10, 10, 350, 350);
  private Rectangle2D.Double square =
    new Rectangle2D.Double(10, 10, 350, 350);

  public void paintComponent(Graphics g) {
    clear(g);
    Graphics2D g2d = (Graphics2D)g;
    g2d.fill(circle);
    g2d.draw(square);
  }

  // super.paintComponent clears off screen pixmap,
  // since we're using double buffering by default.
  protected void clear(Graphics g) {
    super.paintComponent(g);
  }

  protected Ellipse2D.Double getCircle() {
    return(circle);
  }

public static void main(String[] args) {
    WindowUtilities.openInJFrame(new ShapeExample(), 380, 400);
  }
}