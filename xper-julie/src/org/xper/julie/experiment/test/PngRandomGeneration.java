package org.xper.julie.experiment.test;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
import java.util.List;
//import java.util.Map;
//import java.util.SortedMap;
//import javax.vecmath.Point3d;
//import java.lang.Thread;

//import org.lwjgl.util.Renderable;
import org.xper.Dependency;
//import org.xper.acq.counter.MarkEveryStepTaskSpikeDataEntry;
//import org.xper.db.vo.GenerationInfo;
import org.xper.drawing.renderer.AbstractRenderer;
//import org.xper.exception.InvalidAcqDataException;
//import org.xper.exception.NoMoreAcqDataException;
import org.xper.exception.VariableNotFoundException;
import org.xper.julie.drawing.ImageSpec;
//import org.xper.experiment.ExperimentTask;
import org.xper.julie.drawing.preview.PNGmaker;
//import org.xper.julie.expt.PngExptSpecGenerator;
//import org.xper.png.util.ExpLogMessage;
import org.xper.julie.util.PngDbUtil;
//import org.xper.png.util.PngIOUtil;
import org.xper.time.TimeUtil;


public class PngRandomGeneration {
	@Dependency
	PngDbUtil dbUtil;
	@Dependency
	TimeUtil globalTimeUtil;
	@Dependency
	AbstractRenderer renderer;			
//	@Dependency
//	PngExptSpecGenerator generator;	
	@Dependency
	int taskCount;
	
	boolean doSaveThumbnails = true;
	boolean useFakeSpikes = false;
	
	PNGmaker pngMaker;
	
	String prefix = "";
	long runNum = 1;
	long genNum = 1;
	long linNum = 0;
	List<String> specifiedPostHocStimuli = new ArrayList<String>(); 
	
	static public enum TrialType { GA };
	TrialType trialType;
	
	
	public void generate() {
		int NumImages = 8;

		ImageSpec im1 = new ImageSpec();
		
		String basepath = "/home/justin/choiceImages/img";
		String ext = ".png";
		
		System.out.print("JK 833862 PngRandomGeneration generate() ");
		long genId = 1;
		try {
			genId = dbUtil.readReadyGenerationInfo().getGenId() + 1;
		} catch (VariableNotFoundException e) {
			dbUtil.writeReadyGenerationInfo(genId, 0);
		}
		for (int i = 0; i < taskCount; i++) {
//			if (i % 10 == 0) {
//				System.out.print(".");
//			}
			im1.setFilename(basepath + Integer.toString((int)(Math.round(Math.random() * NumImages))) + ext);
		
			System.out.println(im1.toXml());
			
			long taskId = System.currentTimeMillis() * 1000L;
     		dbUtil.writeStimSpec(taskId, im1.toXml());
			dbUtil.writeTaskToDo(taskId, taskId, -1, genId);
		}
		dbUtil.updateReadyGenerationInfo(genId, taskCount);
		System.out.println("done.");
	}



//	
//	private void writeExptStart() {
//		writeExptLogMsg("START");
//	}
//	
//	private void writeExptStop() {
//		writeExptLogMsg("STOP");
//	}
//	
//	private void writeExptGenDone() {
//		writeExptLogMsg("GEN_DONE");
//	}
//	
//	private void writeExptFirstTrial(Long trialId) {
//		writeExptLogMsg("FIRST_TRIAL=" + trialId);
//		dbUtil.writeDescriptiveFirstTrial(trialId);
//	}
//	
//	private void writeExptLastTrial(Long trialId) {
//		writeExptLogMsg("LAST_TRIAL=" + trialId);
//		dbUtil.writeDescriptiveLastTrial(trialId);
//	}
//
//	
//	private void writeExptLogMsg(String status) {
//		// write ExpLog message
//		long tstamp = globalTimeUtil.currentTimeMicros();
//		ExpLogMessage msg = new ExpLogMessage(status,trialType.toString(),prefix,runNum,genNum,tstamp);
//		dbUtil.writeExpLog(tstamp,ExpLogMessage.toXml(msg));
//	}
//	
	// ---------------------------
	// ---- Getters & Setters ----
	// ---------------------------
	
	public PngDbUtil getDbUtil() {
		return dbUtil;
	}

	public void setDbUtil(PngDbUtil dbUtil) {
		this.dbUtil = dbUtil;
		pngMaker = new PNGmaker(dbUtil);
	}

	public TimeUtil getGlobalTimeUtil() {
		return globalTimeUtil;
	}

	public void setGlobalTimeUtil(TimeUtil globalTimeUtil) {
		this.globalTimeUtil = globalTimeUtil;
	}

//	public PngExptSpecGenerator getGenerator() {
//		return generator;
//	}
//
//	public void setGenerator(PngExptSpecGenerator generator) {
//		this.generator = generator;
//	}
	
	public AbstractRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(AbstractRenderer renderer) {
		this.renderer = renderer;
	}
	
	public int getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	
	//public static void main(String[] args) {
	//	CreateDbDataSource cdbs = new CreateDbDataSource();
	//	DataSource ds = cdbs.getDataSource();
	//	PngDbUtil dbUtil = new PngDbUtil(ds);
	//	
	//	PngRandomGeneration prg = new PngRandomGeneration();
	//	prg.setDbUtil(dbUtil);
	//	
	//	prg.getPrefix();
	//	prg.getRunNum();
	//	prg.getGenNum();
	//	prg.getLinNum();
	//	
	//	prg.getSpikeResponses();
	//}
}
