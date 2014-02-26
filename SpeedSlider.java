import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;

/** Class extending JSlider. Sets up JSlider to represent the range of ship
 * speed.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-10-25
 */
public class SpeedSlider extends JSlider
{
    /** Default constructor. Creates JSlider with labels "Stopped", "Slow", and
     * "Fast", representing the range of ship speed.
     */
    public SpeedSlider()
    {
        super(SwingConstants.HORIZONTAL, 0, 20, 5);
        setMajorTickSpacing(5);
        setMinorTickSpacing(1);
        setPaintTicks(true);

        //Create the label table
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        JLabel stopped = new JLabel("Stop");
            stopped.setFont(new Font("SansSerif", Font.PLAIN, 8));
            stopped.setForeground(Color.LIGHT_GRAY);
        JLabel slow = new JLabel("Slow");
            slow.setFont(new Font("SansSerif", Font.PLAIN, 8));
            slow.setForeground(Color.LIGHT_GRAY);
        JLabel fast = new JLabel("Fast");
            fast.setFont(new Font("SansSerif", Font.PLAIN, 8));
            fast.setForeground(Color.LIGHT_GRAY);
        labelTable.put(new Integer(0), stopped);
        labelTable.put(new Integer(2), slow);
        labelTable.put(new Integer(20), fast);
        
        setLabelTable(labelTable);
        
        setPaintLabels(true);
    }

}
