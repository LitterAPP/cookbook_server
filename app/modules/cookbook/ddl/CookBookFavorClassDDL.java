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
 * @createDate 2017-11-17 13:48:21
 **/
@Table(name="cook_book_favor_class")
public class CookBookFavorClassDDL{
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

	@Column(name="class_id", type=DbType.Int)
	private Integer classId;
	public Integer getClassId() {
		return classId;
	}
	public void setClassId(Integer classId){
		this.classId=classId;
	}

	@Column(name="user_id", type=DbType.Int)
	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId){
		this.userId=userId;
	}

	@Column(name="favor_value", type=DbType.Int)
	private Integer favorValue;
	public Integer getFavorValue() {
		return favorValue;
	}
	public void setFavorValue(Integer favorValue){
		this.favorValue=favorValue;
	}

	public static CookBookFavorClassDDL newExample(){
		CookBookFavorClassDDL object=new CookBookFavorClassDDL();
		object.setId(null);
		object.setClassId(null);
		object.setUserId(null);
		object.setFavorValue(null);
		return object;
	}
}
