package modules.cookbook.service;

import java.util.List;

import com.google.gson.JsonObject;

import jws.Logger;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.BaiduAccessTokenDDL;
import util.API;

public class BaiduAccessTokenService {
	
	public static void add(BaiduAccessTokenDDL BaiduAccessTokenDDL){
		Dal.insert(BaiduAccessTokenDDL);
	}
	
	public static BaiduAccessTokenDDL get(){
		List<BaiduAccessTokenDDL> results =  Dal.select("BaiduAccessTokenDDL.*", null, null, 0, 1);
		if(results==null || results.size() == 0 ||  results.get(0).getExpireIn() - System.currentTimeMillis() < 60000) {
			Dal.delete(new Condition("BaiduAccessTokenDDL.id",">",0));
			JsonObject tokenJson = API.getBaiDuAccessToken("Gt6CSNg5vnBTGK5oPIRO6l4M", "HA97b4Q2KjxfUC9p6La7DrSCGe1OvCoN");
			if(tokenJson==null || tokenJson.get("errcode")!=null){
				Logger.error("请求微信获取accessToken错误,%s", tokenJson);
			} 
			String accessTokenStr = tokenJson.get("access_token").getAsString();
			long expireIn = tokenJson.get("expires_in").getAsLong() * 1000;
			
			BaiduAccessTokenDDL newBaiduAccessTokenDDL = new BaiduAccessTokenDDL();
			newBaiduAccessTokenDDL.setToken(accessTokenStr);
			newBaiduAccessTokenDDL.setExpireIn(System.currentTimeMillis()+expireIn);
			add(newBaiduAccessTokenDDL); 
			return newBaiduAccessTokenDDL;
		}else{
			return results.get(0);
		}
		 
		 
	} 

}
