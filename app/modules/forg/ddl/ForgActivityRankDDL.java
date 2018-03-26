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
 * @createDate 2018-02-27 15:28:06
 **/
@Table(name="forg_activity_rank")
public class ForgActivityRankDDL{
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

	@Column(name="activity_id", type=DbType.Int)
	private Integer activityId;
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId){
		this.activityId=activityId;
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

	@Column(name="flows", type=DbType.Int)
	private Integer flows;
	public Integer getFlows() {
		return flows;
	}
	public void setFlows(Integer flows){
		this.flows=flows;
	}

	@Column(name="rank", type=DbType.Int)
	private Integer rank;
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank){
		this.rank=rank;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static ForgActivityRankDDL newExample(){
		ForgActivityRankDDL object=new ForgActivityRankDDL();
		object.setId(null);
		object.setActivityId(null);
		object.setUserId(null);
		object.setNickName(null);
		object.setAvatar(null);
		object.setFlows(null);
		object.setRank(null);
		object.setCreateTime(null);
		return object;
	}
}
