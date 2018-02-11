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
 * @createDate 2017-11-17 14:14:09
 **/
@Table(name="cook_book_collect_stat")
public class CookBookCollectStatDDL{
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

	@Column(name="cook_book_id", type=DbType.Int)
	private Integer cookBookId;
	public Integer getCookBookId() {
		return cookBookId;
	}
	public void setCookBookId(Integer cookBookId){
		this.cookBookId=cookBookId;
	}

	@Column(name="collect_value", type=DbType.Int)
	private Integer collectValue;
	public Integer getCollectValue() {
		return collectValue;
	}
	public void setCollectValue(Integer collectValue){
		this.collectValue=collectValue;
	}

	public static CookBookCollectStatDDL newExample(){
		CookBookCollectStatDDL object=new CookBookCollectStatDDL();
		object.setId(null);
		object.setUserId(null);
		object.setCookBookId(null);
		object.setCollectValue(null);
		return object;
	}
}
