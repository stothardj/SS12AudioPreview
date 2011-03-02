package main;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.net.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import sound.SoundWrapper;

import com.obj.Face;
import com.obj.Group;
import com.obj.Vertex;
import com.obj.WavefrontObject;

public class ExampleApplet extends Applet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Canvas display_parent;
	WavefrontObject stadium;
	Vertex camera;
	//SeatSoundWrapper seats;
	boolean pright, pleft, pup, pdown, pspace;
	private SoundWrapper audioPlayer;
	private static final float audioOffset = 0.2f;
	
	/** Thread which runs the main game loop */
	Thread gameThread;
	
	/** is the game loop running */
	boolean running = false;	
	
	public void startLWJGL() {
		
		gameThread = new Thread() {
			public void run() {
				running = true;
				try {
					Display.setParent(display_parent);
					Display.create();
					initGL();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				gameLoop();
			}
		};
		gameThread.start();
	}
	
	
	/**
	 * Tell game loop to stop running, after which the LWJGL Display will 
	 * be destoryed. The main thread will wait for the Display.destroy().
	 */
	private void stopLWJGL() {
		running = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		
	}

	public void stop() {
		
	}
	
	/**
	 * Applet Destroy method will remove the canvas, 
	 * before canvas is destroyed it will notify stopLWJGL()
	 * to stop the main game loop and to destroy the Display
	 */
	public void destroy() {
		remove(display_parent);
		super.destroy();
	}
	
	public void init() {
		System.err.println("Program began");
		audioInit();
		setLayout(new BorderLayout());
		try {
			display_parent = new Canvas() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 8561150810006220384L;
				public final void addNotify() {
					super.addNotify();
					startLWJGL();
				}
				public final void removeNotify() {
					stopLWJGL();
					super.removeNotify();
				}
			};
			display_parent.setSize(getWidth(),getHeight());
			add(display_parent);
			display_parent.setFocusable(true);
			display_parent.requestFocus();
			display_parent.setIgnoreRepaint(true);
			setVisible(true);
		} catch (Exception e) {
			System.err.println(e);
			throw new RuntimeException("Unable to create display");
		}
	}
	
	public void audioInit() {
		audioPlayer = new SoundWrapper(6);
		URL portalMusic, portalVocals, play, pause, prevseat, nextseat;
		try {
			portalMusic = new URL(getCodeBase(), "Still Alive from Portal (Music Only synced).wav");
			portalVocals = new URL(getCodeBase(), "Still Alive from Portal (Vocals Only).wav");
			play = new URL(getCodeBase(), "play.wav");
			pause = new URL(getCodeBase(), "pause.wav");
			prevseat = new URL(getCodeBase(), "prevseat.wav");
			nextseat = new URL(getCodeBase(), "nextseat.wav");
			audioPlayer.initializeSource(portalMusic.getFile(), true, false);
			audioPlayer.setSourcePos(0, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{5.000f, 2.000f, -21.000f}));
			audioPlayer.initializeSource(portalVocals.getFile(), true, false);
			audioPlayer.setSourcePos(1, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{-4.000f, 2.000f, -21.000f}));
			audioPlayer.initializeSource(play.getFile(), false, true);
			audioPlayer.initializeSource(pause.getFile(), false, true);
			audioPlayer.initializeSource(prevseat.getFile(), false, true);
			audioPlayer.initializeSource(nextseat.getFile(), false, true);
		} catch(MalformedURLException e) {
			e.printStackTrace();
		}
	}

	protected void initGL() {
		int w, h;
		FloatBuffer light_ambient = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{0.000f, 0.000f, 0.000f, 1.0f});
		
		FloatBuffer light_diffuse = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{0.600f, 0.600f, 0.600f, 1.0f});
		FloatBuffer light_specular = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{0.100f, 0.100f, 0.100f, 1.0f});
		FloatBuffer light_emission = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{0.000f, 0.000f, 0.000f, 0.000f});
		FloatBuffer light_position = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{0.000f, -16.000f, 3.000f, 3.000f});
		
		FloatBuffer cube_color = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{0.200f, 0.200f, 0.200f, 1.000f});
		FloatBuffer cube_specular = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{1.000f, 1.000f, 1.000f, 1.000f});
		FloatBuffer cube_emission = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{0.000f, 0.000f, 0.000f, 0.000f});
		
		light_ambient.rewind();
		light_position.rewind();
		cube_color.rewind();
		cube_specular.rewind();
		cube_emission.rewind();
		light_diffuse.rewind();
		light_specular.rewind();
		light_emission.rewind();
		
		float cube_shininess = 128.0f;
		
		w = getWidth();
		h = getHeight();
        GL11.glViewport(0,0,w,h);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(45.0f, ((float)w/(float)h),0.1f,100.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_AMBIENT_AND_DIFFUSE, GL11.GL_FILL);
        
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, light_ambient);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, light_diffuse);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, light_specular);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_EMISSION, light_emission);
        
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_COLOR, cube_color);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, cube_specular);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, cube_emission);
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, cube_shininess);
        
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, light_position);
        
        URL stadiumUrl;
        try {
        stadiumUrl = new URL(getCodeBase(), "../models/stadium.obj");
        System.err.println("Reading file from " + stadiumUrl.getFile());
        stadium = new WavefrontObject(stadiumUrl.getFile());
        } catch(MalformedURLException e) {
        	e.printStackTrace();
        }
        
        camera = new Vertex(0,2,0);
        audioPlayer.setListenerPos((FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()}));
        /*
        float[] stadiumExtrema = extremaVerts(p, p.getObjVerts("Cube"));
        ArrayList<Point3D> ss = p.getObjVerts("Grid");
        float[] seatExtrema = extremaVerts(p, ss);
        
        Iterator<Point3D>it = ss.iterator();
        for(int i = 0; i<6; i++)
        	System.err.print(stadiumExtrema[i] + ", ");
        System.err.println();
        for(int i = 0; i<6; i++)
        	System.err.print(seatExtrema[i] + ", ");
        System.err.println();
        
        //Padding (move to xml later)
        stadiumExtrema[5] -= 16;
        stadiumExtrema[0] += 4;
        stadiumExtrema[3] -= 4;
        
        while(it.hasNext()) {
        	Point3D curr = it.next();
        	if(seatExtrema[3] != seatExtrema[0])
        		curr.x = (curr.x - seatExtrema[0]) / (seatExtrema[3] - seatExtrema[0]) * (stadiumExtrema[3] - stadiumExtrema[0]) + stadiumExtrema[0];
        	//if(seatExtrema[4] != seatExtrema[1])
        	//	curr.y = (curr.y - seatExtrema[1]) / (seatExtrema[4] - seatExtrema[1]) * (stadiumExtrema[4] - stadiumExtrema[1]) + stadiumExtrema[1];
        	if(seatExtrema[5] != seatExtrema[2])
        		curr.z = (curr.z - seatExtrema[2]) / (seatExtrema[5] - seatExtrema[2]) * (stadiumExtrema[5] - stadiumExtrema[2]) + stadiumExtrema[2];
        }
        seats = new SeatSoundWrapper(ss);
        */
	}
		
    private void render(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT |GL11.GL_DEPTH_BUFFER_BIT);
        
        Iterator<Group> groupIt = stadium.getGroups().iterator();
        while(groupIt.hasNext()) {
        	Iterator<Face> faceIt = groupIt.next().getFaces().iterator();
        	while(faceIt.hasNext()) {
        		Face face = faceIt.next();
        		Vertex[] verts = face.getVertices();
        		if(face.getType() == Face.GL_TRIANGLES) {
        			drawTriangle(verts[0],verts[1],verts[2]);
        		} else {
        			drawQuad(verts[0],verts[1],verts[2],verts[3]);
        		}
        	}
        }
        GL11.glLoadIdentity();
    }

    public void controlCamera() {
    	boolean up, down, left, right, shift, space;
    	up = Keyboard.isKeyDown(Keyboard.KEY_UP);
    	down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
    	left = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
    	right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
    	space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
    	
    	shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    	
    	if(shift) {
    		Vertex pos = null;
    		boolean tleft = left;
    		boolean tright = right;
    		boolean tup = up;
    		boolean tdown = down;
    		left = !left && pleft;
    		right = !right && pright;
    		pright = tright;
    		pleft = tleft;

    		
    		up = !up && pup;
    		down = !down && pdown;
    		pup = tup;
    		pdown = tdown;
    		/*
	    	if( left && !right) {
	    		audioPlayer.singlePlay(4);
	    		pos = seats.prevSeatCoord();
	    	}
	    	else if( right && !left) {
	    		audioPlayer.singlePlay(5);
	    		pos = seats.nextSeatCoord();
	    	} 	else if( down && !up)
	    		pos = seats.incSeatCoord(-30);
	    	else if( up && !down)
	    		pos = seats.incSeatCoord(30);

	    	if( pos != null ) {
	    		System.err.println("Pos is "+pos);
		    	camera = new Point3D(pos);
	    	}
	    	*/
    	} else {
    		boolean tspace = space;
    		space = !space && pspace;
    		pspace = tspace;
	    	if( up && !down)
	    		camera.setZ(camera.getZ() - 1);
	    	else if( down && !up)
	    		camera.setZ(camera.getZ() + 1);
	    	if( left && !right)
	    		camera.setX(camera.getX() - 1);
	    	else if( right && !left)
	    		camera.setX(camera.getX() + 1);
	    	this.setListenerPosition();
	    	if(space) {
	    		System.err.println("Space Pressed");
	    		if(audioPlayer.areAllPlaying()) {
	    			audioPlayer.singlePlay(3);
	    			audioPlayer.pause();
	    		}
	    		else {
	    			audioPlayer.singlePlay(2);
	    			audioPlayer.play();
	    		}
	    	}
	    	//System.err.println("Camera is at "+camera);
    	}
    }
    public void controlLight() {
/*
    	FloatBuffer light_position = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{1.000f, camera.z, camera.y, camera.x});
    	light_position.rewind();
    	GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, light_position);
    	*/
    }
    public void setListenerPosition() { 	
    	    	audioPlayer.setListenerPos((FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()*0.9f}).rewind());
    	    	audioPlayer.setSourcePos(2, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()*0.9f}).rewind());
    	    	audioPlayer.setSourcePos(3, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()*0.9f}).rewind());
    	    	audioPlayer.setSourcePos(4, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()*0.9f}).rewind());
    	    	audioPlayer.setSourcePos(5, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()*0.9f}).rewind());
    }
	public void gameLoop() {
		while(running) {
			Display.sync(60);
			render();
			Display.update();
			controlCamera();
			controlLight();
			
			//System.err.println("X: "+camera.x+ "Y: "+camera.y+"Z:"+camera.z);
		}
		
		Display.destroy();
	}
	
	public void drawTriangle(Vertex p1, Vertex p2, Vertex p3) {
        GL11.glLoadIdentity();

        // Fill
        GL11.glColor3f((new Double(p1.getX()).hashCode() % 273) / 273.0f,
        		(new Double(p2.getZ()).hashCode() % 273) / 273.0f,
        		(new Double(p3.getY()).hashCode() % 273) / 273.0f);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex3f(p1.getX() - camera.getX(), p1.getY() - camera.getY(), p1.getZ() - camera.getZ());
        GL11.glVertex3f(p2.getX() - camera.getX(), p2.getY() - camera.getY(), p2.getZ() - camera.getZ());
        GL11.glVertex3f(p3.getX() - camera.getX(), p3.getY() - camera.getY(), p3.getZ() - camera.getZ());
        GL11.glEnd();
        
        // Line
        
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glPolygonOffset(1, 1);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex3f(p1.getX() - camera.getX(), p1.getY()- camera.getY(), p1.getZ() - camera.getZ());
        GL11.glVertex3f(p2.getX() - camera.getX(), p2.getY() - camera.getY(), p2.getZ() - camera.getZ());
        GL11.glVertex3f(p3.getX() - camera.getX(), p3.getY() - camera.getY(), p3.getZ() - camera.getZ());
        GL11.glEnd();
        
	}
	
	public void drawQuad(Vertex p1, Vertex p2, Vertex p3, Vertex p4) {
        GL11.glLoadIdentity();

        // Fill
        GL11.glColor3f((new Double(p1.getX()).hashCode() % 273) / 273.0f,
        		(new Double(p2.getZ()).hashCode() % 273) / 273.0f,
        		(new Double(p3.getY()).hashCode() % 273) / 273.0f);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(p1.getX() - camera.getX(), p1.getY() - camera.getY(), p1.getZ() - camera.getZ());
        GL11.glVertex3f(p2.getX() - camera.getX(), p2.getY() - camera.getY(), p2.getZ() - camera.getZ());
        GL11.glVertex3f(p3.getX() - camera.getX(), p3.getY() - camera.getY(), p3.getZ() - camera.getZ());
        GL11.glVertex3f(p4.getX() - camera.getX(), p4.getY() - camera.getY(), p4.getZ() - camera.getZ());
        GL11.glEnd();
        
        // Line
        
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glPolygonOffset(1, 1);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(p1.getX() - camera.getX(), p1.getY() - camera.getY(), p1.getZ() - camera.getZ());
        GL11.glVertex3f(p2.getX() - camera.getX(), p2.getY() - camera.getY(), p2.getZ() - camera.getZ());
        GL11.glVertex3f(p3.getX() - camera.getX(), p3.getY() - camera.getY(), p3.getZ() - camera.getZ());
        GL11.glVertex3f(p4.getX() - camera.getX(), p4.getY() - camera.getY(), p4.getZ() - camera.getZ());        
        GL11.glEnd();
        
	}

}