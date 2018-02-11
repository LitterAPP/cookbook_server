package modules.forg.service;

import jws.dal.Dal;
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
}
