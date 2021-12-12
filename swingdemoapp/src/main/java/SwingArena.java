import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.net.URL ;

/**
 * A Swing GUI element that displays a grid on which you can draw images, text and lines.
 */
public class SwingArena extends JPanel
{
    // Represents the image to draw. You can modify this to introduce multiple images.
    private static final String IMAGE_FILE1 = "1554047213.png";
    private ImageIcon robot1;
    
    private static final String IMAGE_FILE2 = "rg1024-isometric-tower.png";
    private ImageIcon castle1;
    
    private static final String IMAGE_FILE3 = "cross.png";
    private ImageIcon cross;

    // The following values are arbitrary, and you may need to modify them according to the 
    // requirements of your application.
    private int gridWidth = 9;
    private int gridHeight = 9;
    
    private double[] robotX = {0.0, 8.0, 8.0, 0.0};
    private double[] robotY = {0.0, 0.0, 8.0, 8.0};
    private int i;
    
    private double castleX = 4.0;
    private double castleY = 4.0;
    
    private double crossX = 9.0;
    private double crossY = 9.0;
    
    private Object mutex = new Object();
    
    //array to store robots
    public ArrayList<robot> robotList = new ArrayList<robot>();
       
    int j=1;
    
    Thread thread;
    
    private double gridSquareSize; // Auto-calculated
    
    private LinkedList<ArenaListener> listeners = null;

    /**
     * Creates a new arena object, loading the robot image.
     */
    public SwingArena()
    {
        // Here's how you get an Image object from an image file (which you provide in the 
        // 'resources/' directory.
        URL url1 = getClass().getClassLoader().getResource(IMAGE_FILE1);
        if(url1 == null)
        {
            throw new AssertionError("Cannot find image file " + IMAGE_FILE1);
        }
        robot1 = new ImageIcon(url1);
        
        URL url2 = getClass().getClassLoader().getResource(IMAGE_FILE2);
        if(url2 == null)
        {
            throw new AssertionError("Cannot find image file " + IMAGE_FILE2);
        }
        castle1 = new ImageIcon(url2);
        
        URL url3 = getClass().getClassLoader().getResource(IMAGE_FILE3);
        if(url3 == null)
        {
            throw new AssertionError("Cannot find image file " + IMAGE_FILE3);
        }
        cross = new ImageIcon(url3);
    }  
    
    //score class to calculate the total score of the game
    //But not called
    public int Score(int x, int y, int z){
            
        int eachSecScore;
        int totSec = x;
        int robotKills;
        int robotDelay = y;
        int playerDelay = z;
        int totScore;
        
        eachSecScore = 10 * totSec;
        
        robotKills = 10 + 100 * (robotDelay / playerDelay);
        
        totScore = robotKills + eachSecScore;
        
        return totScore;
        
    }
    
