package modules.forg.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;
/**
 * 
 * @author auto
 * @createDate 2018-03-08 15:57:52
 **/
@Table(name="forg_reading_comments")
public class ForgReadingCommentsDDL{
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

	@Column(name="read_id", type=DbType.Int)
	private Integer readId;
	public Integer getReadId() {
		return readId;
	}
	public void setReadId(Integer readId){
		this.readId=readId;
	}

	@Column(name="user_id", type=DbType.Int)
	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId){
		this.userId=userId;
	}

	@Column(name="nick_name", type=DbType.Varchar)
	private String nickName;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName){
		this.nickName=nickName;
	}

	@Column(name="avatar", type=DbType.Varchar)
	private String avatar;
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar){
		this.avatar=avatar;
	}

	@Column(name="reply_id", type=DbType.Int)
	private Integer replyId;
	public Integer getReplayId() {
		return replyId;
	}
	public void setReply(Integer replyId){
		this.replyId=replyId;
	}

	@Column(name="comment", type=DbType.Varchar)
	private String comment;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment){
		this.comment=comment;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static ForgReadingCommentsDDL newExample(){
		ForgReadingCommentsDDL object=new ForgReadingCommentsDDL();
		object.setId(null);
		object.setReadId(null);
		object.setUserId(null);
		object.setNickName(null);
		object.setAvatar(null); 
		object.setComment(null);
		object.setCreateTime(null);
		return object;
	}
}
