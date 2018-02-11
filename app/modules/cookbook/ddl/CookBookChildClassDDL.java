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
 * @createDate 2017-11-09 13:33:54
 **/
@Table(name="cook_book_child_class")
public class CookBookChildClassDDL{
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

	@Column(name="parent_id", type=DbType.Int)
	private Integer parentId;
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId){
		this.parentId=parentId;
	}

	@Column(name="name", type=DbType.Varchar)
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name=name;
	}

	public static CookBookChildClassDDL newExample(){
		CookBookChildClassDDL object=new CookBookChildClassDDL();
		object.setId(null);
		object.setClassId(null);
		object.setParentId(null);
		object.setName(null);
		return object;
	}
}
