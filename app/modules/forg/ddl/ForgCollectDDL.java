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
 * @createDate 2018-03-08 13:01:43
 **/
@Table(name="forg_collect")
public class ForgCollectDDL{
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

	@Column(name="collect_time", type=DbType.DateTime)
	private Long collectTime;
	public Long getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Long collectTime){
		this.collectTime=collectTime;
	}

	public static ForgCollectDDL newExample(){
		ForgCollectDDL object=new ForgCollectDDL();
		object.setId(null);
		object.setBookId(null);
		object.setUserId(null);
		object.setCollectTime(null);
		return object;
	}
}
