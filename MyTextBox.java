import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/** Class extending JTextField. Sets up text box with hint. 
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-10-25
 */
public class MyTextBox extends JTextField implements FocusListener
{
    private String prompt;

    /** Standard constructor. Initializes text box with hint in light gray.
     * Adds focus listener to clear the hint when user tries to type.
     * @param p Prompt (hint)
     */
    public MyTextBox (String p)
    {
	    super(p, 10);
        prompt = p;
        addFocusListener(this);
        setForeground(Color.LIGHT_GRAY);
    }

    /** Focus listener: when user tries to type, hint clears away.
     */
    public void focusGained(FocusEvent e)
    {
        if (getText().equals(prompt))
        {
            setText("");
            setForeground(Color.BLACK);
            repaint();
            revalidate();
        }
    }

    /** Focus listener: when user leaves textfield blank, hint is restored.
     */
    public void focusLost(FocusEvent e)
    {
        if (getText().equals(""))
        {
            setText(prompt);
            setForeground(Color.LIGHT_GRAY);
            repaint();
            revalidate();
        }
    } 
}
