package modules.cookbook.service;

import java.util.List;
import java.util.Random;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.CookBookCollectStatDDL;
import util.ThreadUtil;

public class CollectStatService {
	
	public static CookBookCollectStatDDL get(long userId,int bookId){
		Condition condition = new Condition("CookBookCollectStatDDL.userId","=",userId);
		condition.add(new Condition("CookBookCollectStatDDL.cookBookId","=",bookId), "and");
		List<CookBookCollectStatDDL> list = Dal.select("CookBookCollectStatDDL.*", condition, null, 0, 1);
		if(list==null||list.size()==0){
			return null;
		}
		return 	list.get(0);
	}
	
	public static boolean collect(long userId,int bookId,int value){
		CookBookCollectStatDDL exist = get(userId,bookId);
		if(exist!=null){
			exist.setCollectValue(exist.getCollectValue()+value);
			return Dal.replace(exist) > 0; 
		}
		CookBookCollectStatDDL add = new CookBookCollectStatDDL();
		add.setCookBookId(bookId);
		add.setUserId(new Long(userId).intValue());
		add.setCollectValue(value);
		return Dal.insert(add)>0;
	}
	
	public static int sumCollectNum(final int bookId){
		Condition condition = new Condition("CookBookCollectStatDDL.cookBookId","=",bookId);
		List<CookBookCollectStatDDL> list = Dal.select("CookBookCollectStatDDL.*", condition, null, 0, 1);
		if(list==null||list.size()==0){ 
			final int random = 100+new Random().nextInt(1000);
			ThreadUtil.sumbit(new Runnable(){
				@Override
				public void run() {
					collect(912,bookId,random);//我自己的ID加喜欢值
				}
			});
			return random;
		}
		return list.size()*100;
	}
}
