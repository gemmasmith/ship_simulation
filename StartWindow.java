import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

/** Window that pops up when the user begins simulation or chooses to add/change
 * his/her ship. Contains interactive controls for user to choose to add a new 
 * ship to the map.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-11-29
 */

public class StartWindow extends JFrame
{
    private Main main;
    private Map map;

    public StartWindow(Main mn, Map m)
    {
        super("Start");
        main = mn;
        map = m;
        
        setSize(425,350);

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setOpaque(true);
        startPanel.setBackground(Color.WHITE);
        JLabel startTitle = new JLabel ("Would you like to control your own lobsterboat?", 
                JLabel.CENTER);
            startTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
            startTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            startPanel.add(startTitle);
        AddShipControl addShipControl = new AddShipControl(map, main);
            startPanel.add(addShipControl);
        JScrollPane startScroller = new JScrollPane(startPanel);        
        
        add(startScroller);

        setLocation(100,100);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);		
    }

}

