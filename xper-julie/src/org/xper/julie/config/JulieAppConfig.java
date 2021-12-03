package org.xper.julie.config;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.Import;
import org.springframework.config.java.annotation.Lazy;
import org.springframework.config.java.annotation.valuesource.SystemPropertiesValueSource;
import org.springframework.config.java.plugin.context.AnnotationDrivenConfig;
import org.springframework.config.java.util.DefaultScopes;
import org.xper.app.experiment.test.RandomGeneration;
import org.xper.classic.TrialEventListener;
import org.xper.config.AcqConfig;
import org.xper.config.BaseConfig;
import org.xper.config.ClassicConfig;
import org.xper.drawing.TaskScene;
import org.xper.drawing.object.BlankScreen;
import org.xper.experiment.Experiment;
import org.xper.experiment.ExperimentRunner;
import org.xper.juice.mock.NullDynamicJuice;
import org.xper.julie.classic.JulieJuiceController;
import org.xper.julie.experiment.JulieTrialExperiment;
import org.xper.julie.experiment.JulieTrialExperimentState;
import org.xper.julie.classic.RectangleScene;
import org.xper.julie.classic.RectangleSpecGenerator;

@Configuration(defaultLazy=Lazy.TRUE)
@SystemPropertiesValueSource
@AnnotationDrivenConfig
@Import(ClassicConfig.class)

public class JulieAppConfig {
	
	@Autowired ClassicConfig classicConfig;
	@Autowired BaseConfig baseConfig;
	@Autowired AcqConfig acqConfig;
	
	
	@Bean
	public RandomGeneration randomGen () {
		RandomGeneration gen = new RandomGeneration();
		gen.setDbUtil(baseConfig.dbUtil());
		gen.setGlobalTimeUtil(acqConfig.timeClient());
		gen.setTaskCount(100);
		gen.setGenerator(generator());
		return gen;
	}
	
	@Bean
	public RectangleSpecGenerator generator() {
		RectangleSpecGenerator gen = new RectangleSpecGenerator();
		return gen;
	}
	
	@Bean
	public TaskScene taskScene() {
		RectangleScene scene = new RectangleScene();
		scene.setRenderer(classicConfig.experimentGLRenderer());
		scene.setFixation(classicConfig.experimentFixationPoint());
		scene.setMarker(classicConfig.screenMarker());
		scene.setBlankScreen(new BlankScreen());
		return scene;
	}
	

	@Bean
	public ExperimentRunner experimentRunner () {
		ExperimentRunner runner = new ExperimentRunner();
		runner.setHost(classicConfig.experimentHost);
		runner.setExperiment(experiment());
		return runner;
	}
	
	@Bean
	public Experiment experiment () {
		JulieTrialExperiment xper = new JulieTrialExperiment();
		xper.setStateObject(experimentState());
		return xper;
	}
	
	
	@Bean
	public JulieTrialExperimentState experimentState () {
		JulieTrialExperimentState state = new JulieTrialExperimentState ();
		state.setLocalTimeUtil(baseConfig.localTimeUtil());
		state.setTrialEventListeners(trialEventListeners());
		state.setSlideEventListeners(classicConfig.slideEventListeners());
		state.setEyeController(classicConfig.eyeController());
		state.setExperimentEventListeners(classicConfig.experimentEventListeners());
		state.setTaskDataSource(classicConfig.taskDataSource());
		state.setTaskDoneCache(classicConfig.taskDoneCache());
		state.setGlobalTimeClient(acqConfig.timeClient());
		state.setDrawingController(classicConfig.drawingController());
		state.setInterTrialInterval(classicConfig.xperInterTrialInterval());
		state.setTimeBeforeFixationPointOn(classicConfig.xperTimeBeforeFixationPointOn());
		state.setTimeAllowedForInitialEyeIn(classicConfig.xperTimeAllowedForInitialEyeIn());
		state.setRequiredEyeInHoldTime(classicConfig.xperRequiredEyeInHoldTime());
		state.setSlidePerTrial(classicConfig.xperSlidePerTrial());
		state.setSlideLength(classicConfig.xperSlideLength());
		state.setInterSlideInterval(classicConfig.xperInterSlideInterval());
		state.setDoEmptyTask(classicConfig.xperDoEmptyTask());
		state.setSleepWhileWait(true);
		state.setPause(classicConfig.xperExperimentInitialPause());
		state.setDelayAfterTrialComplete(classicConfig.xperDelayAfterTrialComplete());
		state.setXperPrintMsg(xperPrintMsg());
		state.setXperPrintSecondMsg(xperPrintSecondMsg());
		return state;
	}
	
	@Bean (scope = DefaultScopes.PROTOTYPE)
	public List<TrialEventListener> trialEventListeners () {
		List<TrialEventListener> trialEventListener = new LinkedList<TrialEventListener>();
		trialEventListener.add(classicConfig.eyeMonitorController());
		trialEventListener.add(classicConfig.trialEventLogger());
		trialEventListener.add(classicConfig.experimentProfiler());
		trialEventListener.add(classicConfig.messageDispatcher());
		trialEventListener.add(juiceController());
		trialEventListener.add(classicConfig.trialSyncController());
		trialEventListener.add(classicConfig.dataAcqController());
		trialEventListener.add(classicConfig.jvmManager());
		if (!acqConfig.acqDriverName.equalsIgnoreCase(acqConfig.DAQ_NONE)) {
			trialEventListener.add(classicConfig.dynamicJuiceUpdater());
		}
		
		return trialEventListener;
	}
	
	@Bean
	public TrialEventListener juiceController() {
		JulieJuiceController controller = new JulieJuiceController();
		if (acqConfig.acqDriverName.equalsIgnoreCase(acqConfig.DAQ_NONE)) {
			controller.setJuice(new NullDynamicJuice());
		} else {
			controller.setJuice(classicConfig.xperDynamicJuice());
		}
		return controller;
	}
	
	@Bean(scope = DefaultScopes.PROTOTYPE)
	public String xperPrintMsg() {
		return baseConfig.systemVariableContainer().get("xper_print_msg", 0);
	}
	
	@Bean(scope = DefaultScopes.PROTOTYPE)
	public String xperPrintSecondMsg() {
		return baseConfig.systemVariableContainer().get("xper_print_second_msg", 0);
	}

}
