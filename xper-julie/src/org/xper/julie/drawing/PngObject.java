package org.xper.julie.drawing;

import org.xper.drawing.Context;
import org.xper.drawing.Drawable;
import org.xper.julie.drawing.stick.MStickSpec;
import org.xper.julie.drawing.stick.MatchStick;

public class PngObject implements Drawable {
	Long id;
	String descId;
	String stimType;
	
	PngObjectSpec pngSpec;
	MStickSpec stickSpec;
	
	MatchStick stick;
		
	@Override
	public void draw(Context context) {
		stick.drawSkeleton();
	}
	
	public void finalizeObject() {
		stick = new MatchStick();
		if (pngSpec.doStickGen) {
			stick.genMatchStickRand();
			stickSpec = new MStickSpec();
			stickSpec.setMStickInfo(stick);
		} else if (pngSpec.doStickMorph) {
			stick.genMatchStickFromShapeSpec(stickSpec);
			stick.mutate(0);
			stickSpec.setMStickInfo(stick);
		} else if (pngSpec.doControlledStickMorph) {
			stick.genMatchStickFromShapeSpec(stickSpec);
//			stickSpec.setMStickInfo(stick); //#######
		} else { // if (pngSpec.doBlenderMorph) {
			stick.genMatchStickFromShapeSpec(stickSpec);
			
		}
	}
	
	public void finalizeObject(int profile, int limb) {
		stick = new MatchStick();
		if (pngSpec.doControlledStickMorph) {
			stick.genMatchStickFromShapeSpec(stickSpec);
			stick.changeRadProfile(profile,limb);
			stickSpec.setMStickInfo(stick);
		}
	}
	
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
	public PngObjectSpec getSpec_java() {
		return pngSpec;
	}
	public void setSpec_java(PngObjectSpec spec) {
		this.pngSpec = spec;
	}
	public MStickSpec getSpec_stick() {
		return stickSpec;
	}
	public void setSpec_stick(MStickSpec spec) {
		this.stickSpec = spec;
	}
	
	public MatchStick getStick() {
		return stick;
	}


}
