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
 * @createDate 2018-03-20 16:23:10
 **/
@Table(name="forg_suggest")
public class ForgSuggestDDL{
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

	@Column(name="content", type=DbType.Varchar)
	private String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content){
		this.content=content;
	}

	@Column(name="img", type=DbType.Varchar)
	private String img;
	public String getImg() {
		return img;
	}
	public void setImg(String img){
		this.img=img;
	}

	@Column(name="user_id", type=DbType.Int)
	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId){
		this.userId=userId;
	}

	@Column(name="form_id", type=DbType.Varchar)
	private String formId;
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId){
		this.formId=formId;
	}

	@Column(name="replay_content", type=DbType.Varchar)
	private String replayContent;
	public String getReplayContent() {
		return replayContent;
	}
	public void setReplayContent(String replayContent){
		this.replayContent=replayContent;
	}

	@Column(name="is_replay", type=DbType.Int)
	private Integer isReplay;
	public Integer getIsReplay() {
		return isReplay;
	}
	public void setIsReplay(Integer isReplay){
		this.isReplay=isReplay;
	}

	@Column(name="replay_time", type=DbType.DateTime)
	private Long replayTime;
	public Long getReplayTime() {
		return replayTime;
	}
	public void setReplayTime(Long replayTime){
		this.replayTime=replayTime;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static ForgSuggestDDL newExample(){
		ForgSuggestDDL object=new ForgSuggestDDL();
		object.setId(null);
		object.setContent(null);
		object.setImg(null);
		object.setUserId(null);
		object.setFormId(null);
		object.setReplayContent(null);
		object.setIsReplay(null);
		object.setReplayTime(null);
		object.setCreateTime(null);
		return object;
	}
}
