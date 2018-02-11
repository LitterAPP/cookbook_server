package controllers.dto;

import java.util.ArrayList;
import java.util.List;

public class BaiduDishDto {

	 /** {"log_id":5898883051074977637,"result_num":5,"result":[
	 * {"calorie":"118","has_calorie":true,"name":"蒸鲈鱼","probability":"0.282576"},
	 * {"calorie":"117","has_calorie":true,"name":"桂花鱼","probability":"0.134859"},
	 * {"calorie":"115","has_calorie":true,"name":"烧海鱼","probability":"0.0931682"},
	 * {"calorie":"105","has_calorie":true,"name":"鲈鱼","probability":"0.081893"},
	 * {"calorie":"92","has_calorie":true,"name":"石斑鱼","probability":"0.0497794"}]}
	 */
	private String log_id;
	private int result_num;
	private List<Result> result = new ArrayList<Result>();
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}
	public int getResult_num() {
		return result_num;
	}



	public void setResult_num(int result_num) {
		this.result_num = result_num;
	} 
	public List<Result> getResult() {
		return result;
	}
	public void setResult(List<Result> result) {
		this.result = result;
	} 
	public class Result{
		private String calorie;
		private boolean has_calorie;
		private String name;
		private String probability;
		public String getCalorie() {
			return calorie;
		}
		public void setCalorie(String calorie) {
			this.calorie = calorie;
		}
		public boolean isHas_calorie() {
			return has_calorie;
		}
		public void setHas_calorie(boolean has_calorie) {
			this.has_calorie = has_calorie;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getProbability() {
			return probability;
		}
		public void setProbability(String probability) {
			this.probability = probability;
		} 
	}
}
