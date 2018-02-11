package controllers;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ning.http.util.DateUtil;

import controllers.dto.BaiduDishDto;
import controllers.dto.SubjectTemplateDto;
import controllers.dto.SubjectTemplateDto.Content;
import jws.Jws;
import jws.Logger;
import jws.cache.Cache;
import jws.http.Request;
import jws.http.Response;
import jws.http.sf.HTTP;
import jws.mvc.Controller;
import modules.cookbook.ddl.BaiduAiDishDDL;
import modules.cookbook.ddl.CookBookClassRelDDL;
import modules.cookbook.ddl.CookBookInfoDDL;
import modules.cookbook.ddl.CookBookMaterialDDL;
import modules.cookbook.ddl.CookBookMaterialSelectDDL;
import modules.cookbook.ddl.CookBookMemuCommentDDL;
import modules.cookbook.ddl.CookBookMemuFavorDDL;
import modules.cookbook.ddl.CookBookMenuDDL;
import modules.cookbook.ddl.CookBookMenuRelDDL;
import modules.cookbook.ddl.CookBookProcessDDL;
import modules.cookbook.ddl.CookBookSubjectDDL;
import modules.cookbook.ddl.CookBookTagDDL;
import modules.cookbook.ddl.CookBookUsersDDL;
import modules.cookbook.ddl.ShopOrderDDL;
import modules.cookbook.ddl.ShopProductDDL;
import modules.cookbook.ddl.ShopProductGroupDDL;
import modules.cookbook.ddl.UserAccountDDL;
import modules.cookbook.service.BaiduAccessTokenService;
import modules.cookbook.service.BaiduDishService;
import modules.cookbook.service.ClassService;
import modules.cookbook.service.CollectStatService;
import modules.cookbook.service.CookBookInfoService;
import modules.cookbook.service.CookBookRelService;
import modules.cookbook.service.FavorClassService;
import modules.cookbook.service.FavorStatService;
import modules.cookbook.service.MaterialSelectService;
import modules.cookbook.service.MaterialService;
import modules.cookbook.service.MenuCommentService;
import modules.cookbook.service.MenuFavorService;
import modules.cookbook.service.MenuService;
import modules.cookbook.service.ProcessService;
import modules.cookbook.service.RecommendService;
import modules.cookbook.service.ShopOrderService;
import modules.cookbook.service.ShopProductGroupService;
import modules.cookbook.service.ShopProductService;
import modules.cookbook.service.SmsService;
import modules.cookbook.service.SubjectService;
import modules.cookbook.service.TagService;
import modules.cookbook.service.UserAccountService;
import modules.cookbook.service.UserService;
import modules.cookbook.service.ViewStatService;
import modules.cookbook.service.dto.ChildClassDto;
import util.AES;
import util.API;
import util.FileUtil;
import util.IDUtil;
import util.MD5Util;
import util.RtnUtil;
import util.ThreadUtil;
import util.WXDecriptUtil;

public class CookBook extends Controller{
	
