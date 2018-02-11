package task;

import com.google.gson.JsonObject;

import jws.Logger;
import modules.cookbook.ddl.BaiduAccessTokenDDL;
import modules.cookbook.service.BaiduAccessTokenService;
import modules.cookbook.service.WXAccessTokenService;
import util.API;

public class BaiDuAccessTokenReflush implements Runnable{

	@Override
	public void run() {
		try{}catch(Exception e){
			Logger.error(e, e.getMessage());
		}
	}

}
