package modules.cookbook.service;

import java.util.List;
import java.util.Random;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.CookBookCollectDDL;

public class CollectService {
	
	public static CookBookCollectDDL get(long userId,int bookId){
		Condition condition = new Condition("CookBookCollectDDL.userId","=",userId);
		condition.add(new Condition("CookBookCollectDDL.cookBookInfoId","=",bookId), "and");
		List<CookBookCollectDDL> list = Dal.select("CookBookCollectDDL.*", condition, null, 0, 1);
		if(list==null||list.size()==0){
			return null;
		}
		return 	list.get(0);
	}
	
	public static boolean collect(long userId,int bookId){
		CookBookCollectDDL exist = get(userId,bookId);
		if(exist!=null){
			return true;
		}
		CookBookCollectDDL add = new CookBookCollectDDL();
		add.setCookBookInfoId(bookId);
		add.setUserId(new Long(userId).intValue());
		add.setCreateTime(System.currentTimeMillis());
		return Dal.insert(add)>0;
	} 
}
