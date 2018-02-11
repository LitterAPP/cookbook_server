package modules.forg.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;
/**
 * 
 * @author auto
 * @createDate 2018-02-09 11:31:27
 **/
@Table(name="forg_bainian_cover")
public class ForgBainianCoverDDL{
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

	@Column(name="oss_key", type=DbType.Varchar)
	private String ossKey;
	public String getOssKey() {
		return ossKey;
	}
	public void setOssKey(String ossKey){
		this.ossKey=ossKey;
	}

	public static ForgBainianCoverDDL newExample(){
		ForgBainianCoverDDL object=new ForgBainianCoverDDL();
		object.setId(null);
		object.setOssKey(null);
		return object;
	}
}
