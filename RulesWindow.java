import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

/** Window displaying rules and objectives of the game.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-11-29
 */

public class RulesWindow extends JFrame implements ActionListener
{
    private Main main;
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 16);
    private final Font roFont = new Font("SansSerif", Font.PLAIN, 14);

    public RulesWindow(Main mn)
    {
        super("Rules and Objectives");
        main = mn;
        
        setSize(450,350);

        JPanel rulesPanel = new JPanel();
            rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
            rulesPanel.setOpaque(true);
            rulesPanel.setBackground(Color.WHITE);
        JLabel rulesTitle = new JLabel ("Game objectives and rules",
                JLabel.CENTER);
            rulesTitle.setFont(titleFont);
            rulesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            rulesPanel.add(rulesTitle);

        JLabel rulesObjtvs = new JLabel ("<html><body><br>"+
            "Objective: Collect any 5 lobster traps from around the bay.<br>"+
            "<br>"+
            "1. <u>How to change direction</u>: Click the map surrounding your <br>"+
            "   lobsterboat. Your boat will point toward where you clicked.<br>"+
            "   There are only 8 possible directions (N, NE, E, SE, S, etc.).<br>"+
            "2. <u>Collisions</u>: Unlike the other ships in the simulation, your<br>"+
            "   boat will not sink upon the first collision. Whichever ship <br>"+
            "   you hit will sink. You will be moved back to your starting <br>"+
            "   location, and your <i>life count</i> will decrease.<br>"+
            "<b>   You can only die 5 times before your game will finish</b>.<br>"+
            "3. <u>How to change speed</u>: Adjust the slider on the right.<br><br>"+
            "Enjoy!<br></body></html>", JLabel.CENTER);
            rulesObjtvs.setFont(roFont);
            rulesObjtvs.setAlignmentX(Component.CENTER_ALIGNMENT);
            rulesPanel.add(rulesObjtvs);

        MyButton ok = new MyButton("OK", Color.BLACK, 12);
            ok.addActionListener(this);
            ok.setAlignmentX(Component.CENTER_ALIGNMENT);
            rulesPanel.add(ok);

        JScrollPane startScroller = new JScrollPane(rulesPanel);        
        
        add(startScroller);

        setLocation(100,100);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    /** Upon clicking "OK", the rules and objective window will close and the
     * simulation will begin.
     */
    public void actionPerformed(ActionEvent event)
    {
        main.closeRulesWindow();
    }
}

