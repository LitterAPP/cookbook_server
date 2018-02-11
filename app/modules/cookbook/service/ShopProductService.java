package modules.cookbook.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import controllers.dto.ProductInfoDto;
import controllers.dto.ProductInfoDto.Group;
import controllers.dto.ProductInfoDto.Image;
import controllers.dto.ProductInfoDto.TextDetail;
import jws.Logger;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.cookbook.ddl.CookBookUsersDDL;
import modules.cookbook.ddl.ShopProductAttrDDL;
import modules.cookbook.ddl.ShopProductAttrRelDDL;
import modules.cookbook.ddl.ShopProductCategoryChildDDL;
import modules.cookbook.ddl.ShopProductCategoryDDL;
import modules.cookbook.ddl.ShopProductCategoryRelDDL;
import modules.cookbook.ddl.ShopProductDDL;
import modules.cookbook.ddl.ShopProductGroupDDL;
import modules.cookbook.ddl.ShopProductImagesDDL;
import util.AmountUtil;
import util.IDUtil;

public class ShopProductService { 
	
	public enum Type{
		PRODUCT_TYPE_ENTITY(0,"实物"),PRODUCT_TYPE_PRIZE(1,"抽奖"),PRODUCT_TYPE_RENT(2,"虚拟");
		private int value;
		private String text;
		Type(int v,String t){
			value = v;
			text = t;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
	}
	
	public enum Status{
		PRODUCT_STATUS_INIT(0,"预览"),PRODUCT_STATUS_SELL(1,"上架"),PRODUCT_STATUS_UNSELL(2,"下架");
		private int value;
		private String text;
		Status(int v,String t){
			value = v;
			text = t;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
	} 
	public static ShopProductDDL get(int id){
		return Dal.select("ShopProductDDL.*", id);
	}
	
	public static ShopProductDDL getByProductId(String productId){
		if(StringUtils.isEmpty(productId)){
			return null;
		}
		List<ShopProductDDL> list = Dal.select("ShopProductDDL.*", new Condition("ShopProductDDL.productId","=",productId) , null, 0, 1);
		if( list==null || list.size() == 0) return null;
		return list.get(0);
	}
	
	public static List<ShopProductDDL> listShopIndexProduct(int page,int pageSize){
		Condition cond = new Condition("ShopProductDDL.showIndex","=",1);
		cond.add(new Condition("ShopProductDDL.store",">",100), "and");
		cond.add(new Condition("ShopProductDDL.status","=",1), "and");
		Sort sort = new Sort("ShopProductDDL.orderBy",true);
		return Dal.select("ShopProductDDL.*", cond, sort, (page-1)*pageSize, pageSize);
	}
	
	public static boolean updateStatus(String productId,int status){
		ShopProductDDL old = getByProductId(productId);
		old.setStatus(status);
		old.setUpdateTime(System.currentTimeMillis());
		return Dal.update(old, "ShopProductDDL.status,ShopProductDDL.updateTime",
				new Condition("ShopProductDDL.productId","=",productId))>0;
	}
	
