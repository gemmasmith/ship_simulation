import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Path2D.Float;
import java.util.Arrays;

/** Class used to draw navigational buoys on Map. Specifies draw method as well
 * as scaling method.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 2.0
 * @since 2013-11-29
 */
public class NavBuoys
{
    //instance variables
    private double scale;
    private double panXChange;
    private double panYChange;
    private int [] xs_real;
    private int [] ys_real;
    private int [] xs_canvas;
    private int [] ys_canvas;
    private int halfBuoyW;
    private int buoyH;

    /** Initializes all coordinates for navigational buoys on map.
     * @param s Current scale of map
     */
    public NavBuoys(double s)
    {
        scale = s;

        panXChange = 0;
        panYChange = 0;
        xs_real = new int [] {450,650,975, 425,1100};
        ys_real = new int [] { 50,650,350,1050,1200};
        xs_canvas = Arrays.copyOf(xs_real, xs_real.length);
        ys_canvas = Arrays.copyOf(ys_real, ys_real.length);

        scaleCoords(scale);
    }

    /** Use to update all rock coordinates to current scale.
     * @param newScale new scale of map
     */
    public void scaleCoords(double newScale)
    {
        scale = newScale;
        for (int i=0; i<xs_canvas.length; i++)
        {
            xs_canvas[i] = (int)((double)xs_real[i]*scale + panXChange);
            ys_canvas[i] = (int)((double)ys_real[i]*scale + panYChange);
        }
        if (scale < 0.4)
        {
            halfBuoyW = 2;
            buoyH = 6;
        }
        else
        {
            halfBuoyW = 4;
            buoyH = 12;
        }
    }

    /** Use to update explosion pan to current pan.
     * @param newPanXChange amount to pan all canvas x coordinates
     * @param newPanYChange amount to pan all canvas y coordinates
     */
    public void panCoords(double newPanXChange, double newPanYChange)
    {
        panXChange = newPanXChange;
        panYChange = newPanYChange;
        scaleCoords(scale);
    }

    /** Returns array of all x coordinates and all y coordinates for all
     * navigational buoys.
     * @return int [][] {all x coordinates, all y coordinates}
     */
    public int [][] getCoords()
    {
        int [][] xsys = new int [][] {xs_canvas, ys_canvas};
        return xsys;
    }

    /** Returns shape of a buoy (diamond).
     * @param x canvas x coordinate
     * @param y canvas y coordinate
     * @return GeneralPath shape
     */
    private GeneralPath buoyShape(double x, double y)
    {
        double [] xs = new double [4];
        double [] ys = new double [4];

        xs[0] = x;
        ys[0] = y;
        xs[1] = x-halfBuoyW;
        ys[1] = y-(buoyH/2);
        xs[2] = x;
        ys[2] = y-buoyH;
        xs[3] = x+halfBuoyW;
        ys[3] = y-(buoyH/2);

        GeneralPath shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 5);
        shape.moveTo(xs[0], ys[0]);
        for (int i=1; i<4; i++)
        {
            shape.lineTo(xs[i], ys[i]);
        }
        shape.closePath();
        return shape;
    }

    /** Draws all navigational buoys on Graphics2D object. Note: the shape of a
     * navigational buoy is not scaled with zooming.
     * @param g2 Graphics2D object on which to draw the buoys
     */
    public void draw(Graphics2D g2)
    {
        Ellipse2D.Double spot;
        GeneralPath shape;
        for (int i=0; i<xs_canvas.length; i++)
        {
            double x = (double) xs_canvas[i];
            double y = (double) ys_canvas[i];
            spot = new Ellipse2D.Double(x-(halfBuoyW/2.0),y-(halfBuoyW/2.0),
                                           halfBuoyW, halfBuoyW);
            shape = buoyShape(x,y);
            g2.setColor(Color.BLACK);            
            g2.draw(spot);
            g2.setColor(Color.RED);
            g2.draw(shape); g2.fill(shape);
        }
    }
}
