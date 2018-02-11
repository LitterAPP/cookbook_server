package modules.cookbook.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.CookBookMemuCommentDDL;

public class MenuCommentService {

	public static boolean addComment(int userId,int menuId,String userName,String userAvatar,String comment){
		CookBookMemuCommentDDL cmt = new CookBookMemuCommentDDL();
		cmt.setComment(comment);
		cmt.setUserAvatar(userAvatar);
		cmt.setUserName(userName);
		cmt.setUserId(userId);
		cmt.setMenuId(menuId);
		cmt.setTimeDesc(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		cmt.setCreateTime(System.currentTimeMillis());
		return Dal.insert(cmt)>0;
	}
	
	public static List<CookBookMemuCommentDDL> listByMenuId(int menuId,int page,int num){
		Condition condition = new Condition("CookBookMemuCommentDDL.menuId","=",menuId);
		return Dal.select("CookBookMemuCommentDDL.*", condition, new Sort("CookBookMemuCommentDDL.id",false), (page-1)*num, num);
	}
}
