package modules.cookbook.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.CookBookMomentCommentDDL;
import modules.cookbook.ddl.CookBookMomentDDL;
import modules.cookbook.ddl.CookBookMomentTimeLineDDL;

/**
 * 动态服务
 * @author fish
 *
 */
public class MomentService {

	public static boolean postedAnMoment(int userId,String speak,String picKeys,int cookBookId){
		if( userId == 0 || StringUtils.isEmpty(speak) ) return false;
		CookBookMomentDDL moment = new CookBookMomentDDL();
		moment.setCookBookId(cookBookId);
		moment.setPicKeys(picKeys);
		moment.setPosetdTime(System.currentTimeMillis());
		moment.setSpeak(speak);
		moment.setUserId(userId);
		return Dal.insert( moment ) > 0;
	}
	
	public static boolean postedComment(int fromUserId,int replyUserId,int momentId,String speak){
		if(momentId==0 || fromUserId==0 || StringUtils.isEmpty(speak)) return false;
		CookBookMomentCommentDDL comment = new CookBookMomentCommentDDL();
		comment.setFromUserId(fromUserId);
		comment.setReplyUserId(replyUserId);
		comment.setMomentId(momentId);
		comment.setSpeak(speak);
		comment.setPostedTime(System.currentTimeMillis());
		return Dal.insert(comment) > 0;
	}
	
	public static boolean updateLastTimeLine(int userId){
		if(userId == 0) return false;
		CookBookMomentTimeLineDDL timeLine = new CookBookMomentTimeLineDDL();
		timeLine.setUserId(userId);
		timeLine.setLastTimeLine(System.currentTimeMillis());
		return Dal.replace(timeLine) > 0;
	}
	
	public static CookBookMomentDDL getMoment(int momentId){
		if( momentId == 0 ) return null;
		return Dal.select("CookBookMomentDDL.*", momentId);
	}
	
	public static List<CookBookMomentDDL> listLastMoment(int userId){
		if( userId == 0 ) return null;
		long lastTimeUnix = lastTimeLine(userId)/1000;
		Condition condition = new Condition("CookBookMomentDDL.userId","=",userId);
		condition.add(new Condition("CookBookMomentDDL.posetdTime",">=",lastTimeUnix), "and");
		updateLastTimeLine(userId);//更新moment lastTime line
		return Dal.select("CookBookMomentDDL.*", condition, new Sort("CookBookMomentDDL.id",false), 0,-1);
	}
	 
	public static List<CookBookMomentDDL> listMoreHisMoment(int userId,int momentId,int number){
		if( userId == 0 ) return null;
		Condition condition = new Condition("CookBookMomentDDL.userId","=",userId);
		condition.add(new Condition("CookBookMomentDDL.id","<",momentId), "and");
		return Dal.select("CookBookMomentDDL.*", condition, new Sort("CookBookMomentDDL.id",false), 0,number);
	}
	
	public static List<CookBookMomentCommentDDL> listComments(int momentId){
		if( momentId == 0 ) return null;
		Condition condition = new Condition("CookBookMomentCommentDDL.momentId","=",momentId);
		Sort sort = new Sort("CookBookMomentCommentDDL.id",false);
		return Dal.select("CookBookMomentCommentDDL.*", condition, sort, 0, -1);
	}
	 
	
	public static int countMoment(int userId){
		long lastTimeUnix = lastTimeLine(userId)/1000;
		int count = Dal.executeCount(CookBookMomentDDL.class, "select count(0) from cook_book_moment where UNIX_TIMESTAMP(posetd_time) >= "+lastTimeUnix);
		return count;
	}
	
	public static int countComment(int userId){
		long lastTimeUnix = lastTimeLine(userId)/1000;
		int count = Dal.executeCount(CookBookMomentCommentDDL.class,
				"select count(0) from cook_book_moment_comment where UNIX_TIMESTAMP(posetd_time) >= "+lastTimeUnix+" and master_user_id="+userId);
		return count;
	}
	
	public static long lastTimeLine(int userId){
		Condition condition = new Condition("CookBookMomentTimeLineDDL.userId","=",userId);
		List<CookBookMomentTimeLineDDL> list = Dal.select("CookBookMomentTimeLineDDL.*", condition, null, 0, 1);
		if( null==list || list.size() == 0){
			return System.currentTimeMillis() - 24*60*60*1000;
		}
		return list.get(0).getLastTimeLine();
	}
}
