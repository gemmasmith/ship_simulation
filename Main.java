import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/** Runs ship simulation program. 
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 4.0
 * @since 2013-11-29
 */

public class Main 
{
	private JPanel centerPanel;
	private JPanel overviewPanel;
    private Map map;
    private Timer timer;
    private boolean timerON;
    private JLabel pauseTitle;
    private MyButton startStopButton;
    private MyButton startOver;
    private MyButton help;
    private UserControl userControl;
    private UserStatus userStatus;
    private Ship userShip;
    private boolean userCreatedShip;

    private MapOverview mapOverview;

    private StartWindow startWindow;
    private GameOverWindow gameOverWindow;
    private RulesWindow rulesWindow;
	private ViewOldScoresWindow oldScoresWindow;
	private SaveScoreWindow saveScoreWindow;
	private PrefWindow prefWindow;
	private AboutWindow aboutWindow;

    private boolean firstTime;
	private boolean nightTime;

	private ArrayList<Score> scores;

    public Main()
    {
        map = new Map(this);
        firstTime = true;
		scores = new ArrayList<Score>();
        
        //START WINDOW_________________________________________________________
        startWindow = new StartWindow(this, map);
        
        //RULES WINDOW_________________________________________________________
        rulesWindow = new RulesWindow(this);

        //GAME OVER WINDOW_____________________________________________________
        gameOverWindow = new GameOverWindow(this);

		//PREFERENCES WINDOW___________________________________________________
		prefWindow = new PrefWindow(this);

		//ABOUT WINDOW_________________________________________________________
		aboutWindow = new AboutWindow(this);

        //REAL PROGRAM_________________________________________________________
        JFrame frame = new JFrame("Ship Radar");
            frame.setSize(1000,800);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Overall panel (which will ultimately get added to the JFrame)
        JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setOpaque(true);
            panel.setBackground(Color.BLACK);

        //Title (top)
        drawTitle(panel);

        // Map (center)
        centerPanel = new JPanel();
            centerPanel.setLayout(new BorderLayout());
            centerPanel.setOpaque(true);
            centerPanel.setBackground(new Color(240,255,255));
            centerPanel.add(map, BorderLayout.CENTER);
            panel.add(centerPanel, BorderLayout.CENTER);
        
        // Control panel (right)
        JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(true);
            rightPanel.setBackground(Color.BLACK);
            drawControls(rightPanel);

        JScrollPane scroller = new JScrollPane(rightPanel);

        panel.add(scroller, BorderLayout.EAST);

        // Zoom panel (left)
        JPanel zoomPanel = new JPanel();
            zoomPanel.setOpaque(true);
            zoomPanel.setBackground(Color.BLACK);
            ZoomSlider zoomSlider = new ZoomSlider(map);
            zoomPanel.add(zoomSlider);
            panel.add(zoomPanel, BorderLayout.WEST);

        frame.add(panel);

		// Menu bar
		JMenuBar menuBar = new JMenuBar();
			JMenu program = new JMenu ("Ship Radar");
			JMenu file = new JMenu ("File");
			JMenu help = new JMenu ("Help");		
		
		JMenuItem program_about = new JMenuItem("About Ship Radar");
			program.add(program_about);
			program_about.addActionListener(new MenuListener("about", this));
		
		JMenuItem program_pref = new JMenuItem("Preferences");
			program.add(program_pref);
			program_pref.addActionListener(new MenuListener("pref", this));

		JMenuItem file_startOver = new JMenuItem("Add/Change Your Ship");
			file.add(file_startOver);
			file_startOver.addActionListener(new StartOverListener());
		
		JMenuItem file_oldScores = new JMenuItem("View Recent Scores");
			file.add(file_oldScores);
			file_oldScores.addActionListener(new MenuListener("oldScores", this));

		JMenuItem file_quit = new JMenuItem("Quit");
			file.add(file_quit);
			file_quit.addActionListener(new MenuListener("quit", this));

		JMenuItem help_rules = new JMenuItem("Rules and Objectives");
			help.add(help_rules);
			help_rules.addActionListener(new HelpListener());

		menuBar.add(program);
		menuBar.add(file);
		menuBar.add(help);
		frame.setJMenuBar(menuBar);
		
		frame.setVisible(true);
        startWindow.setVisible(true);
    	rulesWindow.setVisible(false);

        timer = new Timer (175, new TimerListener()); // milliseconds
        timerON = false;
	    timer.start();
    }
    
