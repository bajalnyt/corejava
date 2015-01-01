package applet.pingpong;

/*
 * This is the class for the human-controlled paddle
 */

public class PaddleLeft{
	//declaration for variables needed
	private int yPos = 0, score;
	final int XPOS = 30;
	
	public PaddleLeft(){
		//sets up the paddle to be at a y position of 120
		setPos(120);
	}
	
	public void setPos(int pos){
		this.yPos = pos;
		//if the y position upper left hand corner is
		//70 pixels of the bottom of the applet window
		if(yPos > 230){
			//set it back to 70 pixels away
			setPos(230);
		}
		//if the y position upper left hand corner is 
		//less than zero(outside the applet window)
		else if(yPos < 0){
			//set the y position to 0
			setPos(0);
		}
	}
	
	public int getPos(){
		return yPos;
	}
	public void setScore(int score){
	        this.score = score;
    }
    public int getScore(){
        return this.score;
    }

}


