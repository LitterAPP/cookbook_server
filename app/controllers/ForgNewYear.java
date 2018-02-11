package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import jws.Jws;
import jws.Logger;
import jws.cache.Cache;
import jws.mvc.Controller;
import modules.common.ddl.FormIdsDDL;
import modules.common.service.FormIdService;
import modules.cookbook.ddl.CookBookUsersDDL;
import modules.cookbook.service.UserService;
import modules.forg.ddl.ForgBainianCoverDDL;
import modules.forg.ddl.ForgBainianRecordDDL;
import modules.forg.ddl.ForgBainianReplayDDL;
import modules.forg.service.BainianService;
import util.API;
import util.RtnUtil;
import util.ThreadUtil;

/**
 * 青蛙读本
 * @author fish
 *
 */
public class ForgNewYear extends Controller{ 
	
	 
	
	public static void isOpen(){
		renderJSON(RtnUtil.returnSuccess("OK",Boolean.valueOf(Jws.configuration.getProperty("ForgNewYear.switch", "true"))));
	}
	
	public static void voiceCommit (String session,String ossKey,int templateId){
		try{   
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			if(StringUtils.isEmpty(ossKey) || ossKey.equals("null") || ossKey.equals("undefined")){
				throw new Exception("上传失败");
			}
			ForgBainianCoverDDL tmpl = BainianService.getTemplate(templateId);
			BainianService.addBainianRecord(tmpl.getOssKey(), ossKey, user.getId().intValue(), user.getNickName(), user.getAvatarUrl());
			renderJSON(RtnUtil.returnSuccess("OK"));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	
	public static void replay (String session,String ossKey,final int recordId){
		try{   
			final CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			if(StringUtils.isEmpty(ossKey) || ossKey.equals("null") || ossKey.equals("undefined")){
				throw new Exception("上传失败");
			}
			BainianService.addReplay(recordId, ossKey, user.getAvatarUrl(), user.getNickName());
			
			ThreadUtil.sumbit(new Runnable(){ 
				@Override
				public void run() { 
					
					String key = "HappyNewYear_"+recordId+"_"+user.getOpenId();
					if(null != Cache.get(key)){
						return;
					}
					
					Map<String,Map> dataMap = new HashMap<String,Map>();
					
					Map<String,String> k1 = new HashMap<String,String>();
					k1.put("value","新年祝福");
					k1.put("color", "#FF0000"); 
				
					Map<String,String> k2 = new HashMap<String,String>();
					k2.put("value", user.getNickName());
					k2.put("color", "#FFA500");
					
					Map<String,String> k3 = new HashMap<String,String>();
					k3.put("value", "福到，财到！我给您送福了，来听听吧~");
					k3.put("color", "#FF0000");
					
					Map<String,String> k4 = new HashMap<String,String>();
					k4.put("value", "点击进入小程序收福啦~");
					k4.put("color", "#DCDCDC");
					
				 
					dataMap.put("keyword1", k1);
					dataMap.put("keyword2", k2);
					dataMap.put("keyword3", k3);
					dataMap.put("keyword4", k4);
					
					ForgBainianRecordDDL record = BainianService.getOneBainian(recordId);
					CookBookUsersDDL sentUser = UserService.get(record.getUserId()); 
							
					String page = "pages/bainian/detail?id="+recordId;
					
					String appId = Jws.configuration.getProperty("forg.appid");
					FormIdsDDL form = FormIdService.getOneForm(appId, sentUser.getOpenId());
					if(form!=null){
						API.sendWxMessage(appId, form.getOpenId(), "GGcGUJisZwfLmeZirYShti2EzBFxFkQAFRSIdftJf9w", page, form.getFormId(), dataMap);
						Cache.set(key, "1", "600s");
					} 
				} 
			});
			
			renderJSON(RtnUtil.returnSuccess("OK"));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listTemplate (String session){
		try{   
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			List<ForgBainianCoverDDL> templates = BainianService.listTemplate();
 			for(ForgBainianCoverDDL templ: templates){
				Map<String,Object> one = new HashMap<String,Object>();
				one.put("id", templ.getId());
				one.put("url", API.getAliOssAccessUrl("tasty", templ.getOssKey(), 0));
				list.add(one);
 			}
			renderJSON(RtnUtil.returnSuccess("OK",list));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void myBainianList(String session){
		try{   
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("total", BainianService.count()); 
			
			List<ForgBainianRecordDDL>  myList = BainianService.listMyBainian(user.getId().intValue());
			if( myList == null || myList.size() == 0){
				renderJSON(RtnUtil.returnSuccess("OK",result));	
			}
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(ForgBainianRecordDDL my : myList){
				Map<String,Object> one = new HashMap<String,Object>();
				one.put("id", my.getId());
				one.put("nickName", my.getNickName());
				one.put("avatar", my.getAvatar());
				one.put("url", API.getAliOssAccessUrl("tasty", my.getCoverOssKey(), 0));
				one.put("recordUrl", API.getAliOssAccessUrl("tasty", my.getRecordOssKey(), 0));
				List<Map<String,Object>> replayers = new ArrayList<Map<String,Object>>();
				List<ForgBainianReplayDDL> replayList = BainianService.listReplays(my.getId().intValue());
				if(replayList!=null && replayList.size()>0){
					for(ForgBainianReplayDDL replay : replayList){
						Map<String,Object> replayer = new HashMap<String,Object>();
						replayer.put("voice", API.getAliOssAccessUrl("tasty", replay.getReplayVoiceOsskey(), 0));
						replayer.put("nickName",replay.getReplayerNickName());
						replayer.put("avatar",replay.getReplayerAvater());
						replayer.put("time",util.DateUtil.format(replay.getReplayTime(), "yyyy-MM-dd"));
						replayers.add(replayer);
					}
				}
				one.put("replayers", replayers);
				list.add(one);
			} 
			result.put("list", list);
			renderJSON(RtnUtil.returnSuccess("OK",result));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void getOneBainian(String session,int id){
		try{   
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}   
			Map<String,Object> one = new HashMap<String,Object>();
			ForgBainianRecordDDL my = BainianService.getOneBainian(id); 
			if(my == null){
				renderJSON(RtnUtil.returnSuccess("OK",one));
			}
			
			
			one.put("id", my.getId());
			one.put("userId", my.getUserId());
			one.put("nickName", my.getNickName());
			one.put("avatar", my.getAvatar());
			one.put("url", API.getAliOssAccessUrl("tasty", my.getCoverOssKey(), 0));
			one.put("recordUrl", API.getAliOssAccessUrl("tasty", my.getRecordOssKey(), 0));
			List<Map<String,Object>> replays = new ArrayList<Map<String,Object>>();
			List<ForgBainianReplayDDL> replayList = BainianService.listReplays(my.getId().intValue());
			if(replayList!=null && replayList.size()>0){
				for(ForgBainianReplayDDL replay : replayList){
					Map<String,Object> replayer = new HashMap<String,Object>();
					replayer.put("voice", API.getAliOssAccessUrl("tasty", replay.getReplayVoiceOsskey(), 0));
					replayer.put("nickName",replay.getReplayerNickName());
					replayer.put("avatar",replay.getReplayerAvater());
					replayer.put("time",util.DateUtil.format(replay.getReplayTime(), "yyyy-MM-dd"));
					replays.add(replayer);
				}
			} 
			one.put("replays", replays);
			renderJSON(RtnUtil.returnSuccess("OK",one));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void getBgMusic(){
		try {
			String url  = API.getAliOssAccessUrl("tasty", "99c78ff55ea64a708cf35ec9aa7bf5fa", 0);
			renderJSON(RtnUtil.returnSuccess("OK",url));
		} catch (Exception e) {
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
}
