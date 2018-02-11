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
 * @createDate 2017-11-20 15:26:49
 **/
@Table(name="cook_book_material_select")
public class CookBookMaterialSelectDDL{
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

	@Column(name="menu_id", type=DbType.Int)
	private Integer menuId;
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId){
		this.menuId=menuId;
	}

	@Column(name="material_id", type=DbType.Int)
	private Integer materialId;
	public Integer getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Integer materialId){
		this.materialId=materialId;
	}

	@Column(name="material_name", type=DbType.Varchar)
	private String materialName;
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName){
		this.materialName=materialName;
	}

	@Column(name="amount", type=DbType.Float)
	private Float amount;
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount){
		this.amount=amount;
	}

	@Column(name="done", type=DbType.Int)
	private Integer done;
	public Integer getDone() {
		return done;
	}
	public void setDone(Integer done){
		this.done=done;
	}

	public static CookBookMaterialSelectDDL newExample(){
		CookBookMaterialSelectDDL object=new CookBookMaterialSelectDDL();
		object.setId(null);
		object.setMenuId(null);
		object.setMaterialId(null);
		object.setMaterialName(null);
		object.setAmount(null);
		object.setDone(null);
		return object;
	}
}
