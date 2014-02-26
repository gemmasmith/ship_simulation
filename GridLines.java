import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/** Class used to draw gridlines on Map. Specifies draw method as well
 * as scaling method.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-11-29
 */

public class GridLines
{
    private double scale;
    private double panXChange;
    private double panYChange;

    private ArrayList<Double> verticalXs_real;
    private int verticalY_real;
    private int horizontalX_real;
    private ArrayList<Double> horizontalYs_real;

    private ArrayList<Double> verticalXs_can;
    private int verticalY_can;
    private int horizontalX_can;
    private ArrayList<Double> horizontalYs_can;

	private Color textColor;
	private Color lineColor;

    public GridLines(double s)
    {
        scale = s;
        panXChange = 0;
        panYChange = 0;

        verticalXs_real = new ArrayList<Double>();
        verticalY_real = -1000;
        horizontalX_real = -2000;
        horizontalYs_real = new ArrayList<Double>();

        verticalXs_can = new ArrayList<Double>();
        verticalY_can = 0;
        horizontalX_can = 0;
        horizontalYs_can = new ArrayList<Double>();

        for (int i=-200; i<200; i++)
        {
            verticalXs_real.add((double)i*12.5);
            horizontalYs_real.add((double)i*12.5);

            verticalXs_can.add((double)i*12.5);
            horizontalYs_can.add((double)i*12.5);
        }

        scaleCoords(scale);

		textColor = Color.DARK_GRAY;
		lineColor = new Color(215,226,226);
    }

	/** When display theme is changed to nightTime, all the text needs to change
	 * color.
	 */
	public void nightTime(boolean nightTime)
	{
		if (nightTime)
		{
			textColor = Color.LIGHT_GRAY;
			lineColor = new Color(93, 97, 122);
		}
		else
		{
			textColor = Color.DARK_GRAY;
			lineColor = new Color(215,226,226);
		}
	}

    /** Use to update gridline coordinates to current scale.
     * @param newScale new scale of map
     */
    public void scaleCoords(double newScale)
    {
        scale = newScale;

        for (int i=0; i<400; i++)
        {
            verticalXs_can.set(i, 
                    (Math.floor(verticalXs_real.get(i)*scale + panXChange)));

            horizontalYs_can.set(i,
                    (Math.floor(horizontalYs_real.get(i)*scale + panYChange)));
        }
    }

    /** Use to update gridline pan to current pan.
     * @param newPanXChange amount to pan all canvas x coordinates
     * @param newPanYChange amount to pan all canvas y coordinates
     */
    public void panCoords(double newPanXChange, double newPanYChange)
    {
        panXChange = newPanXChange;
        panYChange = newPanYChange;
        scaleCoords(scale);
    }

    /** Draws gridlines on Graphics2D object.
     * @param g2 Graphics2D object
     */
    public void draw(Graphics2D g2)
    { 
        g2.setColor(lineColor);

        int spacing;
        if(scale < 0.5)
            spacing = 4;
        else if (scale > 1.5)
            spacing = 1;
        else spacing = 2;

        for (int i=0; i<400; i+=spacing)
        {
            g2.drawLine(verticalXs_can.get(i).intValue(), verticalY_can,
                    verticalXs_can.get(i).intValue(), 2000);
            g2.drawLine(horizontalX_can, horizontalYs_can.get(i).intValue(),
                    2000, horizontalYs_can.get(i).intValue());
        }

        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.setColor(textColor);

        for (int i=0; i<400; i+=(4*spacing))
        {
            g2.drawString(""+verticalXs_real.get(i).intValue(),
                    verticalXs_can.get(i).intValue(), 10);
            g2.drawString(""+horizontalYs_real.get(i).intValue(),
                    10, horizontalYs_can.get(i).intValue());
        }
    }    
}
