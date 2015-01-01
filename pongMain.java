package applet.pingpong;

import java.applet.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

//we need the applet methods and the MouseMotionListener interface
//(used for the human controlled paddle)

public class pongMain extends Applet implements MouseMotionListener, 
ActionListener
{
    //declare an instance of the ball and two paddles
    Ball ball;
    PaddleLeft pLeft;
    PaddleRight pRight;
    // a font used to display the score
    Font newFont = new Font("sansserif", Font.BOLD, 20);
    //The image is going to be double buffered to avoid flicker
    Graphics bufferGraphics;
    //the Image will contain everything drawn on bufferGraphics
    Image offscreen;
    //variables used to set the width and height of the applet
    final int WIDTH = 500, HEIGHT = 300;
    //variable used to record the time the game has proceeded to
    //to tell how long the player has lasted
    long currentTime;
    
    //I am going to use a timer to do a certain list of tasks
    //every 15 milliseconds (67 FPS)
    Timer time = new Timer(15, this);
    
    public void init()
    {
        //set the applet to be 500*300
        setSize(WIDTH, HEIGHT);
        //we now instantiate our ball and two paddles
        ball = new Ball();
        pLeft = new PaddleLeft();
        //pRight is set to current ball position -35 because it is 70 pixels long
        pRight = new PaddleRight(ball.getY() - 35);
        
        //mouseMotionListener allows the player to control their paddle
        addMouseMotionListener(this);
        setBackground(Color.black);
        //create offscreen image to draw on
        offscreen = createImage(WIDTH, HEIGHT);
        bufferGraphics = offscreen.getGraphics();
        
        time.start();
        
    }
    
    //every 15 milliseconds the timer triggers the actionPerformed method
    public void actionPerformed(ActionEvent arg0)
    {
        if(pRight.getScore() == 10 || pLeft.getScore() == 10)
        {
            //after the game needs to end we stop the timer,
            //and calculate how long the player lasted
            //(current time - initial time)
            time.stop();
            currentTime = System.currentTimeMillis() - currentTime;
            repaint();
        }
        else
        {
            //move the ball
            ball.move();
            //lines the computer paddle up
            pRight.setPos(ball.getY() - 35);
            //checks the ball for a collision
            checkCollision();
            //repaints the applet
            repaint();
        }
    }
    
    public void checkCollision()
    {
        //remember the ball is 10*10, x and y is the top left corner
        //if the top left corner y pos is 0 or 290 we reverse its direction
        //by multiplying ball.dy by -1
        if(ball.getY() <= 0 || ball.getY() >= 290)
        {
            ball.dy = (ball.dy * -1);
        }
        
        //if the ball is at the right hand edge of the human paddle
        //and the boolean method hitPaddle() is true, then we reverse dx
        if((ball.getX() == 40) && hitLeftPaddle())
        {
            ball.dx = (ball.dx * -1);
        }
        
        //the computer paddle can't miss so if the ball is at its left edge
        //it must rebound
        if(ball.getX() == 460 && hitRightPaddle())
        {
            ball.dx = (ball.dx * -1);
        }
        
        //If the ball reaches the edge of the applet on the human side,
        //missing the paddle reset the ball and icrement the score
        if(ball.getX() == 0)
        {
            pRight.setScore(pRight.getScore() + 1);
            ball.reset();
        }
        
        if(ball.getX() == WIDTH)
        {
            pLeft.setScore(pLeft.getScore() + 1);
            ball.reset();
        }
    }
    
    public boolean hitLeftPaddle()
    {
        boolean didHit = false;
        //this just checks if the ball has hit the paddle
        if((pLeft.getPos() - 10) <= ball.getY() && (pLeft.getPos() + 70) > ball.getY())
        {
            if(pLeft.getPos() + 35 < ball.getY())
            {
                ball.dy = 5;
            }
            else
            {
                ball.dy = -5;
            }
            didHit = true;
        }
        return didHit;
    }
    
    public boolean hitRightPaddle()
    {
        boolean didHit = false;
        //this just checks if the ball has hit the paddle
        if((pRight.getPos() - 10) <= ball.getY() && (pRight.getPos() + 70) > ball.getY())
        {
            if(pRight.getPos() + 35 < ball.getY())
            {
                ball.dy = 5;
            }
            else
            {
                ball.dy = -5;
            }
            didHit = true;
        }
        return didHit;
    }
        
    public void paint(Graphics g)
    { 
          //instead of using the typical graphics, we are going to
          //use bufferGraphics (which we declared at the beginning
          //of the class) to draw onto our off-screen image
          
          // first clear off the image
          bufferGraphics.clearRect(0,0,WIDTH,HEIGHT);
          
          //Now draw the paddles in white
          bufferGraphics.setColor(Color.white);
          //xPos never changes, yPos does. make the paddles 10*70
          //left
          bufferGraphics.fillRect(pLeft.XPOS,pLeft.getPos(),10,70);
          //Right
          bufferGraphics.fillRect(pRight.XPOS, pRight.getPos(),10,70);
          
          //this draws our mid court lines and scores in grey
          bufferGraphics.setColor(Color.lightGray);
          bufferGraphics.setFont(newFont);
          //show players hopeless circumstance
          bufferGraphics.drawString(" " + pLeft.getScore(), 150, 15);
          //get the score from paddleright
          bufferGraphics.drawString(" "+ pRight.getScore(),300,15);
          //mid court divider
          //bufferGraphics.fillRect(240,0,20,300);
          
          //Remember, we painted one last time after the computer won
          if(pRight.getScore() == 10)
          {
              bufferGraphics.drawString("You LOSE",40,150);
          }
          
          if(pLeft.getScore() == 10)
          {
              bufferGraphics.drawString("You WIN",40,150);
          }
              
           //draw the ball
           bufferGraphics.setColor(Color.white);
           bufferGraphics.fillRect(ball.getX(),ball.getY(),10,10);
           //finally draw the offscreen image to the applet
           g.drawImage(offscreen,0,0,this);
           
           //this line makes sure all the monitors are up to date before proceeding
           Toolkit.getDefaultToolkit().sync();
        }
        
    public void update(Graphics g)
    {
        paint(g);
    }
        
    public void mouseMoved(MouseEvent evt)
    {
            pLeft.setPos(evt.getY() -35);
    }
        
    public void mouseDragged(MouseEvent evt)
    {
            //this is placeholder
    }
        
       
    }