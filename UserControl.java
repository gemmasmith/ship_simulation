import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;

/** Control panel referring to the current user ship (if any). Control
 * panel consists of information (text) about the ship, a widget to change the
 * speed of the ship.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 2.0
 * @since 2013-11-29
 */
public class UserControl extends JPanel implements ChangeListener 
{
    private Map map;
    
    private JLabel title;
    private JLabel [] infos;
    private String [] infoStrings;
    private JLabel speedTitle;
    private SpeedSlider speedSlider;
    private JLabel backTitle;
    private MyButton originalSpeedButton;
    private boolean speedChanged;
    
    private Ship userShip;

    /** Standard constructor. Formats control panel and adds all proper widgets
     * to "Select a ship." control panel. Adds listeners where necessary.
     * @param m Map on which to base all data and response actions
     */
    public UserControl(Map m)
    {
        map = m;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(true);
        setBackground(Color.BLACK);
        setBorder (new LineBorder(Color.RED, 1));

        title = new JLabel("Your boat information.");
            title.setFont(new Font("SansSerif", Font.BOLD, 16));
            title.setForeground(Color.WHITE);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel blank = new JLabel("blank");
            blank.setFont(new Font("SansSerif", Font.PLAIN, 6));

        infos = new JLabel [4];
        for (int i=0; i<infos.length; i++)
        {
            infos[i] = new JLabel(".");
            infos[i].setFont(new Font("SansSerif", Font.PLAIN, 12));
            infos[i].setForeground(Color.BLACK);
            infos[i].setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        speedTitle = new JLabel("Adjust slider to change your speed.");
            speedTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
            speedTitle.setForeground(Color.DARK_GRAY);
            speedTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        speedSlider = new SpeedSlider();
            speedSlider.addChangeListener(this);
            speedSlider.setEnabled(false);
            speedChanged = false;

        backTitle = new JLabel("Change your speed back to original.");
            backTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
            backTitle.setForeground(Color.DARK_GRAY);
            backTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        originalSpeedButton = new MyButton
                              ("Undo changes", Color.BLACK, 12);
            originalSpeedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            originalSpeedButton.addActionListener(new BackListener());
            originalSpeedButton.setEnabled(false);


        add(blank);
        add(title);
        for (int i=0; i<infos.length; i++)
            add(infos[i]);
        add(speedTitle);
        add(speedSlider);
        add(backTitle);
        add(originalSpeedButton);

        javax.swing.Timer timer = new javax.swing.Timer (100, 
                                                         new TimerListener());
	    timer.start();
    }
   
    public void setSpeedChanged(boolean bool) {speedChanged = bool;}

    /** Enables or disables this set of interactive widgets.
     * @param enable true if enabling, false otherwise
     */
    public void setEnabled(boolean enable)
    {
        if (enable)
        {
            title.setForeground(Color.WHITE);
            speedSlider.setEnabled(true);
            speedTitle.setForeground(Color.WHITE);
            
            userShip = map.getUserShip();
            
            double angle = userShip.getDir();
            String direction = "No direction";

            if (angle > 10 && angle < 80)
                direction = "NE";
            else if (angle >= 80 && angle <= 100)
                direction = "E";
            else if (angle > 100 && angle < 170)
                direction = "SE";
            else if (angle >= 170 && angle <= 190)
                direction = "S";
            else if (angle > 190 && angle < 260)
                direction = "SW";
            else if (angle >= 260 && angle <= 280)
                direction = "W";
            else if (angle > 280 && angle < 350)
                direction = "NW";
            else if (angle >= 350 || angle <= 10)
                direction = "N";

            infoStrings = new String [4];
            infoStrings[0] = "Name: "+userShip.getText();
            infoStrings[1] = "Type: "+userShip.getType();
            infoStrings[2] = "Current coordinates: ("+userShip.getRealX()+
                             ", "+userShip.getRealY()+")";
            infoStrings[3] = "Current direction: "+direction;

            for (int i=0; i<infos.length; i++)
            {
                infos[i].setForeground(Color.WHITE);
                infos[i].setText(infoStrings[i]);
            }

            if (speedChanged)
            {
                backTitle.setForeground(Color.WHITE);
                originalSpeedButton.setEnabled(true);
            }
        }
        else
        {
            title.setForeground(Color.DARK_GRAY);
            speedTitle.setForeground(Color.DARK_GRAY);
            speedSlider.setEnabled(false);
            backTitle.setForeground(Color.DARK_GRAY);
            originalSpeedButton.setEnabled(false);
            for (int i=0; i<infos.length; i++)
            {
                infos[i].setForeground(Color.BLACK);
                infos[i].setText(".");
            }
        }
    }

    /** Upon changing the speedSlider, checks dropdown menu for ship choice and
     * changes the speed of said ship according to value of speedSlider.
     */
    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting())
        {
            int speed = source.getValue();
            speedChanged = true;
            userShip.changeSpeed(speed);
            backTitle.setForeground(Color.WHITE);
            originalSpeedButton.setEnabled(true);
        }
    }
    /** Class to implement ActionListener for the originalSpeedButton.
     */
    class BackListener implements ActionListener
    {
        /** Upon clicking originalSpeedButton, checks dropdown menu for ship
         * choice and changes the speed of said ship according to value of
         * slider.
         */
        public void actionPerformed(ActionEvent event)
        {
            userShip.changeToOriginalSpeed();
            backTitle.setForeground(Color.DARK_GRAY);
            originalSpeedButton.setEnabled(false);
        }
    }
    /** Class to implement ActionListener for the timer.
     */
    class TimerListener implements ActionListener
    {
        /** Upon hearing the timer, the widgets will update, depending on
         * whether the user has selected a ship and if so, the type of ship.
         */
        public void actionPerformed(ActionEvent event)
        {
            if (map.getUserCreatedShip())
            {
                title.setForeground(Color.WHITE);
                speedSlider.setEnabled(true);
                speedTitle.setForeground(Color.WHITE);
                
                userShip = map.getUserShip();
                
                double angle = userShip.getDir();
                String direction = "No direction";

                if (angle > 10 && angle < 80)
                    direction = "NE";
                else if (angle >= 80 && angle <= 100)
                    direction = "E";
                else if (angle > 100 && angle < 170)
                    direction = "SE";
                else if (angle >= 170 && angle <= 190)
                    direction = "S";
                else if (angle > 190 && angle < 260)
                    direction = "SW";
                else if (angle >= 260 && angle <= 280)
                    direction = "W";
                else if (angle > 280 && angle < 350)
                    direction = "NW";
                else if (angle >= 350 || angle <= 10)
                    direction = "N";
    
                infoStrings = new String [4];
                infoStrings[0] = "Name: "+userShip.getText();
                infoStrings[1] = "Type: "+userShip.getType();
                infoStrings[2] = "Current coordinates: ("+userShip.getRealX()+
                                 ", "+userShip.getRealY()+")";
                infoStrings[3] = "Current direction: "+direction;

                for (int i=0; i<infos.length; i++)
                {
                    infos[i].setForeground(Color.WHITE);
                    infos[i].setText(infoStrings[i]);
                }
            }
            else
            {
                title.setForeground(Color.DARK_GRAY);
                speedTitle.setForeground(Color.DARK_GRAY);
                speedSlider.setEnabled(false);
                for (int i=0; i<infos.length; i++)
                {
                    infos[i].setForeground(Color.BLACK);
                    infos[i].setText(".");
                }
            }
        }
    }
}
