package modules.cookbook.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jws.cache.Cache;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.ShopCouponMngDDL;

public class ShopCouponMngService {
	public static final int COUPON_PLATFROM=0;
	public static final int COUPON_NONE_PLATFROM=1;
	
	public static ShopCouponMngDDL get(int id){
		return Dal.select("ShopCouponMngDDL.*", id);
	}
	
	public static boolean getCoupon(int couponId,int userId){
		ShopCouponMngDDL coupon = get(couponId);
		if(coupon == null) return false;
		String key = "GET_COUPON_"+userId+"_"+couponId;
		Object value = Cache.get(key);
		int gets = value==null?0:Integer.parseInt(String.valueOf(value));
		if(gets>=coupon.getLimitTimes()){
			return false;
		}
		/*{ 
		    "productId":"product_1"
		    "sellerId":1156,
		    "price": 8888
		}
		*/ 
		String ruleTemp = "{\"productId\":\"%s\",\"sellerId\":%s,\"price\":%s}";
		String rule = String.format(ruleTemp, 
						coupon.getLimitProductId()==null?"":coupon.getLimitProductId(),
						coupon.getLimitSellerId()==null?0:coupon.getLimitSellerId(),
						coupon.getLimitPrice()==null?0:coupon.getLimitPrice());
		
		boolean result = UserAccountService.createCouponAccount(userId, coupon.getCouponName(), coupon.getAmount(),
				rule, coupon.getExpireTime());
		Cache.set(key, gets+1,"28d"); 
		return result;
	}
	
	/**
	 * 优先选择可领取的券活动
	 * @param productId
	 * @return
	 */
	public static List<ShopCouponMngDDL> selectCouponActivities(String productId,int sellerId,int count){
		
		List<ShopCouponMngDDL> result = new ArrayList<ShopCouponMngDDL>();
		if(count<0){
			return result;
		}
		
		//优先根据商品ID过滤符合条件的
		if(!StringUtils.isEmpty(productId)){
			Condition cond = new Condition("ShopCouponMngDDL.startTime","<=",System.currentTimeMillis());
			cond.add(new Condition("ShopCouponMngDDL.endTime",">",System.currentTimeMillis()), "and");
			cond.add(new Condition("ShopCouponMngDDL.expireTime",">",System.currentTimeMillis()), "and");
			Sort sort = new Sort("ShopCouponMngDDL.expireTime",false);
			cond.add(new Condition("ShopCouponMngDDL.couponType","=",COUPON_NONE_PLATFROM), "and");
			cond.add(new Condition("ShopCouponMngDDL.limitProductId","=",productId), "and");
			List<ShopCouponMngDDL> list = Dal.select("ShopCouponMngDDL.*", cond, sort, 0, count);
			if(list!=null && list.size() >= count){
				result.addAll(list);
				return result;
			}
			if(list != null){
				result.addAll(list);
			}
		}
		
		if(sellerId != 0){
			Condition cond = new Condition("ShopCouponMngDDL.startTime","<=",System.currentTimeMillis());
			cond.add(new Condition("ShopCouponMngDDL.endTime",">",System.currentTimeMillis()), "and");
			cond.add(new Condition("ShopCouponMngDDL.expireTime",">",System.currentTimeMillis()), "and");
			Sort sort = new Sort("ShopCouponMngDDL.expireTime",false);
			cond.add(new Condition("ShopCouponMngDDL.couponType","=",COUPON_NONE_PLATFROM), "and");
			cond.add(new Condition("ShopCouponMngDDL.limitSellerId","=",sellerId), "and");
			count -= result.size();
			if(count<=0){
				return result;
			}
			List<ShopCouponMngDDL> list = Dal.select("ShopCouponMngDDL.*", cond, sort, 0, count);
			if(list!=null && list.size() >= count){
				result.addAll(list);
				return result;
			}
			if(list != null){
				result.addAll(list);
			}
		} 
		
		 
		if(count<=0){
			return result;
		} 
		Condition cond = new Condition("ShopCouponMngDDL.startTime","<=",System.currentTimeMillis());
		cond.add(new Condition("ShopCouponMngDDL.endTime",">",System.currentTimeMillis()), "and");
		cond.add(new Condition("ShopCouponMngDDL.expireTime",">",System.currentTimeMillis()), "and");
		Sort sort = new Sort("ShopCouponMngDDL.expireTime",false);
		cond.add(new Condition("ShopCouponMngDDL.couponType","=",COUPON_PLATFROM), "and");
		List<ShopCouponMngDDL> list = Dal.select("ShopCouponMngDDL.*", cond, sort, 0, count);
		if(list!=null && list.size() >= count){
			result.addAll(list);
			return result;
		} 
		return result; 
	}
}
