package applet.pingpong;

/*
 * This is the Ball class
 */

public class Ball{
	//variables for the x and y position
	private int xPos,yPos;
	//the direction of the x and y position. Dy is set to -5 because,
	//remeber that the applet "grid" has it's (0, 0) orgin at the 
	//top lefthand corner of the applet window. For the mathimatically
	//minded among us, the applet is essentially quadrant IV
	public int dx = 5, dy = -5;
	
	public Ball(){
		//sets the initial ball position to near the center of the 
		//screen
		setPos(250, 140);
	}
	
	public void setPos(int x, int y){
		this.xPos = x;
		this.yPos = y;
	}
	
	public int getX(){
		return xPos;
	}
	
	public int getY(){
		return yPos;
	}
	//this is the method used to move the ball
	public void move(){
		//it takes the current x and y position, then adds their current
		//direction of movement to them, giving us a shift in the ball's
		//position in the applet
		setPos(this.getX() + dx, this.getY() + dy);
	}	
	//reset method for when the computer scores
	public void reset(){
		//the same initial setup as before
		setPos(250,140);
		dx = 5;
		dy = -5;
	}
}



