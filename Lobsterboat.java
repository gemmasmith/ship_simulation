import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D.Float;

/** Lobsterboat class, extends Ship. Specifies draw method for lobsterboats.
 * The shape of lobsterboats is represented by a red, oblong pentagon, with a
 * triangular bow and flat stern.
 * 
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 3.0
 * @since 2013-11-08
 */

public class Lobsterboat extends Ship
{
    /** Average daily catch, in pounds*/
    private int dailyCatch;

    /** Standard constructor
     * @param x real x coordinate of lobsterboat
     * @param y real y coordinate of lobsterboat
     * @param angle Direction (degrees 0-359) in which lobsterboat is moving
     * @param speed Speed in which lobsterboat is moving
     */
    public Lobsterboat(double s, int x, int y, double angle, int speed,
                       String name, double panX, double panY)
    {
        super(s, 30, x, y, angle, speed, new Color(39, 119, 39), name, panX, panY);
        shape = shipShape((double)xcoord_canvas, (double)ycoord_canvas);
        lastShape = shipShape((double)xcoord_canvas, (double)ycoord_canvas);
        dailyCatch = 100;
    }

    /** Returns ship type "Lobsterboat".
     * @return Ship type in string form
     */
    public String getType(){return "Lobsterboat";}

    public int getDailyCatch(){return dailyCatch;}

    public void changeDailyCatch(int newDailyCatch){dailyCatch = newDailyCatch;}

    /** Returns shape of the lobster boat.
     * @return Shape of the lobsterboat
     */
    protected GeneralPath shipShape(double x, double y)
    {
        double [] xs = new double [5];
        double [] ys = new double [5];

        double tipLen = (double) length_canvas/5;
        double bodyLen = (double) (length_canvas/5)*4;

        xs[0] = x;
        ys[0] = y;
        xs[1] = x;
        ys[1] = y - bodyLen;
        xs[2] = x + (width/2);
        ys[2] = ys[1] - tipLen;
        xs[3] = x + width;
        ys[3] = ys[1];
        xs[4] = xs[3];
        ys[4] = y;

        GeneralPath ship = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 5);
        ship.moveTo(xs[0], ys[0]);
        for (int i=1; i<5; i++)
        {
            ship.lineTo(xs[i], ys[i]);
        }
        ship.closePath();
        return ship;
    }

    public void draw(Graphics2D g2)
    {
        lastShape = shape;

        Shape nextShape = shipShape((double)xcoord_canvas, (double)ycoord_canvas);

        AffineTransform rotate = new AffineTransform ();
        rotate.setToRotation(radians, xcoord_canvas, ycoord_canvas);
        shape = rotate.createTransformedShape(nextShape);
        
        int textX = xcoord_canvas; int textY;
        if (currDirection < 80 || currDirection > 280)
            textY = ycoord_canvas+(int)(width);
        else
            textY = ycoord_canvas;

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
            if (color != Color.YELLOW)
            {
                g2.setColor(textColor);
                g2.setFont(new Font("SansSerif",Font.PLAIN,10));
                g2.drawString(text,textX,textY);
            }
            else
            {
                g2.setColor(Color.RED);
                g2.setFont(new Font("SansSerif",Font.BOLD,14));
                g2.drawString(text,xcoord_canvas,ycoord_canvas);
            }
        }
    }
}

