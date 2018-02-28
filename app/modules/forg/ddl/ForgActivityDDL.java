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
 * @createDate 2018-02-28 14:26:13
 **/
@Table(name="forg_activity")
public class ForgActivityDDL{
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

	@Column(name="prize_example_pic", type=DbType.Varchar)
	private String prizeExamplePic;
	public String getPrizeExamplePic() {
		return prizeExamplePic;
	}
	public void setPrizeExamplePic(String prizeExamplePic){
		this.prizeExamplePic=prizeExamplePic;
	}

	@Column(name="act_title", type=DbType.Varchar)
	private String actTitle;
	public String getActTitle() {
		return actTitle;
	}
	public void setActTitle(String actTitle){
		this.actTitle=actTitle;
	}

	@Column(name="act_desc1", type=DbType.Varchar)
	private String actDesc1;
	public String getActDesc1() {
		return actDesc1;
	}
	public void setActDesc1(String actDesc1){
		this.actDesc1=actDesc1;
	}

	@Column(name="act_desc2", type=DbType.Varchar)
	private String actDesc2;
	public String getActDesc2() {
		return actDesc2;
	}
	public void setActDesc2(String actDesc2){
		this.actDesc2=actDesc2;
	}

	@Column(name="act_desc3", type=DbType.Varchar)
	private String actDesc3;
	public String getActDesc3() {
		return actDesc3;
	}
	public void setActDesc3(String actDesc3){
		this.actDesc3=actDesc3;
	}

	@Column(name="act_desc4", type=DbType.Varchar)
	private String actDesc4;
	public String getActDesc4() {
		return actDesc4;
	}
	public void setActDesc4(String actDesc4){
		this.actDesc4=actDesc4;
	}

	@Column(name="act_desc5", type=DbType.Varchar)
	private String actDesc5;
	public String getActDesc5() {
		return actDesc5;
	}
	public void setActDesc5(String actDesc5){
		this.actDesc5=actDesc5;
	}

	@Column(name="act_desc6", type=DbType.Varchar)
	private String actDesc6;
	public String getActDesc6() {
		return actDesc6;
	}
	public void setActDesc6(String actDesc6){
		this.actDesc6=actDesc6;
	}

	@Column(name="act_desc7", type=DbType.Varchar)
	private String actDesc7;
	public String getActDesc7() {
		return actDesc7;
	}
	public void setActDesc7(String actDesc7){
		this.actDesc7=actDesc7;
	}

	@Column(name="rank", type=DbType.Int)
	private Integer rank;
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank){
		this.rank=rank;
	}

	@Column(name="start_time", type=DbType.DateTime)
	private Long startTime;
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime){
		this.startTime=startTime;
	}

	@Column(name="end_time", type=DbType.DateTime)
	private Long endTime;
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime){
		this.endTime=endTime;
	}

	public static ForgActivityDDL newExample(){
		ForgActivityDDL object=new ForgActivityDDL();
		object.setId(null);
		object.setBookId(null);
		object.setBookName(null);
		object.setBookCover(null);
		object.setPrizeExamplePic(null);
		object.setActTitle(null);
		object.setActDesc1(null);
		object.setActDesc2(null);
		object.setActDesc3(null);
		object.setActDesc4(null);
		object.setActDesc5(null);
		object.setActDesc6(null);
		object.setActDesc7(null);
		object.setRank(null);
		object.setStartTime(null);
		object.setEndTime(null);
		return object;
	}
}
