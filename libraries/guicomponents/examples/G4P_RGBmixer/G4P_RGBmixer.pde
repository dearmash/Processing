import guicomponents.*;

/*
Simple program that creates 3 knobs that are used to control the
RGB balance for a rectangle fill.
*/
GKnob knbRed, knbGreen, knbBlue;
int r, g, b;
int kx, ky;

void setup() {
  size(400, 220);
  r = g = b = 160;
  kx = 20;
  ky = 20;

  G4P.setMouseOverEnabled(true);
  G4P.cursor(HAND, CROSS);

  knbRed = new GKnob(this, kx, ky, 150, 150, 270);
  knbRed.setControlMode(GKnob.CTRL_ANGULAR);
  knbRed.setRotArcOnly(true);
  knbRed.setLimits(r, 0, 255);
  knbRed.setNbrTickMarks(9);
  knbRed.localColor = GCScheme.getColor(this, GCScheme.RED_SCHEME);

  knbGreen = new GKnob(this, kx + 8, ky, 150, 270, 20);
  knbGreen.setControlMode(GKnob.CTRL_ANGULAR);
  knbGreen.setRotArcOnly(true);
  knbGreen.setLimits(g, 0, 255);
  knbGreen.setNbrTickMarks(9);
  knbGreen.localColor = GCScheme.getColor(this, GCScheme.GREEN_SCHEME);

  knbBlue = new GKnob(this, kx + 4, ky + 9, 150, 20, 150);
  knbBlue.setControlMode(GKnob.CTRL_ANGULAR);
  knbBlue.setRotArcOnly(true);
  knbBlue.setLimits(b, 0, 255);
  knbBlue.setNbrTickMarks(9);

  stroke(0);
  strokeWeight(3);
  rectMode(CORNERS);
}

void draw() {
  background(220, 220, 255);
  fill(r, g, b);
  rect(kx + 190, 40, width - 40, height - 40);
  fill(0);
  text("Drag mouse in circular movement around the knobs centre", 10, height-10);
}

void handleKnobEvents(GKnob knob) {
  if (knob == knbRed) 
    r = knob.getValue();
  else if (knob == knbGreen)
    g = knob.getValue();
  else if (knob == knbBlue)
    b = knob.getValue();
}

