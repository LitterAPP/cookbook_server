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
 * @createDate 2017-11-14 09:56:23
 **/
@Table(name="cook_book_moment")
public class CookBookMomentDDL{
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

	@Column(name="speak", type=DbType.Varchar)
	private String speak;
	public String getSpeak() {
		return speak;
	}
	public void setSpeak(String speak){
		this.speak=speak;
	}

	@Column(name="pic_keys", type=DbType.Varchar)
	private String picKeys;
	public String getPicKeys() {
		return picKeys;
	}
	public void setPicKeys(String picKeys){
		this.picKeys=picKeys;
	}

	@Column(name="cook_book_id", type=DbType.Int)
	private Integer cookBookId;
	public Integer getCookBookId() {
		return cookBookId;
	}
	public void setCookBookId(Integer cookBookId){
		this.cookBookId=cookBookId;
	}

	@Column(name="posetd_time", type=DbType.DateTime)
	private Long posetdTime;
	public Long getPosetdTime() {
		return posetdTime;
	}
	public void setPosetdTime(Long posetdTime){
		this.posetdTime=posetdTime;
	}

	public static CookBookMomentDDL newExample(){
		CookBookMomentDDL object=new CookBookMomentDDL();
		object.setId(null);
		object.setUserId(null);
		object.setSpeak(null);
		object.setPicKeys(null);
		object.setCookBookId(null);
		object.setPosetdTime(null);
		return object;
	}
}
