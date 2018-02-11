package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import modules.forg.ddl.ForgActivityDDL;
import modules.forg.ddl.ForgMusicDDL;
import modules.forg.ddl.ForgReadingBooksDDL;
import modules.forg.ddl.ForgReadingContentShotDDL;
import modules.forg.ddl.ForgReadingRecordDDL;
import modules.forg.service.ForgActivityService;
import modules.forg.service.ForgBookService;
import modules.forg.service.ForgBookShotService;
import modules.forg.service.ForgBoolRecordService;
import modules.forg.service.ForgMusicService;
import modules.forg.service.ForgReadHisService;
import util.API;
import util.DateUtil;
import util.RtnUtil;
import util.ThreadUtil;

/**
 * 青蛙读本
 * @author fish
 *
 */
public class Forg extends Controller{
	/**
	 * 
	 * @param session
	 * @param bookName
	 * @param bookCover
	 * @param bookDesc
	 * @param shots  ,号分隔数组
	 */
	public static void createBooks(String session,String bookName,String bookCover,
			String bookDesc,String shots,int bookId,int musicId){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			ForgReadingBooksDDL book = ForgBookService.get(bookId);
			if(book != null){ 
				ForgBookService.updateBook(bookId, bookName, bookCover, bookDesc,musicId);
				ForgBookShotService.multiInsert((int)bookId, shots.split(",")); 
				renderJSON(RtnUtil.returnSuccess("OK",bookId));
			}
			bookId = (int)ForgBookService.createBook(bookName, bookCover, bookDesc, user.getNickName(), user.getAvatarUrl(), user.getId().intValue(),musicId);
			if(bookId>0){
				ForgBookShotService.multiInsert((int)bookId, shots.split(","));
				//后台任务发送推送消息
				final int book_id = bookId;
				final String book_name = bookName;
				ThreadUtil.sumbit(new Runnable(){ 
					@Override
					public void run() { 
						Map<String,Map> dataMap = new HashMap<String,Map>();
						
						Map<String,String> k1 = new HashMap<String,String>();
						k1.put("value",book_name);
						k1.put("color", "#121212"); 
					
						Map<String,String> k2 = new HashMap<String,String>();
						k2.put("value", "小青蛙运营专员");
						k2.put("color", "#838B8B");
						
						Map<String,String> k3 = new HashMap<String,String>();
						k3.put("value", DateUtil.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm"));
						k3.put("color", "#838B8B");
						
						Map<String,String> k4 = new HashMap<String,String>();
						k4.put("value", "赶紧去朗读吧，再晚沙发就被别人抢走了哦~");
						k4.put("color", "#0000EE");
						
						dataMap.put("keyword1", k1);
						dataMap.put("keyword2", k2);
						dataMap.put("keyword3", k3); 
						dataMap.put("keyword4", k4);
						String page = "pages/index/index?bookId="+book_id;
						
						String appId = Jws.configuration.getProperty("forg.appid");
						List<FormIdsDDL> list = FormIdService.listDistinct(appId);
						if(list==null || list.size() == 0){
							return ;
						}
						for(FormIdsDDL form : list){
							API.sendWxMessage(appId, form.getOpenId(), "aisJSMAnCekDS5AkCLu8r0RQUCvopchIh-I77xzAEWo", page, form.getFormId(), dataMap);
						} 
					} 
				});
				renderJSON(RtnUtil.returnSuccess("OK",bookId));
			}else{
				renderJSON(RtnUtil.returnFail("保存失败"));
			}			
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void getBook(String session,int bookId,boolean record,int shareUserId,boolean addBookFlow){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			ForgReadingBooksDDL book = ForgBookService.get(bookId);
			List<ForgReadingContentShotDDL> shotList = ForgBookShotService.listByBookId(bookId);
			if(book == null || shotList == null || shotList.size() == 0 ){
				renderJSON(RtnUtil.returnFail("读本不存在"));
			} 
			
			if(addBookFlow){
				book = ForgBookService.addBookFlow(book);
				//发微信消息
				final CookBookUsersDDL flowedUser = UserService.get(book.getUploaderUid());
				final String open_id = flowedUser.getOpenId(); 
				final int book_id = book.getId();
				final String book_name = book.getBookName();
				final String flow_name = user.getNickName();
				ThreadUtil.sumbit(new Runnable(){ 
					@Override
					public void run() { 
						//避免每次点赞都发，导致提醒过量，控制1小时收一条(一本书一个人)
						String key = "bookAddFlow_"+book_id+"_"+open_id;
						if(null != Cache.get(key)){
							return;
						}
						Map<String,Map> dataMap = new HashMap<String,Map>();
						
						Map<String,String> k1 = new HashMap<String,String>();
						k1.put("value",book_name);
						k1.put("color", "#121212"); 
					
						Map<String,String> k2 = new HashMap<String,String>();
						k2.put("value", flow_name);
						k2.put("color", "#838B8B");
						
						Map<String,String> k3 = new HashMap<String,String>();
						k3.put("value", "点击进入“小青蛙读本”小程序查看");
						k3.put("color", "#838B8B");
						
					 
						dataMap.put("keyword1", k1);
						dataMap.put("keyword2", k2);
						dataMap.put("keyword3", k3);
						
						String page = "pages/index/index?bookId="+book_id;
						
						String appId = Jws.configuration.getProperty("forg.appid");
						FormIdsDDL form = FormIdService.getOneForm(appId, open_id);
						if(form!=null){
							API.sendWxMessage(appId, form.getOpenId(), "lmcSlxTF1wFlBBMkR4ucTLfNRFoLhLEFqiXp2PKjPSM", page, form.getFormId(), dataMap);
							Cache.set(key, "1","1h");
						} 
					} 
				});
			}else{
				ForgReadHisService.replaceHis(user, book);
			} 
			
			Map<String,Object> bookMap = new HashMap<String,Object>();
			bookMap.put("id", book.getId());
			bookMap.put("bookName", book.getBookName());
			bookMap.put("bookCover", API.getAliOssAccessUrl("tasty", book.getBookCover(), 0));
			bookMap.put("bookDesc", book.getBookDesc());
			bookMap.put("recommed", book.getRecommed());
			bookMap.put("musicId", book.getMusicId()==null?-1:book.getMusicId()); 
			bookMap.put("uploaderUid", book.getUploaderUid());
			bookMap.put("uploaderNickname", book.getUploaderNickname());
			bookMap.put("uploaderAvatar", book.getUploaderAvatar());
			bookMap.put("flows", book.getFlows());
			bookMap.put("createDate", DateUtil.format(book.getCreateTime(), "yyyy-MM-dd"));
			
			
			 
			for(ForgReadingContentShotDDL shot : shotList){
				shot.setPageShot(API.getAliOssAccessUrl("tasty", shot.getPageShot(), 0));
			}
			Map<String,Object> result = new HashMap<String,Object>();
			//
			if(record){
				//得到自己的录音，放在列表第一位置
				ForgReadingRecordDDL selfRecord = ForgBoolRecordService.getByBookIdAndUid(bookId, user.getId().intValue());
				if(selfRecord!=null){
					Map<String,Object> selfRecordMap = new HashMap<String,Object>();
					selfRecordMap.put("id", selfRecord.getId());
					selfRecordMap.put("bookId", selfRecord.getBookId());
					selfRecordMap.put("userId", selfRecord.getUserId());
					selfRecordMap.put("recordUrl", API.getAliOssAccessUrl("tasty", selfRecord.getRecordUrl(), 0));
					selfRecordMap.put("nickName", selfRecord.getNickName());
					selfRecordMap.put("avatar", selfRecord.getAvatar());
					
					if(selfRecord.getDuration()==null || selfRecord.getDuration() == 0){
						selfRecordMap.put("duration", "0分10秒");
					}else{
						int duration = selfRecord.getDuration()==null?0:selfRecord.getDuration();
						selfRecordMap.put("duration", duration/60+"分"+duration%60+"秒");
					} 
					 
					selfRecordMap.put("flows", selfRecord.getFlows());
					selfRecordMap.put("playTimes", selfRecord.getPlayTimes());
					selfRecordMap.put("createTime", DateUtil.format(selfRecord.getCreateTime(), "yyyy-MM-dd"));
 					result.put("selfRecord", selfRecordMap);
				}
				//
				List<ForgReadingRecordDDL> otherRecords = ForgBoolRecordService.listOthers(bookId, user.getId().intValue(),shareUserId);
				List<Map<String,Object>> otherRecordsMap = new ArrayList<Map<String,Object>>();
				if(otherRecords!=null && otherRecords.size()>0){
					for(ForgReadingRecordDDL aRecord : otherRecords){
						Map<String,Object> othersRecordMap = new HashMap<String,Object>();
						othersRecordMap.put("id", aRecord.getId());
						othersRecordMap.put("bookId", aRecord.getBookId());
						othersRecordMap.put("userId", aRecord.getUserId());
						othersRecordMap.put("recordUrl", API.getAliOssAccessUrl("tasty", aRecord.getRecordUrl(), 0));
						othersRecordMap.put("nickName", aRecord.getNickName());
						othersRecordMap.put("avatar", aRecord.getAvatar());
						
						if(aRecord.getDuration()==null || aRecord.getDuration() == 0){
							othersRecordMap.put("duration", "0分10秒");
						}else{
							int duration = aRecord.getDuration()==null?0:aRecord.getDuration();
							othersRecordMap.put("duration", duration/60+"分"+duration%60+"秒");
						}
						
						othersRecordMap.put("flows", aRecord.getFlows());
						othersRecordMap.put("playTimes", aRecord.getPlayTimes());
						othersRecordMap.put("createTime", DateUtil.format(aRecord.getCreateTime(), "yyyy-MM-dd"));
						otherRecordsMap.add(othersRecordMap);
					}
					result.put("otherRecords", otherRecordsMap);
				}
			}	
			//得到分享用户的声音
			if(shareUserId > 0){
				//得到自己的录音，放在列表第一位置
				ForgReadingRecordDDL shareRecord = ForgBoolRecordService.getByBookIdAndUid(bookId, shareUserId);
				if(shareRecord!=null){
					Map<String,Object> shareRecordMap = new HashMap<String,Object>();
					shareRecordMap.put("id", shareRecord.getId());
					shareRecordMap.put("bookId", shareRecord.getBookId());
					shareRecordMap.put("userId", shareRecord.getUserId());
					shareRecordMap.put("recordUrl", API.getAliOssAccessUrl("tasty", shareRecord.getRecordUrl(), 0));
					shareRecordMap.put("nickName", shareRecord.getNickName());
					shareRecordMap.put("avatar", shareRecord.getAvatar());
					
					if(shareRecord.getDuration()==null || shareRecord.getDuration() == 0){
						shareRecordMap.put("duration", "0分10秒");
					}else{
						int duration = shareRecord.getDuration()==null?0:shareRecord.getDuration();
						shareRecordMap.put("duration", duration/60+"分"+duration%60+"秒");
					} 
					 
					shareRecordMap.put("flows", shareRecord.getFlows());
					shareRecordMap.put("playTimes", shareRecord.getPlayTimes());
					shareRecordMap.put("createTime", DateUtil.format(shareRecord.getCreateTime(), "yyyy-MM-dd"));
 					result.put("shareRecord", shareRecordMap);
				}
			}
			result.put("book", bookMap);
			result.put("shots", shotList);
			renderJSON(RtnUtil.returnSuccess("OK",result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listRecommedBooks(String session,int top){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			List<ForgReadingBooksDDL> list = ForgBookService.listRecommendBooks(top);
			if(list!=null && list.size()>0){
				for(ForgReadingBooksDDL book:list){
					Map<String,Object> bookMap = new HashMap<String,Object>();
					bookMap.put("id", book.getId());
					bookMap.put("bookName", book.getBookName());
					bookMap.put("bookCover", API.getAliOssAccessUrl("tasty", book.getBookCover(), 0));
					bookMap.put("bookDesc", book.getBookDesc());
					bookMap.put("recommed", book.getRecommed());
					bookMap.put("musicId", book.getMusicId()==null?-1:book.getMusicId()); 
					bookMap.put("uploaderUid", book.getUploaderUid());
					bookMap.put("uploaderNickname", book.getUploaderNickname());
					bookMap.put("uploaderAvatar", book.getUploaderAvatar());
					bookMap.put("flows", book.getFlows());
					bookMap.put("createDate", DateUtil.format(book.getCreateTime(), "yyyy-MM-dd")); 
					result.add(bookMap);
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK",result));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listLastBooks(String session,int page,int pageSize){
		try{ 
			if(pageSize==0 || pageSize > 100){
				pageSize = 100;
			}
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			List<ForgReadingBooksDDL> list = ForgBookService.listLastBooks(page, pageSize);
			if(list!=null && list.size()>0){
				
				Collections.sort(list,new Comparator<ForgReadingBooksDDL>(){
					@Override
					public int compare(ForgReadingBooksDDL o1, ForgReadingBooksDDL o2) {
						return o1.getId() - o2.getId();
					}
					
				});
				
				for(ForgReadingBooksDDL book:list){
					Map<String,Object> bookMap = new HashMap<String,Object>();
					bookMap.put("id", book.getId());
					bookMap.put("bookName", book.getBookName());
					bookMap.put("bookCover", API.getAliOssAccessUrl("tasty", book.getBookCover(), 0));
					bookMap.put("bookDesc", book.getBookDesc());
					bookMap.put("recommed", book.getRecommed());
					bookMap.put("musicId", book.getMusicId()==null?-1:book.getMusicId()); 
					bookMap.put("uploaderUid", book.getUploaderUid());
					bookMap.put("uploaderNickname", book.getUploaderNickname());
					bookMap.put("uploaderAvatar", book.getUploaderAvatar());
					bookMap.put("flows", book.getFlows());
					bookMap.put("createDate", DateUtil.format(book.getCreateTime(), "yyyy-MM-dd")); 
					result.add(bookMap);
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK",result));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listRandomBooks(String session ){
		try{   
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			List<ForgReadingBooksDDL> list = ForgBookService.listRandomBooks();
			if(list!=null && list.size()>0){
				for(ForgReadingBooksDDL book:list){
					Map<String,Object> bookMap = new HashMap<String,Object>();
					bookMap.put("id", book.getId());
					bookMap.put("bookName", book.getBookName());
					bookMap.put("bookCover", API.getAliOssAccessUrl("tasty", book.getBookCover(), 0));
					bookMap.put("bookDesc", book.getBookDesc());
					bookMap.put("recommed", book.getRecommed());
					bookMap.put("musicId", book.getMusicId()==null?-1:book.getMusicId()); 
					bookMap.put("uploaderUid", book.getUploaderUid());
					bookMap.put("uploaderNickname", book.getUploaderNickname());
					bookMap.put("uploaderAvatar", book.getUploaderAvatar());
					bookMap.put("flows", book.getFlows());
					bookMap.put("createDate", DateUtil.format(book.getCreateTime(), "yyyy-MM-dd")); 
					result.add(bookMap);
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK",result));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	
	public static void listHotBooks(String session,int page,int pageSize){
		try{  
			if(pageSize==0 || pageSize > 100){
				pageSize = 100;
			}
			
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			List<ForgReadingBooksDDL> list = ForgBookService.listHotBooks(page, pageSize);
			if(list!=null && list.size()>0){
				Collections.sort(list,new Comparator<ForgReadingBooksDDL>(){
					@Override
					public int compare(ForgReadingBooksDDL o1, ForgReadingBooksDDL o2) {
						return o1.getFlows() - o2.getFlows();
					}
					
				}); 
				for(ForgReadingBooksDDL book:list){
					Map<String,Object> bookMap = new HashMap<String,Object>();
					bookMap.put("id", book.getId());
					bookMap.put("bookName", book.getBookName());
					bookMap.put("bookCover", API.getAliOssAccessUrl("tasty", book.getBookCover(), 0));
					bookMap.put("bookDesc", book.getBookDesc());
					bookMap.put("recommed", book.getRecommed());
					bookMap.put("musicId", book.getMusicId()==null?-1:book.getMusicId()); 
					bookMap.put("uploaderUid", book.getUploaderUid());
					bookMap.put("uploaderNickname", book.getUploaderNickname());
					bookMap.put("uploaderAvatar", book.getUploaderAvatar());
					bookMap.put("flows", book.getFlows());
					bookMap.put("createDate", DateUtil.format(book.getCreateTime(), "yyyy-MM-dd")); 
					result.add(bookMap);
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK",result));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void recordCommit(String session,int bookId,String recordUrl,int recordTimingValue){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			
			if(StringUtils.isEmpty(recordUrl) || "null".equals(recordUrl.toLowerCase())){
				throw new Exception("上传失败"+recordUrl);
			}
			
			ForgReadingRecordDDL record = ForgBoolRecordService.getByBookIdAndUid(bookId, user.getId().intValue());
			if(record==null){
				ForgBoolRecordService.create(bookId, user.getId().intValue(), recordUrl, user.getNickName(), user.getAvatarUrl(),recordTimingValue);
			}else{
				ForgBoolRecordService.update(record, recordUrl,recordTimingValue);
			}
			renderJSON(RtnUtil.returnSuccess("OK"));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void recordAddFlow(String session,int recordId){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			int flows = ForgBoolRecordService.addFlow(recordId);
			ForgReadingRecordDDL record = ForgBoolRecordService.get(recordId);
			//发微信消息
			final CookBookUsersDDL flowedUser = UserService.get(record.getUserId());
			final String open_id = flowedUser.getOpenId();
			final int book_id = record.getBookId();
			
			ForgReadingBooksDDL book = ForgBookService.get(book_id);
			final String book_name = book==null?"有人给你录音点赞了":book.getBookName();
			final String flow_name = user.getNickName();
			ThreadUtil.sumbit(new Runnable(){ 
				@Override
				public void run() { 
					String key = "RecordAddFlow_"+book_id+"_"+open_id;
					if(null != Cache.get(key)){
						return;
					}					
					Map<String,Map> dataMap = new HashMap<String,Map>();
					
					Map<String,String> k1 = new HashMap<String,String>();
					k1.put("value","有人给您在《"+book_name+"》的朗读点赞了");
					k1.put("color", "#121212"); 
				
					Map<String,String> k2 = new HashMap<String,String>();
					k2.put("value", flow_name);
					k2.put("color", "#838B8B");
					
					Map<String,String> k3 = new HashMap<String,String>();
					k3.put("value", "点击进入“小青蛙读本”小程序查看");
					k3.put("color", "#838B8B");
					
				 
					dataMap.put("keyword1", k1);
					dataMap.put("keyword2", k2);
					dataMap.put("keyword3", k3);
					
					String page = "pages/index/index?bookId="+book_id+"&shareUserId="+flowedUser.getId();
					
					String appId = Jws.configuration.getProperty("forg.appid");
					FormIdsDDL form = FormIdService.getOneForm(appId, open_id);
					if(form!=null){
						API.sendWxMessage(appId, form.getOpenId(), "lmcSlxTF1wFlBBMkR4ucTLfNRFoLhLEFqiXp2PKjPSM", page, form.getFormId(), dataMap);
						Cache.set(key, "1", "1h");
					} 
				} 
			});
			
			renderJSON(RtnUtil.returnSuccess("OK",flows));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	public static void recordAddPlayTimes(String session,int recordId){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			int flows = ForgBoolRecordService.addPlayTimes(recordId);
			renderJSON(RtnUtil.returnSuccess("OK",flows));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listMusic(int type){
		try{ 
			List<Object> list = new ArrayList<Object>();
			List<ForgMusicDDL> musicList = ForgMusicService.listMusiceByType(type);
			if(musicList == null || musicList.size() == 0){
				renderJSON(RtnUtil.returnSuccess("OK",list));
			}
			for(ForgMusicDDL forgMusic : musicList){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", forgMusic.getId());
				map.put("name", forgMusic.getName());
				map.put("url", API.getAliOssAccessUrl("tasty",forgMusic.getOssKey(), 0));
				list.add(map);
			} 
			renderJSON(RtnUtil.returnSuccess("OK",list));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void getActivity(String session){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			ForgActivityDDL activity = ForgActivityService.getActivity();
			if(activity == null ){
				renderJSON(RtnUtil.returnSuccess("OK"));
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("closed", Boolean.valueOf(Jws.configuration.getProperty("activity.closed","true")));
			map.put("id", activity.getId());
			map.put("title", activity.getActTitle());
			map.put("startTime", DateUtil.format(activity.getStartTime()));
			map.put("endTime", DateUtil.format(activity.getEndTime()));
			
			List<String> descList = new ArrayList<String>();
			if(!StringUtils.isEmpty(activity.getActDesc1())){
				descList.add(activity.getActDesc1());
			}
			
			if(!StringUtils.isEmpty(activity.getActDesc2())){
				descList.add(activity.getActDesc2());
			}
			
			if(!StringUtils.isEmpty(activity.getActDesc3())){
				descList.add(activity.getActDesc3());
			}
			
			if(!StringUtils.isEmpty(activity.getActDesc4())){
				descList.add(activity.getActDesc4());
			}
			
			if(!StringUtils.isEmpty(activity.getActDesc5())){
				descList.add(activity.getActDesc5());
			}
			
			if(!StringUtils.isEmpty(activity.getActDesc6())){
				descList.add(activity.getActDesc6());
			}
			
			if(!StringUtils.isEmpty(activity.getActDesc7())){
				descList.add(activity.getActDesc7());
			}
			map.put("desc", descList);
			renderJSON(RtnUtil.returnSuccess("OK",map));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
		
	}
	
	public static void getRecordUrl(int recordId,String session){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			ForgReadingRecordDDL record = ForgBoolRecordService.get(recordId);
			String recordUrl = API.getAliOssAccessUrl("tasty", record.getRecordUrl(), 0);
			renderJSON(RtnUtil.returnSuccess("OK",recordUrl));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listBooks(String session,int hotOrLast,int jx,int page,int pageSize){
		try{  
			if(pageSize==0 || pageSize > 30){
				pageSize = 30;
			}
			
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			List<ForgReadingBooksDDL> list = ForgBookService.listBooks(hotOrLast,jx,page, pageSize);
			if(list!=null && list.size()>0){
				if(hotOrLast==1){
					Collections.sort(list,new Comparator<ForgReadingBooksDDL>(){
						@Override
						public int compare(ForgReadingBooksDDL o1, ForgReadingBooksDDL o2) {
							return o1.getFlows() - o2.getFlows();
						}
						
					});
				}else{
					Collections.sort(list,new Comparator<ForgReadingBooksDDL>(){
						@Override
						public int compare(ForgReadingBooksDDL o1, ForgReadingBooksDDL o2) {
							return o1.getId() - o2.getId();
						}
						
					});
				}
				for(ForgReadingBooksDDL book:list){
					Map<String,Object> bookMap = new HashMap<String,Object>();
					bookMap.put("id", book.getId());
					bookMap.put("bookName", book.getBookName());
					bookMap.put("bookCover", API.getAliOssAccessUrl("tasty", book.getBookCover(), 0));
					bookMap.put("bookDesc", book.getBookDesc());
					bookMap.put("recommed", book.getRecommed());
					bookMap.put("musicId", book.getMusicId()==null?-1:book.getMusicId()); 
					bookMap.put("uploaderUid", book.getUploaderUid());
					bookMap.put("uploaderNickname", book.getUploaderNickname());
					bookMap.put("uploaderAvatar", book.getUploaderAvatar());
					bookMap.put("flows", book.getFlows());
					bookMap.put("createDate", DateUtil.format(book.getCreateTime(), "yyyy-MM-dd")); 
					result.add(bookMap);
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK",result));		
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void main(String[] args){
		List<Integer> test = new ArrayList<Integer>();
		test.add(3);
		test.add(5);
		test.add(4);
		Collections.sort(test, new Comparator<Integer>(){ 
			@Override
			public int compare(Integer o1, Integer o2) { 
				return o1-o2;
			} 
		});
		for(Integer a : test){
			System.out.println(a);
		}
	}
}
