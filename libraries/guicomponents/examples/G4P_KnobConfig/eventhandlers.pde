void handleNextButton(GButton button) {
  if (button.eventType == GButton.CLICKED) {
    sizeX = nextSizeX;
    sizeY = nextSizeY;
    startAng = newStartAng;
    endAng = newEndAng;
    knob.dispose();
    if (sizeX == sizeY)
      knob = new GKnob(this, kcx - sizeX / 2, kcy - sizeY/  2, sizeX, startAng, endAng);
    else
      knob = new GKnobOval(this, kcx - sizeX / 2, kcy - sizeY/  2, sizeX, sizeY, startAng, endAng);
    btnNextKnob.setVisible(false);
    initKnob(knob);
    bezelWidth = knob.getBezelWidth();
    txfBezelWidth.setText("" + bezelWidth);
  }
}

void handleMakeCode(GButton button) {
  if (button.eventType == GButton.CLICKED) {
    String[] line = loadStrings("knobbase.txt");
    String kname = "knob" + knobNo++;
    String[] modeName = {
      "CTRL_ANGULAR", "CTRL_HORIZONTAL", "CTRL_VERTICAL"
    };

    StringBuilder s = new StringBuilder(line[0] + '\n');
    if (sizeX == sizeY) {
      s.append(build(line[1], "GKnob", kname) + '\n');
      s.append("\n\n" + line[2] + '\n');
      s.append(line[3] + '\n');
      s.append(build(line[4], kname, knob.getX(), knob.getY(), sizeX, startAng, endAng) + '\n');
    }
    else {
      s.append(build(line[1], "GKnobOval", kname) + '\n');
      s.append("\n\n" + line[2] + '\n');
      s.append(line[5] + '\n');
      s.append(build(line[6], kname, knob.getX(), knob.getY(), sizeX, sizeY, startAng, endAng) + '\n');
    }
    if (valLow != 0  || valHigh != 100)
      s.append(build(line[7], kname, valCurr, valLow, valHigh) + '\n');
    if (mSensitivity != 1)
      s.append(build(line[8], kname, mSensitivity) + '\n');
    if (inertia != 1)
      s.append(build(line[9], kname, inertia) + '\n');
    if (nbrTicks != 2)
      s.append(build(line[10], kname, nbrTicks) + '\n');
    s.append(build(line[11], kname, modeName[mode - GKnob.CTRL_ANGULAR]) + '\n');
    s.append(build(line[12], kname, bezelWidth) + '\n');
    if (!valueArcVisible)	
      s.append(build(line[13], kname, valueArcVisible) + '\n');
    if (overRoteArc)	
      s.append(build(line[14], kname, overRoteArc) + '\n');
    if (rotArcOnly)	
      s.append(build(line[15], kname, rotArcOnly) + '\n');
    GClip.copy(new String(s));
    println(s);
  }
}

void handleChangeBevelSize(GTextField textfield) { 
  if (textfield.eventType == GTextField.ENTERED) {
    int bw;
    try {
      bw = Integer.parseInt(txfBezelWidth.getText());
      bw = constrain(bw, 0, min(sizeX, sizeY));
    } 
    catch (NumberFormatException e) {
      bw = bezelWidth;
    }	
    bezelWidth = bw;
    knob.setBezelWidth(bezelWidth);
    txfBezelWidth.setText("" + bezelWidth);
  }
}

void handleChangeSensitivity(GTextField textfield) { 
  if (textfield.eventType == GTextField.ENTERED) {
    float sen;
    try {
      sen = Float.parseFloat(txfMSensitivity.getText());
      sen = constrain(sen, 0.01f, 20);
    } 
    catch (NumberFormatException e) {
      sen = mSensitivity;
    }	
    mSensitivity = sen;
    knob.setSensitivity(mSensitivity);
    txfMSensitivity.setText("" + mSensitivity);
  }
}

void handleChangeLimits(GTextField textfield) { 
  if (textfield.eventType == GTextField.ENTERED) {
    float pv = (valLow == valHigh) ? 0 : (valCurr - valLow)/(valHigh - valLow);
    float v;
    if (textfield == txfValLow) {
      try {
        v = Float.parseFloat(txfValLow.getText());
        v = PApplet.constrain(v, -999.9f, 999.9f);
      } 
      catch (NumberFormatException e) {
        v = valLow;
      }	
      valLow = v;
      txfValLow.setText("" + valLow);
    }
    if (textfield == txfValHigh) {
      try {
        v = Float.parseFloat(txfValHigh.getText());
        v = PApplet.constrain(v, -999.9f, 999.9f);
      } 
      catch (NumberFormatException e) {
        v = valHigh;
      }	
      valHigh = v;
      txfValHigh.setText("" + valHigh);
    }
    valCurr = valLow + (valHigh - valLow) * pv;
    knob.setLimits(valCurr, valLow, valHigh);
  }
}

