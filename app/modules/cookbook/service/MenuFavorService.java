package modules.cookbook.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.CookBookMemuCommentDDL;
import modules.cookbook.ddl.CookBookMemuFavorDDL;

public class MenuFavorService {

	public static void addFavor(int userId,int menuId,String userName,String userAvatar,int cancel){
		CookBookMemuFavorDDL cmf = new CookBookMemuFavorDDL();
 		cmf.setUserAvatar(userAvatar);
		cmf.setUserName(userName);
		cmf.setUserId(userId);
		cmf.setMenuId(menuId); 
		cmf.setCancel(cancel);
		Dal.replace(cmf);
	}
	
	public static List<CookBookMemuFavorDDL> listByMenuId(int menuId,int page,int num){
		Condition condition = new Condition("CookBookMemuFavorDDL.menuId","=",menuId);
		condition.add(new Condition("CookBookMemuFavorDDL.cancel","=",0), "and");
		return Dal.select("CookBookMemuFavorDDL.*", condition, new Sort("CookBookMemuFavorDDL.id",false), (page-1)*num, num);
	}
}
