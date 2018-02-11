package mjh;

import java.util.List;
import java.util.Random;

import org.bouncycastle.jce.provider.JCEMac.MD5;

import jws.Logger;
import jws.cache.Cache;
import modules.cookbook.ddl.CookBookMenuDDL;
import modules.cookbook.ddl.CookBookUsersDDL;
import modules.cookbook.service.MenuCommentService;
import modules.cookbook.service.MenuFavorService;
import modules.cookbook.service.MenuService;
import modules.cookbook.service.UserService;
import util.MD5Util;

public class MjhCommentMenu {
	
	final static String[] comments = {"流口水了，太香了","香","超赞","不错先收藏起来",
			"必须分享给好友","真的是这样吗，我怀疑小编的水准","好吃","美味",
			"挺有食欲的","就那样吧","很想学学怎么做的","要是我老婆每天给我做就好了",
			"别人家的食谱","真心感谢小编","有没有人是营养师的","加我qq157085863",
			"搭配的还不错","我是那种不被照片迷惑的吃货","这个菜谱很靠谱","谢谢哈 待会就想做了 谢谢分享",
			"好吃好吃！赞！","感谢人民，感谢组织","馋的流口水！","嘿哈","呵呵","丢",
			"这人气","小编联系方式留一个","我爱萝卜青菜"};
	
	public static void comment(int menuId){
		//
		int flag = 0;
		List<CookBookUsersDDL> users = UserService.findMJH();			
		int userSize = users.size();
		int userIndex =  new Random().nextInt(userSize);
		CookBookUsersDDL user = users.get(userIndex);		
		
		if(null != Cache.get("user_"+user.getId())){
			return ;
		}
		
		//同一个菜单最多马甲评论20条
		if( null !=Cache.get("count_"+menuId) && Integer.parseInt(String.valueOf(Cache.get("count_"+menuId))) > 20){
			return;
		}
		Cache.set("user_"+user.getId(), "1", "24h");		
	    //模拟用户进入 间隔1~5分钟进入一个用户		 
		//模拟用户随机访问一个menu
		/*int menuSize = menus.size();
		int menuIndex = new Random().nextInt(menuSize);
		int menuId = menus.get(menuIndex).getId();*/
		if(new Random().nextInt(3) == 1){
			 String comment = comments[new Random().nextInt(comments.length)];
			 String commentKey = MD5Util.md5(comment);
			 if(null == Cache.get(commentKey)){
				 MenuCommentService.addComment(user.getId().intValue(), menuId, user.getNickName(), user.getAvatarUrl(), comment);
				 flag++;
				 Cache.set(commentKey, "1", "24h");	
				 if( null !=Cache.get("count_"+menuId) ){
					 Cache.set("count_"+menuId,Integer.parseInt( Cache.get("count_"+menuId).toString() )+1,"24h"); 
				 }else{
					 Cache.set("count_"+menuId,1,"24h");
				 } 
			 }
		 }		
		 if(new Random().nextInt(2) == 1){
			 MenuFavorService.addFavor(user.getId().intValue(),menuId, user.getNickName(), user.getAvatarUrl(), 0);
			 flag++;
		 } 
		 
		if(flag == 0){
			return ;
		}
		try {
			int sleep = (new Random().nextInt(60)%(60-2+1) + 2)*60*1000;
			Logger.info("间隔 %s 分钟再来一个用户", sleep/60000);
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		 
	}
}
