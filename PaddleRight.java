package applet.pingpong;

/*
 * PaddleRight has essentially the same set up as the human-controlled
 * paddle except that it uses the y position of the ball to determine 
 * the paddle location instead of using a mouseMovement listener
 */
public class PaddleRight{
	
	private int yPos = 0, score;
	final int XPOS = 460;
	
	//the constructor takes in an integer( which is, in our case,
	//the y position of the ball)
	public PaddleRight(int ballPos){
		//sets the position and sets the score to 0
		setPos(ballPos);
		setScore(0);
	}
	
	public void setPos(int pos){
		//same set up as in Paddle left
		this.yPos = pos;
		if(yPos > 230){
			setPos(230);
		}
		else if(yPos < 0){
			setPos(0);
		}
	}
	
	public int getPos(){
		return yPos;
	}
	//setters and getters for int score
	public void setScore(int score){
		this.score = score;
	}
	
	public int getScore(){
		return this.score;
	}

}

