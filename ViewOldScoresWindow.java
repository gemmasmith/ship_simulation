import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;

/** Window that pops up when user selects "View Recent Scores" in the menubar.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-12-09
 */

public class ViewOldScoresWindow extends JFrame implements ActionListener
{
	private JFrame window;
    private Main main;
	private ArrayList<Score> scores;
	private MyButton close;

    public ViewOldScoresWindow(Main mn)
    {
        super("View Recent Scores");
		window = this;
        main = mn;
		scores = main.getScores();
        
        setSize(425,350);
        setLocation(100,100);	

        JPanel panel = new JPanel();
    	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        panel.setOpaque(true);
        	panel.setBackground(Color.WHITE);

		JLabel title = new JLabel ("Recent scores");
			title.setFont(new Font("SansSerif", Font.BOLD, 16));
	    	title.setAlignmentX(Component.CENTER_ALIGNMENT);
	        panel.add(title);

		String htmlString = "<html><body><br>";
		if (scores.isEmpty())
			htmlString += "(You have not saved any scores since opening the "+
						 "program.)<br><br>";

		else
		{
			htmlString += "Here are the scores you have saved since opening "+
						  "the program:<br><br>";
			Score s;
			for (int i=0; i<scores.size(); i++)
			{
				s = scores.get(i);
				htmlString += s.getDate()+", "+s.getName()+": "+
											"<br>&nbsp;&nbsp;&nbsp;&nbsp;"+
							  "Lobster Count: "+s.getLobsters()+
							  				"<br>&nbsp;&nbsp;&nbsp;&nbsp;"+
							  "Life Count: "+s.getLife() + "<br>";
			}
		}
		htmlString += "<br><br></body></html>";

		JLabel listScores = new JLabel (htmlString, JLabel.CENTER);
            listScores.setFont(new Font("SansSerif", Font.PLAIN, 12));
            listScores.setAlignmentX(Component.CENTER_ALIGNMENT);			
            panel.add(listScores);

		close = new MyButton("Close", Color.BLACK, 12);
			close.addActionListener(this);
            close.setAlignmentX(Component.CENTER_ALIGNMENT);			
			panel.add(close);

		add(panel);

		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);		
	}

	public void actionPerformed(ActionEvent e)
	{
		window.setVisible(false);
		main.closeWindow();
		window.dispose();		
	}
}


