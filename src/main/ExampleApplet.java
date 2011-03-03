package main;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.List;

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

/**
 * Main applet class
 * @author AudioPreview
 *
 */
public class ExampleApplet extends Applet {
	private static final long serialVersionUID = -2044141944829708675L;
	Canvas display_parent;
	WavefrontObject stadium, marker;
	private Vertex camera, listener;
	private float cameraAngle;
	private float cameraAngle2;
	private boolean topView;
	boolean [] prevKeyboard;
	private SoundWrapper audioPlayer;
	private SeatSoundWrapper seats;
	private int currentSeat, currentRow, currentSeatArea;
	private Venue venue;
	
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

	public void start() {}
	public void stop() {}
	
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
		FloatBuffer light_ambient = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{0.200f, 0.200f, 0.200f, 1.0f});
		
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
        
        List<Venue> venues = null;
        URL stadiumUrl, seatsUrl, markerUrl;
        StadiumParser stadiumParser = new StadiumParser();
        try {
        stadiumUrl = new URL(getCodeBase(), "../models/stadium.obj");
        seatsUrl = new URL(getCodeBase(), "../models/seats.xml");
        markerUrl = new URL(getCodeBase(), "../models/marker.obj");
        venues = stadiumParser.parse(seatsUrl);
        System.err.println("Reading file from " + stadiumUrl.getFile());
        stadium = new WavefrontObject(stadiumUrl.getFile());
        marker = new WavefrontObject(markerUrl.getFile());
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
        this.currentSeatArea = 0;
        this.currentSeat = 0;
        this.currentRow = 0;
        this.venue = venues.get(0);
        this.seats = new SeatSoundWrapper(this.venue.getSeatAreas().get(this.currentSeatArea));
        camera = new Vertex(seats.getSeatCoordVertex(this.currentRow, this.currentSeat));
        listener = new Vertex(camera);
        cameraAngle = 0;
        cameraAngle2 = 0;
        topView = true;
        
        audioPlayer.setListenerPos((FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()}));
        
        prevKeyboard = new boolean[Keyboard.KEYBOARD_SIZE];
	}
		
    private void render(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT |GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        GL11.glRotatef(cameraAngle, 1, 0, 0);
        GL11.glRotatef(cameraAngle2, 0, 1, 0);
        GL11.glTranslatef(-camera.getX(), -camera.getY(), -camera.getZ());
        drawModel(stadium);
        GL11.glTranslatef(listener.getX(), listener.getY(), listener.getZ());
        drawModel(marker);
        GL11.glLoadIdentity();
    }
    
    public void drawModel(WavefrontObject w) {
        Iterator<Group> groupIt = w.getGroups().iterator();
        while(groupIt.hasNext()) {
        	Group g = groupIt.next();
    		Vertex kd = g.getMaterial().getKd();        		
    		GL11.glColor3f(kd.getX(), kd.getY(), kd.getZ());
        	Iterator<Face> faceIt = g.getFaces().iterator();
        	while(faceIt.hasNext()) {
        		Face face = faceIt.next();
        		drawFace(face);
        	}
        }    	
    }
    
    public boolean onKeyup(int key) {
    	boolean temp = prevKeyboard[key];
    	prevKeyboard[key] = Keyboard.isKeyDown(key);
    	return temp && !prevKeyboard[key];
    }

    public void controlCamera() {
    	boolean up, down, left, right, shift;
    	
    	shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    	
		if(onKeyup(Keyboard.KEY_C))
			topView = !topView;
		
        if(topView) {
        	if(cameraAngle < 90) {
        		cameraAngle = Math.min(cameraAngle + 1, 90);
        		cameraAngle2 = Math.min(cameraAngle2 + 2, 180);
        		camera.setY(camera.getY() + 0.4f);
        	}
        } else {
        	if(cameraAngle > 0) {
        		cameraAngle = Math.max(cameraAngle - 1, 0);
        		cameraAngle2 = Math.max(cameraAngle2 - 2, 0);
        		camera.setY(camera.getY() - 0.4f);
        	}
        }
    	
    	if(shift) {
        	up = Keyboard.isKeyDown(Keyboard.KEY_UP) ^ topView;
        	down = Keyboard.isKeyDown(Keyboard.KEY_DOWN) ^ topView;
        	left = Keyboard.isKeyDown(Keyboard.KEY_LEFT) ^ topView;
        	right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) ^ topView;
    		if( up && !down) {
    			camera.setZ(camera.getZ()-1);
    			listener.setZ(camera.getZ());
    		} else if( down && !up) {
    			camera.setZ(camera.getZ()+1);
    			listener.setZ(camera.getZ());
    		}
    		if( left && !right) {
    			camera.setX(camera.getX()-1);
    			listener.setX(camera.getX());
    		} else if( right && !left) {
    			camera.setX(camera.getX()+1);
    			listener.setX(camera.getX());
    		}
    		this.setListenerPosition(camera);
    	} 
    	else {
    		Vertex pos = null;
    		up = onKeyup(Keyboard.KEY_UP) ^ topView;
    		down = onKeyup(Keyboard.KEY_DOWN) ^ topView;
    		left = onKeyup(Keyboard.KEY_LEFT) ^ topView;
    		right = onKeyup(Keyboard.KEY_RIGHT) ^ topView;
    		int[] seatNumRow = new int[2];
	    	if( left && !right) {
	    		System.out.println("LEFT");
	    		audioPlayer.singlePlay(4);
	    		seatNumRow = seats.decIncSeat(this.currentSeat, this.currentRow, false);
	    		this.currentSeat = seatNumRow[1];
	    		this.currentRow = seatNumRow[0];
	    		pos = seats.getSeatCoordVertex(this.currentSeat, this.currentRow);
	    		System.out.println("Current row " + this.currentRow);
	    		System.out.println("Current seat " + this.currentSeat);
	    	}
	    	else if( right && !left) {
	    		System.out.println("RIGHT");
	    		audioPlayer.singlePlay(5);
	    		seatNumRow = seats.decIncSeat(this.currentSeat, this.currentRow, true);
	    		this.currentSeat = seatNumRow[1];
	    		this.currentRow = seatNumRow[0];
	    		pos = seats.getSeatCoordVertex(this.currentSeat, this.currentRow);
	    		System.out.println("Current row " + this.currentRow);
	    		System.out.println("Current seat " + this.currentSeat);
	    	}
	    	else if( down && !up) {
	    		this.currentRow = seats.decrementRow(this.currentRow);
	    		pos = seats.getSeatCoordVertex(this.currentSeat, this.currentRow);
	    	}
	    	else if( up && !down) {
	    		this.currentRow = seats.incrementRow(this.currentRow);
	    		pos = seats.getSeatCoordVertex(this.currentSeat, this.currentRow);
	    	}
	    	else if(onKeyup(Keyboard.KEY_N)) {
	    		this.currentSeatArea = ++this.currentSeatArea % this.venue.getSeatAreas().size();
	    		System.out.println(this.currentSeatArea);
	    		seats.setSeatArea(this.venue.getSeatAreas().get(this.currentSeatArea));
	    		this.currentSeat = 0;
	    		this.currentRow = 0;
	    		pos = seats.getSeatCoordVertex(this.currentSeat, this.currentRow);
	    	}
	    	else if(onKeyup(Keyboard.KEY_SPACE)) {
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

	    	if( pos != null ) {
	    		System.err.println("Pos is "+pos);
		    	listener = new Vertex(pos);
		    	camera.setX(listener.getX());
		    	camera.setZ(listener.getZ());
		    	this.setListenerPosition(pos);
	    	}
    	}
    }
    public void controlLight() {
/*
    	FloatBuffer light_position = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{1.000f, camera.getX(), camera.getY(), camera.getZ()});
    	light_position.rewind();
    	GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, light_position);
*/
    }
    public void setListenerPosition(Vertex pos) { 	
    	    	audioPlayer.setListenerPos((FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{pos.getX(), pos.getY(), pos.getZ()}).rewind());
    	    	audioPlayer.setSourcePos(2, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{pos.getX(), pos.getY(), pos.getZ()}).rewind());
    	    	audioPlayer.setSourcePos(3, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{pos.getX(), pos.getY(), pos.getZ()}).rewind());
    	    	audioPlayer.setSourcePos(4, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{pos.getX(), pos.getY(), pos.getZ()}).rewind());
    	    	audioPlayer.setSourcePos(5, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{pos.getX(), pos.getY(), pos.getZ()}).rewind());
