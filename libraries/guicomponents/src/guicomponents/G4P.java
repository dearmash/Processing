/*
  Part of the GUI for Processing library 
  	http://www.lagers.org.uk/g4p/index.html
	http://gui4processing.googlecode.com/svn/trunk/

  Copyright (c) 2008-09 Peter Lager

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package guicomponents;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PStyle;

/**
 * This class has only static methods. It keeps track of all GComponents created.
 * Its primary role is encapsulated in the draw() method which can override
 * the default Processing drawing mechanism.
 * 
 * @author Peter Lager
 *
 */
public class G4P implements PConstants, GConstants {

	/**
	 * Set of all the GUI components created
	 */
	private static List<GComponent> allComponents = new LinkedList<GComponent>();

	/**
	 * List of GWindows
	 */
	private static List<GWindow> allWinApps = new LinkedList<GWindow>();

	/**
	 * Set of PApplet windows disabled
	 */
	private static List<PApplet> autoDrawDisabled = new LinkedList<PApplet>();

	// Will be set when and first component is created
	public static PApplet mainWinApp = null;

	public static PStyle g4pStyle = null;

	public static boolean messages = true;

	/** INTERNAL USE ONLY  Cursor over changer */
	private static GCursorImageChanger mcd = new GCursorImageChanger();
	public static boolean overControl = false;
	public static boolean cursorChangeEnabled = false;
	public static int mouseOff = ARROW;
	public static int mouseOver = HAND;

	private final static int PCAM_AVAILABLE = 0;
	private final static int PCAM_UNAVAILABLE = 1;
	private final static int PCAM_UNINITIALISED = 2;

	private static Object peasyCam;
	private static Method beginHud, endHud;
	private static int camStatus = PCAM_UNINITIALISED;


	// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????

	public static void listComponents(String tab){
		for(GComponent c : allComponents){
			if(c.parent == null)
				System.out.println(tab + c);
			else {
				if(c.children != null){
					tab += "  ";
					System.out.println(tab + c);
					listChildComponents(c, tab + "  ");
				}
			}
		}
	}

	public static void listChildComponents(GComponent c, String tab){
		for(GComponent child : c.children){
			System.out.println(tab + child);
			if(child.children != null){
				listChildComponents(child, tab + "  ");
			}
		}
	}



	// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????





	/**
	 * Enables or disables cursor over component change
	 * 
	 * This is ignored if no G4P components have been created yet
	 * @param enable
	 */
	public static void setMouseOverEnabled(boolean enable){
		cursorChangeEnabled = enable;
		// If disabling make sure that the cursor is set to mouseOff
		// for the mainWinApp and all control windows
		if(cursorChangeEnabled == false){
			mainWinApp.cursor(mouseOff);
			Iterator<GWindow> iter = allWinApps.iterator();
			while(iter.hasNext()){
				iter.next().papplet.cursor(mouseOff);
			}
		}
	}

	/**
	 * Inform G4P which cursor shapes will be used.
	 * Initial values are ARROW (off) and HAND (over)
	 * 
	 * @param cursorOff
	 * @param cursorOver
	 */
	public static void cursor(int cursorOff, int cursorOver){
		mouseOff = cursorOff;
		mouseOver = cursorOver;
	}

	/**
	 * Inform G4P which cursor to use for mouse over.
	 * 
	 * @param cursorOver
	 */
	public static void cursor(int cursorOver){
		mouseOver = cursorOver;
	}

	/**
	 * INTERNAL USE ONLY <br>
	 * This should be called by all ctors in GComponent and since all GUI 
	 * components inherit from GComponent and are required to call a 
	 * GComponent ctor then all GUI components will automatically be 
	 * registered in the set.
	 * 
	 * @param c the component that has been created.
	 */
	public static void addComponent(GComponent c){
		if(g4pStyle == null)
			getStyle();
		if(allComponents.contains(c)){
			GMessenger.message(ADD_DUPLICATE, c ,null);
		}
		else
			allComponents.add(c);
	}

	/**
	 * INTERNAL USE ONLY <br>
	 * Remove a component so that it is permanently unavailable. <br>
	 * Do not call this method directly otherwise your program will crash. <br>
	 * Use the components dispose method instead.
	 * 
	 * @param c the component to remove
	 */
	public static void dumpComponent(GComponent c){
		allComponents.remove(c);
	}

	/**
	 * INTERNAL USE ONLY <br>
	 * Used to register the main window for cursor over behaviour.
	 * 
	 */
	public static void setMainApp(PApplet theApplet){
		if(mainWinApp == null){
			mainWinApp = theApplet;
			mainWinApp.registerPost(mcd);
		}
	}

