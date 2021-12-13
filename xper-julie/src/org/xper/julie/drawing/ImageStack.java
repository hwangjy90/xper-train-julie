package org.xper.julie.drawing;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.xper.drawing.Context;
import org.xper.drawing.Drawable;
import org.xper.julie.drawing.preview.DrawingManager;
//import org.xper.png.expt.generate.PngGAParams;

public class ImageStack implements Drawable {

	private static final int BYTES_PER_PIXEL = 3; //3 for RGB, 4 for RGBA

	int numFrames = 1;
    IntBuffer textureIds;
    boolean texturesLoaded = false;
    int frameNum = 0;
    float scaler = 1.0f; // scales actual image size to viewport size
   
    HashMap<String, Integer> nameMap = new HashMap<String, Integer>();
    List<Integer> textureList = new ArrayList<Integer>();	
    int div = 1;
    
    // this probably should from the database 
    String ext = ".png"; 
    String imageName;
    String baseName;
    
    // keep track of which images go with with stim by storing the last image ndx for 
    // each stim (3)
    int currNdx = 0;
    int[] stopNdx = {0, 0, 0};
    
	double screenWidth;
	double screenHeight;
	
	// timing ...
	long start;
   	
	// the list of filenames to load.  
    public void loadImages(List<Map<String, Object>> stimInfo){    
    	start = System.nanoTime();    	
  
    	String baseName;
//    	String stimType;
    	String ext = ".png";

    	List<String> fullFilenames = new ArrayList<String>();
    	
    	// determine how many frames for this trial while building filename(s) and loading the Texture
    	numFrames = 0;
    	
    	nameMap.clear();
    	textureList.clear();

    	for(Map<String, Object>si : stimInfo) {
//    		stimType = (String)si.get("stimType");
    		baseName = (String)si.get("descId");
    		imageName = baseName + ext;
    		fullFilenames.add(imageName);    
      		}
    	
		 
		//this is important, it resizes the textureIds IntBuffer
		setNumFrames(numFrames);
		frameNum = 0;

    	GL11.glGenTextures(textureIds); 
    	
		int n = 0;
			    	
		// for each filename, 
		for(String str : fullFilenames) {
			loadTexture(str, n++);
		}
	
    	// assume success?!
    	texturesLoaded = true;
    }
	
    
    // call this before loadTexture and after setNumFrames
    public void genTextures() {
    	GL11.glGenTextures(textureIds); 

    }
    

    public int loadTexture(String pathname, int textureIndex) {

    	// JK quick hack for image names etc
//    	int n = textureIndex % 5;
//    	String filename = String.format("%d.png", n);
    	
//    	pathname = "/home/justin/jkcode/ConnorLab/Alice/images/"  + filename;
//    	System.out.println("JK 82273 ImageStack:loadTexture()  " + pathname);

    	// if it's been used before, just retrieve the index and add it to the list
 	    // otherwise, add the name, index pair to the map and call loadTexture() 
    	if(nameMap.containsKey(pathname)) {
			textureList.add(nameMap.get(pathname));   // reuse the (previously loaded) texture
			
			//System.out.println("JK 8273 ImageStack:loadTexture()  reusing " + pathname + " : " + textureIndex + 
    		//		" textureIds = " + textureIds.get(textureIndex));  
			
			return textureIds.get(textureIndex);      // and return
		} else {
			nameMap.put(pathname, textureIndex);                 // otherwise, add it and load it
			textureList.add(textureIndex);
		}
    	
    	
    	try {
    		File imageFile = new File(pathname);
    		BufferedImage img = ImageIO.read(imageFile);

    		byte[] src = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();

    		// reorder the 4 bytes per pixel data  
//abgr2rgba(src);
			bgr2rgb(src);

    		ByteBuffer pixels = (ByteBuffer)BufferUtils.createByteBuffer(src.length).put(src, 0x00000000, src.length).flip();

    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIds.get(textureIndex));

    		// from http://wiki.lwjgl.org/index.php?title=Multi-Texturing_with_GLSL
    		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
    		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);

    		if(pixels.remaining() % 3 == 0) {
    			// only for RGB
    		 	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, img.getWidth(), img.getHeight(), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, pixels);
    		} else {
    			// RGBA
    			GL11.glTexImage2D( GL11.GL_TEXTURE_2D, 0,  GL11.GL_RGBA8, img.getWidth(), img.getHeight(), 0,  GL11.GL_RGBA,  GL11.GL_UNSIGNED_BYTE, pixels);
    		}
    		   
    		System.out.println("JK 5353 ImageStack:loadTexture() " + imageFile + " : " + textureIndex + 
    			   				" textureIds = " + textureIds.get(textureIndex));    		

    		return textureIds.get(textureIndex);


    	} catch(IOException e) {
    		e.printStackTrace();
    		System.out.println("ImageStack::loadTexture() : path is : " + pathname);
    		throw new RuntimeException(e);
    	}
    }
    
	@Override
	public void draw(Context context) {
		System.out.println("JK 9587 ImageStack::draw()");
		
		// JK 2981  18 July 2018 
		float width = (float) screenWidth  / scaler; //  2    // texture.getImageWidth();
		float height = (float) screenHeight / scaler; //  2    // texture.getImageHeight();		
		float yOffset = -height / 2;
		float xOffset = -width / 2;

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);  	
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIds.get(textureList.get(frameNum)));

        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(xOffset, yOffset);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(xOffset + width, yOffset);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(xOffset + width, yOffset + height);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(xOffset, yOffset + height);
        GL11.glEnd();
     
        GL11.glDisable(GL11.GL_TEXTURE_2D);
  
        if(frameNum < numFrames - 1){
        	frameNum += 1;
        } 
	}

	
	public void cleanUp() {
		
		for(int i = 0; i < numFrames; i++) {
			GL11.glDeleteTextures(textureIds.get(i));
		}
		
		// clear intbuffer
		textureIds.clear();
				
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		if(true){
			testImage();
			return;
		}
	}

	public void setNumFrames(int numImgs) {
		this.numFrames = numImgs;
		textureIds = BufferUtils.createIntBuffer(this.numFrames);	
	}
	
	
    
