package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.forg.ddl.ForgActivityDDL;

public class ForgActivityService {

	public static ForgActivityDDL getActivity(){
		Condition condition = new Condition("ForgActivityDDL.startTime","<",System.currentTimeMillis());
		condition.add(new Condition("ForgActivityDDL.endTime",">",System.currentTimeMillis()), "and");
		List<ForgActivityDDL> acts = Dal.select("ForgActivityDDL.*", condition, null, 0, 1);
		if( acts == null || acts.size() == 0) return null;
		return acts.get(0);
	}
}
