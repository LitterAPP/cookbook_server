package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgSuggestDDL;

public class ForgSuggestService {

	public static ForgSuggestDDL get(int id){
		ForgSuggestDDL suggest = Dal.select("ForgSuggestDDL.*", id);
		return suggest;
	}
	public static void commit(int userId,String content,String img,String formId){
		ForgSuggestDDL suggest = new ForgSuggestDDL();
		suggest.setContent(content);
		suggest.setCreateTime(System.currentTimeMillis());
		suggest.setIsReplay(0);
		suggest.setFormId(formId);
		suggest.setImg(img);
		suggest.setUserId(userId);
		Dal.insert(suggest);
	}
	
	public static void repaly(int id,String replayContent){
		ForgSuggestDDL suggest = get(id);
		if(suggest == null) return ;
		suggest.setReplayTime(System.currentTimeMillis());
		suggest.setIsReplay(1);
		suggest.setReplayContent(replayContent);
		Dal.update(suggest, "ForgSuggestDDL.replayTime,ForgSuggestDDL.replayContent,ForgSuggestDDL.isReplay", new Condition("ForgSuggestDDL.id","=",id));
	}
	
	public static List<ForgSuggestDDL> list(int page,int pageSize){
		return Dal.select("ForgSuggestDDL.*", null, new Sort("ForgSuggestDDL.id",false), (page-1)*pageSize, pageSize);
	}
}
