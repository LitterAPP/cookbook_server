package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgBainianCoverDDL;
import modules.forg.ddl.ForgBainianRecordDDL;
import modules.forg.ddl.ForgBainianReplayDDL;

public class BainianService {

	public static ForgBainianCoverDDL getTemplate(int id){
		return Dal.select("ForgBainianCoverDDL.*", id);
	}
	
	public static List<ForgBainianCoverDDL> listTemplate(){
		return Dal.select("ForgBainianCoverDDL.*", null, null, 0, -1);
	}
	
	public static long addBainianRecord(String coverOssKey,String recordOssKey,int userId,String nickName,String avatar){
		ForgBainianRecordDDL record = new ForgBainianRecordDDL();
		record.setAvatar(avatar);
		record.setCoverOssKey(coverOssKey);
		record.setCreateTime(System.currentTimeMillis());
		record.setGreetings("");
		record.setNickName(nickName);
		record.setRecordOssKey(recordOssKey);
		record.setUserId(userId);
		return Dal.insertSelectLastId(record);
	}
	
	public static List<ForgBainianRecordDDL> listMyBainian(int userId){
		Condition cond = new Condition("ForgBainianRecordDDL.userId","=",userId);
		return Dal.select("ForgBainianRecordDDL.*", cond, new Sort("ForgBainianRecordDDL.id",false), 0,50);
	}
	
	public static ForgBainianRecordDDL getOneBainian(int id){
		return Dal.select("ForgBainianRecordDDL.*", id);
	}
	
	public static int count(){
		return Dal.count(ForgBainianRecordDDL.class);
	}
	
	public static List<ForgBainianReplayDDL> listReplays(int id){
		Condition cond = new Condition("ForgBainianReplayDDL.replayId","=",id);
		return Dal.select("ForgBainianReplayDDL.*", cond, new Sort("ForgBainianRecordDDL.id",false), 0,50);
	}
	
	public static void addReplay(int replayId,String replayVoiceOsskey,String replayerAvater,String replayerNickName){
		ForgBainianReplayDDL replay = new ForgBainianReplayDDL();
		replay.setReplayerAvater(replayerAvater);
		replay.setReplayerNickName(replayerNickName);
		replay.setReplayId(replayId);
		replay.setReplayTime(System.currentTimeMillis());
		replay.setReplayVoiceOsskey(replayVoiceOsskey);
		Dal.insert(replay);
	}
}
