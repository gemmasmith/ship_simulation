import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.geom.Ellipse2D.Double;

import java.io.*;
import javax.sound.sampled.*;

/** Map class, extends JComponent. Creates and maintains a drawing canvas to
 * represent the "radar screen". Provides main redrawing routine. Maintains
 * list of ships on radar.
 * 
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 4.0
 * @since 2013-11-29
 */

public class Map extends JComponent implements MouseListener,
                                               MouseMotionListener
{
    private Main main;
    private boolean animationON;

    private ArrayList<Ship> listShips;
    private ArrayList<Ship> listSunkenShips;
    private int numShips;
    private int random = 1; //counts the number of addRandomShips calls
    private double scale; // 0<scale<1 for zoomed out, 1<scale for zoomed in

    private boolean userCreatedShip;
    private Ship userShip;
    private int userOriginalX;
    private int userOriginalY;
    private int userLobsterCount;
    private int userLifeCount;

    private Island islands;
    private Rocks rocks;
    private NavBuoys navBuoys;
    private ArrayList<LobsterPot> lobsterPots;
    private ArrayList<Explosion> listExplosions;

    private GridLines gridlines;

    private int realVisibleW;
    private int realVisibleH;
    private int realVisibleX;
    private int realVisibleY;

    private double lastPanXChange;
    private double lastPanYChange;
    private double panXChange;
    private double panYChange;
    private double panAnchorX;
    private double panAnchorY;
    private boolean panDragging;

    private final File EXPLODE = new File("./explosion.wav");
    private final File U_COLLIDE = new File("./oops.wav");
    private final File U_LOBSTER = new File("./success.wav");

	private boolean mute;
    private Clip explodeNoise;
    private Clip userCollisionNoise;
    private Clip userLobsterNoise;
    
    /** Default constructor 
     */
    public Map(Main mn)
    {
        main = mn;
        scale = 0.8;
        panXChange = 0;
        panYChange = 0;

        listShips = new ArrayList<Ship>();
        //new Sailboat(scale, real_length, xcoord_real, ycoord_real, currDirection, 
        //             currSpeed, name, panX, panY)
        listShips.add(new Sailboat(scale, 50, 450, 250, Ship.SE, 3, "Traveller",0.0,0.0)); 
        listShips.add(new Sailboat(scale, 20, 100, 80, Ship.W, 1, "Lil Bo Peep",0.0,0.0));
        listShips.add(new Sailboat(scale, 20, 200, -50, Ship.S, 1, "Second Wind",0.0,0.0));
        listShips.add(new Sailboat(scale, 20, 100, 700, Ship.N, 1, "Wind Dancer",0.0,0.0));
        listShips.add(new Sailboat(scale, 30, 900,1000, Ship.NW,2,"Full Sail",0.0,0.0));
        listShips.add(new Sailboat(scale, 50, 400, 250, Ship.SE, 3, "High roller",0.0,0.0));
        listShips.add(new Sailboat(scale, 20, 500, 300, Ship.NE, 1, "Skipper",0.0,0.0));
        listShips.add(new Sailboat(scale, 20, 30, 400, Ship.SW, 2, "Porpoise",0.0,0.0));
        //new Lobsterboat(scale, xcoord_real, ycoord_real, currDirection, currSpeed, 
        //                name, panX, panY)
        listShips.add(new Lobsterboat(scale, 200, 450, Ship.SE,5,"Lobstah Mobstah",0.0,0.0));
        listShips.add(new Lobsterboat(scale, 600,-50, Ship.SW,5,"High Maintenance",0.0,0.0));
        listShips.add(new Lobsterboat(scale, 250, 550, Ship.NW, 0, "Prawn Star",0.0,0.0));
        listShips.add(new Lobsterboat(scale, 600,400, Ship.W, 5, "After Taxes",0.0,0.0));
        listShips.add(new Lobsterboat(scale, 750,700,Ship.N, 4, "Lobster King",0.0,0.0));
        listShips.add(new Lobsterboat(scale, 400, 500, Ship.NE, 10, "Full Throttle",0.0,0.0));
        listShips.add(new Lobsterboat(scale, 100, 100, Ship.S, 10, "Pecabee",0.0,0.0));
        //new Tanker(scale, xcoord_real, ycoord_real, currDirection, currSpeed,
        //           name, panX, panY)
        listShips.add(new Tanker(scale, -100, 300, Ship.E, 2, "You Betcha",0.0,0.0));
        listShips.add(new Tanker(scale, 1100, 600, Ship.W, 2, "Oily",0.0,0.0));
        listShips.add(new Tanker(scale, -300, 1200, Ship.NE, 2, "Big guy",0.0,0.0));

        listSunkenShips = new ArrayList<Ship>();
        listShips.add(new Sailboat(scale, 75, 50, 500, Ship.E, 0, "H.M.S. Pinafore",0.0,0.0));
        listShips.add(new Sailboat(scale, 10, 300, 400, Ship.SE, 0, "Little Dinghy",0.0,0.0));
        for (int i=0; i<2; i++){
            listShips.get(listShips.size()-1).changeToSunken();
            sink(listShips.get(listShips.size()-1));
        }

        numShips = listShips.size();
        
        userCreatedShip = false;

        islands = new Island(scale);
        rocks = new Rocks(scale);
        navBuoys = new NavBuoys(scale);
        
        lobsterPots = new ArrayList<LobsterPot>();
        int [] pots_xs = new int [] {75,125,200,350,125,100,300,360,200,600,575,625,
                              675,975, 450,1075,800,825,775,575,315,800,350};
        int [] pots_ys = new int [] {50,100,150,125,300,500,475,400,700,600,675,675,
                              650,375,1075,1175,400,425,850,850,265,100,875};
        for (int i=0; i<pots_xs.length; i++)
            lobsterPots.add(new LobsterPot(scale, pots_xs[i], pots_ys[i]));

        listExplosions = new ArrayList<Explosion>();

        realVisibleX = 0;
        realVisibleY = 0;
        lastPanXChange = 0;
        lastPanYChange = 0;
        panDragging = false;
        gridlines = new GridLines(scale);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /** Add new ship to map. Useful (instead of just calling constructor) when
     * adding new ship from user input. type "Sailboat" will call the Sailboat()
     * constructor, type "Lobsterboat" will call the Lobsterboat() constructor,
     * etc. Also updates other data structures in map object.
     * @param type String representation of ship type ("Sailboat", "Lobsterboat"
     * or "Tanker")
     * @param shipName Name of ship
     * @param realx Starting x coordinate
     * @param realy Starting y coordinate
     * @param speed Starting speed
     * @param direction Starting direction
     */
    public Ship addUserShip(String type, String shipName, int realx, int realy, 
                            int speed, double direction)
    {
        Ship ship  = new Lobsterboat(scale, realx, realy, direction, speed, shipName,
                                     panXChange, panYChange);
        userOriginalX = realx;
        userOriginalY = realy;
        numShips++;
        listShips.add(ship);
        ship.highlight(true);
        userShip = ship;
        userCreatedShip = true;
        userLifeCount = 5;
        userLobsterCount = 0;
        return ship;
    }

    /** Remove ship from map.
     * @param ship Ship which will be removed.
     */
    public void removeShip(Ship ship)
    {
        listShips.remove(ship);
        numShips = listShips.size();
    }

    /** Updates scale of all components of map.
     * @param newScale new scale for map
     */
    public void updateScale(double newScale)
    {
        scale = newScale;
        for (int i=0; i<numShips; i++)
        {
            Ship ship = listShips.get(i);
            ship.scaleCoords(scale);
        }
        for (int i=0; i<listSunkenShips.size(); i++)
        {
            Ship sunken = listSunkenShips.get(i);
            sunken.scaleCoords(scale);
        }

        islands.scaleCoords(scale);
        rocks.scaleCoords(scale);
        navBuoys.scaleCoords(scale);
        for (int i=0; i<lobsterPots.size(); i++)
        {
            LobsterPot pot = lobsterPots.get(i);
            pot.scaleCoords(scale);
        }
        for (int i=0; i<listExplosions.size(); i++)
        {
            Explosion e = listExplosions.get(i);
            e.scaleCoords(scale);
        }

        gridlines.scaleCoords(scale);

        setRealVisibleDims();
        main.updateMapOverview();
    }

    /** Pans all objects in the map according to the current panXChange and
     * panYChange. Also calculates the real coordinates of the topleft corner,
     * to pass to the small map overview in the main program.
     */
    public void panMap()
    {
        for (int i=0; i<numShips; i++)
        {
            Ship ship = listShips.get(i);
            ship.panCoords(panXChange, panYChange);
        }
        for (int i=0; i<listSunkenShips.size(); i++)
        {
            Ship sunken = listSunkenShips.get(i);
            sunken.panCoords(panXChange, panYChange);
        }

        islands.panCoords(panXChange, panYChange);
        rocks.panCoords(panXChange, panYChange);
        navBuoys.panCoords(panXChange, panYChange);
        for (int i=0; i<lobsterPots.size(); i++)
        {
            LobsterPot pot = lobsterPots.get(i);
            pot.panCoords(panXChange, panYChange);
        }
        for (int i=0; i<listExplosions.size(); i++)
        {
            Explosion e = listExplosions.get(i);
            e.panCoords(panXChange, panYChange);
        }
        gridlines.panCoords(panXChange, panYChange);

        realVisibleX = (int)((0 - panXChange)/scale);
        realVisibleY = (int)((0 - panYChange)/scale);
        setRealVisibleDims();
        main.updateMapOverview();
    }

    /* Set functions
     */
    public void setUserCreatedShip(boolean bool) {userCreatedShip = bool;}
    public void setRealVisibleDims()
    {
        Dimension size = this.getSize();
        int canvasW = size.width;
        int canvasH = size.height;

        realVisibleW = (int)Math.floor((double)canvasW/scale);
        realVisibleH = (int)Math.floor((double)canvasH/scale);
    }

	/** Turns on/off all sounds in the game.
	 */
	public void mute(boolean enable) {mute = enable;}

	/** Sets the display theme to Nighttime or Daytime.
	 */
	public void nightTime(boolean enable)
	{
		for (int i=0; i<numShips; i++)
        {
            Ship ship = listShips.get(i);
            ship.nightTime(enable);
        }
        for (int i=0; i<listSunkenShips.size(); i++)
        {
            Ship sunken = listSunkenShips.get(i);
            sunken.nightTime(enable);
        }
		gridlines.nightTime(enable);

	}

    /* Getter functions
     */
    public ArrayList<Ship> getListShips() {return listShips;}
	public ArrayList<Ship> getListSunkenShips() {return listSunkenShips;}
    public int getNumShips() {return numShips;}
    public boolean getUserCreatedShip() {return userCreatedShip;}
    public Ship getUserShip() {return userShip;}
    public int getUserOriginalX() {return userOriginalX;}
    public int getUserOriginalY() {return userOriginalY;}
    public int getUserLifeCount() {return userLifeCount;}
    public ArrayList<LobsterPot> getPots() {return lobsterPots;}
    public Island getIslands() {return islands;}
    public Rocks getRocks() {return rocks;}
    public NavBuoys getNavBuoys() {return navBuoys;}
    public int getRealVisibleW() {return realVisibleW;}
    public int getRealVisibleH() {return realVisibleH;}
    public int getRealVisibleX() {return realVisibleX;}
    public int getRealVisibleY() {return realVisibleY;}

    /** Drawing routine that is called when JFrame is made visible 
     * or resized.
     * @param g Graphics object on which to draw the canvas
     */
    public void paintComponent (Graphics g)
    {
        setRealVisibleDims();
        main.updateMapOverview();
        Graphics2D g2 = (Graphics2D) g;

        islands.draw(g2);
        gridlines.draw(g2);
        rocks.draw(g2);
        navBuoys.draw(g2);
        
        //Draw lobsterpots
        for (int i=0; i<lobsterPots.size(); i++)
        {
            LobsterPot pot = lobsterPots.get(i);
            pot.draw(g2);
        }
        //Draw ships
        for (int i=0; i<numShips; i++)
        {
            Ship ship = listShips.get(i);
            ship.draw(g2);
        }
        for (int i=0; i<listSunkenShips.size(); i++)
        {
            Ship sunken = listSunkenShips.get(i);
            sunken.draw(g2);
        }
        //Draw explosions
        for (int i=0; i<listExplosions.size(); i++)
        {
            Explosion e = listExplosions.get(i);
            e.draw(g2);
        }
    }

    /** Routine that is called when animation is updated. This routine
     * updates both the ships' animation and the explosions' animation.
     */
    public void animate ()
    {
        for (int i=0; i<numShips; i++)
        {
            Ship ship = listShips.get(i);
            ship.updateLocation(listShips, listSunkenShips,
                                islands, rocks, navBuoys, lobsterPots, this);
        }
        for (int i=0; i<listExplosions.size(); i++)
        {
            Explosion explosion = listExplosions.get(i);
            explosion.updateSize(listShips, listSunkenShips, this);
        }
    }

    /** Sinks a ship by changing its visible attributes and removing it from
     * list of live ships.
     * @param ship ship which will be sunk
     */
    public void sink(Ship ship)
    {
        listShips.remove(ship);
        numShips = listShips.size();
        listSunkenShips.add(ship);
    }

    /** Removes this sunken ship from the map. This is used only when an
     * explosion hits a sunken ship.
     * @param sunken Sunken ship which will be removed
     */
    public void destroySunken(Ship sunken)
    {
        listSunkenShips.remove(sunken);
    }

    /** Explode tanker.
     * @param tanker tanker causing the explosion
     * @param ship ship involved in collision (if none, ship = tanker)
     * @param x canvas x coordinate of collision
     * @param y canvas y coordinate of collision
     */
    public void explodeTanker(Ship tanker, Ship ship, int x, int y)
    {
		if (!mute)
		{
			try{
				Clip explodeNoise = AudioSystem.getClip();
				explodeNoise.open(AudioSystem.getAudioInputStream(EXPLODE));
				explodeNoise.start();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
        listShips.remove(tanker);
        listShips.remove(ship);
        numShips = listShips.size();
        listExplosions.add(new Explosion(scale, x, y, panXChange, panYChange));
    }

    /** When an explosion finishes (after reaching a certain size), it will be
     * removed from the list of current explosions.
     * @param e explosion which will be removed
     */
    public void removeExplosion(Explosion e)
    {
        listExplosions.remove(e);
    }

    /** When the user's ship passes over a lobsterpot, its lobster count will
     * increase, and the user's status will be updated in the righthand column
     * of the main program.
     */
    public void userCatchLobsters()
    {
		if (!mute)
		{
    		try{
            	Clip userLobsterNoise = AudioSystem.getClip();
	            userLobsterNoise.open(AudioSystem.getAudioInputStream(U_LOBSTER));
    	        userLobsterNoise.start();
        	}
	        catch (Exception e) {
    	        System.out.println(e.getMessage());
        	}
		}
        userLobsterCount++;
        main.updateUserStatus(userLobsterCount, userLifeCount);
    }

    /** When the user's ship collides with anything, its life count will
     * decrease, and the user's status will be updated in the righthand column
     * of the main program.
     */
    public void userCollision()
    {
		if (!mute)
		{
			try{
				Clip userCollisionNoise = AudioSystem.getClip();
				userCollisionNoise.open(AudioSystem.getAudioInputStream(U_COLLIDE));
				userCollisionNoise.start();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
        userLifeCount--;
        if (userLifeCount <=0)
        {
            userShip.changeToSunken();
            sink(userShip);
        }
        else
        {
            userShip.changeLocation(userOriginalX, userOriginalY);
        }
        main.updateUserStatus(userLobsterCount, userLifeCount);
    }

    /** MouseListener routine. Called when user clicks on map. Selects or moves
     * ships according to current state of map.
     */
    public void mouseClicked(MouseEvent event)
    {
        if (userCreatedShip && !panDragging)
        {
            double x = (double) event.getX();
            double y = (double) event.getY();
            double newDirection = userShip.nextDirection(x, y);
            userShip.changeDirection(newDirection);
            repaint();
        }
    }

    /** MouseListener routine. Called when user releases from a mouse click/drag.
     */
    public void mouseReleased(MouseEvent event) {panDragging = false;}

    public void mousePressed(MouseEvent event) {}
    public void mouseEntered(MouseEvent event){}
    public void mouseExited(MouseEvent event){}    

    /** MouseMotionListener routine. Called when user is "dragging". Pans the
     * map.
     */    
    public void mouseDragged(MouseEvent event)
    {
        if (!panDragging)
        {
            lastPanXChange = panXChange;
            lastPanYChange = panYChange;
            panAnchorX = (double) event.getX();
            panAnchorY = (double) event.getY();
            panDragging = true;
        }
        double x = (double) event.getX();
        double y = (double) event.getY();
        panXChange = lastPanXChange + (x - panAnchorX);
        panYChange = lastPanYChange + (y - panAnchorY);

        panMap();
        repaint();
    }

    public void mouseMoved(MouseEvent event){}
}
