import guicomponents.*;

private GWindow[] window;
private int sx,sy,ex,ey;
private boolean done;

private GButton btnStart;
private GLabel lblInstr;

void setup(){
  size(600,50);
  btnStart = new GButton(this, "CreateWindows", 10,10,80,30);
  lblInstr = new GLabel(this,"Use the mouse to draw a rectangle in any of the 3 windows",100,10,490);
  lblInstr.setVisible(false);
}

public void createWindows(){
  int col;
  window = new GWindow[3];
  for(int i = 0; i < 3; i++){
    col = (128 << (i * 8)) | 0xff000000;
    window[i] = new GWindow(this, "Window "+i, 130+i*210, 100+i*100,200,200,false, JAVA2D);
    window[i].setBackground(col);
    window[i].addData(new MyWinData());
    window[i].addDrawHandler(this, "windowDraw");
    window[i].addMouseHandler(this, "windowMouse");
  }
}

/**
 * Click the button to create the windows.
 * @param button
 */
public void handleButtonEvents(GButton button){
  if(window == null && button.eventType == GButton.CLICKED){
    createWindows();
    lblInstr.setVisible(true);
  }
}

/**
 * Draw for the main window
 */
void draw(){
  background(192);
}

/**
 * Handles mouse events for ALL GWindow objects
 *  
 * @param appc the PApplet object embeded into the frame
 * @param data the data for the GWindow being used
 * @param event the mouse event
 */
public void windowMouse(GWinApplet appc, GWinData data, MouseEvent event){
  MyWinData data2 = (MyWinData)data;
  switch(event.getID()){
  case MouseEvent.MOUSE_PRESSED:
    data2.sx = data2.ex = appc.mouseX;
    data2.sy = data2.ey = appc.mouseY;
    data2.done = false;
    break;
  case MouseEvent.MOUSE_RELEASED:
    data2.ex = appc.mouseX;
    data2.ey = appc.mouseY;
    data2.done = true;
    break;
  case MouseEvent.MOUSE_DRAGGED:
    data2.ex = appc.mouseX;
    data2.ey = appc.mouseY;
    break;			
  }
}

/**
 * Handles drawing to the windows PApplet area
 * 
 * @param appc the PApplet object embeded into the frame
 * @param data the data for the GWindow being used
 */
public void windowDraw(GWinApplet appc, GWinData data){
  MyWinData data2 = (MyWinData)data;
  if(!(data2.sx == data2.ex && data2.ey == data2.ey)){
    appc.stroke(255);
    appc.strokeWeight(2);
    appc.noFill();
    if(data2.done){
      appc.fill(128);
    }
    appc.rectMode(CORNERS);
    appc.rect(data2.sx, data2.sy, data2.ex, data2.ey);
  }
}	

/**
 * Simple class that extends GWinData and holds the data that is specific
 * to a particular window.
 * 
 * @author Peter Lager
 *
 */
class MyWinData extends GWinData {
  public int sx,sy,ex,ey;
  public boolean done;
}
