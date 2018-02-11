package dto;

import java.util.List;

public class CookBookClassDto {

	private int classid;
	private int parentid;
	private String name;
	private List<CookBookClassDto> list;
	
	public int getClassid() {
		return classid;
	}
	public void setClassid(int classid) {
		this.classid = classid;
	}
	public int getParentid() {
		return parentid;
	}
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<CookBookClassDto> getList() {
		return list;
	}
	public void setList(List<CookBookClassDto> list) {
		this.list = list;
	} 
}
