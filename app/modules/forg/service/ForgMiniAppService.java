package modules.forg.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Sort;
import modules.forg.ddl.ForgMiniAppDDL;

public class ForgMiniAppService {

	public static  List<ForgMiniAppDDL> listMiniApps(int page,int pageSize){
		page = page<=0?1:page;
		pageSize = (pageSize<=0 || pageSize > 10)?10:pageSize; 
		return Dal.select("ForgMiniAppDDL.*", null, new Sort("ForgMiniAppDDL.orderBy",false), (page-1)*pageSize, pageSize);
	}
	
}
