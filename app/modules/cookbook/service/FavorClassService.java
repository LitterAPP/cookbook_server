package modules.cookbook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.CookBookClassRelDDL;
import modules.cookbook.ddl.CookBookFavorClassDDL;
/**
 * 用户浏览分类
 * 用来智能推荐
 * @author fish
 *
 */
public class FavorClassService {

	public static void favor(int userId,int classId){
		CookBookFavorClassDDL exist = findByUniqueKey(userId,classId);
		if(exist == null){
			CookBookFavorClassDDL favor = new CookBookFavorClassDDL();
			favor.setClassId(classId);
			favor.setUserId(userId);
			favor.setFavorValue(1);
			Dal.insert(favor);
		}else{
			exist.setFavorValue(exist.getFavorValue()+1);
			Dal.replace(exist);
		}
	}
	
	public static CookBookFavorClassDDL findByUniqueKey(int userId,int classId){
		Condition condition = new Condition("CookBookFavorClassDDL.userId","=",userId);
		condition.add( new Condition("CookBookFavorClassDDL.classId","=",classId), "and");
		List<CookBookFavorClassDDL> list = Dal.select("CookBookFavorClassDDL.*", condition, null, 0, 1);
		if( list==null || list.size() == 0 ){
			return null;
		}
		return list.get(0);
	}
	
	public static List<CookBookFavorClassDDL> findByUserId(long userId){
		Condition condition = new Condition("CookBookFavorClassDDL.userId","=",userId);
		List<CookBookFavorClassDDL> list = Dal.select("CookBookFavorClassDDL.*", condition, null, 0, -1);
		return list;
	} 
	
}
