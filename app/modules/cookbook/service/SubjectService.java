package modules.cookbook.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.CookBookSubjectDDL;

public class SubjectService {

	public static List<CookBookSubjectDDL> listSubject(){
		Condition condition = new Condition("CookBookSubjectDDL.status","=",1);
		Sort sort = new Sort("CookBookSubjectDDL.createTime",false);
		return Dal.select("CookBookSubjectDDL.*", condition, sort, 0, -1);
	}
	
	public static CookBookSubjectDDL getSubject(int id){
		Condition condition = new Condition("CookBookSubjectDDL.id","=",id);
		Condition condition2 = new Condition("CookBookSubjectDDL.status","=",1);
		condition.add(condition2, "and");
		List<CookBookSubjectDDL>  list =  Dal.select("CookBookSubjectDDL.*", condition, null, 0, 1);
		if(list==null || list.size() == 0)return null;
		return list.get(0);
	}
	
}
