package controllers;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dto.forg.TotalRankDto;
import jws.Jws;
import jws.Logger;
import jws.cache.Cache;
import jws.mvc.Controller;
import modules.common.ddl.FormIdsDDL;
import modules.common.service.FormIdService;
import modules.cookbook.ddl.CookBookUsersDDL;
import modules.cookbook.service.UserService;
import modules.forg.ddl.ForgActivityDDL;
import modules.forg.ddl.ForgActivityRankDDL;
import modules.forg.ddl.ForgCollectDDL;
import modules.forg.ddl.ForgFollowDDL;
import modules.forg.ddl.ForgMiniAppDDL;
import modules.forg.ddl.ForgMusicDDL;
import modules.forg.ddl.ForgReadSpeedConfigDDL;
import modules.forg.ddl.ForgReadingBooksDDL;
import modules.forg.ddl.ForgReadingCommentsDDL;
import modules.forg.ddl.ForgReadingContentShotDDL;
import modules.forg.ddl.ForgReadingHistoryDDL;
import modules.forg.ddl.ForgReadingRecordDDL;
import modules.forg.ddl.ForgReadingRecordsDDL;
import modules.forg.ddl.ForgSuggestDDL;
import modules.forg.service.ForgActivityService;
import modules.forg.service.ForgBookRecordService;
import modules.forg.service.ForgBookService;
import modules.forg.service.ForgBookShotService;
import modules.forg.service.ForgCollectService;
import modules.forg.service.ForgFollowService;
import modules.forg.service.ForgMiniAppService;
import modules.forg.service.ForgMusicService;
import modules.forg.service.ForgReadCommentService;
import modules.forg.service.ForgReadHisService;
import modules.forg.service.ForgReadSpeedService;
import modules.forg.service.ForgSuggestService;
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
			String bookDesc,String shots,String wordCounts,int bookId,int musicId){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			ForgReadingBooksDDL book = ForgBookService.get(bookId);
			if(book != null){ 
				ForgBookService.updateBook(bookId, bookName, bookCover, bookDesc,musicId);
				ForgBookShotService.multiInsert((int)bookId, shots.split(","),wordCounts.split(",")); 
				renderJSON(RtnUtil.returnSuccess("OK",bookId));
			}
			bookId = (int)ForgBookService.createBook(bookName, bookCover, bookDesc, user.getNickName(), user.getAvatarUrl(), user.getId().intValue(),musicId);
			if(bookId>0){
				ForgBookShotService.multiInsert((int)bookId, shots.split(","),wordCounts.split(","));
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
							API.sendWxMessage(appId, form.getOpenId(), "0ZAEDZBQ_LJyODOaSG1LEQZULkBe_gX6Rpf6YjgrOuA", page, form.getFormId(), dataMap);
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
	
	public static void getOneReaderInfo(String session,int readId){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			ForgReadingRecordDDL record = ForgBookRecordService.get(readId);
			if(record==null){
				throw new Exception("不存在朗读信息,readId="+readId);
			}
			ForgReadingBooksDDL book = ForgBookService.get(record.getBookId());
			if(book==null){
				throw new Exception("不存在读本信息,bookId="+record.getBookId());
			}
			List<ForgReadingContentShotDDL> shots = ForgBookShotService.listByBookId(record.getBookId());
			if( shots==null || shots.size()==0){
				throw new Exception("读本不存在内容页,bookId="+record.getBookId());
			}
			
			ForgReadSpeedConfigDDL speedConfig = ForgReadSpeedService.getByUserId(user.getId().intValue());
 			int speakSpeed = speedConfig==null?Integer.parseInt(Jws.configuration.getProperty("speak.speed","100")):speedConfig.getSpeed();
			double perSec = new BigDecimal(new Double(speakSpeed/60d).toString())
					.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			
			
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("readId", record.getId());
			result.put("bookId", record.getBookId());
			result.put("bookName", book.getBookName());
			
			result.put("readUserId", record.getUserId());
			result.put("readerName", record.getNickName());
			result.put("readerAvatar", record.getAvatar());
			result.put("readerPlayTimes", record.getPlayTimes());
			result.put("readerFlows", record.getFlows());
			result.put("readTime", DateUtil.format(record.getCreateTime(),"yyyy-MM-dd HH时"));
			//封装shots及录音信息
			List<Map<String,Object>> shotList = new ArrayList<Map<String,Object>>();
			int total = shots.size();
			int comp = 0;
			for(ForgReadingContentShotDDL shot : shots){
				Map<String,Object> shotMap = new HashMap<String,Object>();
				shotMap.put("pageId", shot.getId());
				shotMap.put("pageNum", shot.getPageNum());
				
				shotMap.put("readTime", shot.getReadTime());
				if(shot.getWordCount()!=null && shot.getWordCount()>0){
					int readTime =  new BigDecimal(new Double(shot.getWordCount()/perSec).toString())
							.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
					shotMap.put("readTime", readTime);
				}
				
				shotMap.put("pageShot", API.getAliOssAccessUrl("tasty", shot.getPageShot(), 0));
				ForgReadingRecordsDDL voice = ForgBookRecordService.getByReadIdAndPageId(readId, shot.getId());
				if(voice == null || voice.getVoiceOss() == null){
					//throw new Exception("朗读音频不存在,readId="+readId+",pageId="+shot.getId());
					shotMap.put("voiceUrl", null);
				}else{
					shotMap.put("voiceUrl", API.getAliOssAccessUrl("tasty", voice.getVoiceOss(), 0));
					comp++;
				}	 
				shotList.add(shotMap);
			}
			NumberFormat numberFormat = NumberFormat.getInstance();  			  
	        // 设置精确到小数点后2位  	  
	        numberFormat.setMaximumFractionDigits(2);  	  
	        String percent = numberFormat.format((float) comp / (float) total * 100); 
	        result.put("percent",percent);
			result.put("shots",shotList);
			
			renderJSON(RtnUtil.returnSuccess("OK",result));
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
						k3.put("value", "点击进入小程序查看");
						k3.put("color", "#838B8B");
						
					 
						dataMap.put("keyword1", k1);
						dataMap.put("keyword2", k2);
						dataMap.put("keyword3", k3);
						
						String page = "pages/index/index?bookId="+book_id;
						
						String appId = Jws.configuration.getProperty("forg.appid");
						FormIdsDDL form = FormIdService.getOneForm(appId, open_id);
						if(form!=null){
							API.sendWxMessage(appId, form.getOpenId(), "pOYwTtS9FWwCZNh-l9VhsQxPiiLcWUVH_LmupVYW6Sg", page, form.getFormId(), dataMap);
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
			bookMap.put("readTimes", ForgBookRecordService.coutReaded(bookId));
			
			ForgReadSpeedConfigDDL speedConfig = ForgReadSpeedService.getByUserId(user.getId().intValue());
 			int speakSpeed = speedConfig==null?Integer.parseInt(Jws.configuration.getProperty("speak.speed","100")):speedConfig.getSpeed();
			double perSec = new BigDecimal(new Double(speakSpeed/60d).toString())
					.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			
			for(ForgReadingContentShotDDL shot : shotList){
				shot.setPageShot(API.getAliOssAccessUrl("tasty", shot.getPageShot(), 0));
				 
				if(shot.getWordCount()!=null && shot.getWordCount()>0){
					int readTime =  new BigDecimal(new Double(shot.getWordCount()/perSec).toString())
							.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
					shot.setReadTime(readTime);
				}
				
			}
			
			Map<String,Object> result = new HashMap<String,Object>();
			//
			if(record){
				//得到自己的录音，放在列表第一位置
				ForgReadingRecordDDL selfRecord = ForgBookRecordService.getByBookIdAndUid(bookId, user.getId().intValue());
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
			}	
			//得到分享用户的声音
			if(shareUserId > 0){
				//得到自己的录音，放在列表第一位置
				ForgReadingRecordDDL shareRecord = ForgBookRecordService.getByBookIdAndUid(bookId, shareUserId);
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
	
	public static void listRecord(String session,int bookId,int byLast,int page,int pageSize){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}//
			Map<String,Object> result = new HashMap<String,Object>();
			
			List<ForgReadingRecordDDL> otherRecords = ForgBookRecordService.listRecords(bookId,byLast,page,pageSize);
			List<Map<String,Object>> otherRecordsMap = new ArrayList<Map<String,Object>>();
			if(otherRecords!=null && otherRecords.size()>0){
				for(ForgReadingRecordDDL aRecord : otherRecords){
					Map<String,Object> othersRecordMap = new HashMap<String,Object>();
					ForgReadingRecordDDL ReaderInfo = ForgBookRecordService.get(aRecord.getId());
					othersRecordMap.put("id", aRecord.getId());
					othersRecordMap.put("playTimes", ReaderInfo==null?0:ReaderInfo.getPlayTimes());
					othersRecordMap.put("bookId", aRecord.getBookId());
					othersRecordMap.put("userId", aRecord.getUserId());
					othersRecordMap.put("recordUrl",  StringUtils.isEmpty(aRecord.getRecordUrl())?null:API.getAliOssAccessUrl("tasty", aRecord.getRecordUrl(), 0));
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
	
	public static void recordCommit(String session,int bookId,String recordUrl,int recordTimingValue,
			String pageIds,String voiceOsss
			){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			/*if(StringUtils.isEmpty(recordUrl) || "null".equals(recordUrl.toLowerCase())){
				throw new Exception("上传失败"+recordUrl);
			}*/			
			long readId = ForgBookRecordService.create(bookId, user.getId().intValue(), recordUrl, user.getNickName(), user.getAvatarUrl(),recordTimingValue);
			if(readId>0){
				String[] pageIdArray = pageIds.split(",");
				String[] voiceOssArray = voiceOsss.split(",");
				for(int i=0;i<pageIdArray.length;i++){
					ForgBookRecordService.replaceVoice(new Long(readId).intValue(), Integer.parseInt(pageIdArray[i]), voiceOssArray[i], 0, 0);
				}
			}else{
				throw new Exception("提交失败");
			}
			renderJSON(RtnUtil.returnSuccess("OK",readId));		
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
			
			String day = DateUtil.format(System.currentTimeMillis(),"yyyy-MM-dd");
			String key = day+"_"+recordId+"_"+user.getId();
			int count =  Cache.get(key)==null?0:Integer.parseInt(String.valueOf(Cache.get(key)));
			Logger.info("给%s点赞，当前缓存自增count为%s，缓存key=%s,Object=%s", recordId,count,key,Cache.get(key));
			
			if(count <= 0){
				int value = count +1;
				Cache.set(key, value, "24h");
				Logger.info("这里---》%s", Cache.get(key));
			}else if(count > 5){
				renderJSON(RtnUtil.returnFail("您今天已经点过赞了，明天再来吧~"));
			} 
			int value = count +1;
			Cache.set(key, value, "24h");
			
			int flows = ForgBookRecordService.addFlow(recordId);
			
			final ForgReadingRecordDDL record = ForgBookRecordService.get(recordId);
			ForgBookRecordService.logFlowed(record.getUserId(), record.getBookId(), recordId, record.getAvatar(),record.getNickName());
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
					k3.put("value", "点击进入小程序查看");
					k3.put("color", "#838B8B");
					
				 
					dataMap.put("keyword1", k1);
					dataMap.put("keyword2", k2);
					dataMap.put("keyword3", k3);
					
					
					String page = "pages/read/view?readId="+record.getId();
					
					String appId = Jws.configuration.getProperty("forg.appid");
					FormIdsDDL form = FormIdService.getOneForm(appId, open_id);
					if(form!=null){
						API.sendWxMessage(appId, form.getOpenId(), "pOYwTtS9FWwCZNh-l9VhsQxPiiLcWUVH_LmupVYW6Sg", page, form.getFormId(), dataMap);
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
			int flows = ForgBookRecordService.addPlayTimes(recordId);
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
	
	/**
	 * 正在进行的活动
	 * @param session
	 */
	public static void getActivity(String session,int actId){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("closed", Boolean.valueOf(Jws.configuration.getProperty("activity.closed","true")));
			
			ForgActivityDDL activity = ForgActivityService.getActivityById(actId);
			if(activity == null ){
				renderJSON(RtnUtil.returnSuccess("OK",map));
			}
			
			map.put("id", activity.getId());
			map.put("title", activity.getActTitle());
			map.put("startTime", DateUtil.format(activity.getStartTime(),"yyyy-MM-dd"));
			map.put("endTime", DateUtil.format(activity.getEndTime(),"yyyy-MM-dd"));
			//活动是否进行中
			map.put("status", activity.getEndTime()<=System.currentTimeMillis()?0:1);
			map.put("prizeExamplePic",API.getAliOssAccessUrl("tasty", activity.getPrizeExamplePic(), 0));
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
	public static void listRank(String session,int actId){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}			
			List<ForgActivityRankDDL> rankTop = new ArrayList<ForgActivityRankDDL>();
			List<ForgActivityRankDDL> rankList = ForgActivityService.listActivityBank(actId,1, 50);
			if(rankList!=null && rankList.size()>=1){
				rankTop.add(rankList.get(0));
			}
			if(rankList!=null && rankList.size()>=2){
				rankTop.add(rankList.get(1));
			}
			if(rankList!=null && rankList.size()>=3){
				rankTop.add(rankList.get(2));
			}
			ForgActivityRankDDL selfRank = ForgActivityService.listActivityBankByUserId(actId, user.getId().intValue());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("rankTop", rankTop);
			map.put("rankList", rankList);
			map.put("selfRank", selfRank);
			renderJSON(RtnUtil.returnSuccess("OK",map));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	/**
	 * 正在进行的活动
	 * @param session
	 */
	public static void listActivity(String session,int page,int pageSize){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			List<ForgActivityDDL> activities = ForgActivityService.listActivity(page, pageSize);
			if(activities == null || activities.size() == 0){
				renderJSON(RtnUtil.returnSuccess("OK"));
			}
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			
			for(ForgActivityDDL activity:activities){
				Map<String,Object> map = new HashMap<String,Object>();
				
				map.put("join", 0);
				ForgReadingRecordDDL record = ForgBookRecordService.getByBookIdAndUid(activity.getBookId(),user.getId().intValue());
				if( record != null ){
					map.put("join", 1);
					map.put("flows", record.getFlows());
				}
				//活动是否结束0=已经结束，1=进行中
				int status = 0;
				if( activity.getStartTime() < System.currentTimeMillis() && activity.getEndTime() > System.currentTimeMillis()){
					status = 1;					
				}else if(activity.getRank() == 1){//排行榜已经生成
					//出排行榜
					//List<ForgActivityRankDDL> rankList = ForgActivityService.listActivityBank(activity.getId(),1, 50);
					ForgActivityRankDDL selfRank = ForgActivityService.listActivityBankByUserId(activity.getId(),user.getId().intValue());
					//map.put("rankList", rankList);
					map.put("selfRank", selfRank);
				}
				map.put("rank", activity.getRank());
				map.put("status", status);
				map.put("bookId", activity.getBookId());
				map.put("id", activity.getId());
				map.put("cover", API.getAliOssAccessUrl("tasty", activity.getBookCover(), 0));
				map.put("title", activity.getActTitle());
				map.put("startTime", DateUtil.format(activity.getStartTime(),"yyyy-MM-dd"));
				map.put("endTime", DateUtil.format(activity.getEndTime(),"yyyy-MM-dd"));
				result.add(map);
			} 
			renderJSON(RtnUtil.returnSuccess("OK",result));
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
			ForgReadingRecordDDL record = ForgBookRecordService.get(recordId);
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
					bookMap.put("recordNumber", ForgBookRecordService.coutReaded(book.getId()));//录音数量
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
	
	public static void collect(String session,int bookId){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			ForgCollectDDL c = ForgCollectService.findByBookIdAndUserId(user.getId().intValue(), bookId);
			if(c != null){//取消收藏
				ForgCollectService.delete(c.getId());
				renderJSON(RtnUtil.returnSuccess("OK",0));		
			}
			ForgCollectService.add(user.getId().intValue(), bookId);
			renderJSON(RtnUtil.returnSuccess("OK",1));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void getCollectStatus(String session,int bookId){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			ForgCollectDDL c = ForgCollectService.findByBookIdAndUserId(user.getId().intValue(), bookId);
			if(c != null){//取消收藏 
				renderJSON(RtnUtil.returnSuccess("OK",1));		
			} 
			renderJSON(RtnUtil.returnSuccess("OK",0));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	
	public static void follow(String session,int followUserId){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			Map<String,Object> result = new HashMap<String,Object>();
			
			ForgFollowDDL c = ForgFollowService.find(user.getId().intValue(), followUserId);
			if(c != null){//取消关注
				ForgFollowService.delete(c.getId());
				result.put("followStatus", 0); 
			}else{
				ForgFollowService.add(user.getId().intValue(), followUserId);
				result.put("followStatus", 1); 
			} 
			result.put("follows", ForgFollowService.countFollower(user.getId().intValue()));
			renderJSON(RtnUtil.returnSuccess("OK",result));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void getFollowStatus(String session,int followUserId){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("followStatus", 0); 
			ForgFollowDDL c = ForgFollowService.find(user.getId().intValue(), followUserId);
			if(c != null){
				result.put("followStatus", 1);  	
			} 
			result.put("follows", ForgFollowService.countFollower(user.getId().intValue()));
			renderJSON(RtnUtil.returnSuccess("OK",result));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void comment(String session,final int readId,final int replyId,final String comments){
		try{ 
			final CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			ForgReadCommentService.add(user.getId().intValue(), readId, comments, replyId, user.getNickName(), user.getAvatarUrl());
			
			//留言回复
			if(replyId>0){
				ThreadUtil.sumbit(new Runnable(){ 
					@Override
					public void run() {  	
						try{
							Map<String,Map> dataMap = new HashMap<String,Map>();
							
							Map<String,String> k1 = new HashMap<String,String>();
							k1.put("value",user.getNickName());
							k1.put("color", "#40d37a"); 
						
							Map<String,String> k2 = new HashMap<String,String>();
							k2.put("value", comments);
							k2.put("color", "#000000");
							
							Map<String,String> k3 = new HashMap<String,String>();
							k3.put("value", DateUtil.format(System.currentTimeMillis()));
							k3.put("color", "#888888");
							
							Map<String,String> k4 = new HashMap<String,String>();
							k4.put("value", "点击进入小程序查看详细");
							k4.put("color", "#838B8B");
							
						 
							dataMap.put("keyword1", k1);
							dataMap.put("keyword2", k2);
							dataMap.put("keyword3", k3);
							dataMap.put("keyword4", k4);
							
							String page = "pages/read/view?readId="+readId;
							
							ForgReadingCommentsDDL replayComment = ForgReadCommentService.get(replyId);
							CookBookUsersDDL toUser  = UserService.get(replayComment.getUserId());
							String appId = Jws.configuration.getProperty("forg.appid");
							FormIdsDDL form = FormIdService.getOneForm(appId, toUser.getOpenId());
							if(form!=null){
								API.sendWxMessage(appId, form.getOpenId(), "gEIMk_eCi7M3Ap5VVEh6MfLIbNGcuxRcMGN4EjUzSps", page, form.getFormId(), dataMap);
							} 
						}catch(Exception e){
							Logger.error(e, e.getMessage());
						} 
					} 
				});
			}else{
				//留言通知读者
				ThreadUtil.sumbit(new Runnable(){ 
					@Override
					public void run() {  	
						try{
							Map<String,Map> dataMap = new HashMap<String,Map>();
							
							Map<String,String> k1 = new HashMap<String,String>();
							k1.put("value",user.getNickName()+"，给你留言了，点击查看详细");
							k1.put("color", "#40d37a");  
							
							Map<String,String> k2 = new HashMap<String,String>();
							k2.put("value", DateUtil.format(System.currentTimeMillis()));
							k2.put("color", "#888888");
							
							Map<String,String> k3 = new HashMap<String,String>();
							k3.put("value", comments);
							k3.put("color", "#000000");  
						 
							dataMap.put("keyword1", k1);
							dataMap.put("keyword2", k2);
							dataMap.put("keyword3", k3); 
							
							String page = "pages/read/view?readId="+readId;
							
							ForgReadingRecordDDL record = ForgBookRecordService.get(readId);
							CookBookUsersDDL toUser  = UserService.get(record.getUserId());
							String appId = Jws.configuration.getProperty("forg.appid");
							FormIdsDDL form = FormIdService.getOneForm(appId, toUser.getOpenId());
							if(form!=null){
								API.sendWxMessage(appId, form.getOpenId(), "BHOClmfeaSWNzFI4O1sq96K8kpZGWPMitNuPdS5J9BQ", page, form.getFormId(), dataMap);
							} 
						}catch(Exception e){
							Logger.error(e, e.getMessage());
						} 
					} 
				});
				
				//留言成功通知
				ThreadUtil.sumbit(new Runnable(){ 
					@Override
					public void run() {  	
						try{
							Map<String,Map> dataMap = new HashMap<String,Map>();
							
							Map<String,String> k1 = new HashMap<String,String>();
							k1.put("value",comments);
							k1.put("color", "#40d37a");  
							
							Map<String,String> k2 = new HashMap<String,String>();
							k2.put("value", DateUtil.format(System.currentTimeMillis()));
							k2.put("color", "#888888");
							 
						 
							dataMap.put("keyword1", k1);
							dataMap.put("keyword2", k2); 
							
							String page = "pages/read/view?readId="+readId;
							 
							String appId = Jws.configuration.getProperty("forg.appid");
							FormIdsDDL form = FormIdService.getOneForm(appId, user.getOpenId());
							if(form!=null){
								API.sendWxMessage(appId, form.getOpenId(), "W00X415f_i3UdvRn5OSIL1-xeFnLqsIr2mXhCOmPWXE", page, form.getFormId(), dataMap);
							} 
						}catch(Exception e){
							Logger.error(e, e.getMessage());
						} 
					} 
				});
			}
			renderJSON(RtnUtil.returnSuccess("OK"));	 
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listComments(String session,int readId,int page,int pageSize){
		try{  
			 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			Map<String,Object> response = new HashMap<String,Object>();
			response.put("total", ForgReadCommentService.count(readId));
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
			List<ForgReadingCommentsDDL> commentList = ForgReadCommentService.list(readId, page, pageSize);
			if(commentList!=null && commentList.size()>0 ){
				for(ForgReadingCommentsDDL c:commentList){
					Map<String,Object> oneComment = new HashMap<String,Object>();
					oneComment.put("id", c.getId());
					oneComment.put("nickName", c.getNickName());
					oneComment.put("avatar", c.getAvatar());
					oneComment.put("comments",c.getComment());
					oneComment.put("time", DateUtil.format(c.getCreateTime()));
					List<ForgReadingCommentsDDL> replyList = ForgReadCommentService.listReply(c.getId(), 1, -1);
					if(replyList!=null && replyList.size()>0){
						List<Map<String,Object>> replyResult = new ArrayList<Map<String,Object>>();
						for(ForgReadingCommentsDDL reply : replyList){
							Map<String,Object> onereply = new HashMap<String,Object>();
							onereply.put("id", reply.getId());
							onereply.put("nickName", reply.getNickName());
							onereply.put("avatar", reply.getAvatar());
							onereply.put("comments",reply.getComment());
							onereply.put("time", DateUtil.format(reply.getCreateTime()));
							replyResult.add(onereply);
						} 
						oneComment.put("replys", replyResult);
					}
					result.add(oneComment);
				}
			}
			response.put("comments", result);
			renderJSON(RtnUtil.returnSuccess("OK",response));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	} 
	
	
	public static void myspace(String session){
		try{  
			 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			
			//查询粉丝数量
			int userId = user.getId().intValue();
			int fans = ForgFollowService.countFollower(userId);
			//查询我关注的数量
			int followed = ForgFollowService.countFollowed(userId);
			
			//查询我朗读的数量
			int reads = ForgBookRecordService.coutReadedByUser(userId);
			//查询我收藏的数量
			int collects = ForgCollectService.countCollect(userId);
			//查询我的足迹数量
			int views = ForgReadHisService.countRead(userId);
			//是否有比赛
			
			boolean racing = false;
			if(ForgActivityService.getActivity() !=null && Boolean.valueOf(Jws.configuration.getProperty("activity.closed","true"))){
				racing = true;
			}
			
			//查询总榜，统计谁获得赞数最多
			TotalRankDto dto = ForgBookRecordService.getTotalRankByUserId(userId);
			
			Map<String,Object> response = new HashMap<String,Object>();
			response.put("fans", fans);
			response.put("followed", followed);
			response.put("reads", reads);
			response.put("collects", collects);
			response.put("views", views);
			response.put("racing", racing);
			response.put("rank", dto.getRank());
			
			renderJSON(RtnUtil.returnSuccess("OK",response));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	} 
	
	public static void getSetting(String session){
		try{  
			 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			
			int speed = Integer.parseInt(Jws.configuration.getProperty("speak.speed", "100"));
 			ForgReadSpeedConfigDDL config = ForgReadSpeedService.getByUserId(user.getId().intValue());
			if( config != null){
				speed = config.getSpeed();
			}
			renderJSON(RtnUtil.returnSuccess("OK",speed));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	public static void setReadSpeed(String session , int speed){
		try{  
			 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			
  			ForgReadSpeedService.add(user.getId().intValue(), speed);
			 
			renderJSON(RtnUtil.returnSuccess("OK"));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listFollower(String session , int page,int pageSize){
		try{   
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			List<Map<String,Object>> response = new ArrayList<Map<String,Object>>();
			List<ForgFollowDDL> list = ForgFollowService.list(user.getId().intValue(), page, pageSize);
			if(list != null && list.size() >0 ){
				for(ForgFollowDDL ff:list){
					CookBookUsersDDL who = UserService.get(ff.getUserId());
					if(who==null)continue;
					Map<String,Object> one = new HashMap<String,Object>();
					one.put("userId",who.getId());
					one.put("nickName",who.getNickName());
					one.put("avatar",who.getAvatarUrl());
					one.put("followTime",DateUtil.format(ff.getFollowTime()));
					//我是否也关注了ta
					ForgFollowDDL followTa = ForgFollowService.find(user.getId().intValue(), who.getId().intValue());
					if(followTa != null){
						one.put("both",true);
					}else{
						one.put("both",false);
					}
					response.add(one);
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK",response));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	//列出我关注了哪些人
	public static void listFollowed(String session , int page,int pageSize){
		try{   
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			List<Map<String,Object>> response = new ArrayList<Map<String,Object>>();
			List<ForgFollowDDL> list = ForgFollowService.listFollowed(user.getId().intValue(), page, pageSize);
			if(list != null && list.size() >0 ){
				for(ForgFollowDDL ff:list){
					CookBookUsersDDL who = UserService.get(ff.getFollowUserId());
					if(who==null)continue;
					Map<String,Object> one = new HashMap<String,Object>();
					one.put("userId",who.getId());
					one.put("nickName",who.getNickName());
					one.put("avatar",who.getAvatarUrl());
					one.put("followTime",DateUtil.format(ff.getFollowTime()));
					response.add(one);
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK",response));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	
	public static void listMyspaceBooks(String session,int otherUserId,int flag,int page,int pageSize){
		try{  
			if(pageSize==0 || pageSize > 10){
				pageSize = 10;
			}
			
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			int userId = user.getId().intValue();
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			
			if(flag == 1){
				List<ForgReadingRecordDDL> records= ForgBookRecordService.listMyRecords(userId, page, pageSize);
				if(records!=null && records.size()>0){
					for(ForgReadingRecordDDL record : records){
						ForgReadingBooksDDL book = ForgBookService.get(record.getBookId());
						if(book == null) continue;
						Map<String,Object> bookMap = new HashMap<String,Object>();
						bookMap.put("id", book.getId());
						bookMap.put("readId", record.getId());
						bookMap.put("bookName", book.getBookName());
						bookMap.put("bookCover", API.getAliOssAccessUrl("tasty", book.getBookCover(), 0));
						bookMap.put("bookDesc", book.getBookDesc());
						bookMap.put("time", DateUtil.format(record.getCreateTime())); 
						result.add(bookMap);
					}
				}
				renderJSON(RtnUtil.returnSuccess("OK",result));	
			}
			
			if(flag == 2){
				List<ForgCollectDDL>  collects= ForgCollectService.listMyCollects(userId, page, pageSize);
				if(collects!=null && collects.size()>0){
					for(ForgCollectDDL collect : collects){
						ForgReadingBooksDDL book = ForgBookService.get(collect.getBookId());
						if(book == null) continue;
						Map<String,Object> bookMap = new HashMap<String,Object>();
						bookMap.put("id", book.getId());
						bookMap.put("bookName", book.getBookName());
						bookMap.put("bookCover", API.getAliOssAccessUrl("tasty", book.getBookCover(), 0));
						bookMap.put("bookDesc", book.getBookDesc());
						bookMap.put("time", DateUtil.format(collect.getCollectTime())); 
						result.add(bookMap);
					}
				}
				renderJSON(RtnUtil.returnSuccess("OK",result));	
			}
			
			if(flag == 3){				
				List<ForgReadingHistoryDDL> hiss= ForgReadHisService.listMyLines(userId, page, pageSize);
				if(hiss!=null && hiss.size()>0){
					for(ForgReadingHistoryDDL his : hiss){
						ForgReadingBooksDDL book = ForgBookService.get(his.getBookId());
						if(book == null) continue;
						Map<String,Object> bookMap = new HashMap<String,Object>();
						bookMap.put("id", book.getId());
						bookMap.put("bookName", book.getBookName());
						bookMap.put("bookCover", API.getAliOssAccessUrl("tasty", book.getBookCover(), 0));
						bookMap.put("bookDesc", book.getBookDesc());
						bookMap.put("time", DateUtil.format(his.getReadTime())); 
						result.add(bookMap);
					}
				}
				renderJSON(RtnUtil.returnSuccess("OK",result));	
				
				
			}
			
			if(flag == 4){// 查询ta的朗读

				List<ForgReadingRecordDDL> records= ForgBookRecordService.listMyRecords(otherUserId, page, pageSize);
				if(records!=null && records.size()>0){
					for(ForgReadingRecordDDL record : records){
						ForgReadingBooksDDL book = ForgBookService.get(record.getBookId());
						if(book == null) continue;
						Map<String,Object> bookMap = new HashMap<String,Object>();
						bookMap.put("id", book.getId());
						bookMap.put("readId", record.getId());
						bookMap.put("bookName", book.getBookName());
						bookMap.put("bookCover", API.getAliOssAccessUrl("tasty", book.getBookCover(), 0));
						bookMap.put("bookDesc", book.getBookDesc());
						bookMap.put("time", DateUtil.format(record.getCreateTime())); 
						result.add(bookMap);
					}
				}
				renderJSON(RtnUtil.returnSuccess("OK",result));	 
			}
			 	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listTotalRank(String session,int page,int pageSize){
		try{  
			if(pageSize==0 || pageSize > 10){
				pageSize = 10;
			}
			
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}  
			List<TotalRankDto> list = ForgBookRecordService.listTotalRank(page, pageSize);
			renderJSON(RtnUtil.returnSuccess("OK",list));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		} 
	}
	
	public static void commitSuggest(String session,String content,String img,String formId){
		try{   
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}  
			ForgSuggestService.commit(user.getId().intValue(), content, img, formId);
			renderJSON(RtnUtil.returnSuccess("OK"));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		} 
	}
	
	public static void listSuggest(String session,int page,int pageSize){
		try{  
			
			if(pageSize==0 || pageSize > 10){
				pageSize = 10;
			}
			
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null || user.getId().intValue() != 1789){
				renderJSON(RtnUtil.returnLoginFail());
			}   
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			
			List<ForgSuggestDDL> list = ForgSuggestService.list(page, pageSize);
			if(list!=null && list.size()>0){
				for(ForgSuggestDDL suggest : list){
					Map<String,Object> one = new HashMap<String,Object>();
					if(!StringUtils.isEmpty(suggest.getImg())){
						one.put("img", API.getAliOssAccessUrl("tasty", suggest.getImg(), 0));
					}
					CookBookUsersDDL theUser = UserService.get(suggest.getUserId());
					one.put("id", suggest.getId());
					one.put("avatar", theUser.getAvatarUrl());
					one.put("nickName", theUser.getNickName());
					one.put("content", suggest.getContent());
					one.put("replayContent", suggest.getReplayContent());
					one.put("isReplay", suggest.getIsReplay()==null?0:suggest.getIsReplay());
					one.put("commitTime", DateUtil.format(suggest.getCreateTime()));
					one.put("replayTime", suggest.getReplayTime()==null?"":DateUtil.format(suggest.getReplayTime()));
					result.add(one);
				}
			}
 			renderJSON(RtnUtil.returnSuccess("OK",result));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		} 
	}
	
	public static void replaySuggest(String session,int id,String replayContent){
		try{  
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null || user.getId().intValue() != 1789){
				renderJSON(RtnUtil.returnLoginFail());
			}   
			ForgSuggestService.repaly(id, replayContent);
			//qEFuEshtaHg1LVUciyzyECoXw87T1K3MVhpYz0g1UuM
			final ForgSuggestDDL suggest = ForgSuggestService.get(id);
			ThreadUtil.sumbit(new Runnable(){ 
				@Override
				public void run() {  	
					try{
						Map<String,Map> dataMap = new HashMap<String,Map>();
						
						Map<String,String> k1 = new HashMap<String,String>();
						k1.put("value",suggest.getContent());
						k1.put("color", "#888888");  
						
						Map<String,String> k2 = new HashMap<String,String>();
						k2.put("value",suggest.getReplayContent() );
						k2.put("color", "#40d37a");
						
						Map<String,String> k3 = new HashMap<String,String>();
						k3.put("value", DateUtil.format(suggest.getCreateTime()));
						k3.put("color", "#000000");  
						
						
						Map<String,String> k4 = new HashMap<String,String>();
						k4.put("value", DateUtil.format(suggest.getReplayTime()));
						k4.put("color", "#000000");  
					 
						dataMap.put("keyword1", k1);
						dataMap.put("keyword2", k2);
						dataMap.put("keyword3", k3); 
						
						String page = "pages/list/list";
						
						String appId = Jws.configuration.getProperty("forg.appid");
						CookBookUsersDDL user = UserService.get(suggest.getUserId());
						API.sendWxMessage(appId, user.getOpenId(), "qEFuEshtaHg1LVUciyzyECoXw87T1K3MVhpYz0g1UuM", page, suggest.getFormId(), dataMap);
						 
					}catch(Exception e){
						Logger.error(e, e.getMessage());
					} 
				} 
			});
			
 			renderJSON(RtnUtil.returnSuccess("OK"));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		} 
	}
	
	public static void listMiniApp(int page,int pageSize){
		try{
			List<ForgMiniAppDDL> list = ForgMiniAppService.listMiniApps(page, pageSize);
			if(list!=null && list.size()>0){
				for(ForgMiniAppDDL mini : list){
					mini.setLogo(API.getAliOssAccessUrl("tasty", mini.getLogo(), 0));
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK",list));	
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void main(String[] args){
		/*List<Integer> test = new ArrayList<Integer>();
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
		}*/
		System.out.println("------->"+"2018-03-05".matches("\\d{4}-\\d{2}-\\d{2}"));
	}
}
