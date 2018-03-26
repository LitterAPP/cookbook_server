package dto.forg;

public class TotalRankDto {

	private Integer userId;
	private String nickName;
	private String avatar;
	private Integer flows;
	private Integer rank;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Integer getFlows() {
		return flows;
	}
	public void setFlows(Integer flows) {
		this.flows = flows;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}  
}
