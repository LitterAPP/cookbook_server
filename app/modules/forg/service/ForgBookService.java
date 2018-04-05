package modules.forg.service;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgReadingBooksDDL;

public class ForgBookService {
	
	public static long createBook(String bookName,String bookCover,String bookDesc,String nickName,String avatar,int userId,int musicId,int type){
		ForgReadingBooksDDL book = new ForgReadingBooksDDL();
		book.setBookCover(bookCover);
		book.setBookDesc(bookDesc);
		book.setBookName(bookName);
		book.setCreateTime(System.currentTimeMillis());
		book.setFlows(0);
		book.setUploaderAvatar(avatar);
		book.setUploaderUid(userId);
		book.setUploaderNickname(nickName);
		book.setRecommed(userId==1789?1:0);
		book.setMusicId(musicId==0?null:musicId);
		book.setStatus(1);
		book.setType(type);
		return Dal.insertSelectLastId(book);
	}
	
	public static void updateBook(int bookId,String bookName,String bookCover,String bookDesc,int musicId,int type){
		ForgReadingBooksDDL book = get(bookId);
		book.setBookCover(bookCover);
		book.setBookDesc(bookDesc);
		book.setBookName(bookName); 	
		book.setMusicId(musicId);
		book.setType(type);
		Dal.update(book, "ForgReadingBooksDDL.type,ForgReadingBooksDDL.bookCover,ForgReadingBooksDDL.musicId,ForgReadingBooksDDL.bookName,ForgReadingBooksDDL.bookDesc",
				new Condition("ForgReadingBooksDDL.id","=",bookId));
	}
	
	public static ForgReadingBooksDDL addBookFlow(ForgReadingBooksDDL book){
		if(book==null)return null;
		book.setFlows(book.getFlows()+1);	
		Dal.update(book, "ForgReadingBooksDDL.flows",
				new Condition("ForgReadingBooksDDL.id","=",book.getId()));
		return book;
	}
	
	public static void delete(int bookId){
		Condition condition = new Condition("ForgReadingBooksDDL.id","=",bookId);
		Dal.delete(condition);
	}
	
	public static ForgReadingBooksDDL get(int bookId){
		return Dal.select("ForgReadingBooksDDL.*", bookId);
	}
	
	/*public static List<ForgReadingBooksDDL> listRecommendBooks(int top){
		Condition condition = new Condition("ForgReadingBooksDDL.recommed","=",1);
		Sort sort = new Sort("ForgReadingBooksDDL.id",true);
		return Dal.select("ForgReadingBooksDDL.*", condition, sort, 0, top);
	}
	
	public static List<ForgReadingBooksDDL> listLastBooks(int page,int pageSize){		 
		Sort sort = new Sort("ForgReadingBooksDDL.id",false);
		return Dal.select("ForgReadingBooksDDL.*", null, sort, (page-1)*pageSize, pageSize);
	}
	
	public static List<ForgReadingBooksDDL> listHotBooks(int page,int pageSize){		 
		Sort sort = new Sort("ForgReadingBooksDDL.flows",false);
		return Dal.select("ForgReadingBooksDDL.*", null, sort, (page-1)*pageSize, pageSize);
	}*/
	
	public static List<ForgReadingBooksDDL> listBooks(int hotOrLast,int recommend,int type,int page,int pageSize){	
		Condition condition = new Condition("ForgReadingBooksDDL.id",">",0);
		Sort sort = new Sort("ForgReadingBooksDDL.id",false);
		if(hotOrLast == 1){
			sort = new Sort("ForgReadingBooksDDL.flows",false);
		}
		if(recommend==1){
			condition.add(new Condition("ForgReadingBooksDDL.recommed","=",1),"and");
		}
		if(type>0){
			condition.add(new Condition("ForgReadingBooksDDL.type","=",type),"and");
		}
		return Dal.select("ForgReadingBooksDDL.*", condition, sort, (page-1)*pageSize, pageSize);
	}
	
	public static int countBooks(int hotOrLast,int recommend,int type){	
		Condition condition = new Condition("ForgReadingBooksDDL.id",">",0);
		if(recommend==1){
			condition.add(new Condition("ForgReadingBooksDDL.recommed","=",1),"and");
		}
		if(type>0){
			condition.add(new Condition("ForgReadingBooksDDL.type","=",type),"and");
		}		
		return Dal.count(condition);
	}
	
	public static List<ForgReadingBooksDDL> listRandomBooks(){		 
		int pageSize = 20;
		int count = Dal.count(new Condition("ForgReadingBooksDDL.id",">",0));
		int offset = 0;
		if( count <= pageSize){
			offset = 0;
		}else{
			Random random = new Random();			
			int randomMax = count - pageSize;
			offset = random.nextInt(randomMax+1);
		} 
		return Dal.select("ForgReadingBooksDDL.*", null, null, offset, pageSize);
	}
	
	public static List<ForgReadingBooksDDL> listBooks(String bookName,int page,int pageSize){	
		Condition condition = null;
		if(!StringUtils.isEmpty(bookName)){
			condition = new Condition("ForgReadingBooksDDL.bookName","like","%"+bookName+"%");
		} 
		Sort sort = new Sort("ForgReadingBooksDDL.flows",false);
		return Dal.select("ForgReadingBooksDDL.*", condition, sort, (page-1)*pageSize, pageSize);
	}
}
