package script;

import common.script.ScriptRunnable;
import jws.cache.Cache;
import mjh.MjhCommentMenu;

public class MJHCommentMenu extends ScriptRunnable{

	private int menuId;
	public MJHCommentMenu(String[] args) {
		super(args);
		if(args==null || args.length==0){
			System.err.println("参数错误，请提供一个菜单ID");
			System.exit(0);
		}
		menuId = Integer.parseInt(args[0]);
	}

	@Override
	public void run() {
		 while(true){
			 if( null !=Cache.get("count_"+menuId) && Integer.parseInt(String.valueOf(Cache.get("count_"+menuId))) >= 20){
				break;
			 }
			 MjhCommentMenu.comment(menuId);
		 }
	}

}
