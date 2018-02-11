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
 * @createDate 2017-11-13 13:28:18
 **/
@Table(name="baidu_access_token")
public class BaiduAccessTokenDDL{
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

	@Column(name="token", type=DbType.Varchar)
	private String token;
	public String getToken() {
		return token;
	}
	public void setToken(String token){
		this.token=token;
	}

	@Column(name="expire_in", type=DbType.DateTime)
	private Long expireIn;
	public Long getExpireIn() {
		return expireIn;
	}
	public void setExpireIn(Long expireIn){
		this.expireIn=expireIn;
	}

	public static BaiduAccessTokenDDL newExample(){
		BaiduAccessTokenDDL object=new BaiduAccessTokenDDL();
		object.setId(null);
		object.setToken(null);
		object.setExpireIn(null);
		return object;
	}
}
