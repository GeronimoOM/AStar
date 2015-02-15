
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

public class Main {
	static DisplayMode displayMode;
	static PerspectiveCamera camera;
    static float lastFrame;
    static float delta;
    static Grid grid;
    static int cellSize = 10;
    
    static VBOCompound backgroundVBOs;
    static VBOCompound blockedVBOs;
    static VBOCompound openedVBOs;
    static HashSet<Integer> opened;
    static VBOCompound closedVBOs;
    static VBOCompound currentVBOs;
    static VBOCompound startVBOs;
    static VBOCompound destVBOs;
    static VBOCompound pathVBOs;
    
    static AStarPathFinder finder;
    
    static long passedTime = 0;
    static long algAnimInterval = 200;
    static long finAnimInterval = 500;
    
    static boolean finAnimBool = true;
    static boolean pause = true;
    static boolean pButtonDown = false;
    static boolean finished = false;
    
    public static void main(String []args){
    	genGrid(50, 40, 0.3);
    	start();
    }

	public static void start(){
		//I have added this line=)
    	try {
    		displayMode = new DisplayMode(960, 540);
    		Display.setDisplayMode(displayMode);
    		Display.create();
    	} catch (LWJGLException e) {
    		e.printStackTrace();
    		Display.destroy();
    		System.exit(1);
    	}
    	
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		camera = new PerspectiveCamera(45f, (float)displayMode.getWidth()/(float)displayMode.getHeight(), 0.1f, (float)(grid.sizeY * cellSize / 2 / Math.tan(Math.PI / 8) + 500));
		camera.setPosition(grid.sizeX * cellSize / 2, grid.sizeY * cellSize / 2, (float)(grid.sizeY * cellSize / 2 / Math.tan(Math.PI / 8) + 10));
		
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
    	glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S, GL_REPEAT);
    	glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T, GL_REPEAT);
    	glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    	glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    	glEnable(GL_TEXTURE_2D);
    	glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
    	
    	glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
    	Mouse.setGrabbed(true);
    	
    	backgroundVBOs = new VBOCompound(loadPNGTexture("background.png"), cellSize, grid.sizeX, grid.sizeY);
    	openedVBOs = new VBOCompound(loadPNGTexture("opened.png"), cellSize);
    	opened = new HashSet<Integer>();
    	closedVBOs = new VBOCompound(loadPNGTexture("closed.png"), cellSize);
    	blockedVBOs = new VBOCompound(loadPNGTexture("blocked.png"), cellSize);
    	for(int v: grid.getBlocked()) blockedVBOs.add(grid.xCoord(v), grid.yCoord(v));
    	Texture currentTexture = loadPNGTexture("current.png");
    	currentVBOs = new VBOCompound(currentTexture, cellSize);
    	Texture startTexture = loadPNGTexture("start.png");
    	Texture startTexture2 = loadPNGTexture("startNoPath.png");
    	startVBOs = new VBOCompound(startTexture, cellSize);
    	startVBOs.add(grid.xCoord(finder.start), grid.yCoord(finder.start));
    	Texture destTexture = loadPNGTexture("dest.png");
    	Texture destTexture2 = loadPNGTexture("destNoPath.png");
    	destVBOs = new VBOCompound(destTexture, cellSize);
    	destVBOs.add(grid.xCoord(finder.dest), grid.yCoord(finder.dest));
    	Texture pathTexture1 = loadPNGTexture("path1.png");
    	Texture pathTexture2 = loadPNGTexture("path2.png");
    	pathVBOs = new VBOCompound(pathTexture1, cellSize);
    	
    	glClearColor(0.49f, 0.61f, 0.69f, 1);
    	
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
        	if(Keyboard.isKeyDown(Keyboard.KEY_P)){
        		if(!pButtonDown) {
        			pause = !pause;
        			pButtonDown = true;
        		}
        	}
        	else pButtonDown = false;
        	
        	if(!pause) {
            	glClearColor(0.49f, 0.61f, 0.69f, 1);
        		if(!finished) {
		        	if(passedTime >= algAnimInterval) {
		        		passedTime = 0;
		        		if(!finder.completed) {
		        			finder.nextStep();
				        	for(int i : finder.openedList) if(!opened.contains(i)) {
				        		opened.add(i);
				        		openedVBOs.add(grid.xCoord(i), grid.yCoord(i));
				        	}
			        		closedVBOs.add(grid.xCoord(finder.current), grid.yCoord(finder.current));
				        	currentVBOs = new VBOCompound(currentTexture, cellSize);
				        	currentVBOs.add(grid.xCoord(finder.current), grid.yCoord(finder.current));
		        		}
		        		else {
		        			if(finder.getPath() != null) for(int i: finder.getPath()) pathVBOs.add(grid.xCoord(i), grid.yCoord(i));
		        			finished = true;
		        		}
	        		}
	        	}
        		else {
        			if(passedTime >= finAnimInterval) {
		        		passedTime = 0;
		        		if(finder.current == finder.dest) {
			        		if(finAnimBool) pathVBOs.setTexture(pathTexture2);
			        		else pathVBOs.setTexture(pathTexture1);
		        		}
		        		else {
		        			if(finAnimBool){
		        				startVBOs.setTexture(startTexture2);
		        				destVBOs.setTexture(destTexture2);
		        			}
			        		else {
			        			startVBOs.setTexture(startTexture);
			        			destVBOs.setTexture(destTexture);
			        		}
		        		}
		        		finAnimBool = !finAnimBool;
        			}
        		}
        	}
        	else glClearColor(0.29f, 0.41f, 0.49f, 1);
        	
        	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        	glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        	glEnableClientState(GL_VERTEX_ARRAY);
    		
        	backgroundVBOs.draw();
        	blockedVBOs.draw();
        	if(!finished){
        		openedVBOs.draw();
	        	closedVBOs.draw();
        	}
        	else pathVBOs.draw();
        	startVBOs.draw();
        	destVBOs.draw();
        	if(!finished) currentVBOs.draw();
        	
        	glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        	glDisableClientState(GL_VERTEX_ARRAY);
			
        	delta = getDelta(); 
        	if(!pause) passedTime += delta;
        	
			camera.update(delta);
			camera.useViewport();
        	Display.update();
        	
    	}
    	Display.destroy();
    	System.exit(0);
    }
	
	
    private static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    private static int getDelta() {
        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrame);
        lastFrame = getTime();
        return delta;
    }

    private static Texture loadPNGTexture(String name){
    	try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + name)));
		} 
    	catch (IOException e) {
			e.printStackTrace();	
		}
    	return null;
    }
    
    private static void genGrid(int xSize, int ySize, double perc){
    	grid = new Grid(xSize, ySize);
    	Random rand = new Random();
    	finder = new AStarPathFinder(grid.graph, rand.nextInt(grid.sizeX * grid.sizeY), rand.nextInt(grid.sizeX * grid.sizeY));
    	for(int i = 0; (double)i / (grid.sizeX * grid.sizeY - 2) < perc;){
    		int randVertex = rand.nextInt(grid.sizeX * grid.sizeY);
    		if(grid.isBlocked(randVertex) || randVertex == finder.start || randVertex == finder.dest) continue;
    		grid.blockVertex(randVertex);
    		i++;
    	}
    }
    
}
