package modules.cookbook.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;
/**
 * 
 * @author auto
 * @createDate 2017-11-14 10:25:05
 **/
@Table(name="cook_book_moment_comment")
public class CookBookMomentCommentDDL{
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="id", type=DbType.Int)
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id){
		this.id=id;
	}

	@Column(name="master_user_id", type=DbType.Int)
	private Integer masterUserId;
	public Integer getMasterUserId() {
		return masterUserId;
	}
	public void setMasterUserId(Integer masterUserId){
		this.masterUserId=masterUserId;
	}

	@Column(name="from_user_id", type=DbType.Int)
	private Integer fromUserId;
	public Integer getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(Integer fromUserId){
		this.fromUserId=fromUserId;
	}

	@Column(name="reply_user_id", type=DbType.Int)
	private Integer replyUserId;
	public Integer getReplyUserId() {
		return replyUserId;
	}
	public void setReplyUserId(Integer replyUserId){
		this.replyUserId=replyUserId;
	}

	@Column(name="moment_id", type=DbType.Int)
	private Integer momentId;
	public Integer getMomentId() {
		return momentId;
	}
	public void setMomentId(Integer momentId){
		this.momentId=momentId;
	}

	@Column(name="speak", type=DbType.Varchar)
	private String speak;
	public String getSpeak() {
		return speak;
	}
	public void setSpeak(String speak){
		this.speak=speak;
	}

	@Column(name="posted_time", type=DbType.DateTime)
	private Long postedTime;
	public Long getPostedTime() {
		return postedTime;
	}
	public void setPostedTime(Long postedTime){
		this.postedTime=postedTime;
	}

	public static CookBookMomentCommentDDL newExample(){
		CookBookMomentCommentDDL object=new CookBookMomentCommentDDL();
		object.setId(null);
		object.setMasterUserId(null);
		object.setFromUserId(null);
		object.setReplyUserId(null);
		object.setMomentId(null);
		object.setSpeak(null);
		object.setPostedTime(null);
		return object;
	}
}