	public static String saveProduct(ProductInfoDto product,CookBookUsersDDL user) throws Exception{
		try{
			ShopProductDDL p = new ShopProductDDL();
			if(!StringUtils.isEmpty(product.productId)){
				ShopProductDDL old = getByProductId(product.productId);
				if(old.getSellerUserId().intValue() != user.getId()){
					throw new Exception("无权限操作");
				}
				p.setCreateTime(old.getCreateTime());
				//下架原商品
				if(old.getStatus() == Status.PRODUCT_STATUS_INIT.getValue()){
					//不新建商品，继续编辑
					product.productId = old.getProductId(); 
				}else{
					//新建商品
					updateStatus(product.productId,Status.PRODUCT_STATUS_UNSELL.getValue());
					product.productId = IDUtil.gen("PRO");
				}
								
			}else{//新建商品
				product.productId = IDUtil.gen("PRO");
				p.setCreateTime(System.currentTimeMillis());
			}
			 
			//1处理分类关系			
			ShopProductCategoryDDL pCategory = ShopCategoryService.getByPCategoryId(product.p_category_id);
			ShopProductCategoryChildDDL subCategory = ShopCategoryService.getBySubCategoryId(product.sub_category_id);
			ShopProductCategoryRelDDL categoryRel = new ShopProductCategoryRelDDL();
			categoryRel.setPCategoryId(pCategory.getCategoryId());
			categoryRel.setSubCategoryId(subCategory.getCategoryId());
			categoryRel.setPCategoryName(pCategory.getCategoryName());
			categoryRel.setSubCategoryName(subCategory.getCategoryName());
			categoryRel.setProductId(product.productId);
			categoryRel.setOrderBy(0);
			Dal.replace(categoryRel);
			
			//2商品属性关系再建立
			String[] attrs = new String[]{null,null,null};
			if(!StringUtils.isEmpty(product.attrs_1)){
				attrs[0] = product.attrs_1;
			}
			if(!StringUtils.isEmpty(product.attrs_2)){
				attrs[1] = product.attrs_2;
			}
			if(!StringUtils.isEmpty(product.attrs_3)){
				attrs[2] = product.attrs_3;
			}
			ShopProductAttrService.delAttrRelByProductId(product.productId);
			for(int i=0;i<attrs.length;i++){
				if(!StringUtils.isEmpty(attrs[i])){
					ShopProductAttrDDL attr = ShopProductAttrService.getByAttrId(attrs[i]);
					ShopProductAttrRelDDL attrRel = new ShopProductAttrRelDDL();
					attrRel.setAttrId(attrs[i]);
					attrRel.setOrderBy(i);
					attrRel.setAttrName(attr.getAttrName());
					attrRel.setProductId(product.productId);
					Dal.insert(attrRel);
				}
			}
			//3建立商品分组关系
			int gOrderBy = 0;
			ShopProductGroupService.delGroupByProductId(product.productId);
			for(Group g : product.groups){
				ShopProductGroupDDL group = new ShopProductGroupDDL();
				group.setGroupId(IDUtil.gen("GRP"));
				group.setProductId(product.productId);
				group.setGroupImage(g.osskey);
				group.setGroupName(g.title);
				group.setGroupPrice(AmountUtil.y2f(g.price1));
				group.setGroupTogetherPrice(AmountUtil.y2f(g.price2));
				group.setOrderBy(gOrderBy);
				Dal.insert(group);
				gOrderBy++;
			}
			//4商品图片关系
			ShopProductImageService.delImageByProductIdAndType(product.productId, ShopProductImageService.SCREENSHOT_TYPE);
			for(Image pic : product.play_pics){
				ShopProductImagesDDL imgRel = new ShopProductImagesDDL();
				imgRel.setImageKey(pic.osskey);
				imgRel.setProductId(product.productId);
				imgRel.setType(ShopProductImageService.SCREENSHOT_TYPE);
				Dal.insert(imgRel);
			}
			ShopProductImageService.delImageByProductIdAndType(product.productId, ShopProductImageService.DETAIL_TYPE);
			if(product.pic_details!=null && product.pic_details.size()>0){
				for(Image pic : product.pic_details){
					ShopProductImagesDDL imgRel = new ShopProductImagesDDL();
					imgRel.setImageKey(pic.osskey);
					imgRel.setProductId(product.productId);
					imgRel.setType(ShopProductImageService.DETAIL_TYPE);
					Dal.insert(imgRel);
				}
			}
			
			p.setStore(product.store);
			p.setProductType(product.productType);
			p.setSellerUserId(user.getId().intValue());
			p.setProductId(product.productId);
			p.setProductBanner(product.banner_pic.osskey);
			p.setProductName(product.title);
			p.setProductCategory(categoryRel.getPCategoryName()+":"+categoryRel.getSubCategoryName()); 
			
			//处理文字详情
			if(product.text_details != null && product.text_details.size() > 0){
				StringBuffer sb = new StringBuffer();
				for(TextDetail td : product.text_details){
					td.value = td.value.replaceAll("`", "'");
					sb.append(td.value).append("`");
				}
				sb.substring(0, sb.length()-1);
				p.setProductDesc(sb.toString());
			}
			
			p.setProductOriginAmount(AmountUtil.y2f(product.price[0]));
			p.setProductNowAmount(AmountUtil.y2f(product.price[1]));
			//拼团
			p.setJoinTogether(product.join_together?1:0);
			if(product.join_together){
				p.setTogetherExpirHour(product.together_info.hour);
				p.setTogetherNumber(product.together_info.num);
				p.setTogetherSales(product.together_info.vcount);
				p.setProductTogetherAmount(AmountUtil.y2f(product.together_info.price));
			} 
			p.setStatus(ShopProductService.Status.PRODUCT_STATUS_INIT.getValue());
			
			p.setSellerTelNumber(user.getMobile());
			p.setSellerWxNumber(user.getSellerWx());
			if(!StringUtils.isEmpty(product.contact_mobile)){
				p.setSellerTelNumber(product.contact_mobile);
			}
			if(!StringUtils.isEmpty(product.contact_wx)){
				p.setSellerWxNumber(product.contact_wx);
			} 
			p.setUpdateTime(System.currentTimeMillis());
			 
			Dal.replace(p);
			
			return product.productId;
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			throw new Exception("保存失败");
		}
	} 

	public static List<ShopProductDDL> listMyProduct(int sellerUid,String keyword,int status,int page,int pageSize){
		if(sellerUid <=0 ) return null;
		Condition condition = new Condition("ShopProductDDL.sellerUserId","=",sellerUid);
		if( status>=0 ){
			condition.add(new Condition("ShopProductDDL.status","=",status), "and");
		}
		if(!StringUtils.isEmpty(keyword)){
			condition.add(new Condition("ShopProductDDL.productName","like","%"+keyword+"%"), "and");
		}
		Sort sort = new Sort("ShopProductDDL.id",false);
		return Dal.select("ShopProductDDL.*", condition, sort, (page-1)*pageSize, pageSize);
	}
}
