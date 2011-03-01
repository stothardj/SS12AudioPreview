package main;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import com.obj.Vertex;

/**
* The vertex and fragment shaders are setup when the box object is
* constructed. They are applied to the GL state prior to the box
* being drawn, and released from that state after drawing.
* @author Stephen Jones
*/
public class Quad {
    public Quad(Vertex p1, Vertex p2, Vertex p3, Vertex p4){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public void draw(Vertex camera){
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -10.0f);

        // Fill
        GL11.glColor3f((new Double(p1.getX()).hashCode() % 273) / 273.0f,
        		(new Double(p2.getZ()).hashCode() % 273) / 273.0f,
        		(new Double(p3.getY()).hashCode() % 273) / 273.0f);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glPolygonOffset(1, 1);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(p1.getX() - camera.getX(), p1.getY() - camera.getY(), p1.getZ() - camera.getZ());
        GL11.glVertex3f(p2.getX() - camera.getX(), p2.getY() - camera.getY(), p2.getZ() - camera.getZ());
        GL11.glVertex3f(p3.getX() - camera.getX(), p3.getY() - camera.getY(), p3.getZ() - camera.getZ());
        GL11.glVertex3f(p4.getX() - camera.getX(), p4.getY() - camera.getY(), p4.getZ() - camera.getZ());
        GL11.glEnd();
        
        // Line
        /*
        GL11.glColor3f(0.0f, 0.0f, 0.0f);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glPolygonOffset(1, 1);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex3f(p1.x - camera.x, p1.y - camera.y, p1.z - camera.z);
        GL11.glVertex3f(p2.x - camera.x, p2.y - camera.y, p2.z - camera.z);
        GL11.glVertex3f(p3.x - camera.x, p3.y - camera.y, p3.z - camera.z);
        GL11.glEnd();
        */

        //release the shader
        ARBShaderObjects.glUseProgramObjectARB(0);

    }
    
    public Vertex p1, p2, p3, p4;

}
