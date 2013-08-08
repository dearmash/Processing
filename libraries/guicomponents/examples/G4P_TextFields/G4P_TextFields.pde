/**
 * TextField demo with GUI control
 * by Peter Lager
 * 
 * Demonstrates the use of the TextField component
 * created by Peter Lager and Brendan Nichols
 * 
 * Supports:
 * > Single and multi-line textfields
 * > Copying and pasting of text between textfields
 *   and with applications e.g. Notepad).
 * > Text can be wider than the textfield width.
 * > In multi-line text can be longer that textfield
 *   height.
 * > Text can be scrolled under program control both
 *   horizontally for single and multiline, and 
 *   vertically for multi-line textfields.
 *
 */

import guicomponents.*;

GTextField txfML1, txfML2, txfSL1, txfSL2, txfEvents;
GLabel lblML1, lblML2, lblSL1, lblSL2, lblEvents;
GCheckbox cbxML1, cbxML2;

GButton btnUp, btnDown, btnLeft, btnRight;

ArrayList events = new ArrayList();

void setup(){
  size(500,330);

  // Sets the colour scheme for the GUI components 
  // Schemes available are 
  // BLUE_SCHEME, GREEN_SCHEME, RED_SCHEME, GREY_SCHEME
  // YELLOW_SCHEME, CYAN_SCHEME, PURPLE_SCHEME
  // Defaults to BLUE_SCHEME 
  GComponent.globalColor = GCScheme.getColor(this,  GCScheme.GREEN_SCHEME);

  /* GFont.getFont() - parameters
   * 1) this (always)
   * 2) font name (see below)
   * 3) font size
   *
   * The font name will depend on the OS used and fonts installed. It should be the same
   * as those listed in the 'Create Font' tool in processing. Alternatively use
   * println(PFont.list());
   * in a Processing sketch
   */
  GComponent.globalFont = GFont.getFont(this, "Arial", 13);

  // Get some text for the multiline components.
  String[] lines = loadStrings("quarks.txt");
  String ml1 = join(lines, '\n');
  lines = loadStrings("nickel.txt");
  String ml2 = join(lines, '\n');

  // Create a text input area the last parameter is true for multiline components
  // (this, initial text, x, y, width, height, true)
  txfML1 = new GTextField(this, ml1, 10, 26, 200, 100, true);
  txfML1.tag = "ML 1 -> ";
  cbxML1 = new GCheckbox(this, "Separation lines", 10, 130, 100);
  lblML1 = new GLabel(this, "ML 1", 10,10,40,16);

  txfML2 = new GTextField(this, ml2, 10, 176, 200, 100, true);
  txfML2.tag = "ML 2 -> ";
  cbxML2 = new GCheckbox(this, "Separation lines", 10, 280, 100);
  lblML2 = new GLabel(this, "ML 2", 10,160,40,16);

  // Create a single line text input area
  // (this, initial text, x, y, width, height)
  txfSL1 = new GTextField(this, "Peter Lager", 240, 26, 220, 16);
  txfSL1.tag = "SL 1 -> ";
  lblSL1 = new GLabel(this, "SL 1", 240, 10, 180, 16);

  txfSL2 = new GTextField(this, "Brendan Nichols", 240, 66, 220, 16);
  txfSL2.tag = "SL 2 -> ";
  lblSL2 = new GLabel(this, "SL 2", 240, 50, 180, 16);

  lblEvents = new GLabel(this, "EVENT LOG", 240, 96, 220);
  lblEvents.setTextAlign(GAlign.CENTER);

  txfEvents = new GTextField(this, "", 240, 116, 220, 204, true);
  txfEvents.setFont("monospaced", 12);

  btnUp = new GButton(this, "Up", 15, 300, 40, 20);
  btnUp.tag = "Button SCROLL UP";
  btnDown = new GButton(this, "Down", 65, 300, 40, 20);
  btnDown.tag = "Button SCROLL DOWN";
  btnLeft = new GButton(this, "Left", 115, 300,40, 20);
  btnLeft.tag = "Button SCROLL LEFT";
  btnRight = new GButton(this, "Right", 165, 300, 40, 20);
  btnRight.tag = "Button SCROLL RIGHT";
}

// Handle TextField events
// Three types of event are reported
// CHANGED     The text has been changed
// SET         The text has been set programmatically using setText()
//             this will not generate a CHANGED event as well
// ENTERED     The enter key has been pressed
void handleTextFieldEvents(GTextField tfield){
  String event = "";
  if(tfield != txfEvents){
    event = tfield.tag;
    switch (tfield.getEventType()){
    case GTextField.CHANGED:
      event += "CHANGED";
      break;
    case GTextField.SET:
      event += "SET";
      break;
    case GTextField.ENTERED:
      event += "ENTERED";
      break;
    }
    addEvent(event);
  }
}

// Separator lines Y/N?
void handleCheckboxEvents(GCheckbox checkbox) { 
  if(checkbox == cbxML1)
    txfML1.setShowLines(checkbox.isSelected());
  if(checkbox == cbxML2)
    txfML2.setShowLines(checkbox.isSelected());	
}

// Scroll text in ML 2
void handleButtonEvents(GButton button) {
  if(button == btnUp)
    txfML2.scroll(GTextField.SCROLL_UP);
  if(button == btnDown)
    txfML2.scroll(GTextField.SCROLL_DOWN);
  if(button == btnRight)
    txfML2.scroll(GTextField.SCROLL_RIGHT);
  if(button == btnLeft)
    txfML2.scroll(GTextField.SCROLL_LEFT);
  addEvent(button.tag);
}

// Add an event to the events textfield
void addEvent(String e){
  events.add("@" + nf(millis(),7) + "  " +e);
  // Limit number of events remembered
  while(events.size() > 30)
    events.remove(0);
  // Join the event messages into one string
  txfEvents.setText(join((String[])events.toArray(new String[events.size()]), '\n'));
  // Scroll down until last event is visible.
  while(txfEvents.scroll(GTextField.SCROLL_DOWN));
}

 void draw(){
  background(200);
}
