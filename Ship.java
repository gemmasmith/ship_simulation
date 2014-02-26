import java.awt.*;
import java.lang.Math;
import java.util.ArrayList;
import java.awt.geom.*;

/** Abstract ship class. Various attributes of ships and related behavior. This
 * class is not intended for use directly, rather it is intended to be used
 * through its subclasses. Most importantly, this class specifies the routine
 * for updating the location of each ship, after a specified time lapse, based
 * on the ship's direction and speed.
 * 
 * @author Gemma Smith <gemma.smith@tufts.edu>
 * @version 4.0
 * @since 2013-11-29
 */

public abstract class Ship
{
	/** North direction (where N is "up" on the graphical map).*/
    public static final double N = 0;
	/** Northeast direction (where N is "up" on the graphical map).*/
    public static final double NE = 45;
	/** East direction (where N is "up" on the graphical map).*/
    public static final double E = 90;
	/** Southeast direction (where N is "up" on the graphical map).*/
    public static final double SE = 135;
	/** South direction (where N is "up" on the graphical map).*/
    public static final double S = 180;
	/** Southwest direction (where N is "up" on the graphical map).*/
    public static final double SW = 225;
	/** West direction (where N is "up" on the graphical map).*/
    public static final double W = 270;
	/** Northwest direction (where N is "up" on the graphical map).*/
    public static final double NW = 315;

    protected int length;
    protected int length_canvas;
    protected double width;
    protected int xcoord_real;
    protected int ycoord_real;
    protected int xcoord_canvas;
    protected int ycoord_canvas;
    protected double currDirection;
    protected double radians;
    protected int currSpeed;
    protected int originalSpeed;
    protected Color color;
    protected String text;
	protected Color textColor;

    protected Shape shape;
    protected Shape lastShape;

    protected double scale;
    protected double panXChange;
    protected double panYChange;

    protected boolean sunken; 

    /** Standard constructor
     * @param len Length of ship
     * @param x x coordinate of ship
     * @param y y coordinate of ship
     * @param angle Direction (degrees 0-359) in which ship is moving
     * @param speed Speed in which ship is moving
     * @param c Color of ship
     * @param label Text corresponding to ship
     */
    public Ship(double s, int len, int x, int y, double angle, int speed, 
                Color c, String label, double panX, double panY){
        scale = s;
        panXChange = panX;
        panYChange = panY;

        length = len;
        xcoord_real = x;
        ycoord_real = y;
        scaleCoords(scale);

        currDirection = angle % 360;
        radians = Math.toRadians(currDirection);
        currSpeed = speed;
        originalSpeed = speed;
        color = c;
        text = label;

        sunken = false;

		textColor = Color.DARK_GRAY;
    }

    /** Use to update ship coordinates to current scale.
     * @param newScale new scale of map
     */
    public void scaleCoords(double s)
    {
        scale = s;
        length_canvas = (int) Math.floor(length*scale);
        xcoord_canvas = (int)(Math.floor(xcoord_real*scale + panXChange));
        ycoord_canvas = (int)(Math.floor(ycoord_real*scale + panYChange));
        width = (double)length_canvas/4;
    }

    /** Use to update the ship canvas coordinates with current pan.
     * @param newPanXChange new overall pan of map
     * @param newPanYChange new overall pan of map
     */
    public void panCoords(double newPanXChange, double newPanYChange)
    {
        panXChange = newPanXChange;
        panYChange = newPanYChange;
        scaleCoords(scale);
    }

    /** Changes the speed of ship object to newSpeed. 
     *  @param newSpeed Desired speed of ship.
     */
    public void changeSpeed(int newSpeed){currSpeed = newSpeed;}

    /** Changes the speed of ship object to originalSpeed.
     */
    public void changeToOriginalSpeed(){currSpeed = originalSpeed;}

