package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgReadingCommentsDDL;

public class ForgReadCommentService {

	public static void add(int userId,int readId,String comments,int replyId,String nickName,String avatar){
		ForgReadingCommentsDDL comment = new ForgReadingCommentsDDL();
		comment.setComment(comments);
		comment.setCreateTime(System.currentTimeMillis());
		comment.setReadId(readId);
		comment.setReply(replyId);
		comment.setUserId(userId);
		comment.setNickName(nickName);
		comment.setAvatar(avatar);
		Dal.insert(comment);
	}
	
	public static ForgReadingCommentsDDL get(int id){
		return Dal.select("ForgReadingCommentsDDL.*", id);
	}
	
	public static List<ForgReadingCommentsDDL> list(int readId,int page,int pageSize){
		
		page = page<=0?1:page;
		pageSize = (pageSize<=0 || pageSize > 10)?10:pageSize; 
		
		Condition cond = new Condition("ForgReadingCommentsDDL.readId","=",readId);
		cond.add( new Condition("ForgReadingCommentsDDL.replyId","=",0), "and");
		Sort sort = new Sort("ForgReadingCommentsDDL.id",false);
		return Dal.select("ForgReadingCommentsDDL.*", cond, sort, (page-1)*pageSize, pageSize);
	}
	
	public static int count(int readId){
		Condition cond = new Condition("ForgReadingCommentsDDL.readId","=",readId);
		cond.add( new Condition("ForgReadingCommentsDDL.replyId","=",0), "and");
		return Dal.count(cond);
	}
	
	public static List<ForgReadingCommentsDDL> listReply(int replyId,int page,int pageSize){
		Condition cond = new Condition("ForgReadingCommentsDDL.replyId","=",replyId);
		Sort sort = new Sort("ForgReadingCommentsDDL.id",false);
		return Dal.select("ForgReadingCommentsDDL.*", cond, sort, (page-1)*pageSize, pageSize);
	}
	
	public static int countReply(int replayId){
		Condition cond = new Condition("ForgReadingCommentsDDL.replyId","=",replayId);
		return Dal.count(cond);
	}
}
