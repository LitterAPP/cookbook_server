package modules.forg.service;

import java.util.List;

import common.GlobalConstants;
import common.QueryConnectionHandler;
import dto.forg.TotalRankDto;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgReadingRecordDDL;
import modules.forg.ddl.ForgReadingRecordsDDL;
import modules.forg.ddl.ForgRecordFlowDDL;

public class ForgBookRecordService {

	public static ForgReadingRecordDDL get(int recordId){
		return Dal.select("ForgReadingRecordDDL.*", recordId);
	}
	public static List<ForgReadingRecordDDL> listMyRecords(int userId,int page,int pageSize){
		Condition condition = new Condition("ForgReadingRecordDDL.userId","=",userId);
		return Dal.select("ForgReadingRecordDDL.*", condition,new Sort("ForgReadingRecordDDL.id",false), (page-1)*pageSize, pageSize);
	}
	public static ForgReadingRecordDDL getByBookIdAndUid(int bookId,int userId){
		Condition condition = new Condition("ForgReadingRecordDDL.bookId","=",bookId);
		condition.add(new Condition("ForgReadingRecordDDL.userId","=",userId), "and");
		List<ForgReadingRecordDDL> list = Dal.select("ForgReadingRecordDDL.*", condition,null, 0, 1);
		if(list==null || list.size()==0) return null;
		return list.get(0);
	}
	public static long create(int bookId,int userId,String recordUrl,String nickName,String avatar,int duration){
		ForgReadingRecordDDL record = new ForgReadingRecordDDL();
		record.setAvatar(avatar);
		record.setNickName(nickName);
		record.setBookId(bookId);
		record.setUserId(userId);
		record.setRecordUrl(recordUrl);
		record.setFlows(0);
		record.setDuration(duration);
		record.setPlayTimes(0);
		record.setCreateTime(System.currentTimeMillis());
		return Dal.insertSelectLastId(record);
	}
	
	public static void replaceVoice(int readId,int pageId,String voiceOss,int fileSize,int duration){
		ForgReadingRecordsDDL records = new ForgReadingRecordsDDL();
		records.setDuration(duration);
		records.setFileSize(fileSize);
		records.setPageId(pageId);
		records.setReadId(readId);
		records.setVoiceOss(voiceOss);
		Dal.replace(records);
	}
	
	public static ForgReadingRecordsDDL getByReadIdAndPageId(int readId,int pageId){
		Condition condition = new Condition("ForgReadingRecordsDDL.readId","=",readId);
		condition.add(new Condition("ForgReadingRecordsDDL.pageId","=",pageId), "and");
		List<ForgReadingRecordsDDL> list = Dal.select("ForgReadingRecordsDDL.*", condition,null, 0, 1);
		if(list==null || list.size()==0) return null;
		return list.get(0);
	}
	 
	public static void update(int bookId,int userId,String recordUrl){
		ForgReadingRecordDDL record = getByBookIdAndUid(bookId,userId);
		record.setRecordUrl(recordUrl);
		Dal.update(record, "ForgReadingRecordDDL.recordUrl", new Condition("ForgReadingRecordDDL.id","=",record.getId()));
	}
	
	public static void update(ForgReadingRecordDDL old,String recordUrl,int duration){
		old.setRecordUrl(recordUrl);
		old.setDuration(duration);
		Dal.update(old, "ForgReadingRecordDDL.recordUrl,ForgReadingRecordDDL.duration", new Condition("ForgReadingRecordDDL.id","=",old.getId()));
	}
	
	public static List<ForgReadingRecordDDL> listOthers(int bookId,int loginUserId,int shareUid){
		Condition condition = new Condition("ForgReadingRecordDDL.bookId","=",bookId);
		//condition.add(new Condition("ForgReadingRecordDDL.userId","!=",loginUserId), "and");
		//condition.add(new Condition("ForgReadingRecordDDL.userId","!=",shareUid), "and");
		Sort sort = new Sort("ForgReadingRecordDDL.flows",false);
		return  Dal.select("ForgReadingRecordDDL.*", condition, sort, 0, 100);
	}
	
