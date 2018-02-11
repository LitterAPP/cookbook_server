package modules.cookbook.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.ShopProductCategoryDDL;
import modules.cookbook.ddl.ShopProductCommunityDDL;
import modules.cookbook.ddl.ShopProductCommunityRelDDL;
import util.IDUtil;

public class ShopCommunityService {

	public static ShopProductCommunityRelDDL get(int id){
		return Dal.select("ShopProductCommunityRelDDL.*", id);
	}
	public static List<ShopProductCommunityRelDDL> listByProductId(String productId){
		Condition condition = new Condition("ShopProductCommunityRelDDL.productId","=",productId);
		return Dal.select("ShopProductCommunityRelDDL.*", condition, null,0, -1);
	}
	
	public static ShopProductCommunityDDL createCommnuityByName(String name){
		ShopProductCommunityDDL community = new ShopProductCommunityDDL();
		community.setCommunityId(IDUtil.gen("COMMUNITY"));
		community.setCommunityName(name);
		community.setCreateTime(System.currentTimeMillis());
		int id = (int)Dal.insertSelectLastId(community);
		community.setId(id);
		return community;
	}
	
	public static ShopProductCommunityDDL getByName(String name){
		Condition condition = new Condition("ShopProductCommunityDDL.communityName","=",name);
		List<ShopProductCommunityDDL> list = Dal.select("ShopProductCommunityDDL.*", condition, null, 0, 1);
		if(list==null || list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
}
