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
 * @createDate 2017-11-20 11:44:55
 **/
@Table(name="cook_book_menu")
public class CookBookMenuDDL{
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

	@Column(name="menu_name", type=DbType.Varchar)
	private String menuName;
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName){
		this.menuName=menuName;
	}
	@Column(name="introduction", type=DbType.Varchar)
	private String introduction;
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Column(name="banner_key", type=DbType.Varchar)
	private String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	@Column(name="user_id", type=DbType.Int)
	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId){
		this.userId=userId;
	}
	
	@Column(name="type", type=DbType.Int)
	private Integer type;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static CookBookMenuDDL newExample(){
		CookBookMenuDDL object=new CookBookMenuDDL();
		object.setId(null);
		object.setMenuName(null);
		object.setUserId(null);
		object.setCreateTime(null);
		return object;
	}
}
