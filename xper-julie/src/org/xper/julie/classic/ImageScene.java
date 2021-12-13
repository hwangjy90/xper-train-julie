package org.xper.julie.classic;

import org.xper.drawing.AbstractTaskScene;
import org.xper.drawing.Context;
import org.xper.experiment.ExperimentTask;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

import org.xper.Dependency;
import org.xper.classic.vo.TrialContext;
//import org.xper.drawing.AbstractTaskScene;
//import org.xper.drawing.Context;
//import org.xper.experiment.ExperimentTask;

import org.xper.julie.drawing.Image;
import org.xper.julie.util.PngDbUtil;

//import org.lwjgl.opengl.GL11;
import org.xper.julie.drawing.ImageSpec;
//import org.xper.julie.expt.generate.PngGAParams;


//
//public class ImageScene extends AbstractTaskScene{
//	
//	JulieCircle obj = new JulieCircle();
//	
//
//	public void initGL(int w, int h) {
//		super.initGL(w, h);
//	}
//	
//	public void setTask(ExperimentTask task) {
//		obj.setSpec(task.getStimSpec());
//	}
//
//	public void drawStimulus(Context context) {
//		obj.draw(context);
//	}
//
//
//}




public class ImageScene extends AbstractTaskScene {
	
	@Dependency
	PngDbUtil dbUtil;
	
	Image image = new Image();
	ImageSpec spec = new ImageSpec();
	 
	double screenWidth;
	double screenHeight;
	
	
	public void initGL(int w, int h) {
		super.initGL(w, h);        
	}
	
	public void setTask(ExperimentTask task) {
//		if(task == null) {
//			System.out.println("PgnExptScene:setTask() ");
//		}
	}

	public void drawStimulus(Context context) {
		System.out.println("JK 0239 PngExptScene:drawStimulus()");
		image.draw(context);
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
		System.out.println("JK 0739 PngExptScene:trialStop\n\n" );
//		blankImage.draw(context);
	}
	
	public void setScreenWidth(double screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	public void setScreenHeight(double screenHeight) {
		this.screenHeight = screenHeight;
	}
	
}