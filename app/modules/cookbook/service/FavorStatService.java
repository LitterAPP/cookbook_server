package modules.cookbook.service;

import java.util.List;
import java.util.Random;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.CookBookFavorStatDDL;
import util.ThreadUtil;
/**
 * 用户点击❤喜欢
 * @author fish
 *
 */
public class FavorStatService {

	public static void favor(int cookBookId,int value){
		CookBookFavorStatDDL exist = findByUniqueKey(cookBookId);
		if(exist == null){
			CookBookFavorStatDDL favor = new CookBookFavorStatDDL();
			favor.setCookBookId(cookBookId); 
			favor.setFavorValue(value);
			Dal.insert(favor);
		}else{
			exist.setFavorValue(exist.getFavorValue()+value);
			Dal.replace(exist);
		}
	}
	
	public static CookBookFavorStatDDL findByUniqueKey(int cookBookId){
		Condition condition = new Condition("CookBookFavorStatDDL.cookBookId","=",cookBookId);
		List<CookBookFavorStatDDL> list = Dal.select("CookBookFavorStatDDL.*", condition, null, 0, 1);
		if( list==null || list.size() == 0 ){
			return null;
		}
		return list.get(0);
	}
	
	public static List<CookBookFavorStatDDL> findByUserId(long userId){
		Condition condition = new Condition("CookBookFavorStatDDL.userId","=",userId);
		List<CookBookFavorStatDDL> list = Dal.select("CookBookFavorStatDDL.*", condition, null, 0, -1);
		return list;
	}
	
	public static int sumFavorValue(final int cookBookId){
		CookBookFavorStatDDL stat = findByUniqueKey(cookBookId);
		if( stat == null ){
			final int random = 1000+new Random().nextInt(1000);
			ThreadUtil.sumbit(new Runnable(){
				@Override
				public void run() {
					favor(cookBookId,random);//我自己的ID加喜欢值
				}
			});
			return random;
		}
		return stat.getFavorValue();
	}
	
}
