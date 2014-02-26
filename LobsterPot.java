import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Path2D.Float;
import java.util.Arrays;

/** Class used to draw lobsterpots on Map. Specifies draw method as well
 * as scaling method.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 2.0
 * @since 2013-11-29
 */
public class LobsterPot
{
    private double scale;
    private double panXChange;
    private double panYChange;
    private int x_real;
    private int y_real;
    private int x_canvas;
    private int y_canvas;
    private boolean recentlyCaught;

    /** Initializes a lobsterpot at real coordinates (x,y).
     * @param s Current scale of map
     */
    public LobsterPot(double s, int realX, int realY)
    {
        scale = s;
        panXChange = 0;
        panYChange = 0;

        x_real = realX;
        y_real = realY;
        x_canvas = (int)Math.floor(realX*scale);
        y_canvas = (int)Math.floor(realY*scale);

        recentlyCaught = false;
    }

    /**Use to update lobsterpot coordinates to current scale.
     * @param newScale new scale of map
     */
    public void scaleCoords(double newScale)
    {
        scale = newScale;
        x_canvas = (int)Math.floor(x_real*scale + panXChange);
        y_canvas = (int)Math.floor(y_real*scale + panYChange);
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

    /* Getter functions.
     */
    public int getRealX(){return x_real;}
    public int getRealY(){return y_real;}
    public int getCanvasX(){return x_canvas;}
    public int getCanvasY(){return y_canvas;}
    public boolean getRecentlyCaught(){return recentlyCaught;}
    public void setRecentlyCaught(boolean bool){recentlyCaught = bool;}

    /** Draws a lobsterpot on Graphics2D object. Note: the shape of a lobsterpot
     * is not scaled with zooming.
     * @param g2 Graphics2D object on which to draw the lobsterpot
     */
    public void draw(Graphics2D g2)
    {
        Color potColor;
        if (recentlyCaught == false)
            potColor = new Color(255,0,255);
        else
            potColor = new Color(102,0,102);

        Ellipse2D.Double spot;
        GeneralPath shape;
        spot = new Ellipse2D.Double(x_canvas-2.5,y_canvas-2.5,5,5);
        g2.setColor(potColor);
        g2.draw(spot);
        g2.fill(spot);
    }
}

