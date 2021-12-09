package org.xper.julie.drawing;

import org.xper.drawing.object.Circle;


public class JulieCircle extends Circle {
	
	CircleSpec spec;
	
	public void setSpec(String s) {
		spec = CircleSpec.fromXml(s);
		setRadius(spec.getRadius()); 
		// setRadius is from Circle.java; getRadius is from CircleSpec.java 
		setSolid(spec.isSolid());
	}

}
