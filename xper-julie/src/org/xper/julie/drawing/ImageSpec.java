package org.xper.julie.drawing;

import com.thoughtworks.xstream.XStream;

public class ImageSpec {
	String filename;
	
	transient static XStream s;
	
	static {
		s = new XStream();
		s.alias("StimSpec", ImageSpec.class);
		//s.useAttributeFor("animation", boolean.class);
	}
	
	public String toXml () {
		return ImageSpec.toXml(this);
	}
	
	public static String toXml (ImageSpec spec) {
		return s.toXML(spec);
	}
	
	public static ImageSpec fromXml (String xml) {
		ImageSpec imgSpec = (ImageSpec)s.fromXML(xml);
		return imgSpec;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public  void setFilename(String fname) {
		filename = fname;
	}
	
	
	public ImageSpec() {}
	
	public ImageSpec(ImageSpec spec) {
		filename = spec.filename;
	}
	
}