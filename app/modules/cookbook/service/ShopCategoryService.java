package modules.cookbook.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.ShopProductCategoryChildDDL;
import modules.cookbook.ddl.ShopProductCategoryDDL;
import modules.cookbook.ddl.ShopProductCategoryRelDDL;
import util.IDUtil;

public class ShopCategoryService {

	public static ShopProductCategoryRelDDL get(int id){
		return Dal.select("ShopProductCategoryRelDDL.*", id);
	}
	
	public static List<ShopProductCategoryRelDDL> listByProductId(String productId){
		Condition condition = new Condition("ShopProductCategoryRelDDL.productId","=",productId);
		return Dal.select("ShopProductCategoryRelDDL.*", condition, null, 0, -1);
	}
	
	public static ShopProductCategoryDDL createPCategory(String pCategoryName){
		ShopProductCategoryDDL p = new ShopProductCategoryDDL();
		p.setCategoryId(IDUtil.gen("CAT"));
		p.setCategoryName(pCategoryName);
		int id = (int)Dal.insertSelectLastId(p);
		p.setId(id);
		return p;
	}
	
	public static ShopProductCategoryChildDDL createChildCategory(String subCategoryName,String pid){
		ShopProductCategoryChildDDL sub = new ShopProductCategoryChildDDL();
		sub.setPCagegoryId(pid);
		sub.setCategoryId(IDUtil.gen("CAT_SUB"));
		sub.setCategoryName(subCategoryName);
		int id = (int)Dal.insertSelectLastId(sub);
		sub.setId(id);
		return sub;
	}
	
	public static ShopProductCategoryDDL getByPCategoryId(String categoryId){
		Condition condition = new Condition("ShopProductCategoryDDL.categoryId","=",categoryId);
		List<ShopProductCategoryDDL> list = Dal.select("ShopProductCategoryDDL.*", condition, null, 0, 1);
		if(list==null || list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	public static ShopProductCategoryDDL getByPCategoryName(String pCategoryName){
		Condition condition = new Condition("ShopProductCategoryDDL.categoryName","=",pCategoryName);
		List<ShopProductCategoryDDL> list = Dal.select("ShopProductCategoryDDL.*", condition, null, 0, 1);
		if(list==null || list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	public static List<ShopProductCategoryDDL> searchByPCategoryName(String pCategoryName){
		if(!StringUtils.isEmpty(pCategoryName)){
			Condition condition = new Condition("ShopProductCategoryDDL.categoryName","like","'%"+pCategoryName+"%'");
			List<ShopProductCategoryDDL> list = Dal.select("ShopProductCategoryDDL.*", condition, null, 0, -1);
			return list;
		}
		return  Dal.select("ShopProductCategoryDDL.*", null, null, 0, -1);
	}
	
	public static ShopProductCategoryChildDDL getBySubCategoryName(String subCategoryName){
		Condition condition = new Condition("ShopProductCategoryChildDDL.categoryName","=",subCategoryName);
		List<ShopProductCategoryChildDDL> list = Dal.select("ShopProductCategoryChildDDL.*", condition, null, 0, 1);
		if(list==null || list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	public static ShopProductCategoryChildDDL getBySubCategoryId(String categoryId){
		Condition condition = new Condition("ShopProductCategoryChildDDL.categoryId","=",categoryId);
		List<ShopProductCategoryChildDDL> list = Dal.select("ShopProductCategoryChildDDL.*", condition, null, 0, 1);
		if(list==null || list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	public static List<ShopProductCategoryChildDDL> searchBySubCategoryName(String subCategoryName){
		if(!StringUtils.isEmpty(subCategoryName)){
			Condition condition = new Condition("ShopProductCategoryChildDDL.categoryName","like","'%"+subCategoryName+"%'");
			List<ShopProductCategoryChildDDL> list = Dal.select("ShopProductCategoryChildDDL.*", condition, null, 0, -1);
			return list;
		}
		return Dal.select("ShopProductCategoryChildDDL.*", null, null, 0, -1);
	}
	
}
