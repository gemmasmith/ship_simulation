import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D.Double;

/** Sailboat class, extends Ship. Specifies draw method for sailboats. The shape
 * of sailboats is represented by a blue oval.
 * 
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 3.0
 * @since 2013-11-08
 */

public class Sailboat extends Ship
{
    /** Number of sails*/
    private int numSails;

    /** Standard constructor
     * @param length Length of sailboat
     * @param x real x coordinate of sailboat
     * @param y real y coordinate of sailboat
     * @param angle Direction (degrees 0-359) in which sailboat is moving
     * @param speed Speed in which sailboat is moving
     */
    public Sailboat(double s, int length, int x, int y, double angle, int speed,
                    String name, double panX, double panY)
    {
        super(s, length, x, y, angle, speed, Color.BLUE, name, panX, panY);
        shape = shipShape((double)xcoord_canvas, (double)ycoord_canvas);  
        lastShape = shipShape((double)xcoord_canvas, (double)ycoord_canvas); 
        numSails = 2;
    }

    /** Returns ship type "Sailboat".
     * @return Ship type in string form
     */
    public String getType(){return "Sailboat";}

    public int getNumSails(){return numSails;}

    public void changeNumSails(int newNumSails){numSails = newNumSails;}

    protected Ellipse2D.Double shipShape(double x, double y)
    {
        double height = (double)length_canvas;

        Ellipse2D.Double ship = new Ellipse2D.Double(x,y,width,height);
        return ship;
    }

    public void draw(Graphics2D g2)
    {
        lastShape = shape;
        Shape nextShape = shipShape((double)xcoord_canvas, (double)ycoord_canvas);

        AffineTransform rotate = new AffineTransform ();
        rotate.setToRotation(radians, xcoord_canvas, (ycoord_canvas+length_canvas));
        shape = rotate.createTransformedShape(nextShape);

        int textX = xcoord_canvas; int textY;
        if (currDirection < 80 || currDirection > 280)
            textY = ycoord_canvas+length_canvas+(int)(width);
        else
            textY = ycoord_canvas+length_canvas;

        if (sunken)
        {
            g2.setColor(Color.LIGHT_GRAY);
            g2.fill(shape);
            g2.setColor(Color.DARK_GRAY);
            g2.draw(shape);
            g2.setColor(textColor);
            g2.setFont(new Font("SansSerif",Font.PLAIN,10));
            g2.drawString(text,textX,textY);
        }
        else
        {
            g2.setColor(color);
            g2.draw(shape);
            g2.fill(shape);
			g2.setColor(textColor);
			g2.setFont(new Font("SansSerif",Font.PLAIN,10));
			g2.drawString(text,textX,textY);
        }
    }
}
