package org.xper.julie.util;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.xper.db.vo.GenerationTaskDoneList;
import org.xper.db.vo.StimSpecEntry;
import org.xper.db.vo.TaskDoneEntry;
import org.xper.util.DbUtil;


public class PngDbUtil extends DbUtil {
	public PngDbUtil() {	
		super();
	}
	
	public PngDbUtil(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	

	
	
	
	public long getStimIdByTaskId(long paramLong) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);
		Map<String, Object> localMap = jt.queryForMap(" select t.stim_id as id from TaskToDo t where t.task_id = ?", new Object[] { new Long(paramLong) });
		
		long l = ((Long)localMap.get("id")).longValue();
		return l;
	}
	
	public void writeStimObjData(long id, String descId, String javaspec, String mstickspec, String blenderspec, String dataspec) {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		jt.update("insert into StimObjData (id, descId, javaspec, mstickspec, blenderspec, dataspec) values (?, ?, ?, ?, ?, ?)", 
				new Object[] { id, descId,  javaspec, mstickspec, blenderspec, dataspec });
	}
	
	public void updateStimObjData(long id, String data) {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		jt.update("update StimObjData set dataspec = ? where id = ?", 
				new Object[] { data, id });
	}
	
	public StimSpecEntry readStimSpec_java(long stimObjId) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);
		return jt.queryForObject(
				" select id, javaspec from StimObjData where id = ? ", 
				new ParameterizedRowMapper<StimSpecEntry> () {
					public StimSpecEntry mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						StimSpecEntry ent = new StimSpecEntry();

						ent.setStimId(rs.getLong("id")); 
						ent.setSpec(rs.getString("javaspec")); 

						return ent;
					}},
				stimObjId);
	}

	public StimSpecEntry readStimSpec_stick(long stimObjId) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);
		return jt.queryForObject(
				" select id, mstickspec from StimObjData where id = ? ", 
				new ParameterizedRowMapper<StimSpecEntry> () {
					public StimSpecEntry mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						StimSpecEntry ent = new StimSpecEntry();

						ent.setStimId(rs.getLong("id")); 
						ent.setSpec(rs.getString("mstickspec")); 

						return ent;
					}},
				stimObjId);
	}
	
	public StimSpecEntry readStimSpec_blender(long stimObjId) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);
		return jt.queryForObject(
				" select id, blenderspec from StimObjData where id = ? ", 
				new ParameterizedRowMapper<StimSpecEntry> () {
					public StimSpecEntry mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						StimSpecEntry ent = new StimSpecEntry();

						ent.setStimId(rs.getLong("id")); 
						ent.setSpec(rs.getString("blenderspec")); 

						return ent;
					}},
				stimObjId);
	}
	
	public StimSpecEntry readStimSpec_blender(String descId) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);
		return jt.queryForObject(
				" select id, blenderspec from StimObjData where descId = ? ", 
				new ParameterizedRowMapper<StimSpecEntry> () {
					public StimSpecEntry mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						StimSpecEntry ent = new StimSpecEntry();

						ent.setStimId(rs.getLong("id")); 
						ent.setSpec(rs.getString("blenderspec")); 

						return ent;
					}},
				descId);
	}
	
	public StimSpecEntry readStimSpec_data(long stimObjId) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);
		return jt.queryForObject(
				" select id, dataspec from StimObjData where id = ? ", 
				new ParameterizedRowMapper<StimSpecEntry> () {
					public StimSpecEntry mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						StimSpecEntry ent = new StimSpecEntry();

						ent.setStimId(rs.getLong("id")); 
						ent.setSpec(rs.getString("dataspec")); 

						return ent;
					}},
				stimObjId);
	}
	
	public long readStimObjIdFromDescriptiveId(String descriptiveId) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);
		return jt.queryForLong("SELECT id from StimObjData where descId = ?", new Object[] { new String(descriptiveId) });
	}
	
	public String readDescriptiveIdFromStimObjId(long stimObjId) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);
		return jt.queryForObject("SELECT descId from StimObjData where id = ?",String.class, stimObjId);
	}
	
//	JK 20 July 2018
	public Map<String, Object> readDescriptiveIdAndTypeFromStimObjId(long stimObjId) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);
		return jt.queryForMap("SELECT descId as descId, EXTRACTVALUE(javaspec, '/PngObjectSpec/stimType') as stimType " +
								 "FROM StimObjData where id = ?", stimObjId);
	
	}
	
//	SELECT  extractvalue(spec, '/StimSpec/object')
//	FROM StimSpec
//	where id = 1532619299807779
	
	
	
// JK 26 July 2018
public List<Long> readAllStimObjIdsByTask(long taskId) {
		
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		
		final List<Long> allIds = new ArrayList<Long>();
				
		jt.query(
				" SELECT EXTRACTVALUE(spec, '/StimSpec/object') as objStr " +
			    " FROM StimSpec where id = ?", 
				new Object[] { taskId },
				new RowCallbackHandler() {
					public void processRow(ResultSet rs) throws SQLException {
						String[] strs;
						strs = (rs.getString("objStr")).split(" ");
						
						for(String s : strs) {							
							allIds.add(Long.parseLong(s));
						}
					}});
		return allIds;
	}
	
