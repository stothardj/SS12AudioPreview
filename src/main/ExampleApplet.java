package main;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.nio.FloatBuffer;
import java.util.Iterator;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ExampleApplet extends Applet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Canvas display_parent;
	Parser p;
	Point3D camera;
	
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
        
        p = new Parser("../models/stadium.obj");
        camera = new Point3D(0,2,0);
	}
	
    private void render(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT |GL11.GL_DEPTH_BUFFER_BIT);
        
        Iterator<Triangle> it = p.triangles.iterator();
        int i = 0;
        while(it.hasNext()) {
        	i ++;
        	it.next().draw(camera);
        }
        GL11.glLoadIdentity();
    }

    public void controlCamera() {
    	boolean up, down, left, right;
    	up =Keyboard.isKeyDown(Keyboard.KEY_UP);
    	down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
    	left = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
    	right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
    	if( up && !down)
    		camera.z --;
    	else if( down && !up)
    		camera.z ++;
    	if( left && !right)
    		camera.x --;
    	else if( right && !left)
    		camera.x ++;
    }
    public void controlLight() {
/*
    	FloatBuffer light_position = (FloatBuffer)BufferUtils.createFloatBuffer(4).put(new float[]{1.000f, camera.z, camera.y, camera.x});
    	light_position.rewind();
    	GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, light_position);
    	*/
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

}