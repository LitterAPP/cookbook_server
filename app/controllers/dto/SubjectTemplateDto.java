package controllers.dto;

import java.util.ArrayList;
import java.util.List;

public class SubjectTemplateDto {

	private String preParagraph;
	private String title;
	private String qrcode;
	
	private List<Content> contents = new ArrayList<Content>();
	
	public String getPreParagraph() {
		return preParagraph;
	}



	public void setPreParagraph(String preParagraph) {
		this.preParagraph = preParagraph;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getQrcode() {
		return qrcode;
	}



	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}



	public List<Content> getContents() {
		return contents;
	}



	public void setContents(List<Content> contents) {
		this.contents = contents;
	}



	public class Content{
		private int num;
		private String name;
		private String paragraph;
		private List<String> pics;
		
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getParagraph() {
			return paragraph;
		}
		public void setParagraph(String paragraph) {
			this.paragraph = paragraph;
		}
		public List<String> getPics() {
			return pics;
		}
		public void setPics(List<String> pics) {
			this.pics = pics;
		} 
		
	}
}
