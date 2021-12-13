package org.xper.julie.experiment.test;

import org.springframework.config.java.context.JavaConfigApplicationContext;
//import org.xper.julie.experiment.test.PngRandomGeneration;
import org.xper.util.FileUtil;

public class JulieRandGen {
	public static void main(String[] args) {
		JavaConfigApplicationContext context = new JavaConfigApplicationContext(
				FileUtil.loadConfigClass("experiment.config_class"));

		PngRandomGeneration gen = context.getBean(PngRandomGeneration.class);
		gen.setTaskCount(100);
		gen.generate();
	}
}
