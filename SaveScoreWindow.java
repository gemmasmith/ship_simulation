import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;

/** Window that pops up when user finishes game and chooses to "start over".
 * Gives the user the option of saving his/her score to a file (named 
 * "SHIP_SCORES_yyyy.mm.dd" in the current working directory). As long as the
 * user is playing in the same day, the new scores will just be appended onto
 * the end of the text file. If the user has not yet played that day, a new file
 * will be created.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-12-09
 */

public class SaveScoreWindow extends JFrame implements ActionListener
{
	private JFrame window;
    private Main main;
	private ArrayList<Score> scores;
	private String defaultName;
	private MyTextBox newName;
	private MyButton save;
	private MyButton cancel;

    public SaveScoreWindow(Main mn)
    {
        super("Save Score");
        main = mn;
		window = this;
        
        setSize(425,100);
        setLocation(100,100);

		scores = main.getScores();
		defaultName = "game"+(scores.size()+1);

        JPanel panel = new JPanel();
    	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        panel.setOpaque(true);
        	panel.setBackground(Color.WHITE);

		JLabel title = new JLabel ("Save Score As:");
			title.setFont(new Font("SansSerif", Font.BOLD, 12));
	    	title.setAlignmentX(Component.CENTER_ALIGNMENT);
	        panel.add(title);

		newName = new MyTextBox(defaultName);
			panel.add(newName);

		JPanel subPanel = new JPanel();
			subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.LINE_AXIS));
	        subPanel.setOpaque(true);
        	subPanel.setBackground(Color.WHITE);
			subPanel.add(Box.createHorizontalGlue());			

		save = new MyButton("Save", Color.BLACK, 12);
			save.addActionListener(this);
			subPanel.add(save);
		
		subPanel.add(Box.createRigidArea(new Dimension(10, 0)));		
		
		cancel = new MyButton("Don't Save Score", Color.BLACK, 12);
			cancel.addActionListener(this);
			subPanel.add(cancel);

		subPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(subPanel);
		add(panel);

		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);		
	}

	public void setHint()
	{
		save.requestFocus();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == save)
		{
			main.saveScore(newName.getText());
		}
		window.setVisible(false);

		main.openStartWindow();
		window.dispose();		
	}

}
