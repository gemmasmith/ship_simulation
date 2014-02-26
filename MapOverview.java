import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.geom.Ellipse2D.Double;

/** A miniature overview display of the entire map, with a red box outlining the
 * current zoomed view.
 * 
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-11-29
 */

public class MapOverview extends JComponent
{
    private Map map;
    private Island islands;
    private Rocks rocks;
    private NavBuoys navBuoys;
    private Rectangle zoomBox;

    private int zoomW;
    private int zoomH;
    private int zoomX;
    private int zoomY;

    public MapOverview(Map m)
    {
        map = m;
        islands = new Island(0.11);
        islands.panCoords(65,25);
        rocks = new Rocks(0.11);
        rocks.panCoords(65,25);
        navBuoys = new NavBuoys(0.11);
        navBuoys.panCoords(65,25);
        
        updateZoomBox();    
    }

    /** Upon panning, zooming, or resizing the window, the red outline of the
     * zoomed view will update, according to the new width, height, and top-left
     * corner of the map.
     */
    public void updateZoomBox()
    {
        int realVisibleW = map.getRealVisibleW();
        int realVisibleH = map.getRealVisibleH();
        int realVisibleX = map.getRealVisibleX();
        int realVisibleY = map.getRealVisibleY();
        zoomW = (int)((double)realVisibleW * 0.11); 
        zoomH = (int)((double)realVisibleH * 0.11);
        zoomX = (int)((double)realVisibleX * 0.11)+65;
        zoomY = (int)((double)realVisibleY * 0.11)+25;

        zoomBox = new Rectangle(zoomX, zoomY, zoomW, zoomH);  
    }

    /** Drawing routine that is called when JFrame is made visible or resized,
     * or upon a repaint() call. Displays map overview.
     */
    public void paintComponent (Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        //Draw islands and shallow water
        islands.draw(g2);
        //Draw rocks
        rocks.draw(g2);
        //Draw navigational buoys
        navBuoys.draw(g2);

        //Draw zoom box
        g2.setColor(Color.RED);
        g2.draw(zoomBox);
    }
}
