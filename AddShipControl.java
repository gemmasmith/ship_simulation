import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;

/** Control panel for adding a new ship to map. Sets up proper dropdown menu,
 * textfields, and buttons for one integrated "interactive control".
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 2.0
 * @since 2013-11-29
 */

public class AddShipControl extends JPanel
{
    private Main main;
    private Map map;
    private MyTextBox shipNameInput;
    private String shipName;
    private MyTextBox lengthInput;
    private int length;
    private MyTextBox xcoordInput;
    private int xcoord;
    private MyTextBox ycoordInput;
    private int ycoord;
    private MyComboBox directionInput;
    private double direction;
    private int speed;
    private SpeedSlider speedSlider;

    /** Standard constructor. Formats control panel and adds all proper widgets
     * to "Add a new ship" control panel. Adds listeners where necessary.
     * @param m Map on which to base all data and response actions
     */
    public AddShipControl(Map m, Main mn)
    {
        main = mn;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(true);
        setBackground(Color.WHITE);

        map = m;
    
        JLabel title = new JLabel("Yes I would! My new lobsterboat:");
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel addShipPanel = new JPanel();
        addShipPanel.setLayout(new BoxLayout(addShipPanel, BoxLayout.Y_AXIS));
        addShipPanel.setOpaque(true);
        addShipPanel.setBackground(Color.WHITE);
        addShipPanel.setBorder (new LineBorder(Color.RED, 1));

        JLabel nameLabel = new JLabel("Ship name");
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            nameLabel.setForeground(Color.DARK_GRAY);
        JLabel xLabel = new JLabel("Starting x coordinate");
            xLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            xLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            xLabel.setForeground(Color.DARK_GRAY);
        JLabel yLabel = new JLabel("Starting y coordinate");
            yLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            yLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            yLabel.setForeground(Color.DARK_GRAY);
        JLabel speedLabel = new JLabel("Starting speed");
            speedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            speedLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            speedLabel.setForeground(Color.DARK_GRAY);

        MyButton addShipButton = new MyButton("Add my ship", Color.BLACK, 12);
            addShipButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            addShipButton.addActionListener(new AddListener(true));

        JLabel noAddLabel = new JLabel("No, I'd rather not. "+
                                       "Show me the plain simulation.");
            noAddLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        MyButton noAdd = new MyButton("Don't add any boat", Color.BLACK, 12);
            noAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
            noAdd.addActionListener(new AddListener(false));

        shipNameInput = new MyTextBox("Ship name");
            shipNameInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        xcoordInput = new MyTextBox("Starting x coordinate");
            xcoordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        ycoordInput = new MyTextBox("Starting y coordinate");
            ycoordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        ArrayList<String> directions = new ArrayList<String>
            (Arrays.asList("N","NE","E","SE","S","SW","W","NW"));
        directionInput = new MyComboBox(directions, "Select a direction.");
            directionInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        speedSlider = new SpeedSlider();
        speedSlider.addChangeListener(new SliderListener());

        add(title);

        addShipPanel.add(nameLabel);
        addShipPanel.add(shipNameInput);
        addShipPanel.add(xLabel);
        addShipPanel.add(xcoordInput);
        addShipPanel.add(yLabel);
        addShipPanel.add(ycoordInput);
        addShipPanel.add(directionInput);
        
        addShipPanel.add(speedLabel);
        addShipPanel.add(speedSlider);
        addShipPanel.add(addShipButton);

        add(addShipPanel);
        add(noAddLabel);
        add(noAdd);
    }

    /** Checks all input (textfields and dropdown menu) for validity.
     * @return Returns true if form has been fully completed, if textfields have
     * not returned any exceptions, and if selection has been made in dropdown
     * menu. Returns false otherwise, as well as printing error messages to
     * stdout.
     */
    private boolean validInput()
    {
        boolean completeForm = true; //all textfields have been filled in
        boolean noExceptions = true; //all textfields (except name) have int
        boolean selectionMade = true; //user has selected a ship type

        if (shipNameInput.getText()=="Ship name" ||
            xcoordInput.getText()=="Starting x coordinate" ||
            ycoordInput.getText()=="Starting y coordinate")
        {
            System.out.println("ERROR: Please fill in all text fields before "+
                               "adding your ship.");
            completeForm = false;
        }
        try
        {
            xcoord = Integer.parseInt(xcoordInput.getText());
            ycoord = Integer.parseInt(ycoordInput.getText());
        }
        catch (NumberFormatException except)
        {
            System.out.println("ERROR: Ship coordinates and speed need to be "+
                               "entered as integers.");
            noExceptions = false;
        }
        if (directionInput.getResponse() == "none")
        {
            System.out.println("ERROR: Please choose a direction.");
            selectionMade = false;
        }
        else
        {
            String d = directionInput.getResponse();
            if (d == "N") direction = 0;
            else if (d == "NE") direction = Ship.NE;
            else if (d == "E") direction = Ship.E;
            else if (d == "SE") direction = Ship.SE;
            else if (d == "S") direction = Ship.S;
            else if (d == "SW") direction = Ship.SW;
            else if (d == "W") direction = Ship.W;
            else if (d == "NW") direction = Ship.NW;
        }
        shipName = shipNameInput.getText();
        speed = speedSlider.getValue();

        return (completeForm && noExceptions && selectionMade);
    }

    /** Class to implement ActionListener for the addShipButton.
     */
    class AddListener implements ActionListener
    {
        private boolean add;

        public AddListener(boolean addShip)
        {
            add = addShip;
        }
        /** Upon clicking addShipButton, the user input will be checked and, if
         * valid, his/her ship will be added to the map.
         */
        public void actionPerformed(ActionEvent event)
        {
            if (add)
            {
                if (validInput())
                {
                    Ship newShip = map.addUserShip("Lobsterboat",shipName,
                        xcoord,ycoord,speed,direction);
                    main.setUserShip(newShip);
                    map.repaint();
                    main.closeStartWindow();
                }
            }
            else
            {
                main.closeStartWindow();
            }
        }
    }

    /** Class to implement ChangeListener for the speedSlider.
     */
    class SliderListener implements ChangeListener
    {
        /** Upon moving the speedSlider, the speed field will be updated.
         */
        public void stateChanged(ChangeEvent e)
        {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting())
                speed = source.getValue();
            if (0 < speed && speed < 2)
                speed = 2;
        }
    }
}
