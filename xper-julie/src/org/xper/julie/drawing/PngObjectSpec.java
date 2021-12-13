package org.xper.julie.drawing;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.thoughtworks.xstream.XStream;

public class PngObjectSpec {
	Long id;
	String descId;
	String stimType;
	
	String gaPrefix;
	long gaRunNum;
	
	boolean doStickGen = false;
	boolean doStickMorph = false;
	boolean doBlenderMorph = false;
	boolean doControlledStickMorph = false;
	
	transient static XStream s;

	static {
		s = new XStream();
		s.alias("PngObjectSpec", PngObjectSpec.class);
	}

	public String toXml() {
		return PngObjectSpec.toXml(this);
	}

	public static String toXml(PngObjectSpec spec) {
		return s.toXML(spec);
	}

	public static PngObjectSpec fromXml(String xml) {
		PngObjectSpec bsoSpec = (PngObjectSpec)s.fromXML(xml);
		return bsoSpec;
	}

	public void writeInfo2File(String fname) {
		String outStr = this.toXml();
		try {
				BufferedWriter out = new BufferedWriter(new FileWriter(fname));
            	out.write(outStr);
	            out.flush();
	            out.close();
        } catch (Exception e) {
        		System.out.println(e);
        }
	}

	public PngObjectSpec() {}

	public PngObjectSpec(PngObjectSpec d) {
		this.id = d.id;
		this.descId = d.descId;
		this.stimType = d.stimType;
		
		this.gaPrefix = d.gaPrefix;
		this.gaRunNum = d.gaRunNum;
		
		this.doStickGen = d.doStickGen;
		this.doStickMorph = d.doStickMorph;
		
		this.doBlenderMorph = d.doBlenderMorph;
		this.doControlledStickMorph = d.doControlledStickMorph;
	}

	// getters and setters
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescId() {
		return descId;
	}
	public void setDescId(String descId) {
		this.descId = descId;
	}
	public String getStimType() {
		return stimType;
	}
	public void setStimType(String type) {
		this.stimType = type;
	}
	public String getGaPrefix() {
		return gaPrefix;
	}
	public void setGaPrefix(String prefix) {
		this.gaPrefix = prefix;
	}
	public long getGaRunNum() {
		return gaRunNum;
	}
	public void setGaRunNum(long runNum) {
		this.gaRunNum = runNum;
	}
	public boolean getDoStickGen() {
		return doStickGen;
	}
	public void setDoStickGen(boolean stickGen) {
		this.doStickGen = stickGen;
	}
	public boolean getDoStickMorph() {
		return doStickMorph;
	}
	public void setDoStickMorph(boolean stickMorph) {
		this.doStickMorph = stickMorph;
	}
	public boolean getDoBlenderMorph() {
		return doBlenderMorph;
	}
	public void setDoBlenderMorph(boolean doBlenderMorph) {
		this.doBlenderMorph = doBlenderMorph;
	}
	public boolean getDoControlledStickMorph() {
		return doControlledStickMorph;
	}
	public void setControlledStickMorph(boolean doControlledStickMorph) {
		this.doControlledStickMorph = doControlledStickMorph;
	}
	
	
}
