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
 * @createDate 2018-03-12 15:08:15
 **/
@Table(name="forg_reading_content_shot")
public class ForgReadingContentShotDDL{
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

	@Column(name="page_num", type=DbType.Int)
	private Integer pageNum;
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum){
		this.pageNum=pageNum;
	}

	@Column(name="page_shot", type=DbType.Varchar)
	private String pageShot;
	public String getPageShot() {
		return pageShot;
	}
	public void setPageShot(String pageShot){
		this.pageShot=pageShot;
	}

	@Column(name="read_time", type=DbType.Int)
	private Integer readTime;
	public Integer getReadTime() {
		return readTime;
	}
	public void setReadTime(Integer readTime){
		this.readTime=readTime;
	}

	@Column(name="word_count", type=DbType.Int)
	private Integer wordCount;
	public Integer getWordCount() {
		return wordCount;
	}
	public void setWordCount(Integer wordCount){
		this.wordCount=wordCount;
	}

	@Column(name="page_text", type=DbType.Varchar)
	private String pageText;
	public String getPageText() {
		return pageText;
	}
	public void setPageText(String pageText){
		this.pageText=pageText;
	}

	public static ForgReadingContentShotDDL newExample(){
		ForgReadingContentShotDDL object=new ForgReadingContentShotDDL();
		object.setId(null);
		object.setBookId(null);
		object.setPageNum(null);
		object.setPageShot(null);
		object.setReadTime(null);
		object.setWordCount(null);
		object.setPageText(null);
		return object;
	}
}