//
// FROM alexandriya_180218_test.StimObjData
//# where id = 1532369879621234
//order by id desc;


//JK 26 July 2018
public List<String> readAllStimTypesByTask(long taskId) {
		
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		final List<String> stimTypes = new ArrayList<String>();
		final List<Long> allIds = readAllStimObjIdsByTask(taskId);
		
		for(Long id : allIds) {
		jt.query(
				" SELECT EXTRACTVALUE(javaspec, '/PngObjectSpec/stimType') as stimStr " +
			    " FROM StimObjData where id = ?", 
				new Object[] { id },
				new RowCallbackHandler() {
					public void processRow(ResultSet rs) throws SQLException {
						stimTypes.add(rs.getString("stimStr"));
					}});
		}
		return stimTypes;
	}

//
//	public List<long> readStimObjIds(long taskId) {
//		final ArrayList<long> result = new ArrayList<long>();
//
//		JdbcTemplate jt = new JdbcTemplate(dataSource);
//		jt.query(
//				" SELECT EXTRACTVALUE(spec, '/StimSpec/object') " +
//				" from RFInfo " +
//				" where tstamp >= ? and tstamp <= ? " + 
//				" order by tstamp ", 
//				new Object[] {startTime, stopTime },
//				new RowCallbackHandler() {
//					public void processRow(ResultSet rs) throws SQLException {
//						RFInfoEntry ent = new RFInfoEntry();
//						ent.setTstamp(rs.getLong("tstamp")); 
//						ent.setInfo(rs.getString("info")); 
//						result.add(ent);
//					}				
//				});
//		return result;
//	}

	
	
	public int readRenderStatus(String prefix, long runNum, long genNum, long linNum) { //#####!
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		int renderStatus = jt.queryForInt("SELECT rendersFinished FROM DescriptiveInfo WHERE prefix = ? "
				+ "AND gaRun = ? AND genNum = ? AND linNum = ?", new Object[] { prefix, runNum, genNum, linNum }); //#####!
		return renderStatus;
	}
	
	public String readCurrentDescriptivePrefix() {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		long tstamp = jt.queryForLong("SELECT max(tstamp) FROM DescriptiveInfo");
		int gaRun = jt.queryForInt("SELECT gaRun FROM DescriptiveInfo WHERE tstamp = ? ", new Object[] { new Long(tstamp) });
		Long cEI = jt.queryForLong("SELECT prefix FROM DescriptiveInfo WHERE tstamp = ? ", new Object[] { new Long(tstamp) });
		
		return new String(cEI.toString() + "_r-" + gaRun);
	}
	
	public String readCurrentDescriptivePrefixAndGen() {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		long tstamp = jt.queryForLong("SELECT max(tstamp) FROM DescriptiveInfo");
		int gaRun = jt.queryForInt("SELECT gaRun FROM DescriptiveInfo WHERE tstamp = ? ", new Object[] { new Long(tstamp) });
		Long cEI = jt.queryForLong("SELECT prefix FROM DescriptiveInfo WHERE tstamp = ? ", new Object[] { new Long(tstamp) });
		Long genNum = jt.queryForLong("SELECT genNum FROM DescriptiveInfo WHERE tstamp = ? ", new Object[] { new Long(tstamp) });
		
		return new String(cEI.toString() + "_r-" + gaRun + "_g-" + genNum);
	}
	
	public void writeCurrentDescriptivePrefixAndGen(long tstamp, String prefix, long gaRun, long genNum, long linNum) { //#####!
        JdbcTemplate jt = new JdbcTemplate(dataSource);
         
        jt.update("insert into DescriptiveInfo (tstamp, prefix, gaRun, genNum, linNum) values (?, ?, ?, ?, ?)", 
				new Object[] { tstamp, prefix, gaRun, genNum, linNum });
    }
	
	public void writeCurrentDescriptivePrefixAndGen(long tstamp, String prefix, long gaRun, long genNum, long linNum, long firstTrial, long lastTrial) { //#####!
        JdbcTemplate jt = new JdbcTemplate(dataSource);
         
        jt.update("insert into DescriptiveInfo (tstamp, prefix, gaRun, genNum, linNum, firstTrial, lastTrial) values (?, ?, ?, ?, ?, ?, ?)", 
				new Object[] { tstamp, prefix, gaRun, genNum, firstTrial, lastTrial });
    }
	
	public void writeDescriptiveFirstTrial(Long id) {
        JdbcTemplate jt = new JdbcTemplate(dataSource);
        long tstamp = jt.queryForLong("SELECT max(tstamp) FROM DescriptiveInfo");
        
        jt.update("update DescriptiveInfo set firstTrial = ? where tstamp = ?", 
            new Object[] { id, tstamp });
    }
    
    public void writeDescriptiveLastTrial(Long id) {
        JdbcTemplate jt = new JdbcTemplate(dataSource);
        long tstamp = jt.queryForLong("SELECT max(tstamp) FROM DescriptiveInfo");
        
        jt.update("update DescriptiveInfo set lastTrial = ? where tstamp = ?", 
                new Object[] { id, tstamp });
    }
	
	public void updateJavaSpec(long id, String javaSpec) {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		jt.update("update StimObjData set javaspec = ? where id = ?", 
				new Object[] { javaSpec, id });
	}
	
	public void writeMStickSpec(long id, String mStickSpec) {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		jt.update("update StimObjData set mstickspec = ? where id = ?", 
				new Object[] { mStickSpec, id });
	}
	
	public void writeVertSpec(long id, String descId, String vertSpec, String faceSpec) {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		jt.update("insert into StimObjDataVert (id, descId, vertSpec, faceSpec) values (?, ?, ?, ?)", 
				new Object[] { id, descId, vertSpec, faceSpec});
	}
	
	
	/**
	 * Get done tasks for the generation.
	 * 
	 * @param genId
	 * @return {@link GenerationTaskDoneList} empty if there is no done tasks
	 *         for the generation in database.
	 */

	public GenerationTaskDoneList readTaskDoneByFullGen(String prefix, long runNum, long genNum, long linNum) { //#####!
		final GenerationTaskDoneList taskDone = new GenerationTaskDoneList();
		taskDone.setGenId(genNum);
		taskDone.setDoneTasks(new ArrayList<TaskDoneEntry>());
		
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		long firstTrial = jt.queryForLong("SELECT firstTrial FROM DescriptiveInfo WHERE prefix = ? "
				+ "AND gaRun = ? AND genNum = ? AND linNum = ?", new Object[] { prefix, runNum, genNum, linNum }); //#####!

		long lastTrial = jt.queryForLong("SELECT lastTrial FROM DescriptiveInfo WHERE prefix = ? "
				+ "AND gaRun = ? AND genNum = ? AND linNum = ?", new Object[] { prefix, runNum, genNum, linNum }); //#####!
		
		jt.query(
			" select tstamp, task_id, part_done" + 
			" from TaskDone "	+ 
			" where task_id between ? and ?",
			new Object[] { firstTrial, lastTrial },
			new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					TaskDoneEntry ent = new TaskDoneEntry();
					ent.setTaskId(rs.getLong("task_id")); 
					ent.setTstamp(rs.getLong("tstamp")); 
					ent.setPart_done(rs.getInt("part_done"));
					taskDone.getDoneTasks().add(ent);
				}});
		return taskDone;
	}
	
	public String readPrefixForRunNum(long runNum) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);		
		return jt.queryForObject("SELECT prefix FROM DescriptiveInfo WHERE gaRun = ? LIMIT 1",String.class, runNum);
	}
	
	public long readGenIdForRunNum(long runNum) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);		
		return jt.queryForLong("SELECT max(genNum) FROM DescriptiveInfo WHERE gaRun = ? ", runNum);
	}

	public long readLinIdForRunNum(long runNum) {
		SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);		
		return jt.queryForLong("SELECT max(linNum) FROM DescriptiveInfo WHERE gaRun = ? AND genNum = (SELECT max(genNum) FROM DescriptiveInfo WHERE gaRun = ?)", new Object[] { runNum, runNum });
	}
	
	public List<Long> readAllStimIdsForRun(String prefix, long runNum, long genNum) {
		
		String descId = "^" + prefix + "_r-" + runNum + "_g-" + genNum;
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		
		final List<Long> allIds = new ArrayList<Long>();
		
		jt.query(
				"SELECT id from StimObjData where descId REGEXP ?", 
				new Object[] { descId },
				new RowCallbackHandler() {
					public void processRow(ResultSet rs) throws SQLException {
						allIds.add(rs.getLong("id"));
					}});
		return allIds;
	}
	
	
	
//	//  JK 9 July 2018
//	//  Get all filenames for a specific stimSpec i.e. trial
//	
//	public List<String> readAllFilenamesForTrial(long stimSpecId) {
//		
//		String descId = "^" + prefix + "_r-" + runNum + "_g-" + genNum;
//		JdbcTemplate jt = new JdbcTemplate(dataSource);
//		
//		final List<Long> allIds = new ArrayList<Long>();
//		
//		jt.query(
//				"SELECT id from StimObjData where descId REGEXP ?", 
//				new Object[] { descId },
//				new RowCallbackHandler() {
//					public void processRow(ResultSet rs) throws SQLException {
//						allIds.add(rs.getLong("id"));
//					}});
//		return allIds;
//		
//		
//		
////		readDescriptiveIdFromStimObjId(stimObjId)
//	}
//	
//	
//	
//	SELECT extractvalue(spec, '/StimSpec/object')  from alexandriya_180218_test.StimSpec
//	#from_unixtime(id / 1e6), id, spec 
//	where id = '1531145060278634'
//	
	
	
}
