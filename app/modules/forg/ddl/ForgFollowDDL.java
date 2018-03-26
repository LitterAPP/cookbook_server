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
 * @createDate 2018-03-08 13:25:10
 **/
@Table(name="forg_follow")
public class ForgFollowDDL{
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

	@Column(name="user_id", type=DbType.Int)
	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId){
		this.userId=userId;
	}

	@Column(name="follow_user_id", type=DbType.Int)
	private Integer followUserId;
	public Integer getFollowUserId() {
		return followUserId;
	}
	public void setFollowUserId(Integer followUserId){
		this.followUserId=followUserId;
	}

	@Column(name="follow_time", type=DbType.DateTime)
	private Long followTime;
	public Long getFollowTime() {
		return followTime;
	}
	public void setFollowTime(Long followTime){
		this.followTime=followTime;
	}

	public static ForgFollowDDL newExample(){
		ForgFollowDDL object=new ForgFollowDDL();
		object.setId(null);
		object.setUserId(null);
		object.setFollowUserId(null);
		object.setFollowTime(null);
		return object;
	}
}
