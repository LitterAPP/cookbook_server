package modules.forg.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgActivityDDL;
import modules.forg.ddl.ForgActivityRankDDL;
import modules.forg.ddl.ForgRecordFlowDDL;

public class ForgActivityService {

	public static ForgActivityDDL getActivity(){
		Condition condition = new Condition("ForgActivityDDL.startTime","<",System.currentTimeMillis());
		condition.add(new Condition("ForgActivityDDL.endTime",">",System.currentTimeMillis()), "and");
		List<ForgActivityDDL> acts = Dal.select("ForgActivityDDL.*", condition, null, 0, 1);
		if( acts == null || acts.size() == 0) return null;
		return acts.get(0);
	}
	
	public static ForgActivityDDL getActivityById(int id){
		return Dal.select("ForgActivityDDL.*", id);				
	}
	
	public static List<ForgActivityDDL> listActivity(int page,int pageSize){		 
		List<ForgActivityDDL> acts = Dal.select("ForgActivityDDL.*", null,
				new Sort("ForgActivityDDL.id",false), (page-1)*pageSize, pageSize);
		return acts;
	}
	
	public static List<ForgActivityRankDDL> listActivityBank(int activityId,int page,int pageSize){
		Condition condition = new Condition("ForgActivityRankDDL.activityId","=",activityId);
		return Dal.select("ForgActivityRankDDL.*", condition, new Sort("ForgActivityRankDDL.rank",true), (page-1)*pageSize, pageSize);
	}
	
	public static ForgActivityRankDDL listActivityBankByUserId(int activityId,int userId){
		Condition condition = new Condition("ForgActivityRankDDL.activityId","=",activityId);
		condition.add(new Condition("ForgActivityRankDDL.userId","=",userId),"and");
		List<ForgActivityRankDDL>  list =  Dal.select("ForgActivityRankDDL.*", condition, new Sort("ForgActivityRankDDL.rank",true),0, 1);
		return (list==null||list.size()==0)?null:list.get(0);
	}
	
	public static List<ForgActivityDDL> listNeedRankActivity(){
		Condition condition = new Condition("ForgActivityDDL.endTime","<",System.currentTimeMillis());
		condition.add(new Condition("ForgActivityDDL.rank","!=",1), "and");
		return Dal.select("ForgActivityDDL.*", condition, null, 0, -1);
	}
	
	public static void updateRanked(int id){
		ForgActivityDDL activity = Dal.select("ForgActivityDDL.*", id);
		if(activity==null) return ;
		activity.setRank(1);
		Dal.update(activity, "ForgActivityDDL.rank", new Condition("ForgActivityDDL.id","=",id));
	}
	
	public static void rankActivity(ForgActivityDDL activity){
		 
		if(activity==null || activity.getEndTime() > System.currentTimeMillis()) return ;
		updateRanked(activity.getId());
		
		//获取时段的点赞记录
		List<ForgRecordFlowDDL> flowList = ForgBookRecordService.listFlows(activity.getStartTime(), activity.getEndTime(), activity.getBookId());
		if(flowList==null || flowList.size() == 0) {
			return ;
		}
		Map<String,ForgActivityRankDDL> rankMap = new HashMap<String,ForgActivityRankDDL>();
		for(ForgRecordFlowDDL flow : flowList){
			String key = String.valueOf(flow.getUserId());
			if(rankMap.containsKey(key)){
				ForgActivityRankDDL rank = rankMap.get(key);
				rank.setFlows(rank.getFlows()+1);
				continue;
			}
			ForgActivityRankDDL rank = new ForgActivityRankDDL();
			rank.setActivityId(activity.getId());
			rank.setAvatar(flow.getAvatar());
			rank.setCreateTime(System.currentTimeMillis());
			rank.setNickName(flow.getNickName());
			rank.setFlows(1);
			rank.setUserId(flow.getUserId());
			rankMap.put(key, rank);
		} 
		List<ForgActivityRankDDL> rankList = new ArrayList<ForgActivityRankDDL>();
		rankList.addAll(rankMap.values()); 
		//降序
		Collections.sort(rankList, new Comparator<ForgActivityRankDDL>(){
			@Override
			public int compare(ForgActivityRankDDL o1, ForgActivityRankDDL o2) { 
				return o2.getFlows() - o1.getFlows();
			}});
		int rankNum = 1;
		for(ForgActivityRankDDL rank : rankList){
			rank.setRank(rankNum); 
			rankNum++;
		}
		Dal.replaceMulti(rankList); 
	}
}
