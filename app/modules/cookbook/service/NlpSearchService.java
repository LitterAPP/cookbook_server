package modules.cookbook.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import modules.cookbook.ddl.BaiduAccessTokenDDL;
import modules.cookbook.ddl.CookBookInfoDDL;
import util.API;

public class NlpSearchService {

	public static List<CookBookInfoDDL> nlpSearch(String text,int classid){
		BaiduAccessTokenDDL accessToken = BaiduAccessTokenService.get();
		JsonObject jsonObject = API.nlpBaiDuLexer(accessToken.getToken(), text);
		if( null == jsonObject || jsonObject.get("items")==null){
			return null;
		}
		List<CookBookInfoDDL> result = new ArrayList<CookBookInfoDDL>();
		JsonArray items = jsonObject.get("items").getAsJsonArray();
		Iterator it = items.iterator();
		while(it.hasNext()){
			JsonElement e = (JsonElement)it.next();
			JsonObject itemJson = e.getAsJsonObject();
			if( itemJson.get("pos").getAsString().equals("nz") || itemJson.get("pos").getAsString().equals("n")){
				List<CookBookInfoDDL> list = CookBookInfoService.search(itemJson.get("item").getAsString(),classid);
				if(list!=null && list.size()>0){
					result.addAll(list);
				}
			}
		}
		return result;
	}
}
