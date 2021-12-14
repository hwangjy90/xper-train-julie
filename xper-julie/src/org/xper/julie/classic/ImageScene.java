package org.xper.julie.classic;

import org.xper.drawing.AbstractTaskScene;
import org.xper.drawing.Context;
import org.xper.drawing.Drawable;
import org.xper.experiment.ExperimentTask;
import org.lwjgl.opengl.GL11;
import org.xper.Dependency;
import org.xper.classic.vo.TrialContext;
import org.xper.julie.drawing.Image;
import org.xper.julie.util.PngDbUtil;
import org.xper.julie.drawing.ImageSpec;

public class ImageScene extends AbstractTaskScene {
	
	@Dependency
	PngDbUtil dbUtil;
	
	Image image = new Image();
	ImageSpec spec = new ImageSpec();
	 
	double screenWidth;
	double screenHeight;
	
	
	public void initGL(int w, int h) {
		super.initGL(w, h);        
		System.out.println("JK 5311 ImageScene::initGL() " + w + ", " + h);
	}
	
	public void setTask(ExperimentTask task) {
//		if(task == null) {
			System.out.println("ImageScene:setTask() ");
//		}
	}

	public void drawStimulus(Context context) {
		System.out.println("JK 0239 ImageScene:drawStimulus()");
		image.draw(context);
	}

	public void drawTask(Context context, final boolean fixationOn) {
		System.out.println("JK 54523 ImageScene::drawTask()");
		// clear the whole screen before define view ports in renderer
		blankScreen.draw(null);
		renderer.draw(new Drawable() {
			public void draw(Context context) {
				if (useStencil) {
					// 0 will pass for stimulus region
					GL11.glStencilFunc(GL11.GL_EQUAL, 0, 1);
				}
				drawStimulus(context);
				if (useStencil) {
					// 1 will pass for fixation and marker regions
					GL11.glStencilFunc(GL11.GL_EQUAL, 1, 1);
				}
				
				if (fixationOn) {
					 fixation.draw(context);
				}
				marker.draw(context);
				if (useStencil) {
					// 0 will pass for stimulus region
					GL11.glStencilFunc(GL11.GL_EQUAL, 0, 1);
				}
			}}, context);
	}
	
	public PngDbUtil getDbUtil() {
		return dbUtil;
	}

	public void setDbUtil(PngDbUtil dbUtil) {
		this.dbUtil = dbUtil;
	}
	
	
	public void trialStart(TrialContext context) {
		spec = ImageSpec.fromXml(context.getCurrentTask().getStimSpec());

		System.out.println("\nJK 55639 ImageScene:trialStart : " + spec.getFilename()); 
		image.loadTexture(spec.getFilename());
	}


	public void trialStop(TrialContext context) {
//		images.cleanUp();
//		blankImage.setFrameNum(0);
		System.out.println("JK 0739 ImageScene:trialStop\n\n" );
//		blankImage.draw(context);
	}
	
	public void setScreenWidth(double screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	public void setScreenHeight(double screenHeight) {
		this.screenHeight = screenHeight;
	}
	
}