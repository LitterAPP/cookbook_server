package modules.cookbook.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dto.APICookBookRspObjectDto;
import dto.CookBookInfoDto;
import dto.CookBookInfoListDto;
import dto.CookBookMaterialDto;
import dto.CookBookProcessDto;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.CookBookClassRelDDL;
import modules.cookbook.ddl.CookBookInfoDDL;
import modules.cookbook.ddl.CookBookMaterialDDL;
import modules.cookbook.ddl.CookBookProcessDDL;
import modules.cookbook.ddl.CookBookTagDDL;
import util.API;

public class CookBookInfoService {

	public static int count(String whereSql) {
		if (StringUtils.isEmpty(whereSql)) {
			return Dal.executeCount(CookBookInfoDDL.class, "select count(0) from cook_book_info ");
		}
		return Dal.executeCount(CookBookInfoDDL.class, "select count(0) from cook_book_info where " + whereSql);
	}

	public static CookBookInfoDDL findByBookId(int cookBookId) {
		Condition condition = new Condition("CookBookInfoDDL.cookBookInfoId", "=", cookBookId);
		List<CookBookInfoDDL> list = Dal.select("CookBookInfoDDL.*", condition, null, 0, 1);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static List<CookBookInfoDDL> findByBookIds(List<Integer> cookBookIds) {
		Condition condition = new Condition("CookBookInfoDDL.cookBookInfoId", "in", cookBookIds);
		List<CookBookInfoDDL> list = Dal.select("CookBookInfoDDL.*", condition, null, 0, -1);
		return list;
	}

	public static List<CookBookInfoDDL> findByOffset(int offset, int number) {
		List<CookBookInfoDDL> list = Dal.select("CookBookInfoDDL.*", null, null, offset, number);
		return list;
	}

	/**
	 * 搜索菜单
	 * 此搜索接口，会同步API的数据
	 * 
	 * @param key
	 * @return
	 */
	public static List<CookBookInfoDDL> search(String key, int classid) {
		//int count = CookBookInfoService.count(" name like '%" + key + "%' ");
		
		Type type = new TypeToken<APICookBookRspObjectDto<CookBookInfoListDto>>() {
		}.getType();
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("keyword", key);
		querys.put("start", String.valueOf(0));
		querys.put("num", String.valueOf(20));
		APICookBookRspObjectDto<CookBookInfoListDto> t = API.hzwsAPI("/recipe/search", "GET", querys, type,
				"6914bab576494f64b376ecffb8ee81eb");
		if (null != t && t.getStatus() == 0 && t.getResult() != null && t.getResult().getList() != null
				&& t.getResult().getList().size() > 0) {
			classify(t.getResult().getList());
		}
		
		Condition condition = new Condition("CookBookInfoDDL.name", "like", "%" + key + "%");
		if (classid > 0) {
			List<CookBookClassRelDDL> rels = CookBookRelService.findByClassIdOffset(classid, 0, -1);
			Set<Integer> cookBookIds = new HashSet<Integer>();
			if (rels != null && rels.size() > 0) {
				for (CookBookClassRelDDL rel : rels) {
					cookBookIds.add(rel.getCookBookId());
				}
				List<Integer> params = new ArrayList<Integer>(cookBookIds);
				condition.add(new Condition("CookBookInfoDDL.cookBookInfoId", "in", params), "and");
			}
		}

		List<CookBookInfoDDL> list = Dal.select("CookBookInfoDDL.*", condition, null, 0, -1);
		return list;
	}

	public static void classify(List<CookBookInfoDto> list) {
		if (list == null || list.size() == 0)
			return;
		for (CookBookInfoDto cbi : list) {
			// cook_book_info
			CookBookInfoDDL info = new CookBookInfoDDL();
			info.setContent(cbi.getContent());
			info.setCookingtime(cbi.getCookingtime());
			info.setCreateTime(System.currentTimeMillis());
			info.setName(cbi.getName());
			info.setPeoplenum(cbi.getPeoplenum());
			info.setPic(cbi.getPic());
			info.setPreparetime(cbi.getPreparetime());
			info.setCookBookInfoId(cbi.getId());
			info.setThirdPlatform("api.jisuapi.com");
			//info.setView(0);
			//info.setZan(0);
			Dal.replace(info);
			List<CookBookClassRelDDL> rels = CookBookRelService.findByClassIdOffset(cbi.getClassid(), 0, 1);
			int pclassid = 0;
			if (rels != null && rels.size() > 0) {
				pclassid = rels.get(0).getClassParentId();
			}
			CookBookClassRelDDL classRel = new CookBookClassRelDDL();
			classRel.setCookBookId(cbi.getId());
			classRel.setClassParentId(pclassid);
			classRel.setClassChildId(cbi.getClassid());
			Dal.replace(classRel);
			// cook_book_tag
			String tag = cbi.getTag();
			if (!StringUtils.isEmpty(tag)) {
				for (String tg : tag.split(",")) {
					CookBookTagDDL tagDDL = new CookBookTagDDL();
					tagDDL.setCookBookInfoId(cbi.getId());
					tagDDL.setTag(tg);
					Dal.insert(tagDDL);
					// 对早餐归类
					if (tg.equals("早餐")) {
						classRel = new CookBookClassRelDDL();
						classRel.setCookBookId(cbi.getId());
						classRel.setClassParentId(561);
						classRel.setClassChildId(562);
						Dal.replace(classRel);
					}

					if (tg.equals("午餐")) {
						classRel = new CookBookClassRelDDL();
						classRel.setCookBookId(cbi.getId());
						classRel.setClassParentId(561);
						classRel.setClassChildId(563);
						Dal.replace(classRel);
					}

					if (tg.equals("下午茶")) {
						classRel = new CookBookClassRelDDL();
						classRel.setCookBookId(cbi.getId());
						classRel.setClassParentId(561);
						classRel.setClassChildId(564);
						Dal.replace(classRel);
					}

					if (tg.equals("晚餐")) {
						classRel = new CookBookClassRelDDL();
						classRel.setCookBookId(cbi.getId());
						/*
						 * classRel.setClassParentId(565);
						 * classRel.setClassChildId(564);
						 */
						classRel.setClassParentId(561);
						classRel.setClassChildId(565);
						Dal.replace(classRel);
					}

					if (tg.equals("夜宵")) {
						classRel = new CookBookClassRelDDL();
						classRel.setCookBookId(cbi.getId());
						/*
						 * classRel.setClassParentId(566);
						 * classRel.setClassChildId(564);
						 */

						classRel.setClassParentId(561);
						classRel.setClassChildId(566);
						Dal.replace(classRel);
					}
				}
			}
			// cook_book_material
			CookBookMaterialDDL cbmDDL = new CookBookMaterialDDL();
			List<CookBookMaterialDto> mlist = cbi.getMaterial();
			if (mlist != null && mlist.size() > 0) {
				int sort = 1;
				for (CookBookMaterialDto cbm : mlist) {
					cbmDDL.setCookBookInfoId(cbi.getId());
					cbmDDL.setAmount(cbm.getAmount());
					cbmDDL.setMname(cbm.getMname());
					cbmDDL.setType(cbm.getType());
					cbmDDL.setOrderby(sort);
					Dal.replace(cbmDDL);
					sort++;
				}
			}
			// cook_book_process
			CookBookProcessDDL cbpDDL = new CookBookProcessDDL();
			List<CookBookProcessDto> plist = cbi.getProcess();
			if (plist != null && plist.size() > 0) {
				int sort = 1;
				for (CookBookProcessDto p : plist) {
					cbpDDL.setCookBookInfoId(cbi.getId());
					cbpDDL.setPcontent(p.getPcontent());
					cbpDDL.setPic(p.getPic());
					cbpDDL.setStep(sort);
					Dal.replace(cbpDDL);
					sort++;
				}
			}
		}
	}
	
	public static void main(String[] args){
		Type type = new TypeToken<APICookBookRspObjectDto<CookBookInfoListDto>>(){}.getType();
			Map<String, String> querys = new HashMap<String,String>();
			querys.put("keyword", "咸蛋黄肉松青团");
			querys.put("start", String.valueOf(0));
			querys.put("num", String.valueOf(20));
			APICookBookRspObjectDto<CookBookInfoListDto> t  = API.hzwsAPI("/recipe/search", "GET", querys, type,"6914bab576494f64b376ecffb8ee81eb");
			System.out.println(new Gson().toJson(t));
	}
}
