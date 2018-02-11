package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgReadingRecordDDL;

public class ForgBoolRecordService {

	public static ForgReadingRecordDDL get(int recordId){
		return Dal.select("ForgReadingRecordDDL.*", recordId);
	}
	public static ForgReadingRecordDDL getByBookIdAndUid(int bookId,int userId){
		Condition condition = new Condition("ForgReadingRecordDDL.bookId","=",bookId);
		condition.add(new Condition("ForgReadingRecordDDL.userId","=",userId), "and");
		List<ForgReadingRecordDDL> list = Dal.select("ForgReadingRecordDDL.*", condition,null, 0, 1);
		if(list==null || list.size()==0) return null;
		return list.get(0);
	}
	public static void create(int bookId,int userId,String recordUrl,String nickName,String avatar,int duration){
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
		Dal.insert(record);
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
		condition.add(new Condition("ForgReadingRecordDDL.userId","!=",loginUserId), "and");
		condition.add(new Condition("ForgReadingRecordDDL.userId","!=",shareUid), "and");
		Sort sort = new Sort("ForgReadingRecordDDL.id",false);
		return  Dal.select("ForgReadingRecordDDL.*", condition, sort, 0, 100);
	}
	public static int addFlow(int recordId){
		ForgReadingRecordDDL record = get(recordId);
		if(record == null)return 0;
		record.setFlows(record.getFlows()+1);
		Dal.update(record, "ForgReadingRecordDDL.flows", new Condition("ForgReadingRecordDDL.id","=",record.getId()));
		return record.getFlows();
	}
	
	public static int addPlayTimes(int recordId){
		ForgReadingRecordDDL record = get(recordId);
		if(record == null)return 0;
		record.setPlayTimes(record.getPlayTimes()+1);
		Dal.update(record, "ForgReadingRecordDDL.playTimes", new Condition("ForgReadingRecordDDL.id","=",record.getId()));
		return record.getFlows();
	}
}
