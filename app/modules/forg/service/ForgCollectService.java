package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgCollectDDL;

public class ForgCollectService {

	public static ForgCollectDDL findByBookIdAndUserId(int userId,int bookId){
		Condition cond = new Condition("ForgCollectDDL.bookId","=",bookId);
		cond.add(new Condition("ForgCollectDDL.userId","=",userId), "and");
		List<ForgCollectDDL> list = Dal.select("ForgCollectDDL.*", cond, null, 0, 1);
		if(list==null || list.size()==0)return null;
		return list.get(0);
	}
	
	public static List<ForgCollectDDL> listMyCollects(int userId,int page,int pageSize){
		Condition cond = new Condition("ForgCollectDDL.userId","=",userId);
		return  Dal.select("ForgCollectDDL.*", cond, new Sort("ForgCollectDDL.id",false), (page-1)*pageSize, pageSize);
	}
	
	public static int countCollect(int userId){
		Condition condition  = new Condition("ForgCollectDDL.userId","=",userId);
		return Dal.count(condition);
	}
	
	public static void delete(int collectId){
		Dal.delete(new Condition("ForgCollectDDL.id","=",collectId));
	}
	
	public static void add(int userId,int bookId){
		ForgCollectDDL c = new ForgCollectDDL();
		c.setBookId(bookId);
		c.setUserId(userId);
		c.setCollectTime(System.currentTimeMillis());
		Dal.replace(c);
	}
}