    /**
     * Moves a robot image to a new grid position. This is highly rudimentary, as you will need
     * many different robots in practice. This method currently just serves as a demonstration.
     */
    public void setRobotPosition()
    {
      
        Random random2 = new Random();
        int randomTime;
        
        randomTime = 500 + random2.nextInt(2001);
        
        try{
            thread.sleep(randomTime);
        }
        catch (InterruptedException e){}
        
        Random random1 = new Random();
        
        int randomNo;
        
        
        randomNo = random1.nextInt(4);
        
        double pos = 0;
        
        //Basically there are only 4 ways that a robot can move. So i used a switch case to determine the direction to move. And in every case it increments only X value, Y value or it decrements X value or Y value.
        
        switch(randomNo){
            case 0:
                pos = robotX[i]++;
                break;
                
            case 1:
                pos = robotX[i]--;
                break;
                
            case 2:
                pos = robotY[i]++;
                break;
                
            case 3:
                pos = robotY[i]--;
                break;
        }
        
        //Here I checked that the robot will move in the grid. So if the robot tries to go over x=8, y=8 or it tries to towards x=-1 and y=-1.
        if(pos == 8 && randomNo == 0){
            
            robotX[i]--;
            
            robot robot1 = new robot();
            robotList.add(robot1);
            
            repaint();
        }
        else if(pos == 0 && randomNo == 1){
            
            robotX[i]++;  
            
            robot robot1 = new robot();
            robotList.add(robot1);
            
            repaint();
        }
        else if(pos == 8 && randomNo == 2){
            
            robotY[i]--;
            
            robot robot1 = new robot();
            robotList.add(robot1);
            
            repaint();
        }
        else if(pos == 0 && randomNo == 3){
            
            robotY[i]++;
            
            robot robot1 = new robot();
            robotList.add(robot1);
            
            repaint();
        }
        else{
            
            robot robot1 = new robot();
            robotList.add(robot1);
            
            repaint();
        }
    }
    
    
    
    
    /**
     * Adds a callback for when the user clicks on a grid square within the arena. The callback 
     * (of type ArenaListener) receives the grid (x,y) coordinates as parameters to the 
     * 'squareClicked()' method.
     */
    public void addListener(ArenaListener newListener)
    {
        if(listeners == null)
        {
            listeners = new LinkedList<>();
            addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent event)
                {
                    int gridX = (int)((double)event.getX() / gridSquareSize);
                    int gridY = (int)((double)event.getY() / gridSquareSize);
                    
                    if(robotX[i] == gridX && robotY[i] == gridY){                        
                        
                        crossX = robotX[i];
                        crossY = robotY[i];
                        
                        try{
                            thread.sleep(1000);
                        }
                        catch (InterruptedException e){}
                        
                        crossX = 0.0;
                        crossY = 8.0;
                        
                        robotX[i] = 8.0;
                        robotY[i] = 8.0;
                    }
                    
                    if(gridX < gridWidth && gridY < gridHeight)
                    {
                        for(ArenaListener listener : listeners)
                        {   
                            listener.squareClicked(gridX, gridY);
                        }
                    }
                }
            });
        }
        listeners.add(newListener);
    }
    
    
    
    /**
     * This method is called in order to redraw the screen, either because the user is manipulating 
     * the window, OR because you've called 'repaint()'.
     *
     * You will need to modify the last part of this method; specifically the sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D gfx = (Graphics2D) g;
        gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                             RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
            (double) getWidth() / (double) gridWidth,
            (double) getHeight() / (double) gridHeight);
            
        int arenaPixelWidth = (int) ((double) gridWidth * gridSquareSize);
        int arenaPixelHeight = (int) ((double) gridHeight * gridSquareSize);
            
            
        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setColor(Color.GRAY);
        gfx.drawRect(0, 0, arenaPixelWidth - 1, arenaPixelHeight - 1); // Outer edge

        for(int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            int x = (int) ((double) gridX * gridSquareSize);
            gfx.drawLine(x, 0, x, arenaPixelHeight);
        }
        
        for(int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            int y = (int) ((double) gridY * gridSquareSize);
            gfx.drawLine(0, y, arenaPixelWidth, y);
        }

        
        // Invoke helper methods to draw things at the current location.
        // ** You will need to adapt this to the requirements of your application. **
        
        drawImage(gfx, castle1, castleX, castleY); 
        
        drawImage(gfx, robot1, robotX[i], robotY[i]);
        drawLabel(gfx, "Robot "+j, robotX[i], robotY[i]);
        
        //lock for the threads
        synchronized(mutex){
            setRobotPosition();
        }
        
        drawImage(gfx, cross, crossX, crossY);           
    }
    
    
    /** 
     * Draw an image in a specific grid location. *Only* call this from within paintComponent(). 
     *
     * Note that the grid location can be fractional, so that (for instance), you can draw an image 
     * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
     *     
     * You shouldn't need to modify this method.
     */
    private void drawImage(Graphics2D gfx, ImageIcon icon, double gridX, double gridY)
    {
        // Get the pixel coordinates representing the centre of where the image is to be drawn. 
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;
        
        // We also need to know how "big" to make the image. The image file has a natural width 
        // and height, but that's not necessarily the size we want to draw it on the screen. We 
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = (double) robot1.getIconWidth();
        double fullSizePixelHeight = (double) robot1.getIconHeight();
        
        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as 
            // wide as a full grid cell, and the height will be set to preserve the aspect 
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else
        {
            // Otherwise, it's the other way around -- full height, and width is set to 
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // Actually put the image on the screen.
        gfx.drawImage(icon.getImage(), 
            (int) (x - displayedPixelWidth / 2.0),  // Top-left pixel coordinates.
            (int) (y - displayedPixelHeight / 2.0), 
            (int) displayedPixelWidth,              // Size of displayed image.
            (int) displayedPixelHeight, 
            null);
    }
    
    
    /**
     * Displays a string of text underneath a specific grid location. *Only* call this from within 
     * paintComponent(). 
     *
     * You shouldn't need to modify this method.
     */
    private void drawLabel(Graphics2D gfx, String label, double gridX, double gridY)
    {
        gfx.setColor(Color.BLUE);
        FontMetrics fm = gfx.getFontMetrics();
        gfx.drawString(label, 
            (int) ((gridX + 0.5) * gridSquareSize - (double) fm.stringWidth(label) / 2.0), 
            (int) ((gridY + 1.0) * gridSquareSize) + fm.getHeight());
    }
    
    /** 
     * Draws a (slightly clipped) line between two grid coordinates. 
     *
     * You shouldn't need to modify this method.
     */
    private void drawLine(Graphics2D gfx, double gridX1, double gridY1, 
                                          double gridX2, double gridY2)
    {
        gfx.setColor(Color.RED);
        
        // Recalculate the starting coordinate to be one unit closer to the destination, so that it
        // doesn't overlap with any image appearing in the starting grid cell.
        final double radius = 0.5;
        double angle = Math.atan2(gridY2 - gridY1, gridX2 - gridX1);
        double clippedGridX1 = gridX1 + Math.cos(angle) * radius;
        double clippedGridY1 = gridY1 + Math.sin(angle) * radius;
        
        gfx.drawLine((int) ((clippedGridX1 + 0.5) * gridSquareSize), 
                     (int) ((clippedGridY1 + 0.5) * gridSquareSize), 
                     (int) ((gridX2 + 0.5) * gridSquareSize), 
                     (int) ((gridY2 + 0.5) * gridSquareSize));
    }
}
