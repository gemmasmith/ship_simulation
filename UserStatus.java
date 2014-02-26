import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;

/** A graphic representation of the user's life and lobster count.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-11-29
 */

public class UserStatus extends JComponent
{
    private int numLobsterPots;
    private int numLife;
    private double percentLobster;
    private double percentLife;

    public UserStatus()
    {
        numLobsterPots = 0;
        numLife = 5;
        percentLobster = (double)numLobsterPots/5.0;
        percentLife = (double)numLife/5.0;
        setSize(270,115);
    }

	public UserStatus(int lobsters, int life)
	{
        numLobsterPots = lobsters;
        numLife = life;
        percentLobster = (double)numLobsterPots/5.0;
        percentLife = (double)numLife/5.0;
        setSize(270,115);
	}

    /** Updates the status fields.
     * @param newNumLobsters new lobster count
     * @param newNumLife new life count
     */
    public void updateStatus(int newNumLobsters, int newNumLife)
    {
        numLobsterPots = newNumLobsters;
        numLife = newNumLife;
        percentLobster = (double)numLobsterPots/5.0;
        percentLife = (double)numLife/5.0;
    }

	public int getNumLobsters() {return numLobsterPots;}
	public int getNumLife() {return numLife;}

    /** Drawing routine that is called when JFrame is made visible or resized,
     * or upon a repaint() call. Graphically depicts the user's life and lobster
     * counts.
     * @param g Graphics object on which to draw
     */
    public void paintComponent (Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        Color lifeColor;
        if (numLife < 3)
            lifeColor = Color.RED;
        else
            lifeColor = Color.GREEN;
        Color lobsterColor = Color.BLUE;
 
        Rectangle lifeBox = new Rectangle(10,10,200,30);
        Rectangle lobsterBox = new Rectangle(10,60,200,30);
        
        Rectangle life;
        Rectangle lobsters;
        if (numLife < 5)
            life = new Rectangle(10,10,(int)(200.0*percentLife),30);
        else
            life = new Rectangle(10,10,200,30);
        if (numLobsterPots < 10)
            lobsters = new Rectangle(10,60,(int)(200.0*percentLobster),30);
        else
            lobsters = new Rectangle(10,60,200,30);

        g2.setColor(lifeColor);
        g2.draw(life); g2.fill(life);

        g2.setColor(lobsterColor);
        g2.draw(lobsters); g2.fill(lobsters);

        g2.setColor(Color.WHITE);
        g2.draw(lifeBox);
        g2.draw(lobsterBox);

        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        g2.drawString("Life",15,53);
        g2.drawString("Lobster Catch",15,103);
    }

}

