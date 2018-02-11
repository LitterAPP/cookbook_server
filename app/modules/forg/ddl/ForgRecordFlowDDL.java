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
 * @createDate 2018-01-26 11:48:31
 **/
@Table(name="forg_record_flow")
public class ForgRecordFlowDDL{
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

	@Column(name="record_id", type=DbType.Int)
	private Integer recordId;
	public Integer getRecordId() {
		return recordId;
	}
	public void setRecordId(Integer recordId){
		this.recordId=recordId;
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

	public static ForgRecordFlowDDL newExample(){
		ForgRecordFlowDDL object=new ForgRecordFlowDDL();
		object.setId(null);
		object.setRecordId(null);
		object.setUserId(null);
		object.setNickName(null);
		object.setAvatar(null);
		object.setCreateTime(null);
		return object;
	}
}
