import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/** Class extending JButton. Sets up JButton with color and size preferences. 
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-10-25
 */
public class MyButton extends JButton implements ActionListener 
{
    private String label;
    private Color color;

    /** Standard constructor. Creates JButton displaying l text and with color
     * and font size preferences.
     * @param l Text to display on button
     * @param c Color of text
     * @param fontSize Size of text (dictating size of button)
     */
    public MyButton (String l, Color c, int fontSize)
    {
        label = l;
        color = c;
        setText(label);
        setForeground (color);
        setFont (new Font("SansSerif", Font.BOLD, fontSize));
    }

    /** Listener: not used in current version.
     */
    public void actionPerformed(ActionEvent e)
    {
    }
}   
