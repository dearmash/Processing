/**
 * Scale Shape with vertical slider.  
 * by Peter Lager. 
 *
 * based on Scale Shape example
 * Illustration by George Brower. 
 * 
 * Use the mouse to move the vertical slider on the left
 * to change the size of the SVG shape. Drag the thumb 
 * quickly and see what happens.
 *
 * This shows how, unlike an imported image, the SVG images
 * do not loose detail when resized.
 */

import guicomponents.*;

PShape bot;
GVertSlider sdrScale;

float zoom;
float sliderScaleFactor;

void setup() {
  size(480, 360);
  smooth();

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
  GComponent.globalFont = GFont.getFont(this, "Serif", 11);

  // The file "bot1.svg" must be in the data folder
  // of the current sketch to load successfully
  bot = loadShape("bot1.svg");
  
  // Create a vertical slider
  // (this, x, y, width, height)
  // default value limits 0-100 and initial value 50
  sdrScale = new GVertSlider(this, 0, 0, 20, height);
  // set slider value limits (initial value, min, max)
  sdrScale.setLimits(60, 10, height);
  // Set thumb inertia for nice visual effect - acceptable
  // values 1 to 100 (default = 1  i.e. no inertia)
  sdrScale.setInertia(15);
  
  
  // This is to make finer changes between zoom values
  // less jerky zooming at lower values.
  sliderScaleFactor = 45.0f / height;
  // Match the slider
  zoom = 0.1 * sliderScaleFactor * sdrScale.getValue();
  
} 

void draw() {
  pushMatrix();
  background(32,128,32);
  translate(width/2, height/2);

  scale(zoom);
  shape(bot, -140, -140);
  popMatrix();
}

// Event handler for G4P slider events
void handleSliderEvents(GSlider slider){
  zoom = sliderScaleFactor * slider.getValue() * 0.1f;
}
