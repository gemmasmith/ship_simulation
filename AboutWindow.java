import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;

/** Window that pops up when user selects "About Ship Radar" from the menubar.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-12-09
 */

public class AboutWindow extends JFrame implements ActionListener
{
	private JFrame window;
    private Main main;

    public AboutWindow(Main mn)
    {
        super("About");
        main = mn;
		window = this;
        
        setSize(425,175);
        setLocation(100,100);

        JPanel panel = new JPanel();
    	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        panel.setOpaque(true);
        	panel.setBackground(Color.WHITE);

		JLabel title = new JLabel ("About Ship Radar", JLabel.CENTER);
			title.setFont(new Font("SansSerif", Font.BOLD, 16));
			title.setForeground(Color.RED);
	    	title.setAlignmentX(Component.CENTER_ALIGNMENT);
	        panel.add(title);

		JLabel aboutText = new JLabel ("<html><body><br>"+
            "Author: Gemma Smith<br>"+
            "Version: 5.0 <br>"+
			"Tufts, COMP 86<br>"+
			"Object-Oriented Programming for Graphical User Interfaces<br>"+
            "Fall 2013<br>"+
			"</body></html>", JLabel.CENTER);
            aboutText.setFont(new Font("SansSerif", Font.BOLD, 12));
            aboutText.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(aboutText);

        MyButton close = new MyButton("Close", Color.BLACK, 12);
            close.addActionListener(this);
            close.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(close);

		add(panel);

		window.setVisible(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);		
	}
    /** Upon clicking "Close", the about window will close and the
     * simulation will restart.
     */
    public void actionPerformed(ActionEvent event)
    {
        main.closeWindow();
		window.setVisible(false);
    }

}


