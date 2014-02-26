import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.geom.Ellipse2D.Double;

/** Class used to draw explosions on Map. Specifies draw method as well
 * as scaling method.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-11-29
 */
public class Explosion
{
    private double scale;
    private double panXChange;
    private double panYChange;
    private int x_real; //real center of explosion
    private int y_real; //real center of explosion
    private int x_canvas; //canvas center
    private int y_canvas; //canvas center
    private int drawCircleX; //top left corner of bounding box
    private int drawCircleY; //top left corner of bounding box
    private int drawStringX; //bottom left corner of "BOOM!"
    private int drawStringY; //bottomleft corner of "BOOM!"
    private int diam_real;
    private int diam_canvas;
    private int r_canvas;
    private boolean exploding; //true if diam < 250

    public Explosion(double s, int canvasX, int canvasY, double panX, double panY)
    {
        scale = s;
        panXChange = panX;
        panYChange = panY;
        x_real = (int)Math.floor((double)(canvasX-panX)/scale);
        y_real = (int)Math.floor((double)(canvasY-panY)/scale);
        x_canvas = canvasX;
        y_canvas = canvasY;

        diam_real = 100;
        updateCanvasCoords();    
    }

    /**Use to update explosion coordinates to current scale.
     * @param newScale new scale of map
     */
    public void scaleCoords(double newScale)
    {
        scale = newScale;
        x_canvas = (int)Math.floor(x_real*scale + panXChange);
        y_canvas = (int)Math.floor(y_real*scale + panYChange);
        updateCanvasCoords();    
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

    /** Calculates all relavent coordinates for drawing explosion on canvas.
     */
    private void updateCanvasCoords()
    {
        diam_canvas = (int)Math.floor(diam_real*scale);
        r_canvas = (int)Math.floor(diam_canvas/2.0);
        drawCircleX = x_canvas - r_canvas;
        drawCircleY = y_canvas - r_canvas;
        drawStringX = x_canvas - 25;
        drawStringY = y_canvas + 5;
    }

    /* Getter functions.
     */
    public int getRealX(){return x_real;}
    public int getRealY(){return y_real;}
    public int getCanvasX(){return x_canvas;}
    public int getCanvasY(){return y_canvas;}
    public int getRealDiam(){return diam_real;}
    public int getCanvasDiam(){return diam_canvas;}

    /** Destroys any live or sunken ships in its path.
     * @param listShips list of all ships on map
     * @param listSunkenShips list of all sunken ships on map
     * @param map map on which the explosion is being drawn
     */
    public void explodeShips(ArrayList<Ship> listShips,
                                       ArrayList<Ship> listSunkenShips,
                                       Map map)
    {
        Ship userShip = map.getUserShip();
        for (int i=0; i<listShips.size(); i++)
        {
            Area explosionArea = new Area(getShape());                    
            Ship ship = listShips.get(i);
            explosionArea.intersect(new Area(ship.getShape()));
            if (!explosionArea.isEmpty())
            {
                Rectangle r = explosionArea.getBounds();
                int newExplosionX = (int)r.getX();
                int newExplosionY = (int)r.getY();
                if (ship.getType()=="Tanker")
                    map.explodeTanker(ship, ship, newExplosionX, newExplosionY);
                else if (ship == userShip)
                    map.userCollision();
                else
                    map.sink(ship);
            }
        }
        for (int i=0; i<listSunkenShips.size(); i++)
        {
            Area explosionArea = new Area(getShape());
            Ship sunken = listSunkenShips.get(i);
            explosionArea.intersect(new Area(sunken.getShape()));
            if (!explosionArea.isEmpty())
                map.destroySunken(sunken);
        }
    }

    /** Animation for explosion. Red circle increases in size until a certain
     * point, then is removed from map.
     * @param listShips list of all ships on map
     * @param listSunkenShips list of all sunken ships on map
     * @param map map on which the explosion is being drawn
     */
    public void updateSize(ArrayList<Ship> listShips,
                           ArrayList<Ship> listSunkenShips,
                           Map map)
    {
        diam_real+=3;
        updateCanvasCoords();
        if (diam_real < 200)
            explodeShips(listShips, listSunkenShips, map);
        else
            map.removeExplosion(this);
    }

    /** Shape of explosion, based on current size.
     */
    public Ellipse2D.Double getShape()
    {
        Ellipse2D.Double shape = new Ellipse2D.Double
            (drawCircleX,drawCircleY,diam_canvas,diam_canvas);
        return shape;
    }

    /** Draws an explosion on Graphics2D object.
     * @param g2 Graphics2D object on which to draw the explosion
     */
    public void draw(Graphics2D g2)
    {
        Ellipse2D.Double explosion = getShape();
        g2.setColor(Color.RED);
        g2.draw(explosion);
        g2.fill(explosion);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif",Font.BOLD,14));
        g2.drawString("BOOM!",drawStringX, drawStringY);
    }

}

