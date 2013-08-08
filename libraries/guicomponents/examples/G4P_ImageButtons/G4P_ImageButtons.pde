import guicomponents.*;

GImageButton btnGhost, btnCoins, btnTJ, btnInfo;
GLabel lblOut;
long timer;
String[] files;

void setup(){
  size(580, 220);
  cursor(CROSS);
  String[] files;

  files = new String[]{
    "infooff.png", "infoover.png", "infodown.png"      };
  btnInfo = new GImageButton(this, "infomask.png", files, 20,14);

  btnGhost = new GImageButton(this, null, "ghosts.png", 3, 40 ,90);

  files = new String[]{
    "tjoff.jpg", "tjover.jpg", "tjdown.jpg"      };
  btnTJ = new GImageButton(this, "tjmask.png", files, 150,10);

  btnCoins = new GImageButton(this, null, "coins.png", 3, 400 ,20);

  lblOut = new GLabel(this, "", 10, 190, 560, 20);
  lblOut.setTextAlign(GAlign.CENTER);
  timer = millis() - 5000;
}

void draw(){
  background(220,220,255);
  if(millis() - timer > 4000){
    lblOut.setText("CLICK ON A BUTTON");
  }
}

//btnGhost, btnCoins, btnTJ, btnInfo;

void handleImageButtonEvents(GImageButton imagebutton) { 
  if(imagebutton == btnGhost)
    lblOut.setText("Ghosts - composite image using transparency");
  else if(imagebutton == btnCoins)
    lblOut.setText("Coins - composite image using transparency");  
  else if(imagebutton == btnTJ)
    lblOut.setText("Tom & Jerry - multiple images using mask");
  else if(imagebutton == btnInfo)
    lblOut.setText("Info - multiple images using mask");
  timer = millis();
}




