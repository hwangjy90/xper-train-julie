package org.xper.julie.drawing;

import com.thoughtworks.xstream.XStream;

public class CircleSpec {

	boolean solid;
	double radius;
	
	transient static XStream s;
	
	static {
		s = new XStream();
		s.alias("StimSpec", CircleSpec.class);
	}
	
	public String toXml () {
		return CircleSpec.toXml(this);
	}
	
	public static String toXml (CircleSpec spec) {
		return s.toXML(spec);
	}
	
	public static CircleSpec fromXml (String xml) {
		CircleSpec g = (CircleSpec)s.fromXML(xml);
		return g;
	}

	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}

	public boolean isSolid() {
		return solid = true;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

}

