package modules.cookbook.service;

import java.util.List;

import common.QueryConnectionHandler;
import jws.dal.Dal;
import modules.cookbook.service.dto.ChildClassDto;

public class ClassService {

	public static List<ChildClassDto> listByClasspid(int classPid){
		String sql = "select class_id classId,parent_id parentId,`name` from `cook_book_child_class` where class_id in(select distinct(class_child_id) from cook_book_class_rel where class_parent_id="+classPid+")";
		ChildClassDto dto = new ChildClassDto();
		return Dal.getConnection("cookbook_db", new QueryConnectionHandler(dto,sql)); 
	}
}
