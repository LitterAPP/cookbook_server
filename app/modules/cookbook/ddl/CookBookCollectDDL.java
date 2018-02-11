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
 * @createDate 2017-11-17 11:25:16
 **/
@Table(name="cook_book_collect")
public class CookBookCollectDDL{
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

	@Column(name="cook_book_info_id", type=DbType.Int)
	private Integer cookBookInfoId;
	public Integer getCookBookInfoId() {
		return cookBookInfoId;
	}
	public void setCookBookInfoId(Integer cookBookInfoId){
		this.cookBookInfoId=cookBookInfoId;
	}

	@Column(name="user_id", type=DbType.Int)
	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId){
		this.userId=userId;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static CookBookCollectDDL newExample(){
		CookBookCollectDDL object=new CookBookCollectDDL();
		object.setId(null);
		object.setCookBookInfoId(null);
		object.setUserId(null);
		object.setCreateTime(null);
		return object;
	}
}
