package modules.cookbook.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.CookBookMaterialDDL;

public class MaterialService {

	public static List<CookBookMaterialDDL> listByCookBookId(int cookBookId){
		Condition condition = new Condition("CookBookMaterialDDL.cookBookInfoId","=",cookBookId);
		return Dal.select("CookBookMaterialDDL.*", condition, new Sort("CookBookMaterialDDL.orderby",false), 0, -1);
	}
	
	
	public static List<CookBookMaterialDDL> listByCookBookIds(List<Integer> cookBookIds){
		Condition condition = new Condition("CookBookMaterialDDL.cookBookInfoId","in",cookBookIds);
		return Dal.select("CookBookMaterialDDL.*", condition, new Sort("CookBookMaterialDDL.orderby",false), 0, -1);
	}
}
