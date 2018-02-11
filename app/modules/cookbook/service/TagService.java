package modules.cookbook.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.CookBookTagDDL;

public class TagService {

	public static List<CookBookTagDDL> listTags(int bookId){
		Condition condition = new Condition("CookBookTagDDL.cookBookInfoId","=",bookId);
		return Dal.select("CookBookTagDDL.*", condition, null, 0, -1);
	}
}
