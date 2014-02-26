import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

/** Window that pops up when the user either "wins" or "loses" game.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 1.0
 * @since 2013-11-29
 */

public class GameOverWindow extends JFrame
{
    private JLabel gameOverTitle;
    private Main main;

    public GameOverWindow(Main mn)
    {
        super("Game Over");
        setSize(410,125);

        main = mn;

        JPanel gameOverPanel = new JPanel();
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
        gameOverPanel.setOpaque(true);
        gameOverPanel.setBackground(Color.WHITE);
        
        gameOverTitle = new JLabel ("Game over.", JLabel.CENTER);
            gameOverTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
            gameOverTitle.setForeground(Color.RED);
            gameOverTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            gameOverPanel.add(gameOverTitle);

        JLabel subTitle = new JLabel("Would you like to play again?", 
				JLabel.CENTER);
            subTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
            subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            gameOverPanel.add(subTitle);            

        MyButton playAgain = new MyButton("Start over", Color.BLACK, 12);
            playAgain.addActionListener(new startOverListener(true, this));
            playAgain.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverPanel.add(playAgain);
        MyButton quit = new MyButton("Quit", Color.BLACK, 12);
            quit.addActionListener(new startOverListener(false, this));
            quit.setAlignmentX(Component.CENTER_ALIGNMENT);
            gameOverPanel.add(quit);

        add(gameOverPanel);

        setLocation(100,100);        
    }

    /** Sets the text in the window to newTitle.
     * @param newTitle text to display
     */
    public void setTitle(String newTitle)
    {
        gameOverTitle.setText(newTitle);
    }

    /** Class to implement ActionListener for the playAgain and quit buttons.
     */
    class startOverListener implements ActionListener
    {
        private boolean startOver;
        private GameOverWindow window;

        public startOverListener(boolean bool, GameOverWindow w)
        {
            window = w;
            startOver = bool;
        }
        /** Upon clicking either the playAgain or quit buttons, the
         * GameOverWindow closes and the game either starts over or quits.
         */
        public void actionPerformed(ActionEvent event)
        {
            if (startOver)
            {
                window.setVisible(false);
                main.openSaveScoreWindow();
            }
            else
            {
                window.setVisible(false);
				main.quit();
            }
        }
    }
}

