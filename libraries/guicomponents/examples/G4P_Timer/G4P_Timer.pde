/**
 * Balls of Vesuvius.  
 * by Peter Lager. 
 *
 * A simple program to demonstrate the use of the GTimer
 * class.
 *
 */

import guicomponents.*;

GHorzSlider sdrRate;
GButton btnStart, btnStop;
GTimer timer;

ArrayList liveBalls, deadBalls;

int rate;

PImage rear, front;

void setup(){
  size(768,600);
  // Create 2 buttons to start and stop the balls
  btnStart = new GButton(this, "Start", 10,10,100,20); // (this, text, x, y, width, height)
  btnStop = new GButton(this, "Stop", 120,10,100,20);
  // Create a slider to control rate of balls erupted.
  sdrRate = new GHorzSlider(this, 230, 10, 360, 20); // (this, x, y, width, height)
  sdrRate.setLimits(50, 10, 120); // (init, min, max)
  sdrRate.setBorder(1);
  // Get timer interval based on initial slider value and limits
  rate = (sdrRate.getMinValue() + sdrRate.getMaxValue()) - sdrRate.getValue();
  // Create a GTimer object that will call the method
  // fireBall 
  // Parameter 1 : the PApplet class i.e. 'this'
  //           2 : the object that has the method to call
  //           3 : the name of the method (parameterless) to call
  //           4 : the interval in millisecs bewteen method calls
  timer = new GTimer(this, this, "fireBall", rate);

  // Balls in the air alive
  liveBalls = new ArrayList(2000);
  // Balls that are below the level of the window
  deadBalls = new ArrayList(100);

  front = loadImage("vfront.png");
  rear = loadImage("vrear.jpg");

  // try and keep it at 30fps
  frameRate(30);

  // Register the pre() method for this class
  registerPre(this);
}

// This method is now called before each call to draw()
void pre(){
  Ball b;
  int i;
  // Update all live balls
  for(i = 0; i < liveBalls.size(); i++){
    b = (Ball)liveBalls.get(i);
    b.update();
    // See if this ball should die if so remember it
    if(b.y > height + 20)
      deadBalls.add(b);
  }
  // Remove dead balls from the list of live balls
  for(i = 0; i < deadBalls.size(); i++){
    liveBalls.remove(deadBalls.get(i));
  }
  // Done with dead balls
  deadBalls.clear();
}

void draw(){
  int i;
  Ball b;

  background(rear);
  for(i = 0; i < liveBalls.size(); i++){
    b = (Ball)liveBalls.get(i);
    b.display();
  }
  image(front,0,0);
}

// This is called when the user drags on the slider
void handleSliderEvents(GSlider slider){
  rate = (sdrRate.getMinValue() + sdrRate.getMaxValue()) - sdrRate.getValue();
  timer.setInterval(rate);
}

// This method is called when a button is clicked
void handleButtonEvents(GButton button){
  if(button == btnStart && button.eventType == GButton.CLICKED)
    timer.start();
  if(button == btnStop && button.eventType == GButton.CLICKED)
    timer.stop();
}

// This method is called by the timer
void fireBall(){
  Ball ball = new Ball();
  liveBalls.add(ball);
}

// Simple class to represent a ball
class Ball {
  public float radius;
  public int col;
  public float x, y;
  public float vx, vy;
  public float gravity = 0.07f;
  public float drag = 0.99;
  public float shrink = 0.999;

  public Ball(){
    x = random(500,540);
    y = 290;
    col = color(random(200,255),random(20,55),0);
    radius = random(3,10);
    vx = random(-3.0, 1.9);
    vy = random(5.5, 8.2);
  }

  public void update(){
    x += vx;
    y -= vy;
    vy -= gravity;
    if(vy < 0)
      vx *= drag;
    radius *= shrink;
  }

  public void display(){
    noStroke();
    fill(col);
    ellipse(x,y,radius,radius);
  }

}