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
 * @createDate 2018-03-09 11:44:52
 **/
@Table(name="forg_mini_app")
public class ForgMiniAppDDL{
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

	@Column(name="app_name", type=DbType.Varchar)
	private String appName;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName){
		this.appName=appName;
	}

	@Column(name="to_appid", type=DbType.Varchar)
	private String toAppid;
	public String getToAppid() {
		return toAppid;
	}
	public void setToAppid(String toAppid){
		this.toAppid=toAppid;
	}

	@Column(name="path", type=DbType.Varchar)
	private String path;
	public String getPath() {
		return path;
	}
	public void setPath(String path){
		this.path=path;
	}

	@Column(name="env_version", type=DbType.Varchar)
	private String envVersion;
	public String getEnvVersion() {
		return envVersion;
	}
	public void setEnvVersion(String envVersion){
		this.envVersion=envVersion;
	}

	@Column(name="logo", type=DbType.Varchar)
	private String logo;
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo){
		this.logo=logo;
	}

	@Column(name="desc", type=DbType.Varchar)
	private String desc;
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc){
		this.desc=desc;
	}

	@Column(name="order_by", type=DbType.Int)
	private Integer orderBy;
	public Integer getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Integer orderBy){
		this.orderBy=orderBy;
	}
	
	@Column(name="related", type=DbType.Int)
	private Integer related; 
	public Integer getRelated() {
		return related;
	}
	public void setRelated(Integer related) {
		this.related = related;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static ForgMiniAppDDL newExample(){
		ForgMiniAppDDL object=new ForgMiniAppDDL();
		object.setId(null);
		object.setAppName(null);
		object.setToAppid(null);
		object.setPath(null);
		object.setEnvVersion(null);
		object.setLogo(null);
		object.setDesc(null);
		object.setOrderBy(null);
		object.setCreateTime(null);
		return object;
	}
}
