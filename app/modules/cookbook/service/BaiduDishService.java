package modules.cookbook.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.BaiduAiDishDDL;

public class BaiduDishService {

	public static BaiduAiDishDDL getByMd5(String md5){
		Condition cond = new Condition("BaiduAiDishDDL.imgDataMd5","=",md5);
		List<BaiduAiDishDDL> list = Dal.select("BaiduAiDishDDL.*", cond, null, 0, 1);
		if( list!=null && list.size()>0)return list.get(0);
		return null;
	}
	
	public static void add(String md5,String base64,String result){
		BaiduAiDishDDL aBaiduAiDishDDL = new BaiduAiDishDDL();
		aBaiduAiDishDDL.setCreateTime(System.currentTimeMillis());
		aBaiduAiDishDDL.setImgData(base64);
		aBaiduAiDishDDL.setImgDataMd5(md5);
		aBaiduAiDishDDL.setResult(result);
		Dal.insert(aBaiduAiDishDDL);
	}
}
