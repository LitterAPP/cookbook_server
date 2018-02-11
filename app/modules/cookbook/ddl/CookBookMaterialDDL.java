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
 * @createDate 2017-11-09 11:38:06
 **/
@Table(name="cook_book_material")
public class CookBookMaterialDDL{
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

	@Column(name="mname", type=DbType.Varchar)
	private String mname;
	public String getMname() {
		return mname;
	}
	public void setMname(String mname){
		this.mname=mname;
	}

	@Column(name="type", type=DbType.Int)
	private Integer type;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type){
		this.type=type;
	}

	@Column(name="amount", type=DbType.Varchar)
	private String amount;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount){
		this.amount=amount;
	}

	@Column(name="orderby", type=DbType.Int)
	private Integer orderby;
	public Integer getOrderby() {
		return orderby;
	}
	public void setOrderby(Integer orderby){
		this.orderby=orderby;
	}

	public static CookBookMaterialDDL newExample(){
		CookBookMaterialDDL object=new CookBookMaterialDDL();
		object.setId(null);
		object.setCookBookInfoId(null);
		object.setMname(null);
		object.setType(null);
		object.setAmount(null);
		object.setOrderby(null);
		return object;
	}
}
