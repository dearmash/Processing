import guicomponents.*;

// G4P components for main window
GPanel pnlControls;
GLabel lblSomeString, lblAlpha, lblAction, lblCursor;
GTextField txfSomeText;
GCombo cboColor, cboFont;
GHorzSlider sdrAlpha;
GKnob knob;
GKnobOval knobOval;
GActivityBar acyBar;
GTimer tmrTimer;
GButton btnTimer;
GCheckbox cbxBusy, cbxMouseOver;
GOptionGroup opgMouseOver;
GOption optHand, optXhair, optMove, optText,optWait;

// G4P components for second window
GButton btnControl;	// Used to create controller window
GWindow windControl;
GHorzSlider sdrHorzPos;
GVertSlider sdrVertPos;

PImage imgBgImage;

int count = 0;

int pX = 10;
int pY = 30;
int pHeight = 280;
int pWidth = 600;

void setup(){
  size(700,340);
  // Load the background image
  imgBgImage = loadImage("bground.jpg");
  // Set the colour scheme
  G4P.setColorScheme(this, GCScheme.GREEN_SCHEME);
  G4P.messagesEnabled(false);

  // Create 2D GUI
  createPanelAndStatusBar();
  createCombos();
  createKnobs();
  createTransparencySlider();
  createTextPlayArea();
  createMouseOverSection();
  createActivityBarSection();
  createTimer();

  // Enable mouse over image changes
  G4P.setMouseOverEnabled(true);

  // Create a second control window
  btnControl = new GButton(this, "Open Panel Position Window", 10, pHeight - 100,200,30);
  pnlControls.add(btnControl);
}

public void createTextPlayArea(){
  lblSomeString = new GLabel(this, "LABEL: Use combo boxes to change color scheme and font", 10, 10, 400, 20 );
  lblSomeString.setBorder(1);
  lblSomeString.setOpaque(true);
  txfSomeText = new GTextField(this, "TEXTFIELD: Use combo boxes to change color scheme and font", 10, 50, 400, 20);

  pnlControls.add(lblSomeString);
  pnlControls.add(txfSomeText);

}

public void createTimer(){
  btnTimer = new GButton(this, "Start", "time.png", 1, pWidth-100, pHeight-60, 100, 40);
  tmrTimer = new GTimer(this, this, "myTimerFunction", 500);	
  pnlControls.add(btnTimer);
}

public void createPanelAndStatusBar(){
  pnlControls = new GPanel(this,"Panel Tab Text (drag to move : click to open/close)",pX,pY,pWidth,pHeight);
  pnlControls.setOpaque(true);
  pnlControls.setCollapsed(false);
  lblAction = new GLabel(this, "USER ACTION FEEDBACK DISPLAYED HERE!", 0, pHeight-20, pWidth, 20);
  lblAction.setBorder(1);
  lblAction.setOpaque(true);
  lblAction.setColorScheme(GCScheme.RED_SCHEME);
  pnlControls.add(lblAction);		
}

public void createActivityBarSection(){
  acyBar = new GActivityBar(this,pWidth-340,pHeight-56,120,10);
  acyBar.start(0);

  cbxBusy = new GCheckbox(this,"Busy?",pWidth - 180,acyBar.getY() - 2,100);
  cbxBusy.setSelected(true);
  cbxBusy.setBorder(0);
  pnlControls.add(acyBar);
  pnlControls.add(cbxBusy);	
}

public void createTransparencySlider(){
  lblAlpha = new GLabel(this,"Adjust Panel transparency ->",0,pHeight-40,150);
  lblAlpha.setFont("Arial", 14);
  sdrAlpha = new GHorzSlider(this,pWidth-400,pHeight-40,299,19);
  sdrAlpha.setBorder(2);
  sdrAlpha.setLimits(255, 128, 255);
  pnlControls.add(lblAlpha);
  pnlControls.add(sdrAlpha);		
}

public void createMouseOverSection(){
  cbxMouseOver = new GCheckbox(this,"Mouse Over Enabled",460,90,80);
  cbxMouseOver.setSelected(true);
  opgMouseOver = new GOptionGroup();
  optHand = new GOption(this, "Hand", 460, 124,90);
  opgMouseOver.addOption(optHand);
  optXhair = new GOption(this, "Cross Hair", 460, 138,90);
  opgMouseOver.addOption(optXhair);
  optMove = new GOption(this, "Move", 460, 152,90);
  opgMouseOver.addOption(optMove);
  optText = new GOption(this, "Text", 460, 166,90);
  opgMouseOver.addOption(optText);
  optWait = new GOption(this, "Wait", 460, 180,90);
  opgMouseOver.addOption(optWait);		
  opgMouseOver.setSelected(optHand);
  lblCursor = new GLabel(this, "Cursor over shape",460,106,120);
  lblCursor.setOpaque(true);

  pnlControls.add(lblCursor);
  pnlControls.add(cbxMouseOver);
  pnlControls.add(optHand);
  pnlControls.add(optXhair);
  pnlControls.add(optMove);
  pnlControls.add(optText);
  pnlControls.add(optWait);
}

public void createCombos() {
  String[] colors = new String[] {
    "Blue", "Green", "Red", "Purple", "Yellow", "Cyan", "Grey"        };
  cboColor = new GCombo(this, colors, 4, 10, 90, 60);
  cboColor.setSelected(1);
  String[] fonts = new String[] { 
    "SansSerif 11", "Serif 11", "Georgia 15", "Times New Roman 16", "Arial Bold 10",
    "Arial 10", "Courier New 9"         };
  cboFont = new GCombo(this, fonts, 4, 120, 90, 160);
  pnlControls.add(cboColor);
  pnlControls.add(cboFont);
}

