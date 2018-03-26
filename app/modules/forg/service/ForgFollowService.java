package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.forg.ddl.ForgFollowDDL;

public class ForgFollowService {

	public static ForgFollowDDL find(int userId,int followUserId){
		Condition cond = new Condition("ForgFollowDDL.followUserId","=",followUserId);
		cond.add(new Condition("ForgFollowDDL.userId","=",userId), "and");
		List<ForgFollowDDL> list = Dal.select("ForgFollowDDL.*", cond, null, 0, 1);
		if(list==null || list.size()==0)return null;
		return list.get(0);
	}
	
	public static List<ForgFollowDDL> list(int followUserId,int page,int pageSize){
		page = page<=0?1:page;
		pageSize = (pageSize>10 || pageSize<=0)?10:pageSize;
		Condition cond = new Condition("ForgFollowDDL.followUserId","=",followUserId);
		return  Dal.select("ForgFollowDDL.*", cond, null, (page-1)*pageSize, pageSize);
	}
	
	public static List<ForgFollowDDL> listFollowed(int userId,int page,int pageSize){
		page = page<=0?1:page;
		pageSize = (pageSize>10 || pageSize<=0)?10:pageSize;
		Condition cond = new Condition("ForgFollowDDL.userId","=",userId);
		return  Dal.select("ForgFollowDDL.*", cond, null, (page-1)*pageSize, pageSize);
	}
	
	public static void delete(int followId){
		Dal.delete(new Condition("ForgFollowDDL.id","=",followId));
	}
	
	public static void add(int userId,int followUserId){
		ForgFollowDDL c = new ForgFollowDDL();
		c.setFollowUserId(followUserId);
		c.setUserId(userId);
		c.setFollowTime(System.currentTimeMillis());
		Dal.replace(c);
	}
	
	public static int countFollower(int userId){
		return Dal.count(new Condition("ForgFollowDDL.followUserId","=",userId));
	}
	
	public static int countFollowed(int userId){
		return Dal.count(new Condition("ForgFollowDDL.userId","=",userId));
	}
}