void handleChangeNbrTicks(GTextField textfield) { 
  if (textfield.eventType == GTextField.ENTERED) {
    int nt;
    try {
      nt = Integer.parseInt(txfTicks.getText());
      nt = constrain(nt, 2, 60);
    } 
    catch (NumberFormatException e) {
      nt = nbrTicks;
    }	
    nbrTicks = nt;
    knob.setNbrTickMarks(nbrTicks);
    txfTicks.setText("" + nbrTicks);
  }
}

void handleChangeInertia(GTextField textfield) { 
  if (textfield.eventType == GTextField.ENTERED) {
    int nt;
    try {
      nt = Integer.parseInt(txfInertia.getText());
      nt = constrain(nt, 1, 50);
    } 
    catch (NumberFormatException e) {
      nt = inertia;
    }	
    inertia = nt;
    knob.setInertia(inertia);
    txfInertia.setText("" + inertia);
  }
}

void handleOptionEvents(GOption selOpt, GOption delselOpt) {
  if (selOpt == this.optAngular)
    knob.setControlMode(GKnob.CTRL_ANGULAR);
  else if (selOpt == this.optVert)
    knob.setControlMode(GKnob.CTRL_VERTICAL);
  else
    knob.setControlMode(GKnob.CTRL_HORIZONTAL);
  mode = knob.getControlMode();
}

void handleCheckboxEvents(GCheckbox checkbox) {
  if (checkbox == cbxValueArc) {
    valueArcVisible = checkbox.isSelected();
    knob.setValueTrackVisible(valueArcVisible);
  }
  else if (checkbox == cbxStrict) {
    if (rotArcOnly) {
      overRoteArc = true;
      knob.setMouseORA(true);
      cbxStrict.setSelected(true);
    }
    else {
      overRoteArc = cbxStrict.isSelected();
      knob.setMouseORA(overRoteArc);
    }
  }
  else if (checkbox == this.cbxRotArcOnly) {
    rotArcOnly = cbxRotArcOnly.isSelected();
    knob.setRotArcOnly(rotArcOnly);
    if (rotArcOnly) {
      overRoteArc = true;
      knob.setMouseORA(true);
      cbxStrict.setSelected(true);
    }
  }
}

void handleChangeSize(GTextField textfield) { 
  if (textfield.eventType == GTextField.CHANGED) {
    btnNextKnob.setVisible(true);
    try {
      nextSizeX = Integer.parseInt(txfSizeX.getText());
    } 
    catch (NumberFormatException e) {
      nextSizeX = sizeX;
    }	
    try {
      nextSizeY = Integer.parseInt(txfSizeY.getText());
    } 
    catch (NumberFormatException e) {
      nextSizeY = sizeY;
    }
  }
  else if (textfield.eventType == GTextField.ENTERED) {
    try {
      nextSizeX = Integer.parseInt(txfSizeX.getText());
      nextSizeX = constrain(nextSizeX, 20, 260);
    } 
    catch (NumberFormatException e) {
      nextSizeX = sizeX;
    }	
    try {
      nextSizeY = Integer.parseInt(txfSizeY.getText());
      nextSizeY = constrain(nextSizeY, 20, 260);
    } 
    catch (NumberFormatException e) {
      nextSizeY = sizeY;
    }
  }
}

void handleChangeRotationArc(GTextField textfield) { 
  if (textfield.eventType == GTextField.CHANGED) {
    btnNextKnob.setVisible(true);
    try {
      newStartAng = Integer.parseInt(txfStartAng.getText());
      newStartAng = getValidArcAngle(newStartAng);
    } 
    catch (NumberFormatException e) {
      newStartAng = startAng;
    }	
    try {
      newEndAng = Integer.parseInt(txfEndAng.getText());
      newEndAng = getValidArcAngle(newEndAng);
    } 
    catch (NumberFormatException e) {
      newEndAng = endAng;
    }
  }
}

void handleKnobEvents(GKnob k) {
  if (k == knob)
    valCurr = k.getValuef();
}

