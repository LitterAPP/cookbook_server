package modules.cookbook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import common.QueryConnectionHandler;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.CookBookFavorStatDDL;
import modules.cookbook.ddl.CookBookViewStatDDL;
import modules.cookbook.service.dto.HotCookBook;
import util.ThreadUtil;

public class ViewStatService {
	
	public static CookBookViewStatDDL get(int bookId){
		Condition condition = new Condition("CookBookViewStatDDL.cookBookId","=",bookId); 
		List<CookBookViewStatDDL> list = Dal.select("CookBookViewStatDDL.*", condition, null, 0, 1);
		if(list==null||list.size()==0){
			return null;
		}
		return 	list.get(0);
	}
	
	public static boolean view(int bookId,int value){
		CookBookViewStatDDL exist = get(bookId);
		if(exist!=null){
			exist.setViewValue(exist.getViewValue()+value);
			return Dal.replace(exist) > 0; 
		}
		CookBookViewStatDDL add = new CookBookViewStatDDL();
		add.setCookBookId(bookId); 
		add.setViewValue(value);
		return Dal.insert(add)>0;
	}
	
	public static int sumViewNum(final int bookId){
		CookBookViewStatDDL stat = get(bookId);
		if( stat == null ){ 
			final int random = 10000+new Random().nextInt(1000);
			ThreadUtil.sumbit(new Runnable(){
				@Override
				public void run() {
					view(bookId,random);//我自己的ID加喜欢值;
				}
			});
			return random;
		}
		return stat.getViewValue(); 
	}
	
	public static List<Integer> hotCookBookId(){
		List<Integer> result = new ArrayList<Integer>();
		HotCookBook dto = new HotCookBook();
		List<HotCookBook> list = Dal.getConnection("cookbook_db", 
				new QueryConnectionHandler(dto,"SELECT cook_book_id hotCookBookId,SUM(view_value) total FROM `cook_book_view_stat` GROUP BY cook_book_id ORDER BY total DESC LIMIT 0,10"));
		for(HotCookBook a:list){
			result.add(a.getHotCookBookId());
		}
		return result;
	}
	
	
}
