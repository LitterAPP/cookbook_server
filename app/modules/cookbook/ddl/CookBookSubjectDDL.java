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
 * @createDate 2017-11-22 17:38:36
 **/
@Table(name="cook_book_subject")
public class CookBookSubjectDDL{
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

	@Column(name="title", type=DbType.Varchar)
	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title){
		this.title=title;
	}

	@Column(name="sub_title", type=DbType.Varchar)
	private String subTitle;
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle){
		this.subTitle=subTitle;
	}

	@Column(name="keywords", type=DbType.Varchar)
	private String keywords;
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords){
		this.keywords=keywords;
	}

	@Column(name="description", type=DbType.Varchar)
	private String description;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description){
		this.description=description;
	}

	@Column(name="data", type=DbType.Varchar)
	private String data;
	public String getData() {
		return data;
	}
	public void setData(String data){
		this.data=data;
	}

	@Column(name="banner_key", type=DbType.Varchar)
	private String bannerKey;
	public String getBannerKey() {
		return bannerKey;
	}
	public void setBannerKey(String bannerKey){
		this.bannerKey=bannerKey;
	}

	@Column(name="type", type=DbType.Int)
	private Integer type;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type){
		this.type=type;
	}

	@Column(name="status", type=DbType.Int)
	private Integer status;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status){
		this.status=status;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static CookBookSubjectDDL newExample(){
		CookBookSubjectDDL object=new CookBookSubjectDDL();
		object.setId(null);
		object.setTitle(null);
		object.setSubTitle(null);
		object.setKeywords(null);
		object.setDescription(null);
		object.setData(null);
		object.setBannerKey(null);
		object.setType(null);
		object.setStatus(null);
		object.setCreateTime(null);
		return object;
	}
}