    /** Takes a line between (x, y) coordinates and the current (canvas)
     * location to calculate the angle of the next direction.
     * @param x point on map from which to calculate next direction
     * @param y
     */
    public double nextDirection(double x, double y)
    {
        double a; double A; double B; double C;
        final double LOW_EDGE = 22;
        final double HIGH_EDGE = 67;
        
        if (x == xcoord_canvas && y == ycoord_canvas) //don't change direction
            return (currDirection);
        
        else if (x == xcoord_canvas && y < ycoord_canvas) //(x,y) directly above
            return N;
        else if (x == xcoord_canvas && y > ycoord_canvas) //(x,y) directly below
            return S;
        else if (y == ycoord_canvas && x < xcoord_canvas) //(x,y) directly left
            return W;
        else if (y == ycoord_canvas && x > xcoord_canvas) //(x,y) directly right
            return E;

        else if (x < xcoord_canvas && y < ycoord_canvas) //(x,y) is up-left 
        {
            A = xcoord_canvas - x;
            B = ycoord_canvas - y;
            C = Math.sqrt((A*A)+(B*B));
            a = Math.toDegrees(Math.asin(A/C));
            if (a < LOW_EDGE) return N;
            else if (a > HIGH_EDGE) return W;
            else return NW;
        }
        else if (x > xcoord_canvas && y < ycoord_canvas) //(x,y) is up-right
        {
            A = x - xcoord_canvas;
            B = ycoord_canvas - y;
            C = Math.sqrt((A*A)+(B*B));
            a = Math.toDegrees(Math.asin(A/C));
            if (a < LOW_EDGE) return N;
            else if (a > HIGH_EDGE) return E;
            else return NE;
        }
        else if (x < xcoord_canvas && y > ycoord_canvas) //(x,y) is down-left
        {
            A = xcoord_canvas - x;
            B = y - ycoord_canvas;
            C = Math.sqrt((A*A)+(B*B));
            a = Math.toDegrees(Math.asin(A/C));
            if (a < LOW_EDGE) return S;
            else if (a > HIGH_EDGE) return W;
            else return SW;
        }
        else if (x > xcoord_canvas && y > ycoord_canvas) //(x,y) is down-right
        {
            A = x - xcoord_canvas;
            B = y - ycoord_canvas;
            C = Math.sqrt((A*A)+(B*B));
            a = Math.toDegrees(Math.asin(A/C));
            if (a < LOW_EDGE) return S;
            else if (a > HIGH_EDGE) return E;
            else return SE;
        }
        System.out.println("shouldn't get to here");
        return N;
    }

    /** Changes the direction of ship object to newDirection.
     *  @param newDirection Desired direction (angle in degrees) of ship.
     */
    public void changeDirection(double newDirection)
    {
        currDirection = newDirection;
        radians = Math.toRadians(currDirection); 
    }

    /** Changes the current location of ship object to (newCanvasX, newCanvasY).
     *  @param newCanvasX
     *  @param newCanvasY
     */
    public void changeLocation(double newRealX, double newRealY)
    {
        xcoord_real = (int) newRealX;
        ycoord_real = (int) newRealY;
        xcoord_canvas = (int) Math.floor(xcoord_real*scale + panXChange);
        ycoord_canvas = (int) Math.floor(ycoord_real*scale + panYChange);
    }

    /** Changes color of ship object to newColor.
     */
    public void changeColor(Color newColor){color = newColor;}
    
    /** Changes label of ship object to newText.
     */
    public void changeText(String newText){text = newText;}

    /** Applies or removes highlighting to selected ship.
     * @param enable true if applying, false otherwise
     */
    public void highlight(boolean enable)
    {
        if (enable)
            changeColor(Color.YELLOW);
        else
        {
            if (getType()=="Lobsterboat") changeColor(new Color(39, 119, 39));
            else if (getType()=="Sailboat") changeColor(Color.BLUE);
            else if (getType()=="Tanker") changeColor(Color.DARK_GRAY);
        }
    }

    /** Changes visible attributes of ship to represent sunken ship.
     */
    protected void changeToSunken()
    {
        sunken = true;
        changeSpeed(0);
        changeText("XX"+getText());
    }

    /** Abstract method returning shape of shio object. Intended to be overwritten
     * in subclasses.
     * @param x canvas x coordinate
     * @param y canvas y coordinate
     * @return Shape
     */
    abstract protected Shape shipShape(double x, double y);

    /** Returns next shape (as in, shape after one more updateLocation call).
     * @return Shape
     */
    public Shape getNextShape()
    {
        int [] nextCoords = nextLocation();
        double x = (double)nextCoords[2];
        double y = (double)nextCoords[3];
        return shipShape(x, y);
    }
    
    /** Calculates next location of ship, based on its current direction and
     * speed. Returns (x,y) coordinates of next location.
     * @return int[] (x,y) coordinates of next location.
     */
    public int[] nextLocation()
    {
        double x = (double)xcoord_real;
        double y = (double)ycoord_real;

        double hyp = (double)currSpeed;        
        double side = hyp * Math.sin(Math.toRadians(45));
        if (0 < side && side < 1)
            side = 1.0;

        if (currDirection == N) //up
            y = y - hyp;
        else if (currDirection == S) //down
            y = y + hyp;
        else if (currDirection == E) //right
            x = x + hyp;
        else if (currDirection == W) //left
            x = x - hyp;
        else if (currDirection == NE) //up-right
        {
            x = x + side;
            y = y - side;
        }
        else if (currDirection == SE) //down-right
        {
            x = x + side;
            y = y + side;
        }
        else if (currDirection == SW) //down-left
        {
            x = x - side;
            y = y + side;
        }
        else if (currDirection == NW) //up-left
        {
            x = x - side;
            y = y - side;
        }

        int [] nextCoords = new int[4];

        nextCoords[0] = (int)x;
        nextCoords[1] = (int)y;
        nextCoords[2] = (int) Math.floor(x*scale + panXChange);
        nextCoords[3] = (int) Math.floor(y*scale + panYChange);

        return nextCoords;
    }

