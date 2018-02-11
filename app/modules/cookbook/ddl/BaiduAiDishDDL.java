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
 * @createDate 2017-11-23 17:53:35
 **/
@Table(name="baidu_ai_dish")
public class BaiduAiDishDDL{
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

	@Column(name="img_data", type=DbType.Varchar)
	private String imgData;
	public String getImgData() {
		return imgData;
	}
	public void setImgData(String imgData){
		this.imgData=imgData;
	}
	
	@Column(name="img_data_md5", type=DbType.Varchar)
	private String imgDataMd5;
	public String getImgDataMd5() {
		return imgDataMd5;
	}
	public void setImgDataMd5(String imgDataMd5) {
		this.imgDataMd5 = imgDataMd5;
	}

	@Column(name="result", type=DbType.Varchar)
	private String result;
	public String getResult() {
		return result;
	}
	public void setResult(String result){
		this.result=result;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static BaiduAiDishDDL newExample(){
		BaiduAiDishDDL object=new BaiduAiDishDDL();
		object.setId(null);
		object.setImgData(null);
		object.setResult(null);
		object.setCreateTime(null);
		return object;
	}
}
