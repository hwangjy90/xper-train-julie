package org.xper.julie.classic;

import org.xper.experiment.StimSpecGenerator;
import org.xper.julie.drawing.RectangleSpec;

public class RectangleSpecGenerator implements StimSpecGenerator{

	public static RectangleSpec generate () {
		RectangleSpec rect = new RectangleSpec();
		rect.setWidth(Math.random()* (300-1) + 1); /* use Math.random() * (max - min)) + min */
		rect.setHeight(Math.random()* (300-1) + 1);
		return rect;	
	}	
	
	public String generateStimSpec() {
		return RectangleSpecGenerator.generate().toXml();
	}
}
