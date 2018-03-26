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
 * @createDate 2018-03-15 18:19:08
 **/
@Table(name="forg_read_speed_config")
public class ForgReadSpeedConfigDDL{
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

	@Column(name="speed", type=DbType.Int)
	private Integer speed;
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed){
		this.speed=speed;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static ForgReadSpeedConfigDDL newExample(){
		ForgReadSpeedConfigDDL object=new ForgReadSpeedConfigDDL();
		object.setId(null);
		object.setUserId(null);
		object.setSpeed(null);
		object.setCreateTime(null);
		return object;
	}
}
