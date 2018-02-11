package modules.cookbook.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.cookbook.ddl.CookBookUsersDDL;

public class UserService {
	
	public static CookBookUsersDDL get(int id){
		return Dal.select("CookBookUsersDDL.*", id);
	}

	public static CookBookUsersDDL login(String session){
		return findBySession(session);
	}
	
	public static CookBookUsersDDL login(String mobile,int code){
		boolean authSms = SmsService.validateSmsCode(mobile, code);
		if(!authSms)return null;
		CookBookUsersDDL exist = findByMoible(mobile);
		if( exist!=null ) {
			exist.setSession(UUID.randomUUID().toString());
			Dal.update(exist, "CookBookUsersDDL.session",new Condition("CookBookUsersDDL.id","=",exist.getId()));
			return exist;
		}
		CookBookUsersDDL user = new CookBookUsersDDL();
		user.setMobile(mobile);
		user.setSession(UUID.randomUUID().toString());
		
		if( Dal.insert(user)> 0){
			return user;
		}
		return null;
	}
	
	public static CookBookUsersDDL findByMoible(String mobile){
		Condition condition = new Condition("CookBookUsersDDL.mobile","=",mobile);
		List<CookBookUsersDDL> list = Dal.select("CookBookUsersDDL.*", condition, null, 0, 1);
		if( list!=null && list.size() == 1){
			return list.get(0);
		}
		return null;
	}
	
	public static CookBookUsersDDL findBySession(String session){
		if( StringUtils.isEmpty(session) ){
			return null;
		}
		Condition condition = new Condition("CookBookUsersDDL.session","=",session);
		List<CookBookUsersDDL> list = Dal.select("CookBookUsersDDL.*", condition, null, 0, 1);
		if( list!=null && list.size() == 1){
			return list.get(0);
		}
		return null;
	}
	
	public static List<CookBookUsersDDL>  findMJH(){
		 
		Condition condition = new Condition("CookBookUsersDDL.session","like","MJH%");
		List<CookBookUsersDDL> list = Dal.select("CookBookUsersDDL.*", condition, null, 0, -1);
		 
		return list;
	}
	
	public static CookBookUsersDDL findByOpenId(String openId){
		if( StringUtils.isEmpty(openId) ){
			return null;
		}
		Condition condition = new Condition("CookBookUsersDDL.openId","=",openId);
		List<CookBookUsersDDL> list = Dal.select("CookBookUsersDDL.*", condition, null, 0, 1);
		if( list!=null && list.size() == 1){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 更新用户资料
	 * @param session
	 * @param openId
	 * @param avatarUrl
	 * @param city
	 * @param country
	 * @param gender
	 * @param nickname
	 * @param province
	 * @param nickName
	 * @return
	 */
	public static CookBookUsersDDL updateUser(String session,
			String avatarUrl,String city,
			String country,Integer gender,
			String province,String nickName,
			String openId
			){
		
		CookBookUsersDDL existBySession = findBySession(session);
		CookBookUsersDDL existByOpenId = findByOpenId(openId);

		if(existBySession==null && existByOpenId == null) {
			CookBookUsersDDL newUser = new CookBookUsersDDL();
			newUser.setSession(UUID.randomUUID().toString());
			newUser.setAvatarUrl(avatarUrl);
			newUser.setCity(city);
			newUser.setCountry(country);
			newUser.setGender(gender);
			newUser.setNickName(nickName);
			newUser.setOpenId(openId);
			newUser.setMobile("");
			newUser.setProvince(province);
			long id = Dal.insertSelectLastId(newUser);
			newUser.setId(id);
			return newUser;
		}
		
		CookBookUsersDDL exist = existByOpenId==null?existBySession:existByOpenId;
		
		exist.setAvatarUrl(avatarUrl);
		exist.setCity(city);
		exist.setCountry(country);
		exist.setGender(gender);
		exist.setNickName(nickName);
		exist.setOpenId(openId);
		exist.setSession(exist.getSession());
		exist.setProvince(province);
		Dal.update(exist, "CookBookUsersDDL.avatarUrl,CookBookUsersDDL.session,"
				+ "CookBookUsersDDL.city,CookBookUsersDDL.country,"
				+ "CookBookUsersDDL.gender,CookBookUsersDDL.nickName,"
				+ "CookBookUsersDDL.openId,CookBookUsersDDL.province",
				new Condition("CookBookUsersDDL.id","=",exist.getId())
			);
		return exist;
	} 
	
	public static void becomeSeller(int userId,String mobile,String sellerWx) throws Exception{
		CookBookUsersDDL user = get(userId);
		if(user == null){
			throw new Exception("用户不存在");
		}
		user.setIsSeller(1);
		user.setSellerMobile(mobile);
		user.setSellerWx(sellerWx);
		Dal.update(user, 
				"CookBookUsersDDL.isSeller,CookBookUsersDDL.sellerMobile,CookBookUsersDDL.sellerWx", 
				new Condition("CookBookUsersDDL.id","=",user.getId()));
		
	}
}
