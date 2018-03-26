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
 * @createDate 2018-03-05 17:54:54
 **/
@Table(name="forg_reading_records")
public class ForgReadingRecordsDDL{
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

	@Column(name="read_id", type=DbType.Int)
	private Integer readId;
	public Integer getReadId() {
		return readId;
	}
	public void setReadId(Integer readId){
		this.readId=readId;
	}

	@Column(name="page_id", type=DbType.Int)
	private Integer pageId;
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId){
		this.pageId=pageId;
	}

	@Column(name="voice_oss", type=DbType.Varchar)
	private String voiceOss;
	public String getVoiceOss() {
		return voiceOss;
	}
	public void setVoiceOss(String voiceOss){
		this.voiceOss=voiceOss;
	}

	@Column(name="fileSize", type=DbType.Int)
	private Integer fileSize;
	public Integer getFileSize() {
		return fileSize;
	}
	public void setFileSize(Integer fileSize){
		this.fileSize=fileSize;
	}

	@Column(name="duration", type=DbType.Int)
	private Integer duration;
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration){
		this.duration=duration;
	}

	public static ForgReadingRecordsDDL newExample(){
		ForgReadingRecordsDDL object=new ForgReadingRecordsDDL();
		object.setId(null);
		object.setReadId(null);
		object.setPageId(null);
		object.setVoiceOss(null);
		object.setFileSize(null);
		object.setDuration(null);
		return object;
	}
}
