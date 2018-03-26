package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.CookBookUsersDDL;
import modules.forg.ddl.ForgReadingBooksDDL;
import modules.forg.ddl.ForgReadingHistoryDDL;

public class ForgReadHisService {
	public static void replaceHis(CookBookUsersDDL user,ForgReadingBooksDDL book){
		ForgReadingHistoryDDL his = new ForgReadingHistoryDDL();
		his.setAvatar(user.getAvatarUrl());
		his.setBookCover(book.getBookCover());
		his.setBookId(book.getId());
		his.setBookName(book.getBookName());
		his.setNickName(user.getNickName());
		his.setReadTime(System.currentTimeMillis());
		his.setUserId(user.getId().intValue());
		Dal.replace(his);
	}
	
	public static List<ForgReadingHistoryDDL> listMyLines(int userId,int page,int pageSize){
		Condition condition = new Condition("ForgReadingHistoryDDL.userId","=",userId);
		return Dal.select("ForgReadingHistoryDDL.*", condition,  new Sort("ForgReadingHistoryDDL.id",false), (page-1)*pageSize, pageSize);
	}
	
	public static int countRead(int userId){
		Condition condition = new Condition("ForgReadingHistoryDDL.userId","=",userId);
		return Dal.count(condition);
	}
}
