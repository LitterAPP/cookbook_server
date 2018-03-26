package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.forg.ddl.ForgReadSpeedConfigDDL;

public class ForgReadSpeedService {

	public static void add(int userId,int speed){
		ForgReadSpeedConfigDDL speedConfig = new ForgReadSpeedConfigDDL();
		speedConfig.setCreateTime(System.currentTimeMillis());
		speedConfig.setSpeed(speed);
		speedConfig.setUserId(userId);
		Dal.insertUpdate(speedConfig, "ForgReadSpeedConfigDDL.speed") ;
	}
	
	public static ForgReadSpeedConfigDDL getByUserId(int userId){
		Condition cond = new Condition("ForgReadSpeedConfigDDL.userId","=",userId);
		List<ForgReadSpeedConfigDDL> list = Dal.select("ForgReadSpeedConfigDDL.*", cond, null, 0, 1);
		if( list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
