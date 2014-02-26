import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Path2D.Float;
import java.util.Arrays;

/** Island class used to draw islands and shallow water on Map. Specifies draw
 * method as well as scaling method.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 2.0
 * @since 2013-11-29
 */
public class Island
{
    private final Color LAND = new Color(255, 255, 153);
    private final Color SHALLOW_WATER = new Color(153, 255, 204);

    //Instance variables
    private double scale;
    private double panXChange;
    private double panYChange;
    
    private GeneralPath bigIsland1;
    private GeneralPath littleIsland;
    private GeneralPath shallowWater1;
    private GeneralPath bigIsland2;
    private GeneralPath shallowWater2;

    private double [] island_xs_real;
    private double [] island_ys_real;
    private double [] island_xs_canvas;
    private double [] island_ys_canvas;

    private double [] littleIsland_xs_real;
    private double [] littleIsland_ys_real;
    private double [] littleIsland_xs_canvas;
    private double [] littleIsland_ys_canvas;

    private double [] shallowWater_xs_real;
    private double [] shallowWater_ys_real;
    private double [] shallowWater_xs_canvas;
    private double [] shallowWater_ys_canvas;

    private double [] island2_xs_real;
    private double [] island2_ys_real;
    private double [] island2_xs_canvas;
    private double [] island2_ys_canvas;

    private double [] shallowWater2_xs_real;
    private double [] shallowWater2_ys_real;
    private double [] shallowWater2_xs_canvas;
    private double [] shallowWater2_ys_canvas;

    /** Initializes all coordinates for Jewell Island and Cliff Island, and
     * the surrounding shallow water, according to the scale provided.
     * @param s current scale of map
     */
    public Island(double s)
    {
        island_xs_real = new double [] {120,150,200,240,242,230,240,300,335,338,
            340,335,345,342,335,320,315,320,290,295,305,307,302,300,210,200,180,
            160,150,140,130};
        island_ys_real = new double [] {360,280,250,200,202,220,225,180,110,115,
            140,180,185,187,182,185,200,205,270,275,270,275,280,300,410,400,405,
            380,380,390,380};        
        island_xs_canvas = Arrays.copyOf(island_xs_real, island_xs_real.length);
        island_ys_canvas = Arrays.copyOf(island_ys_real, island_ys_real.length);


        littleIsland_xs_real = new double [] {240,270,310,245};
        littleIsland_ys_real = new double [] {160,120, 85,165};
        littleIsland_xs_canvas = new double [] {240,270,310,245};
        littleIsland_ys_canvas = new double [] {160,120, 85,165};

        shallowWater_xs_real = new double [] {105,100,170,220,280,300,320,370,
            375,370,375,370,350,280,250,200,135};
        shallowWater_ys_real = new double [] {360,280,180,150, 70, 50, 70, 65,
             70, 90,105,185,300,410,415,413,395};
        shallowWater_xs_canvas = Arrays.copyOf(shallowWater_xs_real, 
                                               shallowWater_xs_real.length);
        shallowWater_ys_canvas = Arrays.copyOf(shallowWater_ys_real, 
                                               shallowWater_ys_real.length);
       
        island2_xs_real = new double [] { 200, 190, 200, 190, 195, 225, 240, 240,
            500,530,560,590,620,640,650,655, 645,625,600,590,527,532,600,625,
            670,680,680,580, 575,605,600,580,570,575,568,572,530,520,510,505,
            500,415, 400,395,400,390,360,350};
        island2_ys_real = new double [] {1150,1150,1120,1100,1075,1055,1050,1025,
            730,725,720,710,710,705,690,695, 715,720,750,790,838,842,880,900,885,
            870,880,1010,1005,945,950,945,950,940,940,930,920,910,885,880,885,
            950, 945,955,970,980,985,990};
        island2_xs_canvas = Arrays.copyOf(island2_xs_real, island2_xs_real.length);
        island2_ys_canvas = Arrays.copyOf(island2_ys_real, island2_ys_real.length);

        shallowWater2_xs_real = new double [] { 180, 120, 180,480,550,650,670,700,
            730,730, 580, 310};
        shallowWater2_ys_real = new double [] {1200,1150,1000,700,660,650,660,800,
            820,880,1100,1150};
        shallowWater2_xs_canvas = Arrays.copyOf(shallowWater2_xs_real,
                                                shallowWater2_xs_real.length);
        shallowWater2_ys_canvas = Arrays.copyOf(shallowWater2_ys_real,
                                                shallowWater2_ys_real.length);

        scale = s;
        panXChange = 0;
        panYChange = 0;
        
        scaleCoords(scale);
    }

