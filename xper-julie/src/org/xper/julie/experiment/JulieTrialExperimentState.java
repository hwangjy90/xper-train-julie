package org.xper.julie.experiment;

import org.xper.Dependency;
import org.xper.classic.vo.SlideTrialExperimentState;

public class JulieTrialExperimentState extends SlideTrialExperimentState{
	
	@Dependency
	String xperPrintMsg;
	
	@Dependency
	String xperPrintSecondMsg;

	public String getXperPrintMsg() {
		return xperPrintMsg;
	}

	public void setXperPrintMsg(String xperPrintMsg) {
		this.xperPrintMsg = xperPrintMsg;
	}
	
	public String getXperPrintSecondMsg() {
		return xperPrintSecondMsg;
	}

	public void setXperPrintSecondMsg(String xperPrintSecondMsg) {
		this.xperPrintSecondMsg = xperPrintSecondMsg;
	}
	
	
	
}
