package dto;

import java.util.List;

public class APICookBookRspArrayDto<T> {
	
	private int status;
	private String msg;
	private List<T> result;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	} 
}
