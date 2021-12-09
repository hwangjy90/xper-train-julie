package org.xper.julie.drawing;

import org.xper.drawing.object.Rectangle;

public class JulieRectangle extends Rectangle{

	public JulieRectangle(double width, double height) {
		super(width, height);
	}

	public JulieRectangle() {
		super(0,0);
	}

	
	RectangleSpec spec;

	public void setSpec(String s) {
		spec = RectangleSpec.fromXml(s);
		setWidth(spec.getWidth());
		setHeight(spec.getHeight());
		setSolid(spec.isSolid());
	}
	

}
