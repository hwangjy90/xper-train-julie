package org.xper.julie.classic;

import org.xper.experiment.StimSpecGenerator;
import org.xper.julie.drawing.CircleSpec;

public class CircleSpecGenerator implements StimSpecGenerator{

	public static CircleSpec generate () {
		CircleSpec circ = new CircleSpec();
		circ.setRadius(Math.random()* (300-1) + 1); 
		/* setRadius from CircleSpec */
		/* use Math.random() * (max - min)) + min */
		return circ;	
	}	
	
	public String generateStimSpec() {
		return CircleSpecGenerator.generate().toXml();
	}
}

