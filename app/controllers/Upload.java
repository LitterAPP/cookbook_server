package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import jws.Logger;
import jws.cache.Cache;
import jws.mvc.Controller;
import modules.cookbook.ddl.CookBookUsersDDL;
import modules.cookbook.service.UserService;
import util.API;
import util.MD5Util;
import util.RtnUtil;

public class Upload extends Controller{
	public static void uploadFile(String session,File file){
		FileChannel channel = null;  
        FileInputStream fs = null; 
		try{ 
			/*CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}*/
			if(file==null || !file.exists()){
				renderJSON(RtnUtil.returnFail("文件不存在"));
			}
			//判断文件二进制流MD5是否已经上传过阿里云
			fs = new FileInputStream(file);  
            channel = fs.getChannel();  
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());  
            while ((channel.read(byteBuffer)) > 0) {  
            } 
			String md5FileKey = MD5Util.md5(byteBuffer.array());
			Logger.info("文件%s,对应的cachekey为%s", file.getName() ,md5FileKey);
			Object o = Cache.get(md5FileKey);
			if(o!=null){
				Logger.info("get object key from cache , md5FileKey=%s,key=%s", md5FileKey,String.valueOf(o));
				renderJSON(RtnUtil.returnSuccess("OK",String.valueOf(o)));
			}
			String objectKey = null;
			//判断是否图片，图片进行压缩
			String fileFix = file.getName().substring(file.getName().lastIndexOf(".")).toLowerCase();
			if(fileFix.equals(".jpg") || 
					fileFix.equals(".jpeg") || 
					fileFix.equals(".png")){
				objectKey = API.uploadImage("tasty", file, 400, 0.9f);
			}else{
				objectKey = API.uploadToAliOss("tasty", file);
			}
			Cache.set(md5FileKey, objectKey, "28d");
			renderJSON(RtnUtil.returnSuccess("OK",objectKey));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}finally{
            try {
				channel.close();
				  fs.close();  
			} catch (Exception e) {
				Logger.error(e, e.getMessage());
				renderJSON(RtnUtil.returnFail(e.getMessage()));
			}  
                      
		}
	}
}