    /** Changes the location of ship object, based on its speed and direction.
     * Checks for collisions at next location and crashes ships if necessary.
     * @return true if location updated successfully, false otherwise (i.e.
     * collision)
     */
    public boolean updateLocation(ArrayList<Ship> listAllShips,
            ArrayList<Ship> listSunkenShips, Island island, Rocks rocks,
            NavBuoys buoys, ArrayList<LobsterPot> pots, Map map)
    {
        Ship userShip = map.getUserShip();
        boolean collided = detectCollision(listAllShips, listSunkenShips, 
                           island, rocks, buoys, map);
        if(!collided)
        {
            if ((this == userShip) && detectLobsterPot(pots))
                map.userCatchLobsters();
            int [] nextCoords = nextLocation();
            xcoord_real = nextCoords[0];
            ycoord_real = nextCoords[1];
            xcoord_canvas = nextCoords[2];
            ycoord_canvas = nextCoords[3];
            return true;
        }
        else if (collided && (this == userShip))
            map.userCollision();
        
        return false;
    }

    /** Detects collisions with other ships or objects.
     * @return true if there IS a collision, false otherwise
     */
    protected boolean detectCollision(ArrayList<Ship> listAllShips,
        ArrayList<Ship> listSunkenShips, Island island, Rocks rocks, 
        NavBuoys buoys, Map map)
    {
        Ship userShip = map.getUserShip();
        //Check if collided with another ship
        Ship i_ship;
        for (int i=0; i<listAllShips.size(); i++)
        {
            i_ship = listAllShips.get(i);
            if (this != i_ship)
            {
                Area intersection = new Area(shape);
                intersection.intersect(new Area(i_ship.shape));
                if (!intersection.isEmpty())
                {
                    Rectangle r = intersection.getBounds();
                    int x = (int)r.getX();
                    int y = (int)r.getY();
                    if (this.getType()=="Tanker")
                    {
                        if (i_ship != userShip)
                            map.explodeTanker(this, i_ship, x, y);
                        else
                            map.explodeTanker(this,this, x, y);
                    }
                    if (i_ship.getType()=="Tanker")
                    {
                        if (this != userShip)
                            map.explodeTanker(i_ship, this, x, y);
                        else
                            map.explodeTanker(this,this, x, y);
                    }
                    else
                    {
                        if (i_ship != userShip)
                        {
                            i_ship.changeToSunken();
                            map.sink(i_ship);
                        }
                        if (this != userShip)
                        {
                            this.changeToSunken();
                            map.sink(this);
                        }
                        map.repaint();
                        return true;
                    }
                }
            }
        }
        //Check if collided with sunken ship
        for (int i=0; i<listSunkenShips.size(); i++)
        {
            i_ship = listSunkenShips.get(i);
            Area intersection = new Area(shape);
            intersection.intersect(new Area(i_ship.shape));
            if (!intersection.isEmpty()){
                Rectangle r = intersection.getBounds();
                int x = (int)r.getX();
                int y = (int)r.getY();
                if (this.getType()=="Tanker")
                    map.explodeTanker(this, this, x, y);
                else
                {
                    if (this != userShip)
                    {
                        this.changeToSunken();
                        map.sink(this);
                    }
                    map.repaint();
                    return true;
                }
            }
        }
        //Check if collided with islands
        GeneralPath [] shapes = island.getShapes();
        for (int i=0; i<shapes.length; i++)
        {
            Area intersection = new Area(shape);
            intersection.intersect(new Area(shapes[i]));
            if (!intersection.isEmpty())
            {
                if (i<=2)
                {
                    Rectangle r = intersection.getBounds();
                    int x = (int)r.getX();
                    int y = (int)r.getY();
                    if (this.getType()=="Tanker")
                        map.explodeTanker(this, this, x, y);
                    else
                    {
                        if (this != userShip)
                        {
                            this.changeToSunken();
                            map.sink(this);
                        }
                        map.repaint();
                        return true;
                    }
                }
        //Check if collided with shallow water (only big ships)
                if (i>2)
                {
                    if (getType()=="Tanker" || (getType()=="Sailboat"&& length>20))
                    {
                        Rectangle r = intersection.getBounds();
                        int x = (int)r.getX();
                        int y = (int)r.getY();
                        if (this.getType()=="Tanker")
                            map.explodeTanker(this, this, x, y);
                        else
                        {
                            this.changeToSunken();
                            map.sink(this);
                            map.repaint();
                            return true;
                        }
                    }
                }
            }
        }
        //Check if collided with rocks
        int [][] rock_xsys = rocks.getCoords();
        int [] rock_xs = rock_xsys[0];
        int [] rock_ys = rock_xsys[1];
        for (int i=0; i<rock_xs.length; i++)
        {
            if (shape.contains(rock_xs[i], rock_ys[i]))
            {
                if (this.getType()=="Tanker")
                    map.explodeTanker(this, this, rock_xs[i], rock_ys[i]);
                else
                {
                    if (this != userShip)
                    {
                        this.changeToSunken();
                        map.sink(this);
                    }
                    map.repaint();
                    return true;
                }
            }     
        }
        //Check if collided with navigational buoys
        int [][] buoys_xsys = buoys.getCoords();
        int [] buoys_xs = buoys_xsys[0];
        int [] buoys_ys = buoys_xsys[1];
        for (int i=0; i<buoys_xs.length; i++)
        {
            if (shape.contains(buoys_xs[i], buoys_ys[i]))
            {
                if (this.getType()=="Tanker")
                    map.explodeTanker(this, this, buoys_xs[i], buoys_ys[i]);
                else
                {
                    if (this != userShip)
                    {
                        this.changeToSunken();
                        map.sink(this);
                    }                    
                    map.repaint();
                    return true;
                }
            }                 
        }
        return false;
    }

