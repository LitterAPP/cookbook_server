package dto;

import java.util.List;

public class CookBookInfoDto {
	private int id;
	private int classid;
	private String name;
	private String peoplenum;
	private String preparetime;
	private String cookingtime;
	private String content;
	private String pic;
	private String tag;
	private List<CookBookMaterialDto> material;
	private List<CookBookProcessDto> process;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getClassid() {
		return classid;
	}
	public void setClassid(int classid) {
		this.classid = classid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPeoplenum() {
		return peoplenum;
	}
	public void setPeoplenum(String peoplenum) {
		this.peoplenum = peoplenum;
	}
	public String getPreparetime() {
		return preparetime;
	}
	public void setPreparetime(String preparetime) {
		this.preparetime = preparetime;
	}
	public String getCookingtime() {
		return cookingtime;
	}
	public void setCookingtime(String cookingtime) {
		this.cookingtime = cookingtime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public List<CookBookMaterialDto> getMaterial() {
		return material;
	}
	public void setMaterial(List<CookBookMaterialDto> material) {
		this.material = material;
	}
	public List<CookBookProcessDto> getProcess() {
		return process;
	}
	public void setProcess(List<CookBookProcessDto> process) {
		this.process = process;
	} 
}