/*    	    	audioPlayer.setSourcePos(2, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()}).rewind());
    	    	audioPlayer.setSourcePos(3, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()}).rewind());
    	    	audioPlayer.setSourcePos(4, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()}).rewind());
    	    	audioPlayer.setSourcePos(5, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[]{camera.getX(), camera.getY(), camera.getZ()}).rewind());*/
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
		audioPlayer.killALData();
		Display.destroy();
	}
	public void drawFace(Face face) {
		Vertex[] verts = face.getVertices();
		Vertex[] norms = face.getNormals();
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
		GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_POINT);
		if(face.getType() == Face.GL_TRIANGLES) {
	        // Fill
	        GL11.glBegin(GL11.GL_TRIANGLES);
	        GL11.glNormal3f(norms[0].getX(), norms[0].getY(), norms[0].getZ());
	        GL11.glVertex3f(verts[0].getX(), verts[0].getY() , verts[0].getZ() );
	        GL11.glNormal3f(norms[1].getX(), norms[1].getY(), norms[1].getZ());
	        GL11.glVertex3f(verts[1].getX(), verts[1].getY() , verts[1].getZ() );
	        GL11.glNormal3f(norms[2].getX(), norms[2].getY(), norms[2].getZ());
	        GL11.glVertex3f(verts[2].getX(), verts[2].getY() , verts[2].getZ() );
	        GL11.glEnd();
		} else {
	        // Fill
	        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glNormal3f(norms[0].getX(), norms[0].getY(), norms[0].getZ());
	        GL11.glVertex3f(verts[0].getX(), verts[0].getY() , verts[0].getZ() );
	        GL11.glNormal3f(norms[1].getX(), norms[1].getY(), norms[1].getZ());
	        GL11.glVertex3f(verts[1].getX(), verts[1].getY() , verts[1].getZ() );
	        GL11.glNormal3f(norms[2].getX(), norms[2].getY(), norms[2].getZ());
	        GL11.glVertex3f(verts[2].getX(), verts[2].getY() , verts[2].getZ() );
	        GL11.glNormal3f(norms[3].getX(), norms[3].getY(), norms[3].getZ());
	        GL11.glVertex3f(verts[3].getX(), verts[3].getY() , verts[3].getZ() );
	        GL11.glEnd();
		}
	}

}