	/**
	 * INTERNAL USE ONLY <br>
	 * Record a new control window
	 * @param window
	 */
	public static void addWindow(GWindow window){
		allWinApps.add(window);
	}

	/**
	 * INTERNAL USE ONLY <br>
	 * Remove control window - called when a ControlWindow is closed
	 * for good.
	 *  
	 * @param window
	 */
	public static void removeWindow(GWindow window){
		allWinApps.remove(window);
	}

	/**
	 * Determines whether a window is still open or has been closed
	 * @param window
	 * @return true if the window is still open
	 */
	public static boolean isWindowActive(GWindow window){
		return allWinApps.contains(window);
	}

	/**
	 * INTERNAL USE ONLY
	 */
	private static void getStyle(){
		PGraphics temp = new PGraphics();

		g4pStyle = temp.getStyle();

		g4pStyle.rectMode = CORNER;
		g4pStyle.ellipseMode = DIAMETER;
		g4pStyle.imageMode = CORNER;
		g4pStyle.shapeMode = CORNER;

		g4pStyle.colorMode = RGB;
		g4pStyle.colorModeA = 255.0f;
		g4pStyle.colorModeX = 255.0f;
		g4pStyle.colorModeY = 255.0f;
		g4pStyle.colorModeZ = 255.0f;
	}

	public static void setTextMode(int mode){
		if(mode == MODEL || mode == SCREEN || mode == SHAPE){
			if(g4pStyle == null){
				PGraphics temp = new PGraphics();
				g4pStyle = temp.getStyle();			
			}
			g4pStyle.textMode = mode;
		}
	}

	/**
	 * Set the color scheme to be used by G4P<br>
	 * Only reqd if different from the default blue scheme to be
	 * global specify before creating GUI components
	 * 
	 * Available schemes:
	 * BLUE_SCHEME, GREEN_SCHEME, RED_SCHEME, PURPLE_SCHEME
	 * YELLOW_SCHEME, CYAN_SCHEME, GREY_SCHEME
	 * 
	 * @param theApplet
	 * @param schemeNo GCScheme.GREEN_SCHEME
	 */
	public static void setColorScheme(PApplet theApplet, int schemeNo){
		// If both theApplet and app are null there is nothing we can do!
		if(theApplet != null)
			setMainApp(theApplet);
		else if(mainWinApp == null)
			return;
		GComponent.globalColor = GCScheme.getColor(mainWinApp,  schemeNo);
	}

	/**
	 * Set the font type and size to be used by G4P<br>
	 * Only reqd if different from the default "Serif" 11 <br>
	 * to be global specify before creating GUI components
	 * 
	 * @param theApplet
	 * @param fontName name of font
	 * @param fontSize font size
	 */
	public static void setFont(PApplet theApplet, String fontName, int fontSize){
		// If both theApplet and app are null there is nothing we can do!
		if(theApplet != null)
			setMainApp(theApplet);
		else if(mainWinApp == null)
			return;
		GComponent.globalFont = GFont.getFont(mainWinApp, fontName, fontSize);
	}

	/**
	 * This method is called once to initialise a PeasyCam object.
	 * 
	 * @param pcam a previously created PeasyCam object.
	 * @return true if it can initialise a PeasyCam object else false.
	 */
	public static boolean setPeasyCam(Object pcam){
		camStatus = PCAM_UNAVAILABLE;
		if(!pcam.getClass().getSimpleName().equals("PeasyCam")){
			GMessenger.message(NOT_PEASYCAM, pcam, null);
			return false;
		}
		try {
			beginHud = pcam.getClass().getMethod("beginHUD", (Class[]) null);
			endHud = pcam.getClass().getMethod("endHUD", (Class[])null);
			peasyCam = pcam;
			camStatus =  PCAM_AVAILABLE;
			return true;
		}
		catch(Exception excp){
			GMessenger.message(HUD_UNSUPPORTED, null, null);
			camStatus = PCAM_UNAVAILABLE;
			return false;
		}
	}

