package task;

import java.util.List;

import jws.Logger;
import modules.forg.ddl.ForgActivityDDL;
import modules.forg.service.ForgActivityService;

/**
 * 阅读点赞排行
 * @author fish
 *
 */
public class FrogRank implements Runnable{
	@Override
	public void run() {
		try{
			 List<ForgActivityDDL> needRankActivities = ForgActivityService.listNeedRankActivity();
			 Logger.info("FrogRank size = %s", needRankActivities==null?0:needRankActivities.size());
			 if(needRankActivities==null || needRankActivities.size() == 0){
				 return;
			 }
			 for(ForgActivityDDL activity : needRankActivities){
				 ForgActivityService.rankActivity(activity);
				 Logger.info("FrogRank done , activityId = %s", activity.getId());
			 }
		}catch(Exception e){
			Logger.error(e, e.getMessage());
		}
	}
}
