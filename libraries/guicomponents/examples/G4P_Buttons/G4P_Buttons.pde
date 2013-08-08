import guicomponents.*;


private int[] ha = new int[]{
  GAlign.LEFT, GAlign.CENTER, GAlign.RIGHT   };
private int[] va = new int[]{
  GAlign.TOP, GAlign.MIDDLE, GAlign.BOTTOM   };

private GButton[] btnsA = new GButton[9];
private GButton[] btnsB = new GButton[9];
private GButton btnSmile0, btnSmile1;

private GOptionGroup optGroup;
private GOption optLeft, optRight;
private GLabel label;

void setup(){
  size(480, 540);

  G4P.setColorScheme(this, GCScheme.BLUE_SCHEME);
  G4P.setFont(this, "Verdana", 12);

  int count = 0;
  for( int v = 0; v < 3; v++){
    for(int h = 0; h < 3; h++){
      btnsA[count] = new GButton(this, "BUG " + count , "bugs/bug"+count+".png", 1, 10 + h*160, 10 + v*80, 140,60);
      btnsA[count].tag = "Bug Image " + count;
      btnsA[count].setTextAlign(ha[h] | va[v]);

      btnsB[count] = new GButton(this, "BUG " + count , 10 + h*160, 370 + v*60, 140, 40);
      btnsB[count].tag = "Bug Text  " + count;
      btnsB[count].setTextAlign(ha[h] | va[v]);
      btnsB[count].fireAllEvents(true);

      count++;
    }
  }

  btnSmile0 = new GButton(this,"smile.png",3,20,270,70,70);
  btnSmile0.tag = "Smile image only";
  btnSmile1 = new GButton(this,"Smile please", "smile.png",3,150,270,300,70);
  btnSmile1.setFont("Verdana", 20, false);
  btnSmile1.tag = "Smile image with text";

  label = new GLabel(this, "Image alignment on button", 10, 240, 220);
  label.setTextAlign(GAlign.RIGHT);

  optGroup = new GOptionGroup();
  optLeft = new GOption(this, "Left", 240, 240, 50);
  optRight = new GOption(this, "Right", 300, 240, 50);
  optGroup.addOption(optLeft);
  optGroup.addOption(optRight);
  optGroup.setSelected(optLeft);

  G4P.setMouseOverEnabled(true);
}

void handleOptionEvents(GOption selected, GOption deselected){
  int align = GAlign.LEFT;
  if(selected == optRight){
    align = GAlign.RIGHT;
  }
  for(int i = 0; i < 9; i++){
    btnsA[i].setImageAlign(align);		
  }
  btnSmile1.setImageAlign(align);
}

void handleButtonEvents(GButton button) {
  System.out.print(button.tag+"\t\t");
  switch(button.eventType){
  case GButton.PRESSED:
    System.out.println("PRESSED");
    break;
  case GButton.RELEASED:
    System.out.println("RELEASED");
    break;
  case GButton.CLICKED:
    System.out.println("CLICKED");
    break;
  default:
    System.out.println("Unknown mouse event");
  }
}	

void draw(){
  background(200);
}
