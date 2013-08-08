// Knob components
GKnob knob = null;
int kcx, kcy;
// LIVE SECTION
// GUI components
GTextField txfStartAng, txfEndAng, txfTicks, txfBezelWidth;
GTextField txfValLow, txfValHigh, txfMSensitivity;
GTextField txfInertia, txfSizeX, txfSizeY;
GLabel lblValueI, lblValueF;
GOptionGroup opg = new GOptionGroup();
GOption optAngular, optHorz, optVert;
GCheckbox cbxValueArc, cbxStrict, cbxRotArcOnly;
GLabel lblCurrValueI, lblCurrValF;
GButton btnNextKnob, btnMakeCode;

// Matching variables
int startAng, endAng, newStartAng, newEndAng;
int nbrTicks, bezelWidth, inertia;
float valLow, valHigh, valCurr, mSensitivity;
int mode;
int knobNo = 0;

boolean valueArcVisible, overRoteArc, rotArcOnly;

int sizeX, sizeY, nextSizeX, nextSizeY;

int textBackCol, textBackColChanged;
int labelBackCol, labelBackColChanged;

void createGUI() {
  int x = 4, y = 10;
  // Knob size
  sizeX = sizeY = nextSizeX = nextSizeY = 140;
  new GLabel(this, "Knob size  X                    Y                    ( >= 20)", x, y, 300);
  txfSizeX = new GTextField(this, "" + sizeX, x + 80, y, 40, 18);
  txfSizeX.addEventHandler(this, "handleChangeSize");
  txfSizeY = new GTextField(this, "" + sizeY, x + 144, y, 40, 18);
  txfSizeY.addEventHandler(this, "handleChangeSize");
  y = 32;
  // Rotation arc
  startAng = newStartAng = 120; 
  endAng = newEndAng = 60;
  new GLabel(this, "Rotation angle                  to                    (0 - 360)", x, y, 300);
  txfStartAng = new GTextField(this, "" + startAng, x + 80, y, 40, 18);
  txfStartAng.addEventHandler(this, "handleChangeRotationArc");
  txfEndAng = new GTextField(this, "" + endAng, x + 144, y, 40, 18);
  txfEndAng.addEventHandler(this, "handleChangeRotationArc");
  // Make next knob button
  btnNextKnob = new GButton(this, "New Knob", x + 280, y - 22, 80, 44);
  btnNextKnob.addEventHandler(this, "handleNextButton");
  btnNextKnob.setVisible(false);
  // Only create make code button if we can get a clipboard
  if (GClip.copy("test")) {
    btnMakeCode = new GButton(this, "Make Code", x + 380, y - 22, 80, 44);
    btnMakeCode.addEventHandler(this, "handleMakeCode");
  }
  // Make the start knob
  knob = new GKnob(this, kcx - sizeX / 2, kcy - sizeY/  2, sizeX, startAng, endAng);
  y = 60;
  // Bezel width
  bezelWidth = knob.getBezelWidth();
  new GLabel(this, "Bezel width                      ( > 0)", x, y, 200);
  txfBezelWidth = new GTextField(this, ""+bezelWidth, x + 80, y, 40, 18);
  txfBezelWidth.addEventHandler(this, "handleChangeBevelSize");
  y = 82;
  // Number of ticks
  nbrTicks = 2;
  new GLabel(this, "No. of ticks                       ( >= 2)", x, y, 200);
  txfTicks = new GTextField(this, "2", x + 80, y, 40, 18);
  txfTicks.addEventHandler(this, "handleChangeNbrTicks");
  y = 104;
  // Whether the value arc is visiblie in the bezel
  valueArcVisible = true;
  cbxValueArc = new GCheckbox(this, "Value arc visible", x, y, 100);
  cbxValueArc.setSelected(true);
  y = 126;
  // If the mouse has to be pressed within the rotation arc 
  overRoteArc = false;
  cbxStrict = new GCheckbox(this, "Mouse over rotation arc only", x, y, 160);
  cbxStrict.setSelected(false);
  y = 148;
  rotArcOnly = false;
  cbxRotArcOnly = new GCheckbox(this, "Show rotation arc only", x, y, 100);
  cbxRotArcOnly.setSelected(false);
  y = 170;
  new GLabel(this, "Rotation control mode", x, y, 200);
  optAngular = new GOption(this, "Angular", x+10, y + 22, 120);
  optHorz = new GOption(this, "Horizontal", x+10, y + 44, 120);
  optVert = new GOption(this, "Vertical", x+10, y + 66, 120);
  opg.addOption(optAngular);
  opg.addOption(optHorz);
  opg.addOption(optVert);
  optHorz.setSelected(true);
  mode = GKnob.CTRL_HORIZONTAL; // default value
  y = 258;
  mSensitivity = 1;
  new GLabel(this, "Mouse sensitivity                      ( > 0.01)", x, y, 300);
  txfMSensitivity = new GTextField(this, ""+mSensitivity, x + 100, y, 40, 18);
  txfMSensitivity.addEventHandler(this, "handleChangeSensitivity");
  y = 280;	
  inertia = 1;
  new GLabel(this, "Inertia                                        ( >= 1)", x, y, 300);
  txfInertia = new GTextField(this, ""+inertia, x + 100, y, 40, 18);
  txfInertia.addEventHandler(this, "handleChangeInertia");
  y = 302;
  valLow = 0;
  valHigh = 100;
  valCurr = 27.5f;
  new GLabel(this, "Value range", x, y, 300);
  txfValLow = new GTextField(this, "" + valLow, x + 80, y, 40, 18);
  txfValLow.addEventHandler(this, "handleChangeLimits");
  txfValHigh = new GTextField(this, "" + valHigh, x + 144, y, 40, 18);
  txfValHigh.addEventHandler(this, "handleChangeLimits");
  knob.setLimits(valCurr, valLow, valHigh);
  y = 324;
  new GLabel(this, "Current value", x, y, 300);
  lblValueI = new GLabel(this, "" + round(valCurr), x + 80, y, 106, 18);
  lblValueI.setOpaque(true);
  y = 340;
  lblValueF = new GLabel(this, "" + valCurr, x + 80, y, 106, 18);
  lblValueF.setOpaque(true);

  textBackCol = txfSizeX.localColor.txfBack;
  labelBackCol = lblValueI.localColor.lblBack;
  textBackColChanged = color(255, 240, 240);
  labelBackColChanged = color(255, 240, 240);
}

