package applet.snake;

import java.applet.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

//we need the applet methods and the MouseMotionListener interface
//(used for the human controlled paddle)

public class SnakeMain extends Applet implements KeyListener, 
ActionListener
{
	private static final long serialVersionUID = 1L;
	// a font used to display the score
    Font newFont = new Font("sansserif", Font.BOLD, 20);
    //The image is going to be double buffered to avoid flicker
    Graphics bufferGraphics;
    //the Image will contain everything drawn on bufferGraphics
    Image offscreen;
    //variables used to set the width and height of the applet
    final int  BORDER = 10, GRIDSIZE = 25, CELLSIZE = 10, 
    		INITIALLENGTH = 3, INITIALSNAKEX = GRIDSIZE/2, INITIALSNAKEY = GRIDSIZE/2, INITIALSPEED = 50
    				, WIDTH = GRIDSIZE+500, HEIGHT = GRIDSIZE+300;
    
    private boolean gameOver = false;
    private boolean keyPressedThisTick = false;
    private boolean wrapAround = false;
    
    //snake variables
    private int snakeLength = INITIALLENGTH;
    private int snakeX = INITIALSNAKEX, snakeY = INITIALSNAKEY;
    private int snakeUpDown = 0, snakeLeftRight = -1;
    private int frameCounter = 0;
    private int speed = INITIALSPEED;
    private int score = 0;
    
    //cherry variables
    private int cherryX, cherryY;
    
    //blue cherry variables
    private int blueCherryX, blueCherryY;
    private int blueCherryCountdown = 0;
    private int blueCherryCooldown = 500;
    private boolean blueCherryOn = false;
    
    //declare the array
    int[][] grid = new int[GRIDSIZE][GRIDSIZE];
    
    //I am going to use a timer to do a certain list of tasks
    //every 15 milliseconds (67 FPS)
    Timer time = new Timer(15, this);  
    
    public void init()
    {
        //set the applet to be 500*300
        setSize(WIDTH, HEIGHT);
        
        gameOver = false;
        grid[snakeY][snakeX] = snakeLength;
        newCherry();
        
        //keyListener allows the player to control the snake
        addKeyListener(this);
        setBackground(Color.black);
        setFocusable(true);
        requestFocus();
        
        //create off-screen image to draw on
        offscreen = createImage(WIDTH, HEIGHT);
        bufferGraphics = offscreen.getGraphics();
        
        //this is the game loop
        time.start();
                      
    }
    
    public void reset()
    {
        snakeLength = INITIALLENGTH;
        snakeX = INITIALSNAKEX;
        snakeY = INITIALSNAKEY;
        snakeUpDown = 0;
        snakeLeftRight = -1;
        frameCounter = 0;
        speed = INITIALSPEED;
        score = 0;
        gameOver = false;
        wrapAround = true;
        keyPressedThisTick = false;
        blueCherryOn = false;
        blueCherryCooldown = 500;
        blueCherryCountdown = 0;
        newCherry();
        
        for (int i = 0; i < GRIDSIZE; i++)
        {
            for (int j = 0; j < GRIDSIZE; j++)
            {
                grid[i][j] = 0;
            }
        }
                
        grid[snakeY][snakeX] = snakeLength;
        
        time.restart();
    }
    
    //every 15 milliseconds the timer triggers the actionPerformed method
    public void actionPerformed(ActionEvent arg0)
    {
        if(gameOver == true)
        {
            //after the game needs to end we stop the timer,
            time.stop();
            repaint();
        }
        else
        {
            frameCounter = frameCounter + 10;
        
            if (frameCounter >= speed)
            {
                checkCollision();
            
                snakeX = snakeX + snakeLeftRight;
                snakeY = snakeY + snakeUpDown;
                
                if (snakeX == 0)
                {
                    if(!wrapAround)
                    {
                        gameOver = true;
                    }
                    snakeX = (GRIDSIZE - 2);
                }
                    
                else if (snakeX == (GRIDSIZE - 1))
                {
                    if(!wrapAround)
                    {
                        gameOver = true;
                    }
                    snakeX = 1;
                }
                    
                if (snakeY == 0)
                {
                    if(!wrapAround)
                    {
                        gameOver = true;
                    }
                    snakeY = (GRIDSIZE - 2);
                }
                    
                else if (snakeY == (GRIDSIZE - 1))
                {
                    if(!wrapAround)
                    {
                        gameOver = true;
                    }
                    snakeY = 1;
                }
            
                for (int i = 0; i < GRIDSIZE; i++)
                {
                    for (int j = 0; j < GRIDSIZE; j++)
                    {
                        if (grid[i][j] > 0)
                        {
                            grid[i][j]--;
                        }
                    }
                }
            
                grid[snakeX][snakeY] = snakeLength;
            
                frameCounter = 0;
                if(blueCherryCountdown > 0)
                {
                    blueCherryCountdown--;
                }
                else
                {
                    blueCherryOn = false;
                }
                
                if(blueCherryCooldown > 0)
                {
                    blueCherryCooldown--;
                }
                
                if(!blueCherryOn && blueCherryCooldown == 0)
                {
                    newBlueCherry();
                }
                keyPressedThisTick = false;
                       
            }
            else
            {
            
            }
            //repaints the applet
            repaint();
        }
    }
    
    public void checkCollision()
    {
        //checks if the snakes next position is already occupied
        int snakeNextX = snakeX + snakeLeftRight;
        int snakeNextY = snakeY + snakeUpDown;
        if(grid[snakeNextX][snakeNextY] > 0)
        {
            gameOver = true;
        }
        
        if(snakeNextX == cherryX && snakeNextY == cherryY)
        {
            snakeLength++;
            score++;
            newCherry();
            if(speed > 20)
            {
                //speed--;
            }
            else
            {
                
            }
        }
        
        if(snakeNextX == blueCherryX && snakeNextY == blueCherryY)
        {
            score = score + 10;
            blueCherryOn = false;
            blueCherryCountdown = 0;
        }
    }
    
         
    public void paint(Graphics g)
    {
        // first clear off the image
        bufferGraphics.clearRect(0,0,WIDTH,HEIGHT);
        
        //Display the score in white
        bufferGraphics.setColor(Color.white);
        
        //draws the counter between ticks
        bufferGraphics.drawString("Score: " + score, BORDER + CELLSIZE*GRIDSIZE + 20, BORDER + CELLSIZE + 10);
        bufferGraphics.drawString("Controls:", BORDER + CELLSIZE*GRIDSIZE + 20, BORDER + CELLSIZE + 34);
        bufferGraphics.drawString("ArrowKeys - Change Direction", BORDER + CELLSIZE*GRIDSIZE + 20, BORDER + CELLSIZE + 46);
        bufferGraphics.drawString("F - Faster", BORDER + CELLSIZE*GRIDSIZE + 20, BORDER + CELLSIZE + 58);
        bufferGraphics.drawString("S - Slower", BORDER + CELLSIZE*GRIDSIZE + 20, BORDER + CELLSIZE + 70);
        bufferGraphics.drawString("W - Toggle Wrap-Around ", BORDER + CELLSIZE*GRIDSIZE + 20, BORDER + CELLSIZE + 82);
        bufferGraphics.drawString("L - Longer ", BORDER + CELLSIZE*GRIDSIZE + 20, BORDER + CELLSIZE + 94);
        bufferGraphics.drawString("K - Shorter ", BORDER + CELLSIZE*GRIDSIZE + 20, BORDER + CELLSIZE + 106);
        
        //Now draw the board in white if the edges are not solid, red if they are
        if(wrapAround) {
            bufferGraphics.setColor(Color.white);
        }
        else {
            bufferGraphics.setColor(Color.red);
        }
        
        //draws the edge of the game
        bufferGraphics.drawLine(BORDER - 1 + CELLSIZE, BORDER - 1 + CELLSIZE, GRIDSIZE*CELLSIZE + BORDER + 1 - CELLSIZE, BORDER - 1 + CELLSIZE);
        bufferGraphics.drawLine(BORDER - 1 + CELLSIZE, BORDER - 1 + CELLSIZE, BORDER - 1 + CELLSIZE, GRIDSIZE*CELLSIZE + BORDER + 1 - CELLSIZE);
        bufferGraphics.drawLine(GRIDSIZE*CELLSIZE + BORDER + 1 - CELLSIZE,BORDER - 1 + CELLSIZE, GRIDSIZE*CELLSIZE + BORDER + 1 - CELLSIZE, GRIDSIZE*CELLSIZE + BORDER + 1 - CELLSIZE);
        bufferGraphics.drawLine(BORDER - 1 + CELLSIZE,GRIDSIZE*CELLSIZE + BORDER + 1 - CELLSIZE,GRIDSIZE*CELLSIZE + BORDER + 1 - CELLSIZE,GRIDSIZE*CELLSIZE + BORDER + 1 - CELLSIZE);
        
        //draws the cherry in white
        bufferGraphics.setColor(Color.white);
        bufferGraphics.fillRect(((cherryX*CELLSIZE) + BORDER),((cherryY*CELLSIZE) + BORDER),CELLSIZE,CELLSIZE);
        
        //draws the blue cherry
        if(blueCherryOn)
        {
            bufferGraphics.setColor(Color.blue);
            bufferGraphics.fillRect(((blueCherryX*CELLSIZE) + BORDER),((blueCherryY*CELLSIZE) + BORDER),CELLSIZE,CELLSIZE);
        }
            
        //draws the snake white if alive, red if dead
        if(gameOver) {
            bufferGraphics.setColor(Color.red);
            bufferGraphics.drawString("GAME OVER.   Press R to reset", BORDER + CELLSIZE*GRIDSIZE + 20, BORDER + CELLSIZE + 22);
        } else {
            bufferGraphics.setColor(Color.white);
        }
        
        for (int i = 0; i < GRIDSIZE; i++)
          {
              for (int j = 0; j < GRIDSIZE; j++)
              {
                   if(grid[i][j] > 0)
                   {
                       bufferGraphics.fillRect(((i*CELLSIZE) + BORDER),((j*CELLSIZE) + BORDER),CELLSIZE,CELLSIZE);
                   }
              }
          }
        
        //finally draw the off-screen image to the applet
        g.drawImage(offscreen,0,0,this);
        //this line makes sure all the monitors are up to date before proceeding
        Toolkit.getDefaultToolkit().sync();
    }
    
    public void update(Graphics g)
    {
        paint(g);
    }
        
    public void keyPressed(KeyEvent e)
    {
    	//System.out.println(KeyEvent.KEY_TYPED);
    	
        if(keyPressedThisTick == true)
        {
            
        }
        
        else
        {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && !gameOver)
            {
                if(snakeUpDown == 0)
                {
                }
                else
                {
                    snakeUpDown = 0;
                    snakeLeftRight = 1;
                    keyPressedThisTick = true;
                }
            }
        
            else if (e.getKeyCode() == KeyEvent.VK_LEFT && !gameOver)
            {
                if(snakeUpDown == 0)
                {
                }
                else
                {
                    snakeUpDown = 0;
                    snakeLeftRight = -1;
                    keyPressedThisTick = true;
                }
            }
        
            else if (e.getKeyCode() == KeyEvent.VK_UP && !gameOver)
            {
                 if(snakeLeftRight == 0)
                {
                }
                else
                {
                 snakeLeftRight = 0;
                    snakeUpDown = -1;
                    keyPressedThisTick = true;
                }
            }
        
            else if (e.getKeyCode() == KeyEvent.VK_DOWN && !gameOver)
            {
                if(snakeLeftRight == 0)
                {
                }
                else
                {
                    snakeLeftRight = 0;
                    snakeUpDown = 1;
                    keyPressedThisTick = true;
                }
            }
            
            else if (e.getKeyCode() == KeyEvent.VK_W && !gameOver)
            {
                if(wrapAround)
                {
                    wrapAround = false;
                }
                else
                {
                    wrapAround = true;
                }
            }
            
            else if (e.getKeyCode() == KeyEvent.VK_F && !gameOver)
            {
                speed = speed - 10;
                if(speed < 10)
                {
                    speed = 10;
                }
            }
            
            else if (e.getKeyCode() == KeyEvent.VK_S && !gameOver)
            {
                speed = speed + 10;
            }
            
            else if (e.getKeyCode() == KeyEvent.VK_L && !gameOver)
            {
                snakeLength++;
            }
            
            else if (e.getKeyCode() == KeyEvent.VK_K && !gameOver)
            {
                snakeLength--;
            }
            
            else if (e.getKeyCode() == KeyEvent.VK_R )
            {
                reset();
            }
        }
    }
        
    public void keyTyped(KeyEvent e)
    {
        //this is placeholder
    }
        
    public void keyReleased(KeyEvent e)
    {
        //this is placeholder
    }
    
    public int getGridSize()
    {
        return GRIDSIZE;
    }
    
    public void newCherry()
    {
        cherryX = 1 + (int)(Math.random() * ((GRIDSIZE - 2 - 1) + 1));
        cherryY = 1 + (int)(Math.random() * ((GRIDSIZE - 2 - 1) + 1));
    }
    
    public void newBlueCherry()
    {
        if(1 + (int)(Math.random() * ((100 - 1) + 1)) == 99)
        {
            blueCherryOn = true;
            blueCherryX = 1 + (int)(Math.random() * ((GRIDSIZE - 2 - 1) + 1));
            blueCherryY = 1 + (int)(Math.random() * ((GRIDSIZE - 2 - 1) + 1));
            blueCherryCountdown = 100;
            blueCherryCooldown = 500;
        }
    }
       
}
