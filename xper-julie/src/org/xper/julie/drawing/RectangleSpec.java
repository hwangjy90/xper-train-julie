package org.xper.julie.drawing;

import com.thoughtworks.xstream.XStream;

public class RectangleSpec {
	
	double width;
	double height;
	boolean solid;
	
	transient static XStream s;
	
	static {
		s = new XStream();
		s.alias("StimSpec", RectangleSpec.class);
	}
	
	public String toXml () {
		return RectangleSpec.toXml(this);
	}
	
	public static String toXml (RectangleSpec spec) {
		return s.toXML(spec);
	}
	
	public static RectangleSpec fromXml (String xml) {
		RectangleSpec g = (RectangleSpec)s.fromXML(xml);
		return g;
	}

	
	
	
	
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	
	public boolean isSolid() {
		return solid = true;
	}
	public void setSolid(boolean solid) {
		this.solid = solid;
	}

}
