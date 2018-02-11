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
 * @createDate 2017-11-14 09:56:51
 **/
@Table(name="cook_book_moment_time_line")
public class CookBookMomentTimeLineDDL{
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

	@Column(name="last_time_line", type=DbType.DateTime)
	private Long lastTimeLine;
	public Long getLastTimeLine() {
		return lastTimeLine;
	}
	public void setLastTimeLine(Long lastTimeLine){
		this.lastTimeLine=lastTimeLine;
	}

	public static CookBookMomentTimeLineDDL newExample(){
		CookBookMomentTimeLineDDL object=new CookBookMomentTimeLineDDL();
		object.setId(null);
		object.setUserId(null);
		object.setLastTimeLine(null);
		return object;
	}
}
