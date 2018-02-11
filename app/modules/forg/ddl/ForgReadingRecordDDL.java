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
 * @createDate 2018-02-05 14:16:30
 **/
@Table(name="forg_reading_record")
public class ForgReadingRecordDDL{
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

	@Column(name="book_id", type=DbType.Int)
	private Integer bookId;
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId){
		this.bookId=bookId;
	}

	@Column(name="user_id", type=DbType.Int)
	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId){
		this.userId=userId;
	}

	@Column(name="record_url", type=DbType.Varchar)
	private String recordUrl;
	public String getRecordUrl() {
		return recordUrl;
	}
	public void setRecordUrl(String recordUrl){
		this.recordUrl=recordUrl;
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

	@Column(name="duration", type=DbType.Int)
	private Integer duration;
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration){
		this.duration=duration;
	}

	@Column(name="flows", type=DbType.Int)
	private Integer flows;
	public Integer getFlows() {
		return flows;
	}
	public void setFlows(Integer flows){
		this.flows=flows;
	}

	@Column(name="play_times", type=DbType.Int)
	private Integer playTimes;
	public Integer getPlayTimes() {
		return playTimes;
	}
	public void setPlayTimes(Integer playTimes){
		this.playTimes=playTimes;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static ForgReadingRecordDDL newExample(){
		ForgReadingRecordDDL object=new ForgReadingRecordDDL();
		object.setId(null);
		object.setBookId(null);
		object.setUserId(null);
		object.setRecordUrl(null);
		object.setNickName(null);
		object.setAvatar(null);
		object.setDuration(null);
		object.setFlows(null);
		object.setPlayTimes(null);
		object.setCreateTime(null);
		return object;
	}
}