	/** Check if user has caught a lobsterpot.
	 */
    public boolean detectLobsterPot(ArrayList<LobsterPot> pots)
    {
        //Check if collided with lobsterpots
        for (int i=0; i<pots.size(); i++)
        {
            LobsterPot pot = pots.get(i);
            int canvasX = pot.getCanvasX();
            int canvasY = pot.getCanvasY();
            if (pot.getRecentlyCaught() != true && shape.contains(canvasX, canvasY))
            {
                pot.setRecentlyCaught(true);
                return true;
            }
        }
        return false;
    }
	
	/** When display theme is changed to nightTime, all the text needs to change
	 * color.
	 */
	public void nightTime(boolean nightTime)
	{
		if (nightTime)
			textColor = Color.LIGHT_GRAY;
		else
			textColor = Color.DARK_GRAY;
	}
    /** Abstract method to draw ship object on a Graphics2D object. Intended to
     * be used only through subclasses.
     */
    abstract public void draw(Graphics2D g2);

    /** Abstract method to return ship type in String form. Intended to be used
     * only through subclasses.
     */
    abstract public String getType();
    /** Returns ship of ship.
     */
    public Shape getShape(){return shape;}
    /** Returns most recent shape of ship.
     */
    public Shape getLastShape(){return lastShape;}
    /** Returns length of ship object.
     */
	public int getLen(){return length;}
    /** Returns length of ship in canvas coordinate system.
     */
    public int getCanvasLen(){return length_canvas;}
    /** Returns width of ship in canvas coordinate system.
     */
    public int getWidth(){return (int)width;}
    /** Returns real x coordinate of ship object.
     */
    public int getRealX(){return xcoord_real;}
    /** Returns real y coordinate of ship object.
     */
    public int getRealY(){return ycoord_real;}
    /** Returns canvas x coordinate of ship object.
     */
    public int getCanvasX(){return xcoord_canvas;}
    /** Returns canvas y coordinate of ship object.
     */
    public int getCanvasY(){return ycoord_canvas;}
     /** Returns direction (in degrees, where 0/360 is N) of ship object. 
     */  
    public double getDir(){return currDirection;}
    /** Returns speed of ship object.
     */
    public int getSpeed(){return currSpeed;}

	public int getOriginalSpeed(){return originalSpeed;}
    /** Returns color of ship object. 
     */
    public Color getColor(){return color;}

	public boolean getSunken() {return sunken;}
    /** Returns label text of object.
     */
    public String getText(){return text;}

	public double getScale() {return scale;}
	public double getPanXChange() {return panXChange;}
	public double getPanYChange() {return panYChange;}

    /** String representation of a ship. To be printed on a println() call.
     */
    public String toString()
    {
        String s =("Ship[name: "+text+", real coords: ("+xcoord_real+", "+ycoord_real+"), "+
                        "canvas coords: ("+xcoord_canvas+", "+ycoord_canvas+")]");
        return s;
    }
    

}