    /** Creates a new panel for title and adds it to larger panel.
     * @param panel Overall JPanel
     */
    private void drawTitle(JPanel panel)
    {
        JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
            titlePanel.setOpaque(true);
            titlePanel.setBackground(Color.BLACK);
        Font titleFont = new Font("SansSerif", Font.BOLD, 30);
        JLabel title = new JLabel ("Ship Radar", JLabel.CENTER);
            title.setFont(titleFont);
            title.setForeground(Color.WHITE);
            titlePanel.add(title);
        JLabel blank = new JLabel("blank");
            titlePanel.add(blank);

        panel.add(titlePanel, BorderLayout.NORTH);
    }

    /** Adds controls to righthand panel of JFrame.
     * @param rightPanel Righthand panel of JFrame
     */
    private void drawControls(JPanel rightPanel)
    {
        JPanel statusPanel = new JPanel();
            statusPanel.setPreferredSize(new Dimension(264,115));
            statusPanel.setLayout(new BorderLayout());
            statusPanel.setOpaque(true);
            statusPanel.setBackground(Color.BLACK);
            userStatus = new UserStatus();
            statusPanel.add(userStatus, BorderLayout.CENTER);

        JPanel pausePanel = new JPanel();
            pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS));
            pausePanel.setOpaque(true);
            pausePanel.setBackground(Color.BLACK);
            pausePanel.setBorder(new LineBorder(Color.RED, 1));

        userControl = new UserControl(map);
            pausePanel.add(userControl);
        
        JLabel blank = new JLabel("blank");
            blank.setFont(new Font("SansSerif", Font.PLAIN, 16));
            pausePanel.add(blank);        
        pauseTitle = new JLabel("Start or Stop Animation", JLabel.LEFT);
            pauseTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
            pauseTitle.setForeground(Color.DARK_GRAY);
            pauseTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            pausePanel.add(pauseTitle);
        startStopButton = new MyButton("Pause", Color.RED, 24);
            startStopButton.addActionListener(new StartStopListener(timerON));
            startStopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startStopButton.setEnabled(false);
            pausePanel.add(startStopButton);
        JLabel blank2 = new JLabel("blank2");
            blank2.setFont(new Font("SansSerif", Font.PLAIN, 16));
            pausePanel.add(blank2);

        JLabel blank3 = new JLabel("blank2");
            blank3.setFont(new Font("SansSerif", Font.PLAIN, 16));
        help = new MyButton("Rules & Objectives", Color.BLACK, 16);
            help.addActionListener(new HelpListener());
            help.setAlignmentX(Component.CENTER_ALIGNMENT);
            help.setEnabled(false);
        startOver = new MyButton("Add/Change My Ship", Color.RED, 16);
            startOver.addActionListener(new StartOverListener());
            startOver.setAlignmentX(Component.CENTER_ALIGNMENT);
            startOver.setEnabled(false);
        JLabel blank4 = new JLabel("blank4");
            blank4.setFont(new Font("SansSerif", Font.PLAIN, 16));

        overviewPanel = new JPanel();
            overviewPanel.setPreferredSize(new Dimension(264,177));
            overviewPanel.setLayout(new BorderLayout());
            overviewPanel.setOpaque(true);
            overviewPanel.setBackground(new Color(240,255,255));
            mapOverview = new MapOverview(map);
            overviewPanel.add(mapOverview, BorderLayout.CENTER);

        rightPanel.add(statusPanel);
        rightPanel.add(pausePanel);
        rightPanel.add(blank3);
        rightPanel.add(help);
        rightPanel.add(startOver);
        rightPanel.add(blank4);
        rightPanel.add(overviewPanel);
    }
    
    /** Upon panning, zooming, or resizing the window, the red outline of the
     * zoomed view will update, according to the new width, height, and top-left
     * corner of the map.
     */
    public void updateMapOverview()
    {
        mapOverview.updateZoomBox();
        mapOverview.repaint();
    }

    /** Updates the status fields in the user status panel.
     * @param newNumLobsters new lobster count
     * @param newNumLife new life count
     */
    public void updateUserStatus(int newLobsterCount, int newLifeCount)
    {
        userStatus.updateStatus(newLobsterCount, newLifeCount);
        userStatus.repaint();
        if (newLobsterCount >= 5)
        {
            gameOver("You successfully collected 10 lobster traps!");
        }
        else if (newLifeCount <=0)
        {
            gameOver("Your ship crashed too many times.");
        }
    }
	
	/** Upon changing the preference to mute/unmute, the game will turn on/off
	 * sound.
	 */
	public void mute(boolean enable)
	{
		map.mute(enable);
	}

	/** Upon changing the display theme (nighttime or not), the display will
	 * update in the map and map overview.
	 */
	public void nightTime(boolean enable)
	{
		map.nightTime(enable);
		nightTime = enable;
		Color backgroundColor;
		if (nightTime)
			backgroundColor = new Color(31,39,94);
		else
			backgroundColor = new Color(240,255,255);

		centerPanel.setBackground(backgroundColor);
		overviewPanel.setBackground(backgroundColor);
	}

	/** When the user chooses to save a score, his/her score will be saved in
	 * the array of recent scores, and appended to the appropriate file on disk.
	 */
	public void saveScore(String name)
	{
		Score score = new Score(userStatus.getNumLobsters(), userStatus.getNumLife(),
								 name);
		scores.add(score);

		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Calendar cal = Calendar.getInstance();
		String date = (String)dateFormat.format(cal.getTime());

		try
		{
			File file = new File("./SHIP_SCORES_"+date+".txt");
	 
			boolean append = true;

			// if file doesnt exists, then create it
			if (!file.exists())
			{
				file.createNewFile();
				append = false;
			}
	 
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), append);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(score.toString());
			bw.close(); 
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
    /** Upon "winning" or "losing" the game, the gameOverWindow will appear with
     * the appropriate news.
     * @param news message to display to user
     */
    public void gameOver(String news)
    {
		userCreatedShip = false;
		openWindow();        
        gameOverWindow.setTitle(news);
        gameOverWindow.setVisible(true);
    }

	public void openSaveScoreWindow()
	{
		openWindow();
		saveScoreWindow = new SaveScoreWindow(this);
		saveScoreWindow.setHint();
	}

    /** Set the global userShip field to the newly created ship.
     * @param newShip ship returned by user creating new ship
     */
    public void setUserShip(Ship newShip)
    {
        userCreatedShip = true;
        map.setUserCreatedShip(true);
        userShip = newShip;
        userControl.setSpeedChanged(false);
        userStatus.updateStatus(0,5);
        userStatus.repaint();
    }

    /** Set to true when start window is open.
     * @param bool true if start window is open, false otherwise
     */
    public void setFirstTime(boolean bool){firstTime = bool;}

	/** Pauses game and disables all widget functions when a new window is
	 * opened.
	 */
	public void openWindow()
	{
        pauseTitle.setForeground(Color.DARK_GRAY);
        startStopButton.setText("Start");
        startStopButton.setEnabled(false);
        userControl.setEnabled(false);
        startOver.setEnabled(false);
        help.setEnabled(false);
        timerON = false;		
	}

	/** Resumes game and enables all widget functions when a popup window is
	 * closed.
	 */
	public void closeWindow()
	{
        pauseTitle.setForeground(Color.WHITE);
        startStopButton.setEnabled(true);
        startStopButton.setText("Pause");
        startOver.setEnabled(true);
        timerON = true;		
	}

    /** Opens start window.
     */
    public void openStartWindow()
    {        
		map.removeShip(userShip);
		map.setUserCreatedShip(false);
		userCreatedShip = false;
        firstTime = true;
		openWindow();
        startWindow.setVisible(true);
   }

    /** Closes start window.
     */
    public void closeStartWindow()
    {
        startWindow.setVisible(false);
        if (userCreatedShip)
		{
            rulesWindow.setVisible(true);
		}
        else
        {
            userStatus.updateStatus(0,0);
            userStatus.repaint();
            map.setUserCreatedShip(false);
            pauseTitle.setForeground(Color.WHITE);
            startStopButton.setEnabled(true);
            startStopButton.setText("Pause");
            startOver.setEnabled(true);
            help.setEnabled(false);
            timerON = true;
        }
    }

    /** Opens rules and objectives window.
     */
    public void openRulesWindow()
    {
		openWindow();
		rulesWindow.setVisible(true);
    }
    
    /** Closes rules and objectives window.
     */
    public void closeRulesWindow()
    {
        firstTime = false;
        startWindow.setVisible(false);
        pauseTitle.setForeground(Color.WHITE);
        startStopButton.setEnabled(true);
        startStopButton.setText("Pause");
        startOver.setEnabled(true);
        if (userCreatedShip)
        {      
            userControl.setEnabled(true);
            help.setEnabled(true);
        }
        else
        {
            userStatus.updateStatus(0,0);
            userStatus.repaint();
        }
        timerON = true;
        
        rulesWindow.setVisible(false);
    }

	/** Class to implement ActionListener for the menu items.
	 */
	class MenuListener implements ActionListener
	{
		private String command;
		private Main main;

		public MenuListener(String com, Main mn)
		{
			command = com;
			main = mn;
		}
		
		/** Upon clicking a menu item, the appropriate window will be opened.
		 */
		public void actionPerformed(ActionEvent event)
		{
			openWindow();			
			if (command == "about")
				aboutWindow.setVisible(true);
			
			else if (command == "pref")
				prefWindow.setVisible(true);
			
			else if (command == "oldScores")
				oldScoresWindow = new ViewOldScoresWindow(main);

			else if (command == "quit")
				quit();
		}
	}

    /** Class to implement ActionListener for the help button.
     */
    class HelpListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            openRulesWindow();
        }
    }
    /** Class to implement ActionListener for the startOver button.
     */
    class StartOverListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            openStartWindow();
        }
    }

    /** Class to implement ActionListener for the startStopButton.
     */
    class StartStopListener implements ActionListener
    {
        private boolean start;

        public StartStopListener(boolean s)
        {
            start = s;
        }
        /** Upon clicking startStopButton, the animation will start/stop, and
         * certain controls will be enabled/disabled.
         */
        public void actionPerformed(ActionEvent event)
        {
            MyButton b = (MyButton) event.getSource();
            if (timerON)
            {
                timerON = false;
                b.setText("Start");
            }
            else
            {
                timerON = true;
                b.setText("Pause");
            }
        }
    }

    /** Class to implement ActionListener for the timer.
     */
    class TimerListener implements ActionListener
    {
        /** Upon hearing the timer, the map will update and repaint, giving the 
         * effect of animation.
         */
        public void actionPerformed(ActionEvent event)
        {
            if (timerON)
            {
                map.animate();
                map.repaint();
            }
        }
    }
   
	public ArrayList<Score> getScores() {return scores;}
	public Map getMap() {return map;}
	public MapOverview getMapOverview() {return mapOverview;}
	public UserStatus getUserStatus() {return userStatus;}
	public Ship getUserShip() {return userShip;}
	public boolean getUserCreatedShip() {return userCreatedShip;}

    /** Quits program.
     */
    public void quit()
    {
        System.exit(0);
    }

    public static void main(String[] args)
    {
        new Main();
    }
}
