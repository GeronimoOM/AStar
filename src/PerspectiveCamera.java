import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class PerspectiveCamera {
	private Vector3f position;
	private Vector3f rotation;
	
	private float mouseSensitivity = 60;
	private float movementSpeed = 80; 
	
	public PerspectiveCamera(float fOV, float aspectRatio, float zNear, float zFar){
		glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(fOV, aspectRatio, zNear, zFar);
		position = new Vector3f();
		rotation = new Vector3f();
	}
	
	public PerspectiveCamera(float fOV, float aspectRatio, float zNear, float zFar, Vector3f pos, Vector3f rot){
		this(fOV, aspectRatio, zNear, zFar);
		position = pos;
		rotation = rot;
	}
	
	void setPosition(float x, float y, float z){
		position = new Vector3f(x, y, z);
	}
	
	void update(float delta){
		rotation.x += (-Mouse.getDY() * mouseSensitivity / 1000);
		rotation.y += (Mouse.getDX() * mouseSensitivity / 1000);
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.x += movementSpeed / 1000 * delta * (float)(Math.cos(Math.toRadians(rotation.x)) * Math.sin(Math.toRadians(rotation.y)));
			position.y -= movementSpeed / 1000 * delta * (float) Math.sin(Math.toRadians(rotation.x));
		    position.z -= movementSpeed / 1000 * delta * (float)(Math.cos(Math.toRadians(rotation.x)) * Math.cos(Math.toRadians(rotation.y)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.x -= movementSpeed / 1000 * delta * (float)(Math.cos(Math.toRadians(rotation.x)) * Math.sin(Math.toRadians(rotation.y)));
			position.y += movementSpeed / 1000 * delta * (float) Math.sin(Math.toRadians(rotation.x));
		    position.z += movementSpeed / 1000 * delta * (float)(Math.cos(Math.toRadians(rotation.x)) * Math.cos(Math.toRadians(rotation.y)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x += movementSpeed / 1000 * delta * (float)Math.sin(Math.toRadians(rotation.y-90));
			position.z -= movementSpeed / 1000 * delta * (float)Math.cos(Math.toRadians(rotation.y-90));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x += movementSpeed / 1000 * delta * (float)Math.sin(Math.toRadians(rotation.y+90));
		    position.z -= movementSpeed / 1000 * delta * (float)Math.cos(Math.toRadians(rotation.y+90));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)){
			position.x += movementSpeed / 1000 * delta * (float)(Math.sin(Math.toRadians(rotation.x)) * Math.sin(Math.toRadians(rotation.y)));
			position.y += movementSpeed / 1000 * delta * (float) Math.cos(Math.toRadians(rotation.x));
		    position.z -= movementSpeed / 1000 * delta * (float)(Math.sin(Math.toRadians(rotation.x)) * Math.cos(Math.toRadians(rotation.y)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_X)){
			position.x -= movementSpeed / 1000 * delta * (float)(Math.sin(Math.toRadians(rotation.x))*Math.sin(Math.toRadians(rotation.y)));
			position.y -= movementSpeed / 1000 * delta * (float) Math.cos(Math.toRadians(rotation.x));
		    position.z += movementSpeed / 1000 * delta * (float)(Math.sin(Math.toRadians(rotation.x))*Math.cos(Math.toRadians(rotation.y)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)){
			movementSpeed += 5;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)){
			movementSpeed -= 5;
			if(movementSpeed < 0) movementSpeed = 0;
		}
	}
	
	void useViewport(){
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glRotatef(rotation.x, 1, 0, 0);
		glRotatef(rotation.y, 0, 1, 0);
	    glTranslatef(-position.x, -position.y, -position.z);
	}
}
