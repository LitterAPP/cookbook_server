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
 * @createDate 2018-02-02 10:48:44
 **/
@Table(name="forg_reading_books")
public class ForgReadingBooksDDL{
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

	@Column(name="book_name", type=DbType.Varchar)
	private String bookName;
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName){
		this.bookName=bookName;
	}

	@Column(name="book_cover", type=DbType.Varchar)
	private String bookCover;
	public String getBookCover() {
		return bookCover;
	}
	public void setBookCover(String bookCover){
		this.bookCover=bookCover;
	}

	@Column(name="book_desc", type=DbType.Varchar)
	private String bookDesc;
	public String getBookDesc() {
		return bookDesc;
	}
	public void setBookDesc(String bookDesc){
		this.bookDesc=bookDesc;
	}

	@Column(name="recommed", type=DbType.Int)
	private Integer recommed;
	public Integer getRecommed() {
		return recommed;
	}
	public void setRecommed(Integer recommed){
		this.recommed=recommed;
	}

	@Column(name="music_id", type=DbType.Int)
	private Integer musicId;
	public Integer getMusicId() {
		return musicId;
	}
	public void setMusicId(Integer musicId){
		this.musicId=musicId;
	}

	@Column(name="uploader_uid", type=DbType.Int)
	private Integer uploaderUid;
	public Integer getUploaderUid() {
		return uploaderUid;
	}
	public void setUploaderUid(Integer uploaderUid){
		this.uploaderUid=uploaderUid;
	}

	@Column(name="uploader_nickname", type=DbType.Varchar)
	private String uploaderNickname;
	public String getUploaderNickname() {
		return uploaderNickname;
	}
	public void setUploaderNickname(String uploaderNickname){
		this.uploaderNickname=uploaderNickname;
	}

	@Column(name="uploader_avatar", type=DbType.Varchar)
	private String uploaderAvatar;
	public String getUploaderAvatar() {
		return uploaderAvatar;
	}
	public void setUploaderAvatar(String uploaderAvatar){
		this.uploaderAvatar=uploaderAvatar;
	}

	@Column(name="flows", type=DbType.Int)
	private Integer flows;
	public Integer getFlows() {
		return flows;
	}
	public void setFlows(Integer flows){
		this.flows=flows;
	}

	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime){
		this.createTime=createTime;
	}

	public String createDate;
	public static ForgReadingBooksDDL newExample(){
		ForgReadingBooksDDL object=new ForgReadingBooksDDL();
		object.setId(null);
		object.setBookName(null);
		object.setBookCover(null);
		object.setBookDesc(null);
		object.setRecommed(null);
		object.setMusicId(null);
		object.setUploaderUid(null);
		object.setUploaderNickname(null);
		object.setUploaderAvatar(null);
		object.setFlows(null);
		object.setCreateTime(null);
		return object;
	}
}
