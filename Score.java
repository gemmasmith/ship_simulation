import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/** Manages the attributes and functions related to users' scores. Specifically,
 * the name of the game, time of the game, and life/lobster scores.
 *
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 4.0
 * @since 2013-11-29
 */

public class Score 
{
	private String name;
	private String time;
	private int numLobsters;
	private int numLife;

	public Score(int lobsters, int life, String n)
	{
		name = n;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		time = (String)dateFormat.format(cal.getTime());

		numLobsters = lobsters;
		numLife = life;
	}

	public String toString()
	{
		return (time+", "+name+":"+
				"\n\tLobster Catch: "+numLobsters+
				"\n\tLife Count: "+numLife+"\n");
	}

	public String getName(){return name;}
	public String getDate(){return time;}
	public int getLobsters(){return numLobsters;}
	public int getLife(){return numLife;}
}
