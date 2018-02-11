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
 * @createDate 2018-02-09 11:30:52
 **/
@Table(name="forg_bainian_record")
public class ForgBainianRecordDDL{
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

	@Column(name="cover_oss_key", type=DbType.Varchar)
	private String coverOssKey;
	public String getCoverOssKey() {
		return coverOssKey;
	}
	public void setCoverOssKey(String coverOssKey){
		this.coverOssKey=coverOssKey;
	}

	@Column(name="record_oss_key", type=DbType.Varchar)
	private String recordOssKey;
	public String getRecordOssKey() {
		return recordOssKey;
	}
	public void setRecordOssKey(String recordOssKey){
		this.recordOssKey=recordOssKey;
	}

	@Column(name="greetings", type=DbType.Varchar)
	private String greetings;
	public String getGreetings() {
		return greetings;
	}
	public void setGreetings(String greetings){
		this.greetings=greetings;
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

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static ForgBainianRecordDDL newExample(){
		ForgBainianRecordDDL object=new ForgBainianRecordDDL();
		object.setId(null);
		object.setCoverOssKey(null);
		object.setRecordOssKey(null);
		object.setGreetings(null);
		object.setUserId(null);
		object.setNickName(null);
		object.setAvatar(null);
		object.setCreateTime(null);
		return object;
	}
}
