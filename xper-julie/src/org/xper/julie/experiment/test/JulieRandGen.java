package org.xper.julie.experiment.test;

import org.springframework.config.java.context.JavaConfigApplicationContext;
import org.xper.app.experiment.test.RandomGeneration;
import org.xper.util.FileUtil;

public class JulieRandGen {
	public static void main(String[] args) {
		
		JavaConfigApplicationContext context = new JavaConfigApplicationContext(
				FileUtil.loadConfigClass("experiment.config_class"));

		RandomGeneration gen = context.getBean(RandomGeneration.class);
		gen.generate();
	}
}
