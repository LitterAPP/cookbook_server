package modules.forg.service;

import java.util.ArrayList;
import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgReadingContentShotDDL;

public class ForgBookShotService {

	public static void multiInsert(int bookId,String[] shots){
		List<ForgReadingContentShotDDL> list = new ArrayList<ForgReadingContentShotDDL>();
		int page = 1;
		for(String shot : shots){
			ForgReadingContentShotDDL forgShot = new ForgReadingContentShotDDL();
			forgShot.setBookId(bookId);
			forgShot.setPageNum(page);
			forgShot.setPageShot(shot);
			forgShot.setPageText("");
			page++;
			list.add(forgShot);
		}
		multiDelete(bookId);
		Dal.insertMulti(list);
	}
	
	public static void multiDelete(int bookId){
		Dal.delete(new Condition("ForgReadingContentShotDDL.bookId","=",bookId));
	}
	
	public static List<ForgReadingContentShotDDL> listByBookId(int bookId){
		Condition condition  = new Condition("ForgReadingContentShotDDL.bookId","=",bookId);
		return Dal.select("ForgReadingContentShotDDL.*", condition, new Sort("ForgReadingContentShotDDL.pageNum",false), 0, -1);
	}
}
