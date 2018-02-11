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
 * @createDate 2017-11-20 13:44:21
 **/
@Table(name="cook_book_menu_rel")
public class CookBookMenuRelDDL{
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

	@Column(name="introduction", type=DbType.Varchar)
	private String introduction;
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Column(name="menu_id", type=DbType.Int)
	private Integer menuId;
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId){
		this.menuId=menuId;
	}

	@Column(name="cook_book_id", type=DbType.Int)
	private Integer cookBookId;
	public Integer getCookBookId() {
		return cookBookId;
	}
	public void setCookBookId(Integer cookBookId){
		this.cookBookId=cookBookId;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static CookBookMenuRelDDL newExample(){
		CookBookMenuRelDDL object=new CookBookMenuRelDDL();
		object.setId(null);
		object.setIntroduction(null);
		object.setMenuId(null);
		object.setCookBookId(null);
		object.setCreateTime(null);
		return object;
	}
}
