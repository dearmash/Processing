import guicomponents.*;

import java.text.MessageFormat;
import java.util.Locale;


void setup() {
  size(520, 380);
  G4P.messagesEnabled(false);
  G4P.setColorScheme(this, GCScheme.GREEN_SCHEME);
  G4P.setMouseOverEnabled(true);
  G4P.cursor(ARROW, CROSS);
  kcx = 370;
  kcy = 210;
  createGUI();
}

void draw() {
  background(240);
  if (sizeX != nextSizeX || txfSizeX.getText().equals(""))
    txfSizeX.localColor.txfBack = textBackColChanged;
  else
    txfSizeX.localColor.txfBack = textBackCol;

  if (sizeY != nextSizeY || txfSizeY.getText().equals(""))
    txfSizeY.localColor.txfBack = textBackColChanged;
  else
    txfSizeY.localColor.txfBack = textBackCol;

  if (startAng != newStartAng || txfStartAng.getText().equals(""))
    txfStartAng.localColor.txfBack = textBackColChanged;
  else
    txfStartAng.localColor.txfBack = textBackCol;

  if (endAng != newEndAng || txfEndAng.getText().equals(""))
    txfEndAng.localColor.txfBack = textBackColChanged;
  else
    txfEndAng.localColor.txfBack = textBackCol;

  lblValueI.setText("" + round(valCurr));
  lblValueF.setText("" + valCurr);

  if (knob.isValueChanging()) {
    lblValueI.localColor.lblBack = labelBackColChanged;
    lblValueF.localColor.lblBack = labelBackColChanged;
  }
  else {
    lblValueI.localColor.lblBack = labelBackCol;
    lblValueF.localColor.lblBack = labelBackCol;
  }
  //		G4P.draw();
}

void initKnob(GKnob k) {
  k.setLimits(valCurr, valLow, valHigh);
  k.setSensitivity(mSensitivity);
  k.setBezelWidth(bezelWidth);
  k.setInertia(inertia);
  k.setNbrTickMarks(nbrTicks);
  k.setControlMode(mode);
  k.setValueTrackVisible(valueArcVisible);
  k.setMouseORA(overRoteArc);
  k.setRotArcOnly(rotArcOnly);
}

int getValidArcAngle(int angle) {
  while (angle < 0) angle+=360;
  while (angle > 360) angle-=360;
  return angle;
}

String build(String pattern, Object ... arguments) {
  return (pattern == null) ? "" : new MessageFormat(pattern, Locale.UK).format(arguments);
}