    /** Use to update all island and shallow water coordinates to current scale.
     * @param newScale new scale of map
     */
    public void scaleCoords(double newScale)
    {
        scale = newScale;
        for (int i=0; i<island_xs_canvas.length; i++)
        {
            island_xs_canvas[i] = island_xs_real[i]*scale + panXChange;
            island_ys_canvas[i] = island_ys_real[i]*scale + panYChange;

        }
        for (int i=0; i<shallowWater_xs_canvas.length; i++)
        {
            shallowWater_xs_canvas[i] = shallowWater_xs_real[i]*scale + panXChange;
            shallowWater_ys_canvas[i] = shallowWater_ys_real[i]*scale + panYChange;
        }
        for (int i=0; i<littleIsland_xs_canvas.length; i++)
        {
            littleIsland_xs_canvas[i] = littleIsland_xs_real[i]*scale + panXChange;
            littleIsland_ys_canvas[i] = littleIsland_ys_real[i]*scale + panYChange;
        }
        for (int i=0; i<island2_xs_canvas.length; i++)
        {
            island2_xs_canvas[i] = island2_xs_real[i]*scale + panXChange;
            island2_ys_canvas[i] = island2_ys_real[i]*scale + panYChange;
        }

        for (int i=0; i<shallowWater2_xs_canvas.length; i++)
        {
            shallowWater2_xs_canvas[i] = shallowWater2_xs_real[i]*scale + panXChange;
            shallowWater2_ys_canvas[i] = shallowWater2_ys_real[i]*scale + panYChange;
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

    /** Creates shapes for island or shallow water area.
     * @param xs array of x coordinates
     * @param ys array of y coordinates
     * @return GeneralPath shape
     */
    private GeneralPath shape(double [] xs, double [] ys)
    {
        GeneralPath shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xs.length);
        shape.moveTo(xs[0], ys[0]);
        for (int i=1; i<(xs.length); i++)
        {
            shape.lineTo(xs[i], ys[i]);
        }
        shape.closePath();

        return shape;
    }

    /** Returns array of all island and shallow water shapes.
     * @return GeneralPath array
     */
    public GeneralPath [] getShapes() 
    {
        GeneralPath [] shapes = {bigIsland1, bigIsland2, littleIsland,
                                 shallowWater1, shallowWater2};
        return shapes;
    }

    /** Draw all shallow water areas and islands on Graphics2D object.
     * @param g2 Graphics2D object on which to draw the islands, etc.
     */
    public void draw(Graphics2D g2)
    {
        shallowWater1 = shape(shallowWater_xs_canvas, shallowWater_ys_canvas);
        g2.setColor(SHALLOW_WATER);
        g2.draw(shallowWater1);
        g2.fill(shallowWater1);

        shallowWater2 = shape(shallowWater2_xs_canvas, shallowWater2_ys_canvas);
        g2.draw(shallowWater2);
        g2.fill(shallowWater2);

        littleIsland = shape(littleIsland_xs_canvas, littleIsland_ys_canvas);
        g2.setColor(LAND);
        g2.fill(littleIsland);
        g2.setColor(Color.DARK_GRAY);
        g2.draw(littleIsland);

        bigIsland1 = shape(island_xs_canvas, island_ys_canvas);
        g2.setColor(LAND);
        g2.fill(bigIsland1);
        g2.setColor(Color.DARK_GRAY);
        g2.draw(bigIsland1);
        
        bigIsland2 = shape(island2_xs_canvas, island2_ys_canvas);
        g2.setColor(LAND);
        g2.fill(bigIsland2);
        g2.setColor(Color.DARK_GRAY);
        g2.draw(bigIsland2);

        if (scale > 0.3)
        {
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif",Font.PLAIN,14));
            g2.drawString("Jewell Is.",(int)(175*scale + panXChange),
                                       (int)(340*scale + panYChange));

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif",Font.PLAIN,14));
            g2.drawString("Cliff Is.",(int)(425*scale + panXChange),
                                      (int)(870*scale + panYChange));
        } 
    }

}
