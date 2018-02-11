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
 * @createDate 2017-11-17 14:16:54
 **/
@Table(name="cook_book_view_stat")
public class CookBookViewStatDDL{
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

	@Column(name="view_value", type=DbType.Int)
	private Integer viewValue;
	public Integer getViewValue() {
		return viewValue;
	}
	public void setViewValue(Integer viewValue){
		this.viewValue=viewValue;
	}

	public static CookBookViewStatDDL newExample(){
		CookBookViewStatDDL object=new CookBookViewStatDDL();
		object.setId(null);
		object.setCookBookId(null);
		object.setViewValue(null);
		return object;
	}
}
