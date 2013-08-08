import guicomponents.*;

GWSlider sdr1, sdr2, sdr3, sdr4, sdr5, sdr6, sdr7;

void setup() {
  size(600, 240);  

  //=============================================================
  // Simple default slider,
  // constructor is `Parent applet', the x, y position and length
  sdr1 = new GWSlider(this,20,20,260);
  
  //=============================================================
  // Slider with a custom skin, check the data folder to find 
  // the `blue18px' folder which stores the used image files.
  sdr2 = new GWSlider(this,"blue18px",20,80,260);
	  
  // there are 3 types
  // GWSlider.DECIMAL  e.g.  0.002
  // GWSlider.EXPONENT e.g.  2E-3
  // GWSlider.INTEGER
  sdr2.setValueType(GWSlider.DECIMAL);
  sdr2.setLimits(0.5f, 0f, 1.0f);
  sdr2.setPrecision(3);
  sdr2.setRenderValueLabel(false); 

  //=============================================================
  // Slider with another custom skin
  sdr3 = new GWSlider(this,"red_yellow18px",20,140,260);
  sdr3.setRenderValueLabel(false); 

  //=============================================================
  // Slider with another custom skin
  sdr4 = new GWSlider(this,"purple18px",20,200,260);
  sdr4.setInertia(30);
  
  //=============================================================
  // Standard slider with labels switched off
  sdr5 = new GWSlider(this,320,20,260);
  sdr5.setValueType(GWSlider.DECIMAL);
  sdr5.setLimits(0.5f, 0f, 1.0f);
  sdr5.setTickCount(3); 
  sdr5.setRenderMaxMinLabel(false); //hides labels
  sdr5.setRenderValueLabel(false);  //hides value label
  sdr5.setStickToTicks(true);       //false by default 
  // `Stick to ticks' enforces that the handle can only rest 
  // at a tick position.
  		
  //=============================================================
  // This example shows small float numbers used and settings
  // the accuracy of the display labels
  sdr6 = new GWSlider(this,320,80,260);
  sdr6.setValueType(GWSlider.EXPONENT);
  sdr6.setLimits(3E-2f, 2.0E-2f, 3.5E-2f);
  sdr6.setTickCount(15); 
  sdr6.setPrecision(1);
  sdr6.setInertia(15);
  sdr6.setStickToTicks(true);
  
  //=============================================================
  // We can also add custom labels to ticks
  // Note: 
  // setTickLabels() changes the number of ticks previously 
  //                 set with setTickCount() to match the 
  //                 number of labels in the array.
  // setTickCount()  cancels labels that were previously set 
  //                 with setTickLabels()
  String[] sdr6TickLabels = new String[] {"A", "B", "C", "D", "E"};
  sdr7 = new GWSlider(this,"green_red20px", 320,140,260);
  sdr7.setTickLabels(sdr6TickLabels);
  sdr7.setLimits(1, 0, 4);
  sdr7.setRenderValueLabel(false); 
  sdr7.setStickToTicks(true);
  sdr7.setValue(2.345f);
  // notice that we are setting a value that is not 
  // exactly a tick when `stick to tick' is true, 
  // setValue will stick to nearest tick value

}

void draw() {
  background(200,200,255);
}

void handleSliderEvents(GSlider slider) {
  println("integer value:" + slider.getValue() + " float value:" + slider.getValuef());
}
