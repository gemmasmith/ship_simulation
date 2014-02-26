import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Path2D.Float;
import java.util.Arrays;

/** Rock class used to draw rocks on Map. Specifies draw method as well as
 * scaling method.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 2.0
 * @since 2013-11-29
 */
public class Rocks
{
    private double scale;
    private double panXChange;
    private double panYChange;
    private int [] xs_real;
    private int [] ys_real;
    private int [] xs_canvas;
    private int [] ys_canvas;
    private String [] names;
    private int rockSize;

    /** Initializes all coordinates for rocks (three clusters: "Channel Rock,"
     * "Eastern Egg Ledge," and "Gull Rock").
     * @param s Current scale of map
     */
    public Rocks(double s)
    {
        scale = s;
        panXChange = 0;
        panYChange = 0;

        xs_real = new int [] {400,405,  1000,1010,990,   450, 460, 450};
        ys_real = new int [] {100, 90,   400, 405,395,  1050,1060,1070};
        xs_canvas = Arrays.copyOf(xs_real, xs_real.length);
        ys_canvas = Arrays.copyOf(ys_real, ys_real.length);

        names = new String [] {"Channel Rock", "Eastern Egg Ledge", "Gull Rock"};

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
            rockSize = 8;
        else
            rockSize = 14;
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
    
    /** Returns array of all x coordinates and all y coordinates for all rocks.
     * @return int [][] {all x coordinates, all y coordinates}
     */
    public int [][] getCoords()
    {
        int [][] xsys = new int [][] {xs_canvas, ys_canvas};
        return xsys;
    }

    /** Draws all rocks on Graphics2D object.
     * @param g2 Graphics2D object on which to draw the rocks
     */
    public void draw(Graphics2D g2)
    {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif",Font.PLAIN,rockSize));        
        for (int i=0; i<xs_canvas.length; i++)
        {
            g2.drawString("*", xs_canvas[i], ys_canvas[i]);
        }

        if (scale > 0.3)
        {
            g2.setFont(new Font("SansSerif",Font.PLAIN,12));
            g2.drawString(names[0],(int)(405*scale + panXChange),
                                   (int)(110*scale + panYChange));
            g2.drawString(names[1],(int)(1010*scale + panXChange),
                                   (int)(415*scale + panYChange));
            g2.drawString(names[2],(int)(460*scale + panXChange),
                                   (int)(1085*scale + panYChange));
        }
    }
}
