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
 * @createDate 2017-11-09 15:38:17
 **/
@Table(name="cook_book_tag")
public class CookBookTagDDL{
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

	@Column(name="tag", type=DbType.Varchar)
	private String tag;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag){
		this.tag=tag;
	}

	public static CookBookTagDDL newExample(){
		CookBookTagDDL object=new CookBookTagDDL();
		object.setId(null);
		object.setCookBookInfoId(null);
		object.setTag(null);
		return object;
	}
}
