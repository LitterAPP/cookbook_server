package modules.cookbook.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.CookBookClassRelDDL;

public class CookBookRelService {
	
	public static int count(String whereSql){
		if(StringUtils.isEmpty(whereSql)){
			return Dal.executeCount(CookBookClassRelDDL.class, "select count(0) from cook_book_class_rel ");
		}
		return Dal.executeCount(CookBookClassRelDDL.class, "select count(0) from cook_book_class_rel where "+whereSql);
	}
	
	public static List<CookBookClassRelDDL> findByOffset(int offset,int number){
 		List<CookBookClassRelDDL> list = Dal.select("CookBookClassRelDDL.*", null, null, offset, number);
		return list;
	}
	public static List<CookBookClassRelDDL> findByClassIdOffset(int classcid,int offset,int number){
		Condition condition = new Condition("CookBookClassRelDDL.classChildId","=",classcid); 
 		List<CookBookClassRelDDL> list = Dal.select("CookBookClassRelDDL.*", condition, null, offset, number);
		return list;
	}
	
	public static List<CookBookClassRelDDL> findByClassIdOffset(int classcid,int classpid,int offset,int number){
		Condition condition = new Condition("CookBookClassRelDDL.classParentId","=",classpid);
		condition.add(new Condition("CookBookClassRelDDL.classChildId","=",classcid),"and");
 		List<CookBookClassRelDDL> list = Dal.select("CookBookClassRelDDL.*", condition, null, offset, number);
		return list;
	}
	
	public static List<CookBookClassRelDDL> findByBookId(int cookBookId){
		Condition condition = new Condition("CookBookClassRelDDL.cookBookId","=",cookBookId);
 		List<CookBookClassRelDDL> list = Dal.select("CookBookClassRelDDL.*", condition, null, 0, -1);
		return list;
	} 
	 
}
