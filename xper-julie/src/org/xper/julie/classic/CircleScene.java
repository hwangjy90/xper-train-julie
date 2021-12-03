package org.xper.julie.classic;

import org.xper.drawing.AbstractTaskScene;
import org.xper.drawing.Context;
import org.xper.experiment.ExperimentTask;
import org.xper.julie.drawing.JulieCircle;
import org.xper.rfplot.RFPlotGaborObject;

public class CircleScene extends AbstractTaskScene{
	
	JulieCircle obj = new JulieCircle();
	

	public void initGL(int w, int h) {
		super.initGL(w, h);
		RFPlotGaborObject.initGL();
	}
	
	public void setTask(ExperimentTask task) {
		obj.setSpec(task.getStimSpec());
	}

	public void drawStimulus(Context context) {
		obj.draw(context);
	}


}
