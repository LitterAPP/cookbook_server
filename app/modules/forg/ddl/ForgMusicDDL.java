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
 * @createDate 2018-01-30 13:32:12
 **/
@Table(name="forg_music")
public class ForgMusicDDL{
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

	@Column(name="name", type=DbType.Varchar)
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name=name;
	}

	@Column(name="oss_key", type=DbType.Varchar)
	private String ossKey;
	public String getOssKey() {
		return ossKey;
	}
	public void setOssKey(String ossKey){
		this.ossKey=ossKey;
	}

	@Column(name="type", type=DbType.Int)
	private Integer type;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type){
		this.type=type;
	}

	public static ForgMusicDDL newExample(){
		ForgMusicDDL object=new ForgMusicDDL();
		object.setId(null);
		object.setName(null);
		object.setOssKey(null);
		object.setType(null);
		return object;
	}
}
