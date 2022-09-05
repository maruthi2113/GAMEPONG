
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Panel extends JPanel implements Runnable{

	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int Bat1_WIDTH = 25;
	static final int Bat1_HEIGHT = 100;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Bat1 Bat11;
	Bat1 Bat12;
	Ball ball;
	Points points;
	
	Panel(){
		newBat1s();
		newBall();
		points = new Points(GAME_WIDTH,GAME_HEIGHT);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void newBall() {
		random = new Random();
		ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
	}
	public void newBat1s() {
		Bat11 = new Bat1(0,(GAME_HEIGHT/2)-(Bat1_HEIGHT/2),Bat1_WIDTH,Bat1_HEIGHT,1);
		Bat12 = new Bat1(GAME_WIDTH-Bat1_WIDTH,(GAME_HEIGHT/2)-(Bat1_HEIGHT/2),Bat1_WIDTH,Bat1_HEIGHT,2);
	}
	public void paint(Graphics g) {
		image = createImage(getWidth(),getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);
	}
	public void draw(Graphics g) {
		Bat11.draw(g);
		Bat12.draw(g);
		ball.draw(g);
		points.draw(g);
Toolkit.getDefaultToolkit().sync(); // I forgot to add this line of code in the video, it helps with the animation

	}
	public void move() {
		Bat11.move();
		Bat12.move();
		ball.move();
	}
	public void checkCollision() {
		
		//bounce ball off top & bottom window edges
		if(ball.y <=0) {
			ball.setYDirection(-ball.yVelocity);
		}
		if(ball.y >= GAME_HEIGHT-BALL_DIAMETER) {
			ball.setYDirection(-ball.yVelocity);
		}
		//bounce ball off Bat1s
		if(ball.intersects(Bat11)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //optional for more difficulty
			if(ball.yVelocity>0)
				ball.yVelocity++; //optional for more difficulty
			else
				ball.yVelocity--;
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		if(ball.intersects(Bat12)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //optional for more difficulty
			if(ball.yVelocity>0)
				ball.yVelocity++; //optional for more difficulty
			else
				ball.yVelocity--;
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		//stops Bat1s at window edges
		if(Bat11.y<=0)
			Bat11.y=0;
		if(Bat11.y >= (GAME_HEIGHT-Bat1_HEIGHT))
			Bat11.y = GAME_HEIGHT-Bat1_HEIGHT;
		if(Bat12.y<=0)
			Bat12.y=0;
		if(Bat12.y >= (GAME_HEIGHT-Bat1_HEIGHT))
			Bat12.y = GAME_HEIGHT-Bat1_HEIGHT;
		//give a player 1 point and creates new Bat1s & ball
		if(ball.x <=0) {
			points.player2++;
			newBat1s();
			newBall();
			System.out.println("HERO WON GAME: "+points.player2);
		}
		if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
			points.player1++;
			newBat1s();
			newBall();
			System.out.println("DON WON GAME WITH: "+points.player1);
		}
	}
	public void run() {
		//game loop
		long lastTime = System.nanoTime();
		double amountOfTicks =60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now -lastTime)/ns;
			lastTime = now;
			if(delta >=1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}
	public class AL extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			Bat11.keyPressed(e);
			Bat12.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			Bat11.keyReleased(e);
			Bat12.keyReleased(e);
		}
	}
}