public void createKnobs(){
  knob = new GKnob(this, 320,90,60,110, 70);
  knob.setNbrTickMarks(11);
  knob.setValue(67);
  knobOval = new GKnobOval(this, 260, 160, 180, 80, 185, 355);
  knobOval.setNbrTickMarks(6);
  knobOval.setRotArcOnly(true);
  knobOval.setValue(25);
  pnlControls.add(knob);
  pnlControls.add(knobOval);
}

public void handleComboEvents(GCombo combo){
  if(cboColor == combo){
    pnlControls.setColorScheme(cboColor.selectedIndex());
    lblSomeString.setColorScheme(cboColor.selectedIndex());
    txfSomeText.setColorScheme(cboColor.selectedIndex());
    acyBar.setColorScheme(cboColor.selectedIndex());
    btnTimer.setColorScheme(cboColor.selectedIndex());
    lblCursor.setColorScheme(cboColor.selectedIndex());
    knob.setColorScheme(cboColor.selectedIndex());
    knobOval.setColorScheme(cboColor.selectedIndex());
    btnControl.setColorScheme(cboColor.selectedIndex());
    cboFont.setColorScheme(cboColor.selectedIndex());
    cboColor.setColorScheme(cboColor.selectedIndex());
    sdrAlpha.setColorScheme(cboColor.selectedIndex());
    sdrAlpha.setValue(255);
    lblAction.setText("Color changed to " + cboColor.selectedText());
  }
  if(cboFont == combo){
    // Get font name and size from
    String[] fs = cboFont.selectedText().split(" ");
    String font = fs[0];
    if(fs.length > 2){
      for(int i = 1; i < fs.length - 1; i++)
        font = font + " " + fs[i];
    }
    int fsize = Integer.parseInt(fs[fs.length - 1]);
    // Set fonts
    lblSomeString.setFont(font, fsize);
    txfSomeText.setFont(font, fsize);
    lblAction.setText("Font changed to " + cboFont.selectedText() + "px");			
  }
}	

public void handleSliderEvents(GSlider slider){
  if(sdrAlpha == slider){
    pnlControls.setAlpha(sdrAlpha.getValue());
    lblAction.setText("Panel transparency is " + pnlControls.getAlpha());			
  }
  if(sdrHorzPos == slider){
    pnlControls.setX(sdrHorzPos.getValue());
  }
  if(sdrVertPos == slider){
    pnlControls.setY(sdrVertPos.getValue());
  }
}

public void handleButtonEvents(GButton button){
  if(btnTimer == button && button.eventType == GButton.CLICKED){
    if(tmrTimer.isRunning()){
      lblAction.setText("Timer stopped");
      btnTimer.setText("Start");
      tmrTimer.stop();
    }
    else {
      lblAction.setText("Timer started");
      btnTimer.setText("Stop");
      tmrTimer.start();
    }
  }
  if(btnControl == button && button.eventType == GButton.CLICKED){
    createControlWindow();
    btnControl.setVisible(false);
  }
}

public void handleCheckboxEvents(GCheckbox cbox){
  if(cbox == cbxBusy){
    if(cbxBusy.isSelected())
      acyBar.start(0);
    else
      acyBar.stop();
  }
  if(cbox == cbxMouseOver){
    G4P.setMouseOverEnabled(cbxMouseOver.isSelected());
  }
}

public void handleOptionEvents(GOption selected, GOption deselected){
  if(selected == optHand)
    G4P.cursor(HAND);
  else if(selected == optXhair)
    G4P.cursor(CROSS);
  else if(selected == optMove)
    G4P.cursor(MOVE);
  else if(selected == optText)
    G4P.cursor(TEXT);
  else if(selected == optWait)
    G4P.cursor(WAIT);
}

public void myTimerFunction(){
  count++;
  lblAction.setText("My timer has counted to " + count);
}

public void handlePanelEvents(GPanel panel){
  if(pnlControls == panel){
    switch (pnlControls.getEventType()){
    case GPanel.DRAGGED:
      if(sdrHorzPos != null && sdrVertPos != null){
        sdrHorzPos.setValue(pnlControls.getX());
        sdrVertPos.setValue(pnlControls.getY());
      }
    }
  }
}
public void createControlWindow(){
  windControl = new GWindow(this, "Controls",600,400,width/4 + 16,height/4 + 16, false, null);
  sdrHorzPos = new GHorzSlider(this,0,height/4,width/4,16);
  sdrHorzPos.setLimits(pX, 0 ,width - pWidth);
  sdrVertPos = new GVertSlider(this,width/4,0,16,height/4);
  sdrVertPos.setLimits(pY, pnlControls.getTabHeight(), height - pHeight);

  windControl.add(sdrHorzPos);
  windControl.add(sdrVertPos);
  windControl.addData(new GWinData());

  windControl.addDrawHandler(this, "drawController");
}

void draw(){
  pushMatrix();
  background(imgBgImage);
  popMatrix();
}

public void drawController(GWinApplet appc, GWinData data){
  appc.stroke(255,255,0);
  appc.strokeWeight(1);
  appc.fill(130,130,0);
  appc.rect(pnlControls.getX()/4, (pnlControls.getY() - pnlControls.getTabHeight())/4, 
  pnlControls.getWidth()/4, (pnlControls.getHeight()+ pnlControls.getTabHeight())/4);
}