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
 * @createDate 2017-11-09 15:40:54
 **/
@Table(name="cook_book_info")
public class CookBookInfoDDL{
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

	@Column(name="name", type=DbType.Varchar)
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name=name;
	}

	@Column(name="peoplenum", type=DbType.Varchar)
	private String peoplenum;
	public String getPeoplenum() {
		return peoplenum;
	}
	public void setPeoplenum(String peoplenum){
		this.peoplenum=peoplenum;
	}

	@Column(name="preparetime", type=DbType.Varchar)
	private String preparetime;
	public String getPreparetime() {
		return preparetime;
	}
	public void setPreparetime(String preparetime){
		this.preparetime=preparetime;
	}

	@Column(name="cookingtime", type=DbType.Varchar)
	private String cookingtime;
	public String getCookingtime() {
		return cookingtime;
	}
	public void setCookingtime(String cookingtime){
		this.cookingtime=cookingtime;
	}

	@Column(name="content", type=DbType.Varchar)
	private String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content){
		this.content=content;
	}

	@Column(name="pic", type=DbType.Varchar)
	private String pic;
	public String getPic() {
		return pic;
	}
	public void setPic(String pic){
		this.pic=pic;
	}

	@Column(name="third_platform", type=DbType.Varchar)
	private String thirdPlatform;
	public String getThirdPlatform() {
		return thirdPlatform;
	}
	public void setThirdPlatform(String thirdPlatform){
		this.thirdPlatform=thirdPlatform;
	}

	@Column(name="ai_name", type=DbType.Varchar)
	private String aiName;
	public String getAiName() {
		return aiName;
	}
	public void setAiName(String aiName){
		this.aiName=aiName;
	}

	@Column(name="ai_calorie", type=DbType.Float)
	private Float aiCalorie;
	public Float getAiCalorie() {
		return aiCalorie;
	}
	public void setAiCalorie(Float aiCalorie){
		this.aiCalorie=aiCalorie;
	}

	@Column(name="ai_probability", type=DbType.Float)
	private Float aiProbability;
	public Float getAiProbability() {
		return aiProbability;
	}
	public void setAiProbability(Float aiProbability){
		this.aiProbability=aiProbability;
	}

	@Column(name="view", type=DbType.Int)
	private Integer view;
	public Integer getView() {
		return view;
	}
	public void setView(Integer view){
		this.view=view;
	}

	@Column(name="zan", type=DbType.Int)
	private Integer zan;
	public Integer getZan() {
		return zan;
	}
	public void setZan(Integer zan){
		this.zan=zan;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public static CookBookInfoDDL newExample(){
		CookBookInfoDDL object=new CookBookInfoDDL();
		object.setId(null);
		object.setCookBookInfoId(null);
		object.setName(null);
		object.setPeoplenum(null);
		object.setPreparetime(null);
		object.setCookingtime(null);
		object.setContent(null);
		object.setPic(null);
		object.setThirdPlatform(null);
		object.setAiName(null);
		object.setAiCalorie(null);
		object.setAiProbability(null);
		object.setView(null);
		object.setZan(null);
		object.setCreateTime(null);
		return object;
	}
}
