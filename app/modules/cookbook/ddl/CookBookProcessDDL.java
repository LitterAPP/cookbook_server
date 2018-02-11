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
 * @createDate 2017-11-09 11:38:12
 **/
@Table(name="cook_book_process")
public class CookBookProcessDDL{
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

	@Column(name="pcontent", type=DbType.Varchar)
	private String pcontent;
	public String getPcontent() {
		return pcontent;
	}
	public void setPcontent(String pcontent){
		this.pcontent=pcontent;
	}

	@Column(name="pic", type=DbType.Varchar)
	private String pic;
	public String getPic() {
		return pic;
	}
	public void setPic(String pic){
		this.pic=pic;
	}

	@Column(name="step", type=DbType.Int)
	private Integer step;
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step){
		this.step=step;
	}

	public static CookBookProcessDDL newExample(){
		CookBookProcessDDL object=new CookBookProcessDDL();
		object.setId(null);
		object.setCookBookInfoId(null);
		object.setPcontent(null);
		object.setPic(null);
		object.setStep(null);
		return object;
	}
}
