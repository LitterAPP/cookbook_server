package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgMusicDDL;

public class ForgMusicService {

	public static List<ForgMusicDDL> listMusiceByType(int type){
		Condition cond = new Condition("ForgMusicDDL.type","=",type);
		return Dal.select("ForgMusicDDL.*", cond, new Sort("ForgMusicDDL.id",false), 0, -1);
	}
	
	public static ForgMusicDDL get(int id){
		return Dal.select("ForgMusicDDL.*", id);
	}
	
}
