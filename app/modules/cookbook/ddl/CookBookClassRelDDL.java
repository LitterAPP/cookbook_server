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
 * @createDate 2017-11-09 13:51:45
 **/
@Table(name="cook_book_class_rel")
public class CookBookClassRelDDL{
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

	@Column(name="cook_book_id", type=DbType.Int)
	private Integer cookBookId;
	public Integer getCookBookId() {
		return cookBookId;
	}
	public void setCookBookId(Integer cookBookId){
		this.cookBookId=cookBookId;
	}

	@Column(name="class_child_id", type=DbType.Int)
	private Integer classChildId;
	public Integer getClassChildId() {
		return classChildId;
	}
	public void setClassChildId(Integer classChildId){
		this.classChildId=classChildId;
	}

	@Column(name="class_parent_id", type=DbType.Int)
	private Integer classParentId;
	public Integer getClassParentId() {
		return classParentId;
	}
	public void setClassParentId(Integer classParentId){
		this.classParentId=classParentId;
	}

	public static CookBookClassRelDDL newExample(){
		CookBookClassRelDDL object=new CookBookClassRelDDL();
		object.setId(null);
		object.setCookBookId(null);
		object.setClassChildId(null);
		object.setClassParentId(null);
		return object;
	}
}
