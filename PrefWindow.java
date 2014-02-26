import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;

/** Window that pops up when the user selects "Preferences" from the menubar.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-12-09
 */

public class PrefWindow extends JFrame implements ActionListener,
	   											  ItemListener
{
	private JFrame window;
    private Main main;
	private JCheckBox mute;
	private JCheckBox nightTime;

    public PrefWindow(Main mn)
    {
        super("Preferences");
        main = mn;
		window = this;
        
        setSize(200,100);
        setLocation(300,300);

        JPanel panel = new JPanel();
    	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        panel.setOpaque(true);
        	panel.setBackground(Color.WHITE);

		mute = new JCheckBox("Mute volume");
			mute.addItemListener(this);
			mute.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(mute);
		nightTime = new JCheckBox("Nighttime Theme");
			nightTime.addItemListener(this);
			nightTime.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(nightTime);

		MyButton done = new MyButton("Done", Color.BLACK, 12);
			done.addActionListener(this);
			done.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(done);

		add(panel);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
	}
	public void itemStateChanged(ItemEvent e)
	{
    	Object source = e.getItemSelectable();
		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			if (source == mute)
			{
				main.mute(true);
			}
			else if (source == nightTime)
			{
				main.nightTime(true);
			}
		}
		if (e.getStateChange() == ItemEvent.DESELECTED)
		{
			if (source == mute)
			{
				main.mute(false);
			}
			else if (source == nightTime)
			{
				main.nightTime(false);
			}			
		}
	}

    /** Listener: not used in current version.
     */
    public void actionPerformed(ActionEvent e)
    {
		window.setVisible(false);
		main.closeWindow();
    }

}

