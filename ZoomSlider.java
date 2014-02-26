import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;

/** Lets the user graphically zoom in or out by sliding a knob. 
 * 
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-11-08
 */
public class ZoomSlider extends JSlider implements ChangeListener
{
    private Map map;

    /** Default constructor. Creates JSlider with labels "+" and "-" to
     * represent zooming in and out.
     * @param m Map on which to base all data and response actions
     */
    public ZoomSlider(Map m)
    {
        super(SwingConstants.VERTICAL, 40, 200, 80); 
        map = m;

        setMinorTickSpacing(25);
        setPaintTicks(true);

        //Create the label table
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        JLabel zoomIn = new JLabel("+");
            zoomIn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            zoomIn.setForeground(Color.LIGHT_GRAY);
        JLabel zoomOut = new JLabel("-");
            zoomOut.setFont(new Font("SansSerif", Font.PLAIN, 12));
            zoomOut.setForeground(Color.LIGHT_GRAY);
        labelTable.put(new Integer(200), zoomIn);
        labelTable.put(new Integer(40), zoomOut);
        
        setLabelTable(labelTable);
        
        setPaintLabels(true);
        
        addChangeListener(this);
    }

    /** Upon sliding the ZoomSlider, the overall scale of the map and all its
     * components will be updated.
     */
    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting())
        {
            double newScale = (double) source.getValue()/100.0;
            map.updateScale(newScale);
            map.repaint();
        }
    }
}

