package org.xper.julie.classic;

import org.xper.drawing.AbstractTaskScene;
import org.xper.drawing.Context;
import org.xper.drawing.object.Rectangle;
import org.xper.experiment.ExperimentTask;
import org.xper.julie.drawing.JulieRectangle;
import org.xper.rfplot.RFPlotGaborObject;

public class RectangleScene extends AbstractTaskScene{
	
	JulieRectangle obj = new JulieRectangle();

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
