package modules.forg.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jws.Jws;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgReadingContentShotDDL;

public class ForgBookShotService {

	public static int speakSpeed = Integer.parseInt(Jws.configuration.getProperty("speak.speed","100"));
	
	public static void multiInsert(int bookId,String[] shots,String[] wordCounts){
		List<ForgReadingContentShotDDL> list = new ArrayList<ForgReadingContentShotDDL>();		 
		double perSec = new BigDecimal(new Double(speakSpeed/60d).toString())
				.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		int page = 1;
		for(int i=0;i<shots.length;i++){
			ForgReadingContentShotDDL forgShot = new ForgReadingContentShotDDL();
			forgShot.setBookId(bookId);
			forgShot.setPageNum(page);
			forgShot.setPageShot(shots[i]);
			forgShot.setPageText("");
			int wordCount = Integer.parseInt(wordCounts[i]);
			forgShot.setWordCount(wordCount);
			int readTime =  new BigDecimal(new Double(wordCount/perSec).toString())
					.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			forgShot.setReadTime(readTime);
			page++;
			list.add(forgShot);
		}
		multiDelete(bookId);
		Dal.insertMulti(list);
	}
	
	public static void main(String[] args){
		BigDecimal b = new BigDecimal(new Double(1.6666666f).toString());
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println(f1);
		System.out.println(new BigDecimal(new Double(20/1.6).toString()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
	}
	
	public static void multiDelete(int bookId){
		Dal.delete(new Condition("ForgReadingContentShotDDL.bookId","=",bookId));
	}
	
	public static List<ForgReadingContentShotDDL> listByBookId(int bookId){
		Condition condition  = new Condition("ForgReadingContentShotDDL.bookId","=",bookId);
		return Dal.select("ForgReadingContentShotDDL.*", condition, new Sort("ForgReadingContentShotDDL.pageNum",false), 0, -1);
	}
}