	/**
	 * 
	 * @param bookId
	 * @param byLast 0=按赞排名  1=按时间最新排序
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public static List<ForgReadingRecordDDL> listRecords(int bookId,int byLast,int page,int pageSize){
		page = page<=0?1:page;
		pageSize = (pageSize<=0 || pageSize>10)?10:pageSize;
		Condition condition = new Condition("ForgReadingRecordDDL.bookId","=",bookId);		
		Sort sort = new Sort("ForgReadingRecordDDL.flows",false);
		if(byLast==1){
			sort = new Sort("ForgReadingRecordDDL.id",false);
		}
		return  Dal.select("ForgReadingRecordDDL.*", condition, sort, (page-1)*pageSize, pageSize);
	}
	
	
	public static int addFlow(int recordId){
		ForgReadingRecordDDL record = get(recordId);
		if(record == null)return 0;
		record.setFlows(record.getFlows()+1);
		Dal.update(record, "ForgReadingRecordDDL.flows", new Condition("ForgReadingRecordDDL.id","=",record.getId()));
		return record.getFlows();
	}
	
	/**
	 * 记录点赞明细
	 */
	public static void logFlowed(int userId,int bookId,int recordId,String avatar,String nickName){
		ForgRecordFlowDDL flow = new ForgRecordFlowDDL();
		flow.setAvatar(avatar);
		flow.setCreateTime(System.currentTimeMillis());
		flow.setNickName(nickName);
		flow.setRecordId(recordId);
		flow.setBookId(bookId);
		flow.setUserId(userId);
		Dal.insert(flow);
	}
	
	public static List<ForgRecordFlowDDL> listFlows(long startTime,long endTime,int bookId){
		Condition condition = new Condition("ForgRecordFlowDDL.bookId","=",bookId);
		condition.add(new Condition("ForgRecordFlowDDL.createTime",">",startTime), "and");
		condition.add(new Condition("ForgRecordFlowDDL.createTime","<",endTime), "and");
		return Dal.select("ForgRecordFlowDDL.*", condition, null, 0, -1);
	}
	
	public static int addPlayTimes(int recordId){
		ForgReadingRecordDDL record = get(recordId);
		if(record == null)return 0;
		record.setPlayTimes(record.getPlayTimes()+1);
		Dal.update(record, "ForgReadingRecordDDL.playTimes", new Condition("ForgReadingRecordDDL.id","=",record.getId()));
		return record.getFlows();
	}
	
	public static int coutReaded(int bookId){
		Condition condition = new Condition("ForgReadingRecordDDL.bookId","=",bookId);
		return Dal.count(condition);
	}
	
	public static int coutReadedByUser(int userId){
		Condition condition = new Condition("ForgReadingRecordDDL.userId","=",userId);
		return Dal.count(condition);
	}
	
	public static TotalRankDto getTotalRankByUserId(int userId){
		String sql="select user_id userId,nick_name nickName,avatar,flows,rank from ( SELECT user_id,nick_name,avatar,flows,@curRank := @curRank + 1 AS rank  FROM (SELECT user_id,nick_name,avatar,SUM(flows) flows FROM `forg_reading_record` GROUP BY user_id  ) t ,(SELECT @curRank :=0) t1 ORDER BY flows DESC) tt where tt.user_id =  "+userId;
		TotalRankDto t = new TotalRankDto();
		List<TotalRankDto>  list = Dal.getConnection(GlobalConstants.dbSoruceCook, new QueryConnectionHandler(t,sql));
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public static List<TotalRankDto> listTotalRank(int page,int pageSize){
		String sql="select user_id userId,nick_name nickName,avatar,flows,rank from ( SELECT user_id,nick_name,avatar,flows,@curRank := @curRank + 1 AS rank  FROM (SELECT user_id,nick_name,avatar,SUM(flows) flows FROM `forg_reading_record` GROUP BY user_id  ) t ,(SELECT @curRank :=0) t1 ORDER BY flows DESC) tt limit "+(page-1)*pageSize+" , "+pageSize;
		TotalRankDto t = new TotalRankDto();
		return  Dal.getConnection(GlobalConstants.dbSoruceCook, new QueryConnectionHandler(t,sql));
	}
}
