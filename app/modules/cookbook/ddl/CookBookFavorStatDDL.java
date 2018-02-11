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
 * @createDate 2017-11-17 14:21:03
 **/
@Table(name="cook_book_favor_stat")
public class CookBookFavorStatDDL{
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

	@Column(name="favor_value", type=DbType.Int)
	private Integer favorValue;
	public Integer getFavorValue() {
		return favorValue;
	}
	public void setFavorValue(Integer favorValue){
		this.favorValue=favorValue;
	}

	public static CookBookFavorStatDDL newExample(){
		CookBookFavorStatDDL object=new CookBookFavorStatDDL();
		object.setId(null); 
		object.setCookBookId(null);
		object.setFavorValue(null);
		return object;
	}
}
