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
 * @createDate 2018-01-30 17:27:09
 **/
@Table(name="forg_reading_history")
public class ForgReadingHistoryDDL{
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

	@Column(name="book_id", type=DbType.Int)
	private Integer bookId;
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId){
		this.bookId=bookId;
	}

	@Column(name="user_id", type=DbType.Int)
	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId){
		this.userId=userId;
	}

	@Column(name="nick_name", type=DbType.Varchar)
	private String nickName;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName){
		this.nickName=nickName;
	}

	@Column(name="avatar", type=DbType.Varchar)
	private String avatar;
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar){
		this.avatar=avatar;
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

	@Column(name="read_time", type=DbType.DateTime)
	private Long readTime;
	public Long getReadTime() {
		return readTime;
	}
	public void setReadTime(Long readTime){
		this.readTime=readTime;
	}

	public static ForgReadingHistoryDDL newExample(){
		ForgReadingHistoryDDL object=new ForgReadingHistoryDDL();
		object.setId(null);
		object.setBookId(null);
		object.setUserId(null);
		object.setNickName(null);
		object.setAvatar(null);
		object.setBookName(null);
		object.setBookCover(null);
		object.setReadTime(null);
		return object;
	}
}
