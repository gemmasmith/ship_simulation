import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;

/** Class extending JComboBox. Sets up dropdown menu. 
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-10-25
 */
public class MyComboBox extends JComboBox implements ItemListener {

    private String response;

    /** Standard constructor. Sets up dropdown menu so that it is initially
     * selecting the prompt; fills the rest of dropdown menu choices with
     * strings from options.
     * @param options ArrayList of options for dropdown menu
     * @param prompt Prompt for dropdown menu
     */
    public MyComboBox (ArrayList<String> options, String prompt)
    {
        response = "none";

        addItem (prompt);
        for (int i=0; i<options.size(); i++)
        {
            addItem(options.get(i));
        }

        setSelectedItem (prompt); // initial    
        addItemListener (this);
    }

    public String getResponse(){return response;}

    /** Upon selecting an option from dropdown menu MyComboBox, that choice
     * will be stored in response field.
     */
    public void itemStateChanged (ItemEvent e) {
        if (e.getStateChange()==ItemEvent.SELECTED)
        {
            response = e.getItem().toString();
        }
    }
}