	public static void loginBySession(String session){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			UserAccountService.createBasicAccount(user.getId().intValue());
			renderJSON(RtnUtil.returnSuccess("OK", user.getSession()));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail("登录失败，请稍后再试"));
		}
	}
	public static void loginByMobileAndCode(String mobile,int code){
		try{
			CookBookUsersDDL user = UserService.login(mobile, code);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			renderJSON(RtnUtil.returnSuccess("OK", user.getSession()));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail("登录失败，请稍后再试"));
		}
	}
	
	public static void sendAutCode(String mobile){
		try{
			if(Cache.get("sendAutCode_"+mobile)!=null){
				renderJSON(RtnUtil.returnFail("发送短信验证码太频繁"));
			}
			int code = SmsService.sendAuthCode(mobile);
			if(code==0){
				renderJSON(RtnUtil.returnFail("发送短信验证码失败,请稍后再试"));
			}
			Cache.set("sendAutCode_"+mobile, "1", "60s");
			renderJSON(RtnUtil.returnSuccess("OK", code));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail("发送短信验证码失败,请稍后再试"));
		}
	}
	/**
	 *
	 * @param nickName
	 * @param avatarUrl
	 * @param gender
	 * @param province
	 * @param city
	 * @param country
	 */
	public static void getUserSession(String session,String nickName ,String avatarUrl ,int gender ,String province ,String city ,String country ){
		CookBookUsersDDL userInfo = UserService.updateUser(session, avatarUrl, city, country, gender, province, nickName,"");
		renderJSON(RtnUtil.returnSuccess("OK",userInfo));
	}
	
	public static void loginByWeixin(String session,String code,String rawData,String encryptedData,
			String signature,String iv,String appid){ 
		
		if(StringUtils.isEmpty(code) || StringUtils.isEmpty(rawData) || StringUtils.isEmpty(iv)
				|| StringUtils.isEmpty(encryptedData)|| StringUtils.isEmpty(signature) || StringUtils.isEmpty(appid)){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		
		try{
 			String queryString = String.format("?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
 					appid,
 					Jws.configuration.getProperty(appid+".secret"),
 					code);
			Request request = new Request("wx","session",queryString);
			Response response = HTTP.GET(request);
			if(response.getStatusCode()!=200){
				renderJSON(RtnUtil.returnFail("请求微信获取sessionKey失败，http状态错误"));
			} 
			Logger.info("response form wx %s", response.getContent());
			JsonObject obj = new JsonParser().parse(response.getContent()).getAsJsonObject();
			String sessionKey = obj.get("session_key").getAsString();
			String signStr = rawData+sessionKey;
			String mySignature =WXDecriptUtil.SHA1(signStr);
			if(!mySignature.equals(signature)){
				Logger.error("SHA1 签名串 %s,mySignature %s ,youSignature %s",signStr, mySignature,signature);
				renderJSON(RtnUtil.returnFail("请求微信获取sessionKey失败，数据不完整"));
			}
			byte[] encryptedDataBase64Decoder = Base64.decodeBase64(encryptedData);
			byte[] sessionKeyBase64Decoder = Base64.decodeBase64(sessionKey);
			byte[] ivBase64Decoder = Base64.decodeBase64(iv);
			AES aes = new AES();
			byte[] aseBytes = aes.decrypt(encryptedDataBase64Decoder, sessionKeyBase64Decoder, ivBase64Decoder);
			String decryptedData = new String(WXDecriptUtil.decode(aseBytes));
			Logger.info("after ecrypt4Base64 %s",decryptedData);
			JsonObject userDataJson = new JsonParser().parse(decryptedData).getAsJsonObject();
			
			String avatarUrl = userDataJson.get("avatarUrl").getAsString();
			int gender = userDataJson.get("gender").getAsInt();
			String nickName = userDataJson.get("nickName").getAsString();
			String city = userDataJson.get("city").getAsString();
			String province = userDataJson.get("province").getAsString();
			String country = userDataJson.get("country").getAsString();
			String openId =  userDataJson.get("openId").getAsString();
			
			CookBookUsersDDL userInfo = UserService.updateUser(session, avatarUrl, city, country, 
					gender, province, nickName,openId);
			
			UserAccountService.createBasicAccount(userInfo.getId().intValue());
			
			renderJSON(RtnUtil.returnSuccess("ok",userInfo));
		}catch(Exception  e){
			Logger.error(e, "");
			renderJSON(RtnUtil.returnFail("请求微信获取sessionKey失败"));
		} 
	}
	
	public static void recommendByTime(String session,int number){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int classid = 0;
			String desc = "";
			if( hour>20 && hour <=23){//夜宵
				classid=566;
				desc="夜宵";
			}else if(hour >=0 && hour <=4){//夜宵
				classid=566;
				desc="夜宵";
			}else if(hour>=5 && hour<=9){//早餐
				classid=562;
				desc="早餐";
			}else if(hour >9 && hour<=12){//午餐
				classid=563;
				desc="午餐";
			}else if(hour >=13 && hour<=16){//下午茶
				classid=564;
				desc="下午茶";
			}else if(hour >16 && hour<=20){//晚餐
				classid=565;
				desc="晚餐";
			}
			List<CookBookInfoDDL> list = RecommendService.recommendByClass(user.getId(), classid, number);
			List<Map<String,Object>> toDtos = new ArrayList<Map<String,Object>>();
			
			if(list != null && list.size()>0){
				for(CookBookInfoDDL book:list){
					Map<String,Object> toDto = new HashMap<String,Object>();
					toDto.put("name", book.getName());
					toDto.put("bookId", book.getCookBookInfoId());
					toDto.put("pic", book.getPic());
					List<CookBookTagDDL> tags = TagService.listTags(book.getCookBookInfoId());
					toDto.put("tags", tags); 
					toDtos.add(toDto);
				}
			}
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("classid", classid);
			result.put("desc", desc);
			result.put("list", toDtos);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail());
		}
	}
	
	public static void recommendByFavor(String session,int number){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			List<CookBookInfoDDL> list = RecommendService.recommendByUser(user.getId(), number);
			
			List<Map<String,Object>> toDtos = new ArrayList<Map<String,Object>>();
			
			if(list != null && list.size()>0){
				for(CookBookInfoDDL book:list){
					Map<String,Object> toDto = new HashMap<String,Object>();
					toDto.put("name", book.getName());
					toDto.put("bookId", book.getCookBookInfoId());
					toDto.put("pic", book.getPic());
					List<CookBookTagDDL> tags = TagService.listTags(book.getCookBookInfoId());
					toDto.put("tags", tags); 
					int favor = FavorStatService.sumFavorValue(book.getCookBookInfoId());
					toDto.put("favor", favor);
					/*int collect = CollectStatService.sumCollectNum(book.getCookBookInfoId());
					toDto.put("collect", collect);*/
					int view = ViewStatService.sumViewNum(book.getCookBookInfoId());
					toDto.put("view", view);
					
					toDtos.add(toDto);
				}
			}			
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("list", toDtos);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail());
		} 
	}
	
	public static void getCookBookInfo(final int cookBookId){
		try{
			CookBookInfoDDL cookBook = CookBookInfoService.findByBookId(cookBookId);
			if(cookBook==null){
				renderJSON(RtnUtil.returnFail());
			}
			List<CookBookTagDDL> tags = TagService.listTags(cookBook.getCookBookInfoId());
			int favor = FavorStatService.sumFavorValue(cookBook.getCookBookInfoId());
			int collect = CollectStatService.sumCollectNum(cookBook.getCookBookInfoId());
			int view = ViewStatService.sumViewNum(cookBook.getCookBookInfoId());
			
			ThreadUtil.sumbit(new Runnable(){
				@Override
				public void run() { 
					ViewStatService.view(cookBookId, 20);
				}
			}); 
			
			List<CookBookProcessDDL> process = ProcessService.listByCookBookId(cookBook.getCookBookInfoId());
			List<CookBookMaterialDDL> material = MaterialService.listByCookBookId(cookBook.getCookBookInfoId());
			
			Map<String,Object> result = new HashMap<String,Object>();
			 
			result.put("cookBook", cookBook);
			result.put("tags", tags);
			result.put("view", view);
			result.put("collect", collect);
			result.put("favor", favor);
			result.put("process", process);
			result.put("material", material);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail());
		}
	}	
	
	public static void listMyMenu(String session){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			List<CookBookMenuDDL> list = MenuService.findMyMenuList(new Long(user.getId()).intValue());
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			if(list!=null && list.size()>0){
				for(CookBookMenuDDL cbm:list){
					Map<String,Object> dto = new HashMap<String,Object>();
					int count = MenuService.countCookBookInMenu(cbm.getId());
					dto.put("count", count);
					dto.put("menuName", cbm.getMenuName()); 
					dto.put("banner",API.getAliOssAccessUrl("tasty", cbm.getKey(), 60)); 
					dto.put("id", cbm.getId()); 
					result.add(dto);
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void addMenu(String session,String name,String key,String introduction,int type){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			boolean result = MenuService.addMenu(name, new Long(user.getId()).intValue(),key,introduction,type);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	public static void delMenu(String session,int menuId){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			boolean result = MenuService.deleteMenu(menuId);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listCookBookByMenuId(String session,int menuId){
		try{
			Map<String,Object> result = new HashMap<String,Object>();
			CookBookMenuDDL menu = MenuService.getMenu(menuId);
			List<CookBookMenuRelDDL> menuRels =  MenuService.listByMenu(menuId);
			CookBookUsersDDL user = UserService.get(menu.getUserId());
			
			CookBookUsersDDL master = UserService.findBySession(session);
			
			menu.setKey(API.getAliOssAccessUrl("tasty", menu.getKey(), 60));
			result.put("menu", menu);
			result.put("user", user);
			
			if(master!=null && master.getId().intValue() == menu.getUserId().intValue()){
				result.put("admin", true);
			}
			
			if(menuRels==null || menuRels.size() == 0){
				renderJSON(RtnUtil.returnSuccess("OK", result));
			}
			
			List<Map<String,Object>> toDtos = new ArrayList<Map<String,Object>>();
			for(CookBookMenuRelDDL mr:menuRels){
				CookBookInfoDDL book = CookBookInfoService.findByBookId(mr.getCookBookId());
				if(book==null)continue;
				Map<String,Object> toDto = new HashMap<String,Object>();
				toDto.put("introduction",mr.getIntroduction());
				toDto.put("name", book.getName());
				toDto.put("bookId", book.getCookBookInfoId());
				toDto.put("pic", book.getPic());
				List<CookBookTagDDL> tags = TagService.listTags(book.getCookBookInfoId());
				toDto.put("tags", tags);
				toDtos.add(toDto);
			} 
			
			result.put("menus", toDtos);			
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void addInMenu(String session,int menuId,int cookBookId){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			boolean result = MenuService.addInMenue(menuId, cookBookId);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void packMaterial(String session , int menuId){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			
			CookBookMenuDDL Menu = MenuService.getMenu(menuId);
			if(Menu.getUserId().intValue() != user.getId().intValue()){
				renderJSON(RtnUtil.returnFail("未登录"));
			}
			
			boolean result = MaterialSelectService.createMaterialList(menuId);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listPackMaterial(int menuId){
		try{
			CookBookMenuDDL menu = MenuService.getMenu(menuId);
			List<CookBookMaterialSelectDDL> list = MaterialSelectService.list(menuId);
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("menu", menu);
			result.put("list", list);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void markMaterial(int materialId){
		try{
			boolean result = MaterialSelectService.markMaterialSelect(materialId);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	} 
	
	public static void deleteCookBookFromMenu(String session,int cookBookId,int menuId){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			
			CookBookMenuDDL Menu = MenuService.getMenu(menuId);
			if(Menu.getUserId().intValue() != user.getId().intValue()){
				renderJSON(RtnUtil.returnFail("未登录"));
			}
			
			boolean result = MenuService.deleteCookBookFromMenue(menuId, cookBookId);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void hotCookBook(){
		try{
			List<Integer> cookBookIds = ViewStatService.hotCookBookId();
			List<CookBookInfoDDL> list = CookBookInfoService.findByBookIds(cookBookIds);
			List<String> hotkeys = new ArrayList<String>();
			if(list!=null&&list.size()>0){
				for(CookBookInfoDDL a : list){
					hotkeys.add(a.getName());
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK", hotkeys));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void search(String key,int classId){
		try{
			List<CookBookInfoDDL> list = CookBookInfoService.search(key, classId);
			List<Map<String,Object>> toDtos = new ArrayList<Map<String,Object>>();
			
			if(list != null && list.size()>0){
				for(CookBookInfoDDL book:list){
					Map<String,Object> toDto = new HashMap<String,Object>();
					toDto.put("name", book.getName());
					toDto.put("bookId", book.getCookBookInfoId());
					toDto.put("pic", book.getPic());
					List<CookBookTagDDL> tags = TagService.listTags(book.getCookBookInfoId());
					toDto.put("tags", tags); 					
					toDtos.add(toDto);
				}
			}			
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("list", toDtos);
			renderJSON(RtnUtil.returnSuccess("OK", result));
 		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		} 
	}
	
	public static void listSubject(){
		try{
			List<CookBookSubjectDDL> list = SubjectService.listSubject();
			List<Map<String,Object>> toDtos = new ArrayList<Map<String,Object>>();
			if(list != null && list.size()>0){
				for(CookBookSubjectDDL subject:list){
					Map<String,Object> toDto = new HashMap<String,Object>();
					toDto.put("id", subject.getId());
					toDto.put("title", subject.getTitle());
					toDto.put("subTitle", subject.getSubTitle());
 					String ossUrl = API.getAliOssAccessUrl("tasty", subject.getBannerKey(), 60);
					toDto.put("bgPicUrl",ossUrl);
					toDto.put("type", subject.getType());
					toDto.put("createTime",DateUtil.formatDate(new Date(subject.getCreateTime()), "yyyy-MM-dd") );
					toDtos.add(toDto);
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK", toDtos));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void subjectDetail(int subjectId){
		try{
			
			CookBookSubjectDDL subject = SubjectService.getSubject(subjectId);
			if(subject==null){
				renderText("没有此活动");
			}
			if(subject.getType() == 1){//分类模版
				
			}
			
			if( subject.getType() == 2){//关键字查询模版
				String jsonData = subject.getData();
				SubjectTemplateDto content = new Gson().fromJson(jsonData, SubjectTemplateDto.class);
				String title = subject.getTitle();
				String keywords = subject.getKeywords();
				String description = subject.getDescription();
				String bannerUrl = API.getAliOssAccessUrl("tasty", subject.getBannerKey(), 60).toString();
				String subTitle = subject.getSubTitle();
				String qrcode = API.getAliOssAccessUrl("tasty", content.getQrcode(), 60);
				content.setQrcode(qrcode);
				
				List<Content> contents = content.getContents();
				if(contents != null && contents.size()>0 ){
					for(Content c : contents){
						List<String> pics = c.getPics();
						if(pics==null || pics.size() == 0) continue;
						for(int i=0;i<pics.size();i++){
							String pickey = pics.get(i);
							if(pickey.startsWith("http")){
								continue;
							}
							pics.set(i, API.getAliOssAccessUrl("tasty", pickey, 60).toString());
						}
					}
				}
				
				renderTemplate("/subject/subjectTemplate1.html",content,title,subTitle,keywords,description,bannerUrl);
			}
			
			renderText("没有此活动");
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderText("服务器异常");
		}
	}
	
	
	public static void listByClass(int classcid,int classpid){
		try{
			List<Map<String,Object>> toDtos = new ArrayList<Map<String,Object>>();
			List<CookBookClassRelDDL> rels = CookBookRelService.findByClassIdOffset(classcid,classpid, 0, -1);
			
			if(rels==null || rels.size()==0){
				renderJSON(RtnUtil.returnSuccess("OK", toDtos));
			}
			
			List<Integer> cookBookIds = new ArrayList<Integer>();
			for(CookBookClassRelDDL rel : rels){
				cookBookIds.add(rel.getCookBookId());
			}
			List<CookBookInfoDDL> list = CookBookInfoService.findByBookIds(cookBookIds);
			
			if(list != null && list.size()>0){
				for(CookBookInfoDDL book:list){
					Map<String,Object> toDto = new HashMap<String,Object>();
					toDto.put("name", book.getName());
					toDto.put("bookId", book.getCookBookInfoId());
					toDto.put("pic", book.getPic());
					List<CookBookTagDDL> tags = TagService.listTags(book.getCookBookInfoId());
					toDto.put("tags", tags);  
					toDtos.add(toDto);
				}
			}	 
			renderJSON(RtnUtil.returnSuccess("OK", toDtos));
 		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		} 
	}
	
	public static void favor(String session,final int cookBookId){
		try{
			 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			final int userId = user.getId().intValue();
			final List<CookBookClassRelDDL> rels = CookBookRelService.findByBookId(cookBookId);
			
			if(rels!=null && rels.size()>0){
				ThreadUtil.sumbit(new Runnable(){
					@Override
					public void run() { 
						for(CookBookClassRelDDL rel : rels){
							FavorClassService.favor(userId, rel.getClassChildId());
						}  
						FavorStatService.favor(cookBookId, 10);
					}
				});
			}  
			renderJSON(RtnUtil.returnSuccess("OK"));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void searchPic(File file){
		try{
			byte[] imgData = FileUtil.readFileByBytes(file.getAbsolutePath());
			String md5 = MD5Util.md5(imgData);
			BaiduAiDishDDL dish = BaiduDishService.getByMd5(md5);
			BaiduDishDto dto = null;
			if( dish!=null ){
				dto = new Gson().fromJson(dish.getResult(), BaiduDishDto.class);
			}else{
				JsonObject json = API.dishBaidu(BaiduAccessTokenService.get().getToken(), imgData, 5);
				BaiduDishService.add(md5, "", json.toString());
				dto = new Gson().fromJson(json, BaiduDishDto.class);
			} 
			renderJSON(RtnUtil.returnSuccess("OK",dto));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listChildClass(int classpid){
		try{
			 List<ChildClassDto> list =	ClassService.listByClasspid(classpid);
			 if(list==null || list.size()==0){
				 renderJSON(RtnUtil.returnSuccess("OK",list));
			 }
			 List<Object> groups = new ArrayList<Object>();
			 List<ChildClassDto> group = new ArrayList<ChildClassDto>();
			 for(int i=1;i<=list.size();i++){
				 if( i%5==0 ){
					 group.add(list.get(i-1));
					 groups.add(group);
					 group = new ArrayList<ChildClassDto>();
				 }else{
					 group.add(list.get(i-1));
				 }
			 }
			 if(group.size()>0){
				 groups.add(group);
			 }
			 renderJSON(RtnUtil.returnSuccess("OK",groups));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	} 
	
	public static void uploadFile(String session,File file){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			String objectKey = API.uploadToAliOss("tasty", file);
			renderJSON(RtnUtil.returnSuccess("OK",objectKey));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	
	public static void commentMenu(String session,int menuId,String comment){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			boolean result = MenuCommentService.addComment(new Long(user.getId()).intValue(), menuId, user.getNickName(), user.getAvatarUrl(), comment);
			if( !result ){
				renderJSON(RtnUtil.returnFail("评论失败"));
			}
			renderJSON(RtnUtil.returnSuccess("OK"));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void favorMenu(String session,int menuId,boolean favored){
		try{ 
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			MenuFavorService.addFavor(new Long(user.getId()).intValue(), menuId, user.getNickName(), user.getAvatarUrl(), favored?1:0);
			renderJSON(RtnUtil.returnSuccess("OK"));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listMenuComments(int menuId,int page,int num){
		try{ 
			Map<String,Object> result = new HashMap<String,Object>();
			List<CookBookMemuCommentDDL>  comments = MenuCommentService.listByMenuId(menuId, page, num);
			List<CookBookMemuFavorDDL>  favors = MenuFavorService.listByMenuId(menuId, 1, 100);
			result.put("comments", comments);
			result.put("favors", favors);
			renderJSON(RtnUtil.returnSuccess("OK",result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listShareMenu(int minId,int lastId,int num){
		try{			
			if(num==0)num=10;
			
			List<CookBookMenuDDL> list = MenuService.listShareMenus(minId,lastId,num);
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			if(list!=null && list.size()>0){
				for(CookBookMenuDDL cbm:list){
					Map<String,Object> dto = new HashMap<String,Object>();					
					if(cbm.getType() == 0){
						dto.put("menuName", cbm.getMenuName()); 
						dto.put("banner",API.getAliOssAccessUrl("tasty", cbm.getKey(), 60)); 
					}
					if(cbm.getType() == 1 ){						
						if(!StringUtils.isEmpty(cbm.getKey())){
							String[] pics = cbm.getKey().split(",");
							for(int i=0;i<pics.length;i++){
								pics[i] = API.getAliOssAccessUrl("tasty", pics[i], 120);
							}
							dto.put("pics",pics); 
						}
						dto.put("introduction", cbm.getIntroduction()); 
					}
					dto.put("type", cbm.getType());
					dto.put("id", cbm.getId()); 
					dto.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(cbm.getCreateTime()))); 
					CookBookUsersDDL user = UserService.get(cbm.getUserId());
					dto.put("user", user); 
					List<CookBookMemuCommentDDL> comments = MenuCommentService.listByMenuId(cbm.getId(), 1, 5);
					dto.put("comments", comments); 
					List<CookBookMemuFavorDDL> favors = MenuFavorService.listByMenuId(cbm.getId(), 1, 8);
					dto.put("favors", favors); 
					result.add(dto);
				}
			}
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	
	public static void sendMoment(String session,String name,String keys,String introduction,int type){
		try{
			name = "用户动态"+System.currentTimeMillis();
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			if(!StringUtils.isEmpty(keys)){
				keys = keys.replaceAll("\"", "");
				keys = keys.replaceAll("\\[", "");
				keys = keys.replaceAll("\\]", "");
			}
			boolean result = false;
			result = MenuService.addMenu(name, new Long(user.getId()).intValue(),keys,introduction,type);
			renderJSON(RtnUtil.returnSuccess("OK", result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	} 
}
