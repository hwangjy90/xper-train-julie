package org.xper.julie.util;

import java.sql.Timestamp;
import java.util.List;

import org.xper.classic.SlideEventListener;
import org.xper.classic.SlideRunner;
import org.xper.classic.TrialDrawingController;
import org.xper.classic.TrialEventListener;
import org.xper.classic.TrialRunner;
import org.xper.classic.vo.SlideTrialExperimentState;
import org.xper.classic.vo.TrialContext;
import org.xper.classic.vo.TrialExperimentState;
import org.xper.classic.vo.TrialResult;
import org.xper.experiment.ExperimentTask;
import org.xper.experiment.EyeController;
import org.xper.julie.experiment.JulieTrialExperimentState;
import org.xper.time.TimeUtil;
import org.xper.util.EventUtil;
import org.xper.util.ThreadHelper;
import org.xper.util.ThreadUtil;
import org.xper.util.TrialExperimentUtil;

public class JulieTrialExperimentUtil extends TrialExperimentUtil {
	
	public static void run(JulieTrialExperimentState state,
			ThreadHelper threadHelper, TrialRunner runner) {

		TimeUtil timeUtil = state.getLocalTimeUtil();
		try {
			threadHelper.started();
			System.out.println("JulieTrialExperiment started.");

			state.getDrawingController().init();
			EventUtil.fireExperimentStartEvent(timeUtil.currentTimeMicros(),
					state.getExperimentEventListeners());

			while (!threadHelper.isDone()) {
				pauseExperiment(state, threadHelper);
				if (threadHelper.isDone()) {
					break;
				}
				// one trial
				runner.runTrial();
				if (threadHelper.isDone()) {
					break;
				}
				// inter-trial interval
				long current = timeUtil.currentTimeMicros();
				ThreadUtil.sleepOrPinUtil(current
						+ state.getInterTrialInterval() * 1000, state,
						threadHelper);
			}
		} finally {
			// experiment stop event
			try {
				System.out.println("SlideTrialExperiment stopped.");
				EventUtil.fireExperimentStopEvent(timeUtil.currentTimeMicros(),
						state.getExperimentEventListeners());
				state.getDrawingController().destroy();

				threadHelper.stopped();
			} catch (Exception e) {
				//logger.warn(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public static TrialResult runTrial (JulieTrialExperimentState stateObject, ThreadHelper threadHelper, SlideRunner runner){
		TrialResult result = JulieTrialExperimentUtil.getMonkeyFixation(stateObject, threadHelper);
		if (result != TrialResult.FIXATION_SUCCESS) {
			return result;
		}

		result = runner.runSlide();
		if (result != TrialResult.TRIAL_COMPLETE) {
			return result;
		}

		TrialExperimentUtil.completeTrial(stateObject, threadHelper);

		return TrialResult.TRIAL_COMPLETE;
	}
	
	/**
	 * Draw the silde.
	 * 
	 * @param i slide index
	 * @param stateObject
	 * @return
	 */
	public static TrialResult doSlide (int i, JulieTrialExperimentState stateObject) {
		TrialDrawingController drawingController = stateObject.getDrawingController();
		ExperimentTask currentTask = stateObject.getCurrentTask();
		TrialContext currentContext = stateObject.getCurrentContext();
		List<? extends SlideEventListener> slideEventListeners = stateObject.getSlideEventListeners();
		EyeController eyeController = stateObject.getEyeController();
		TimeUtil timeUtil = stateObject.getLocalTimeUtil();
		
		// show current slide
		drawingController.showSlide(currentTask, currentContext);
		long slideOnLocalTime = timeUtil.currentTimeMicros();
		currentContext.setCurrentSlideOnTime(slideOnLocalTime);
		EventUtil.fireSlideOnEvent(i, slideOnLocalTime,
				slideEventListeners);

		// wait for current slide to finish
		do {
			if (!eyeController.isEyeIn()) {
				TrialExperimentUtil.breakTrial(stateObject);
				currentContext.setAnimationFrameIndex(0);
				return TrialResult.EYE_BREAK;
			}
			if (stateObject.isAnimation()) {
				currentContext.setAnimationFrameIndex(currentContext.getAnimationFrameIndex()+1);
				drawingController.animateSlide(currentTask,
						currentContext);
				/*
				if (logger.isDebugEnabled()) {
					long t = timeUtil.currentTimeMicros();
					logger.debug(new Timestamp(t/1000).toString() + " " + t % 1000 + " frame: " + currentContext.getAnimationFrameIndex());
				}
				*/
			}
		} while (timeUtil.currentTimeMicros() < slideOnLocalTime
				+ stateObject.getSlideLength() * 1000);

		// finish current slide
		drawingController.slideFinish(currentTask, currentContext);
		long slideOffLocalTime = timeUtil.currentTimeMicros();
		currentContext.setCurrentSlideOffTime(slideOffLocalTime);
		EventUtil.fireSlideOffEvent(i, slideOffLocalTime,
						currentContext.getAnimationFrameIndex(),
						slideEventListeners);
		currentContext.setAnimationFrameIndex(0);
		
		return TrialResult.SLIDE_OK;
	}
	
	
	public static TrialResult getMonkeyFixation(JulieTrialExperimentState state,
			ThreadHelper threadHelper) {
		TrialDrawingController drawingController = state.getDrawingController();
		TrialContext currentContext = state.getCurrentContext();
		TimeUtil timeUtil = state.getLocalTimeUtil();
		List<? extends TrialEventListener> trialEventListeners = state
				.getTrialEventListeners();
		EyeController eyeController = state.getEyeController();
		ExperimentTask currentTask = state.getCurrentTask();
		
		// trial init
		long trialInitLocalTime = timeUtil.currentTimeMicros();
		currentContext.setTrialInitTime(trialInitLocalTime);
		EventUtil.fireTrialInitEvent (trialInitLocalTime, trialEventListeners, currentContext);

		// trial start
		drawingController.trialStart(currentContext);
		long trialStartLocalTime = timeUtil.currentTimeMicros();
		currentContext.setTrialStartTime(trialStartLocalTime);
		EventUtil.fireTrialStartEvent(trialStartLocalTime, trialEventListeners,
				currentContext);

		// prepare fixation point
		drawingController.prepareFixationOn(currentContext);

		// time before fixation point on
		ThreadUtil.sleepOrPinUtil(trialStartLocalTime
				+ state.getTimeBeforeFixationPointOn() * 1000, state,
				threadHelper);

		// fixation point on
		drawingController.fixationOn(currentContext);
		long fixationPointOnLocalTime = timeUtil.currentTimeMicros();
		currentContext.setFixationPointOnTime(fixationPointOnLocalTime);
		System.out.println("FixationON:" + state.getXperPrintMsg());
		EventUtil.fireFixationPointOnEvent(fixationPointOnLocalTime,
				trialEventListeners, currentContext);
		
		// wait for initial eye in
		boolean success = eyeController
				.waitInitialEyeIn(fixationPointOnLocalTime
						+ state.getTimeAllowedForInitialEyeIn() * 1000);

		if (!success) {
			// eye fail to get in
			long initialEyeInFailLocalTime = timeUtil.currentTimeMicros();
			currentContext.setInitialEyeInFailTime(initialEyeInFailLocalTime);
			drawingController.initialEyeInFail(currentContext);
			EventUtil.fireInitialEyeInFailEvent(initialEyeInFailLocalTime,
					trialEventListeners, currentContext);
			return TrialResult.INITIAL_EYE_IN_FAIL;
		}

		// got initial eye in
		long eyeInitialInLoalTime = timeUtil.currentTimeMicros();
		currentContext.setInitialEyeInTime(eyeInitialInLoalTime);
		EventUtil.fireInitialEyeInSucceedEvent(eyeInitialInLoalTime,
				trialEventListeners, currentContext);

		// prepare first slide
		currentContext.setSlideIndex(0);
		currentContext.setAnimationFrameIndex(0);
		drawingController.prepareFirstSlide(currentTask, currentContext);

		// wait for eye hold
		success = eyeController.waitEyeInAndHold(eyeInitialInLoalTime
				+ state.getRequiredEyeInHoldTime() * 1000);

		if (!success) {
			// eye fail to hold
			long eyeInHoldFailLocalTime = timeUtil.currentTimeMicros();
			currentContext.setEyeInHoldFailTime(eyeInHoldFailLocalTime);
			drawingController.eyeInHoldFail(currentContext);
			
			EventUtil.fireEyeInHoldFailEvent(eyeInHoldFailLocalTime,
					trialEventListeners, currentContext);
			return TrialResult.EYE_IN_HOLD_FAIL;
		}
		// get fixation, start stimulus
				long eyeHoldSuccessLocalTime = timeUtil.currentTimeMicros();
				currentContext.setFixationSuccessTime(eyeHoldSuccessLocalTime);
				System.out.println("SUCCESS:" + state.getXperPrintSecondMsg());
				EventUtil.fireFixationSucceedEvent(eyeHoldSuccessLocalTime,
						trialEventListeners, currentContext);

				return TrialResult.FIXATION_SUCCESS;
			}
}
