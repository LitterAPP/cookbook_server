package modules.cookbook.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.CookBookMenuDDL;
import modules.cookbook.ddl.CookBookMenuRelDDL;

public class MenuService {

	public static CookBookMenuDDL getMenu(int menuId){
		return Dal.select("CookBookMenuDDL.*", menuId);
	}
	public static boolean addMenu(String name,int userId,String key,String introduction,int type) throws Exception{
		CookBookMenuDDL cbm = findByName(userId,name);
		if(cbm!=null){
			throw new Exception("菜单已经存");
		}
		CookBookMenuDDL ncbm = new CookBookMenuDDL();
		ncbm.setUserId(userId);
		ncbm.setMenuName(name);
		ncbm.setKey(key);
		ncbm.setType(type);
		ncbm.setIntroduction(introduction);
		ncbm.setCreateTime(System.currentTimeMillis());
		return Dal.insert(ncbm)>0;
	}
	
	public static CookBookMenuDDL findByName(int userId,String name){
		Condition condition = new Condition("CookBookMenuDDL.userId","=",userId);
		condition.add( new Condition("CookBookMenuDDL.menuName","=",name), "and");
		List<CookBookMenuDDL> list = Dal.select("CookBookMenuDDL.*", condition, null, 0, 1);
		if(list==null || list.size() == 0)return null;
		return list.get(0);
	}
	
	public static List<CookBookMenuDDL> findMyMenuList(int userId){
		Condition condition = new Condition("CookBookMenuDDL.userId","=",userId);
		List<CookBookMenuDDL> list = Dal.select("CookBookMenuDDL.*", condition, 
				new Sort("CookBookMenuDDL.createTime",false), 0, -1);
		return list;
	}
	
	public static List<CookBookMenuDDL> listShareMenus(int minId,int lastId,int num){
		if( minId == 0 && lastId==0){
			return  Dal.select("CookBookMenuDDL.*", null, 
						new Sort("CookBookMenuDDL.id",false), 0, num);
		}
		
		if(minId == 0 && lastId > 0){
			Condition condition = new Condition("CookBookMenuDDL.id",">",lastId);
			return  Dal.select("CookBookMenuDDL.*", condition, 
					new Sort("CookBookMenuDDL.id",false), 0, num);
		}
		
		if(minId > 0 && lastId == 0){
			Condition condition = new Condition("CookBookMenuDDL.id","<",minId);
			return  Dal.select("CookBookMenuDDL.*", condition, 
					new Sort("CookBookMenuDDL.id",false), 0, num);
		}
		
		return null;
		
	}
	
	public static boolean deleteMenu(int menuId){
		//级联删除
		deleteMenuRel(menuId);
		return Dal.delete(new Condition("CookBookMenuDDL.id","=",menuId))>0;
	}
	
	public static boolean deleteMenuRel(int menuId){
		Condition condition = new Condition("CookBookMenuRelDDL.menuId","=",menuId);
		return Dal.delete(condition)>0;
	}
	
	public static List<CookBookMenuRelDDL> listByMenu(int menuId){
		Condition condition = new Condition("CookBookMenuRelDDL.menuId","=",menuId);
 		return Dal.select("CookBookMenuRelDDL.*", condition, null, 0, -1);
	}
	
	public static boolean deleteCookBookFromMenue(int menuId,int cookBookId){
		Condition condition = new Condition("CookBookMenuRelDDL.menuId","=",menuId);
		condition.add(new Condition("CookBookMenuRelDDL.cookBookId","=",cookBookId), "and");
		return Dal.delete(condition) > 0 ;
	}
	
	public static boolean addInMenue(int menuId,int cookBookId){
		CookBookMenuRelDDL cbmr = new CookBookMenuRelDDL();
		cbmr.setCookBookId(cookBookId);
		cbmr.setMenuId(menuId);
		cbmr.setIntroduction("");
		cbmr.setCreateTime(System.currentTimeMillis());
		return Dal.replace(cbmr)>0;
	}
	
	public static int countCookBookInMenu(int menuId){
		return Dal.count(new Condition("CookBookMenuRelDDL.menuId","=",menuId));
	}
}
