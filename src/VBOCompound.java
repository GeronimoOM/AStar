import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.*;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;

public class VBOCompound {
	private int size = 0;
	private int textureId = 0;
	private int cellSize;
	private int VertexVBOId = 0;
	private int TextureVBOId = 0;
	private float[] vertices;
	private float[] textures;
	//true for dynamic, false for static
	boolean dynamicUsage = false;

	public VBOCompound(Texture t, int cellSize){
		textureId = t.getTextureID();
		this.cellSize = cellSize;
		vertices = new float[0];
		textures = new float[0];
	}
	
	public VBOCompound(Texture t, int cellSize, int x, int y){
		this(t, cellSize);
		dynamicUsage = true;
		size = x * y;
		vertices = new float[size * 18];
		textures = new float[size * 12];
		
	    for(int i = 0; i < x; i++){
	    	for(int j = 0; j < y; j++){
	        	vertices[18 * (y * i + j)] = i * cellSize;
        		vertices[18 * (y * i + j) + 1] = j * cellSize;
        		vertices[18 * (y * i + j) + 2] = 0;
        		vertices[18 * (y * i + j) + 3] = (i + 1) * cellSize;
        		vertices[18 * (y * i + j) + 4] = j * cellSize;
        		vertices[18 * (y * i + j) + 5] = 0;
        		vertices[18 * (y * i + j) + 6] = i * cellSize;
        		vertices[18 * (y * i + j) + 7] = (j + 1) * cellSize;
        		vertices[18 * (y * i + j) + 8] = 0;
        		vertices[18 * (y * i + j) + 9] = i * cellSize;
        		vertices[18 * (y * i + j) + 10] = (j + 1) * cellSize;
        		vertices[18 * (y * i + j) + 11] = 0;
        		vertices[18 * (y * i + j) + 12] = (i + 1) * cellSize;
        		vertices[18 * (y * i + j) + 13] = j * cellSize;
        		vertices[18 * (y * i + j) + 14] = 0;
        		vertices[18 * (y * i + j) + 15] = (i + 1) * cellSize;
        		vertices[18 * (y * i + j) + 16] = (j + 1) * cellSize;
        		vertices[18 * (y * i + j) + 17] = 0;
        	}
        }
	   
	    for(int k = 0; k < x * y; k++){
			textures[12 * k] = 0;
			textures[12 * k + 1] = 0;
			textures[12 * k + 2] = 1;
			textures[12 * k + 3] = 0;
			textures[12 * k + 4] = 0;
			textures[12 * k + 5] = 1;
			textures[12 * k + 6] = 0;
			textures[12 * k + 7] = 1;
			textures[12 * k + 8] = 1;
			textures[12 * k + 9] = 0;
			textures[12 * k + 10] = 1;
			textures[12 * k + 11] = 1;
	    }
   
        bindBufferData();
	}
	
	public void setTexture(Texture t){
		textureId = t.getTextureID();
	}
	
	public void add(int i, int j){
		float[] newVertices = new float[(size + 1) * 18];
	    for(int k = 0; k < size * 18; k++) newVertices[k] = vertices[k];
	    newVertices[size * 18] = i * cellSize;
	    newVertices[size * 18 + 1] = j * cellSize;
	    newVertices[size * 18 + 2] = 0;
	    newVertices[size * 18 + 3] = (i + 1) * cellSize;
	    newVertices[size * 18 + 4] = j * cellSize;
	    newVertices[size * 18 + 5] = 0;
	    newVertices[size * 18 + 6] = i * cellSize;
	    newVertices[size * 18 + 7] = (j + 1) * cellSize;
	    newVertices[size * 18 + 8] = 0;
		newVertices[size * 18 + 9] = i * cellSize;
		newVertices[size * 18 + 10] = (j + 1) * cellSize;
		newVertices[size * 18 + 11] = 0;
		newVertices[size * 18 + 12] = (i + 1) * cellSize;
		newVertices[size * 18 + 13] = j * cellSize;
		newVertices[size * 18 + 14] = 0;
		newVertices[size * 18 + 15] = (i + 1) * cellSize;
		newVertices[size * 18 + 16] = (j + 1) * cellSize;
		newVertices[size * 18 + 17] = 0;
		vertices = newVertices;

	    textures = new float[(size + 1) * 12];
	    for(int k = 0; k < size + 1; k++){
	    	textures[12 * k] = 0;
	    	textures[12 * k + 1] = 0;
	    	textures[12 * k + 2] = 1;
	    	textures[12 * k + 3] = 0;
	    	textures[12 * k + 4] = 0;
	    	textures[12 * k + 5] = 1;
	    	textures[12 * k + 6] = 0;
	    	textures[12 * k + 7] = 1;
	    	textures[12 * k + 8] = 1;
	    	textures[12 * k + 9] = 0;
	    	textures[12 * k + 10] = 1;
	    	textures[12 * k + 11] = 1;
	    }

        size++;
        
        bindBufferData();
        
	}
	
	public void bindBufferData(){	
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(size * 18);
		vertexData.put(vertices);
		vertexData.flip();
		
		VertexVBOId = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, VertexVBOId);
        if(dynamicUsage) glBufferData(GL_ARRAY_BUFFER, vertexData, GL_DYNAMIC_DRAW);
        else glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        FloatBuffer textureData = BufferUtils.createFloatBuffer(size * 12);
        textureData.put(textures);
        textureData.flip();
        
		TextureVBOId = glGenBuffers();
        
        glBindBuffer(GL_ARRAY_BUFFER, TextureVBOId);
        if(dynamicUsage) glBufferData(GL_ARRAY_BUFFER, textureData, GL_DYNAMIC_DRAW);
        else glBufferData(GL_ARRAY_BUFFER, textureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

	}
	
	public void bindTexture(){
		glBindTexture(GL_TEXTURE_2D, textureId);
	}
	
	public void useBuffers(){
    	glBindBuffer(GL_ARRAY_BUFFER, VertexVBOId);
    	glVertexPointer(3, GL_FLOAT, 0, 0L);
    	
		glBindBuffer(GL_ARRAY_BUFFER, TextureVBOId);
    	glTexCoordPointer(2, GL_FLOAT, 0, 0L);

	}
	
	public void draw(){
		if(size > 0){
			bindTexture();
			useBuffers();
			glDrawArrays(GL_TRIANGLES, 0, size * 6);
		}
	}

	
}
