package modules.cookbook.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.CookBookProcessDDL;

public class ProcessService {

	public static List<CookBookProcessDDL> listByCookBookId(int cookBookId){
		Condition condition = new Condition("CookBookProcessDDL.cookBookInfoId","=",cookBookId);
		return Dal.select("CookBookProcessDDL.*", condition, new Sort("CookBookProcessDDL.step",true), 0, -1);
	}
}
