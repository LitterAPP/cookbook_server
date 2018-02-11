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
 * @createDate 2017-11-29 17:31:23
 **/
@Table(name="cook_book_memu_comment")
public class CookBookMemuCommentDDL{
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

	@Column(name="user_id", type=DbType.Int)
	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId){
		this.userId=userId;
	}

	@Column(name="user_name", type=DbType.Varchar)
	private String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}

	@Column(name="user_avatar", type=DbType.Varchar)
	private String userAvatar;
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar){
		this.userAvatar=userAvatar;
	}

	@Column(name="comment", type=DbType.Varchar)
	private String comment;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment){
		this.comment=comment;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}
	
	@Column(name="time_desc", type=DbType.Varchar)
	private String timeDesc;	
	public String getTimeDesc() {
		return timeDesc;
	}
	public void setTimeDesc(String timeDesc) {
		this.timeDesc = timeDesc;
	}
	
	public static CookBookMemuCommentDDL newExample(){
		CookBookMemuCommentDDL object=new CookBookMemuCommentDDL();
		object.setId(null);
		object.setMenuId(null);
		object.setUserId(null);
		object.setUserName(null);
		object.setUserAvatar(null);
		object.setComment(null);
		object.setCreateTime(null);
		return object;
	}
}