	/**
	 * This method is provided to simplify using G4P with PeasyCam. <br>
	 * If the PeasyCam object has been set then a parameter and it will 
	 * automatically call the beginHUD and endHUD methods for you.
	 * <pre>
	 * pcam.beginHUD();
	 * G4P.draw();
	 * pcam.endHUD(); </pre>
	 * becomes
	 * <pre>
	 * G4P.draw();
	 * </pre>
	 */
	public static void draw(){
		if(camStatus != PCAM_AVAILABLE){
			draw(mainWinApp);			
		}
		else {
			try {
				beginHud.invoke(peasyCam, (Object[])null);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			draw(mainWinApp);
			try {
				endHud.invoke(peasyCam, (Object[])null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * When you first use G4P(app) it switches off auto draw for the 
	 * PApplet app.
	 * 
	 */
	public static void draw(PApplet app){
		if(allComponents.size() > 0){
			// Time to take over the responsibility for drawing
			if(!autoDrawDisabled.contains(app))
				unregisterFromPAppletDraw(app);
			// Draw the components on the mainWinApp only.
			// Note that GPanels will call the appropriate
			// draw methods for the components on them
			app.hint(DISABLE_DEPTH_TEST);
			for(GComponent comp : allComponents){
				if(comp.getParent() == null && comp.getPApplet() == app)
					comp.draw();
			}
			app.hint(ENABLE_DEPTH_TEST);
		}
	}

	/**
	 * Unregister all GComponents that have no parent from PApplets auto draw
	 * mechanism. The G4P.draw() will now be responsible for drawing the 
	 * components.
	 * 
	 * It is called once on the first call to G4P.draw()
	 * 
	 */
	private static void unregisterFromPAppletDraw(PApplet app) {
		for(GComponent comp : allComponents){
			if(comp.getParent() == null && comp.getPApplet() == app){
				comp.regDraw = false;
				comp.getPApplet().unregisterDraw(comp);
			}
		}
		autoDrawDisabled.add(app);
	}

	/**
	 * Once disabled you need to call G4P.draw() from the draw()
	 *  method if you wish to see the GUI ever again!
	 */
	public static void disableAutoDraw(){
		unregisterFromPAppletDraw(mainWinApp);
		GMessenger.message(DISABLE_AUTO_DRAW, null, null);
	}

	/**
	 * Is autodraw on for the main PApplet window?
	 * 
	 */
	public static boolean isAutoDrawOn(){
		return isAutoDrawOn(mainWinApp);
	}

	/**
	 * Is autodraw on for the PApplet app?
	 */
	public static boolean isAutoDrawOn(PApplet app){
		return !autoDrawDisabled.contains(app);
	}

	/**
	 * G4P has a range of support messages eg <br>if you create a GUI component 
	 * without an event handler or, <br>a slider where the visible size of the
	 * slider is less than the difference between min and max values.
	 * 
	 * This method allows the user to enable (default) or disable this option. If
	 * disable then it should be called before any GUI components are created.
	 * 
	 * @param enable
	 */
	public static void messagesEnabled(boolean enable){
		messages = enable;
	}

	/**
	 * This will sort the GUI controls on the main sketch PApplet.
	 */
	public static void setDrawOrder(){
		setDrawOrder(mainWinApp);
	}

	/**
	 * This will sort the GUI controls in a secondary window.
	 * @param window the GWindow object
	 */
	public static void setDrawOrder(GWindow window){
		PApplet app = window.papplet;
		setDrawOrder(app);
	}
	
	/**
	 * If you are using GPanel or GCombo it would be useful to call this method in setup
	 * or customGUI (if using GUI builder tool). <br>
	 * DO NOT CALL this method from inside the draw() method. <br>
	 * This will sort the draw order for GUI controls based on their z attribute - note that
	 * a control will be drawn before a control with a higher z. If two controls have the 
	 * same z value, then the controls are ordered by their vertical screen position. This means
	 * that controls near the bottom of the display will be drawn before those nearer the top.
	 * @param windowApp the PApplet object
	 */
	public static void setDrawOrder(PApplet windowApp){
		Collections.sort(allComponents, new GComponent.Z_Order());
		if(windowApp != null && isAutoDrawOn(windowApp)){
			for(GComponent comp : allComponents){
				if(comp.getParent() == null && comp.getPApplet() == windowApp)
					comp.getPApplet().unregisterDraw(comp);
			}
			for(GComponent comp : allComponents){
				if(comp.getParent() == null && comp.getPApplet() == windowApp)
					comp.getPApplet().registerDraw(comp);
			}	
		}
	}
	
	/**
	 * INTERNAL USE ONLY <br>
	 * Used to bring a panel to the front of the display. <br>
	 * Do not use this method directly. 
	 */
	public static void moveToFrontForDraw(GComponent comp){
		PApplet app = comp.getPApplet();
		if(allComponents.remove(comp)){
			allComponents.add(comp);
			if(comp.parent == null && app != null && isAutoDrawOn(app)){
				app.unregisterDraw(comp);
				app.registerDraw(comp);
			}
		}
	}
	
	/**
	 * INTERNAL USE ONLY <br>
	 * Used to ensure the panel is the last component to be tested for mouse events. <br>
	 * Do not use this method. 
	 */
	public static void moveToFrontForMouse(GComponent comp){
		PApplet app = comp.getPApplet();
		if(app != null && allComponents.remove(comp)){
			allComponents.add(comp);
			app.unregisterMouseEvent(comp);
			app.registerMouseEvent(comp);
		}
	}
	

	
}
