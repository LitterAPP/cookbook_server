package modules.cookbook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.CookBookMaterialDDL;
import modules.cookbook.ddl.CookBookMaterialSelectDDL;
import modules.cookbook.ddl.CookBookMenuRelDDL;

public class MaterialSelectService {

	public static boolean createMaterialList(int menuId){
		
		//创建前先删除
		Condition delCon = new Condition("CookBookMaterialSelectDDL.menuId","=",menuId);
		Dal.delete(delCon);
		
		List<CookBookMenuRelDDL> relList = MenuService.listByMenu(menuId);
		if(relList==null || relList.size()==0) return true;
		
		List<Integer> bookIds = new ArrayList<Integer>();
		for(CookBookMenuRelDDL rel : relList){
			bookIds.add(rel.getCookBookId());
		}
		
		List<CookBookMaterialDDL>  materials = MaterialService.listByCookBookIds(bookIds);
		if(materials==null || materials.size()==0) return true;
		Map<String,Float> amounts = new HashMap<String,Float>();
		
		for(CookBookMaterialDDL material : materials){
			String amount = material.getAmount();
			float amountNum = 0;
			if(amount.endsWith("g")){
				amountNum = Float.parseFloat(amount.substring(0, amount.indexOf("g")));
			} 
			String key = material.getMname();
			if(amounts.containsKey(key)){
				amounts.put(key, amounts.get(material.getMname())+amountNum);
			}else{
				amounts.put(key, 0f);
			}
		}
		for (String key : amounts.keySet()) {  
			CookBookMaterialSelectDDL cbms = new CookBookMaterialSelectDDL();
  			cbms.setMaterialId(0);
			cbms.setAmount(amounts.get(key));
			cbms.setMaterialName(key);
			cbms.setDone(0);
			cbms.setMenuId(menuId);
			Dal.insert(cbms);
		}  
		return true;
	}
	
	public static List<CookBookMaterialSelectDDL> list(int menuId){
		Condition condition = new Condition("CookBookMaterialSelectDDL.menuId","=",menuId);
		return Dal.select("CookBookMaterialSelectDDL.*", condition, new Sort("CookBookMaterialSelectDDL.done",false), 0, -1);
	}
	
	public static boolean markMaterialSelect(int materialId){
		CookBookMaterialSelectDDL select = Dal.select("CookBookMaterialSelectDDL.*", materialId);
		if(select==null)return false;
		if(select.getDone() == 0){
			select.setDone(1);
		}else if(select.getDone() == 1){
			select.setDone(0);
		}
		return Dal.update(select, "CookBookMaterialSelectDDL.done", 
				new Condition("CookBookMaterialSelectDDL.id","=",materialId))>0;
	}
}
