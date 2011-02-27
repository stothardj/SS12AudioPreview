package main;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ExampleApplet extends Applet implements KeyListener {
	
	Canvas display_parent;
	Parser p;
	Point3D camera;
	
	boolean up, down, left, right;
	
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
		
		this.addKeyListener(this);
		
		setLayout(new BorderLayout());
		try {
			display_parent = new Canvas() {
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
                
        p = new Parser("../models/stadium.obj");
        camera = new Point3D(0,0,0);
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
    	if( up && !down)
    		camera.y ++;
    	else if( down && !up)
    		camera.y --;
    	if( left && !right)
    		camera.x --;
    	else if( right && !left)
    		camera.x ++;
    }
	public void gameLoop() {
		while(running) {
			Display.sync(60);
			render();
			Display.update();
			controlCamera();
		}
		
		Display.destroy();
	}


	@Override
	public void keyTyped(KeyEvent e) {}


	@Override
	public void keyPressed(KeyEvent e) {
		System.err.println("Key pressed");
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			up = true;
			break;
		case KeyEvent.VK_DOWN:
			down = true;
			break;
		case KeyEvent.VK_LEFT:
			left = true;
			break;
		case KeyEvent.VK_RIGHT:
			right = true;
			break;
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		System.err.println("Key released");
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			up = false;
			break;
		case KeyEvent.VK_DOWN:
			down = false;
			break;
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		}		
	}
}