import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D.Float;

/** Tanker class, extends Ship. Specifies draw method for a tanker ship. Tanker
 * shape is similar to that of a lobster boat, but much bigger, gray, and has
 * equipment hanging off the stern.
 * 
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 3.0
 * @since 2013-11-08
 */

public class Tanker extends Ship
{
    /** Amount of oil that the tanker is carrying */
    private int loadSize;

    /** Standard constructor
     * @param x x coordinate of tanker
     * @param y y coordinate of tanker
     * @param angle Direction (degrees 0-359) in which tanker is moving
     * @param speed Speed in which tanker is moving
     */
    public Tanker(double s, int x, int y, double angle, int speed, String name, 
                  double panX, double panY)
    {
        super(s, 200, x, y, angle, speed, Color.DARK_GRAY, name, panX, panY);
        shape = shipShape((double)xcoord_canvas, (double)ycoord_canvas);
        lastShape = shipShape((double)xcoord_canvas, (double)ycoord_canvas);
        loadSize = 100;
    }

    /** Returns ship type "Tanker".
     * @return Ship type in string form
     */
    public String getType(){return "Tanker";}

    public int getLoadSize(){return loadSize;}

    public void changeLoadSize(int newLoadSize){loadSize = newLoadSize;}

    /** Returns shape of the tanker.
     * @return Shape of the tanker
     */
    protected GeneralPath shipShape(double x, double y)
    {
        double [] xs = new double [8];
        double [] ys = new double [8];

        double tipLen = (double) length_canvas/7;
        double bodyLen = (double) (length_canvas/7)*6;

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
        xs[5] = xs[2];
        ys[5] = y;
        xs[6] = xs[2];
        ys[6] = y + (tipLen/2);
        xs[7] = xs[2];
        ys[7] = y;

        GeneralPath ship = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 5);
        ship.moveTo(xs[0], ys[0]);
        for (int i=1; i<8; i++)
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

        g2.setColor(color);
        g2.draw(shape);
        g2.fill(shape);

        int textX = xcoord_canvas; int textY = ycoord_canvas;

		g2.setColor(textColor);
		g2.setFont(new Font("SansSerif",Font.PLAIN,10));
		g2.drawString(text,textX,textY);

    }
}


