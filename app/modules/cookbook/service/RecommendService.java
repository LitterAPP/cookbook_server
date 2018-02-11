package modules.cookbook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import modules.cookbook.ddl.CookBookClassRelDDL;
import modules.cookbook.ddl.CookBookFavorClassDDL;
import modules.cookbook.ddl.CookBookInfoDDL;

public class RecommendService {
	/**
	 * 根据用户喜好推荐
	 * 
	 * @param session
	 * @param number
	 */
	public static List<CookBookInfoDDL> recommendByUser(long userId, int number) {
		Random random = new Random();
		List<CookBookFavorClassDDL> list = FavorClassService.findByUserId(userId);
		if (null == list || list.size() == 0) {// 随机出菜谱
			int seedOffset = CookBookInfoService.count(null) - number;
			int randomOffset = random.nextInt(seedOffset + 1);
			return CookBookInfoService.findByOffset(randomOffset, number);
		}
		// 按分类计算占比
		float total = 0;
		for (CookBookFavorClassDDL favor : list) {
			total += favor.getFavorValue();
		}
		// 防止全部出同一类
		total = total + (total <= 10 ? 3 : total / 10);
		int[] classIdRandomContainer = new int[100];
		int start = 0;
		for (CookBookFavorClassDDL favor : list) {
			int base = (int) ((favor.getFavorValue() / total) * 100);
			for (int i = start; i < (start + base); i++) {
				classIdRandomContainer[i] = favor.getClassId();
			}
			start += base;
		}
		// 随机数组
		for (int i = 0; i < 100; i++) {
			int randomIndex = random.nextInt(100);
			// i与randomIndex交换
			int tmp = classIdRandomContainer[randomIndex];
			classIdRandomContainer[randomIndex] = classIdRandomContainer[i];
			classIdRandomContainer[i] = tmp;
		}
		List<CookBookInfoDDL> result = new ArrayList<CookBookInfoDDL>();
		Map<String, String> bookIdCache = new HashMap<String, String>();
		do {
			int randomIndex = random.nextInt(100);
			int classId = classIdRandomContainer[randomIndex];
			if (classId == 0) {
				int seedOffset = CookBookInfoService.count(null);
				int randomOffset = random.nextInt(seedOffset);
				List<CookBookInfoDDL> lists = CookBookInfoService.findByOffset(randomOffset, 1);
				if (null != lists && lists.size() > 0
						&& !bookIdCache.containsKey(String.valueOf(lists.get(0).getCookBookInfoId()))) {
					result.add(lists.get(0));
					bookIdCache.put(String.valueOf(lists.get(0).getCookBookInfoId()), null);
				}
			} else {
				int seedOffset = CookBookRelService.count(" class_child_id=" + classId);
				int randomOffset = random.nextInt(seedOffset);
				List<CookBookClassRelDDL> rels = CookBookRelService.findByClassIdOffset(classId, randomOffset, 1);
				if (rels == null || rels.size() == 0) {
					classIdRandomContainer[randomIndex] = 0;
					continue;
				}
				int cookBookId = rels.get(0).getCookBookId();
				CookBookInfoDDL cookBook = CookBookInfoService.findByBookId(cookBookId);
				if (cookBook == null) {
					classIdRandomContainer[randomIndex] = 0;
					continue;
				}
				if (!bookIdCache.containsKey(String.valueOf(cookBookId))) {
					result.add(cookBook);
					bookIdCache.put(String.valueOf(cookBookId), null);
				}
			}
		} while (result.size() < number);

		return result;
	}

	/**
	 * 指定分类随机推荐
	 * 
	 * @param userId
	 * @param classId
	 * @param number
	 */
	public static List<CookBookInfoDDL> recommendByClass(long userId,int classId, int number) {
		Random random = new Random();
		Map<String, String> bookIdCache = new HashMap<String, String>();
		List<CookBookInfoDDL> result = new ArrayList<CookBookInfoDDL>();
		do {
 			if (classId == 0) {
				int seedOffset = CookBookInfoService.count(null);
				int randomOffset = random.nextInt(seedOffset);
				List<CookBookInfoDDL> lists = CookBookInfoService.findByOffset(randomOffset, 1);
				if (null != lists && lists.size() > 0
						&& !bookIdCache.containsKey(String.valueOf(lists.get(0).getCookBookInfoId()))) {
					result.add(lists.get(0));
					bookIdCache.put(String.valueOf(lists.get(0).getCookBookInfoId()), null);
				}
			} else {
				int seedOffset = CookBookRelService.count(" class_child_id=" + classId);
				int randomOffset = random.nextInt(seedOffset);
				List<CookBookClassRelDDL> rels = CookBookRelService.findByClassIdOffset(classId, randomOffset, 1);
				if (rels == null || rels.size() == 0) {
					continue;
				}
				int cookBookId = rels.get(0).getCookBookId();
				CookBookInfoDDL cookBook = CookBookInfoService.findByBookId(cookBookId);
				if (cookBook == null) { 
					continue;
				}
				if (!bookIdCache.containsKey(String.valueOf(cookBookId))) {
					result.add(cookBook);
					bookIdCache.put(String.valueOf(cookBookId), null);
				}
			}
		} while (result.size() < number);
		
		return result;
	}
}