//    public void loadFrames(String baseFilename){
//        
//    	textureIds.clear();    	
//    	GL11.glGenTextures(textureIds);
//    	
//    	for(int n = 0; n < numFrames; n++){
//    		if(n <  10){
//    			imageName = PngGAParams.resourcePath + baseFilename + Integer.toString(n) + ext;
//    		} else {
//    			imageName = PngGAParams.resourcePath + baseFilename + ext;
//    		}		
//    		loadTexture(imageName, n);    		
//    	}       	
//    	// assume success?!
//    	texturesLoaded = true;    	
//    }
    
    

    
    

    // use this to reorder the RGB bytes 
    void bgr2rgb(byte[] target) {
    	byte tmp;

    	for(int i=0x00000000; i<target.length; i+=0x00000003) {
    		tmp = target[i];
    		target[i] = target[i+0x00000002];
    		target[i+0x00000002] = tmp;
    	}
    }
    
    
    // repack abgr to rgba    
    void abgr2rgba(byte[] target) {
    	byte tmpAlphaVal;
    	byte tmpBlueVal;
    	
    	for(int i=0x00000000; i<target.length; i+=0x00000004) {
    		tmpAlphaVal = target[i];
    		target[i] = target[i+0x00000003];
    		tmpBlueVal = target[i+0x00000001];
    		target[i+0x00000001] = target[i+0x00000002];
    		target[i+0x00000002] = tmpBlueVal;
    		target[i+0x00000003] = tmpAlphaVal;
    	}
    }

    

	
		public void setBaseName(String baseFilename){
			baseName  = baseFilename;
		}


		public void setFrameNum(int newFrameNum) {
			if(newFrameNum < numFrames && newFrameNum >= 0) {
				frameNum = newFrameNum;
			}else {
				System.out.println("ImageStack:setFrameNum() : newFrameNum is not right : " +  newFrameNum); 
			}
		}
		
		public void setScreenWidth(double screenWidth) {
			this.screenWidth = screenWidth/2;
		}
		
		public void setScreenHeight(double screenHeight) {
			this.screenHeight = screenHeight;
		}
		

		public static int loadTexture(BufferedImage image){

			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

			ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

			for(int y = 0; y < image.getHeight(); y++){
				for(int x = 0; x < image.getWidth(); x++){
					int pixel = pixels[y * image.getWidth() + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
					buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
					buffer.put((byte) (pixel & 0xFF));               // Blue component
					buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
				}
			}

			buffer.flip(); // DO NOT FORGET THIS

			// You now have a ByteBumffer filled with the color data of each pixel.
			// Now just create a texture ID and bind it. Then you can load it using 
			// whatever OpenGL method you want, for example:

			int textureID =  GL11.glGenTextures(); //Generate texture ID
			GL11.glBindTexture( GL11.GL_TEXTURE_2D, textureID); //Bind texture ID

			//Setup wrap mode
			GL11.glTexParameteri( GL11.GL_TEXTURE_2D,  GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri( GL11.GL_TEXTURE_2D,  GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

			//Setup texture scaling filtering
			GL11.glTexParameteri( GL11.GL_TEXTURE_2D,  GL11.GL_TEXTURE_MIN_FILTER,  GL11.GL_NEAREST);
			GL11.glTexParameteri( GL11.GL_TEXTURE_2D,  GL11.GL_TEXTURE_MAG_FILTER,  GL11.GL_NEAREST);

			//Send texel data to OpenGL
			// JK 10 July 2018 	RGBA vs RGB		
			GL11.glTexImage2D( GL11.GL_TEXTURE_2D, 0,  GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0,  GL11.GL_RGBA,  GL11.GL_UNSIGNED_BYTE, buffer);
//    		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, image.getWidth(), image.getHeight(), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);

			//Return the texture ID so we can bind it later again
			return textureID;
		}


		
		public static BufferedImage loadImage(String loc){
			try {
				return ImageIO.read(ImageStack.class.getResource(loc));
			} catch (IOException e) {
				//Error Handling Here
			}
			return null;
		}	
		
		

		public static void testImage(){
			String resourcePath = "/home/justin/jkcode/ConnorLab/Alice/images/"; 
			String ext = ".png"; // ".png";  // 
			String baseFilename = "";  //		
			String fullFilename = resourcePath + baseFilename + ext;
			int screenHeightmm = 1440/1;
			int screenWidthmm = 2560/1;			
		    int numImages = 4;    
			DrawingManager testWindow = new DrawingManager(screenHeightmm, screenWidthmm);
			List<ImageStack> imageStacks = new ArrayList<ImageStack>();
			IntBuffer textureIds = BufferUtils.createIntBuffer(numImages);	
			GL11.glGenTextures(textureIds);
					
			for(int i = 0; i < numImages; i++){
				ImageStack s = new ImageStack();
				s.setScreenHeight(screenHeightmm);
				s.setScreenWidth(screenWidthmm);
	   			s.setNumFrames(1);
	   			
       			fullFilename = resourcePath + baseFilename + Integer.toString(i) + ext;
       			s.loadTexture(fullFilename, i);
       			imageStacks.add(s);  // add object to be drawn
			}
			testWindow.setStimObjs(imageStacks);
			testWindow.drawStimuli();
			
		}
		

		
}
		
		
		

