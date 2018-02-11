package controllers;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import controllers.dto.ProductInfoDto;
import controllers.dto.ProductInfoDto.Group;
import controllers.dto.ProductInfoDto.Image;
import controllers.dto.ProductInfoDto.TextDetail;
import controllers.dto.ProductInfoDto.TogetherInfo;
import jws.Logger;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.mvc.Controller;
import jws.mvc.Http;
import modules.cookbook.ddl.CookBookUsersDDL;
import modules.cookbook.ddl.ShopApplyInfoDDL;
import modules.cookbook.ddl.ShopExpressCodeDDL;
import modules.cookbook.ddl.ShopExpressDDL;
import modules.cookbook.ddl.ShopOrderDDL;
import modules.cookbook.ddl.ShopProductAttrDDL;
import modules.cookbook.ddl.ShopProductAttrRelDDL;
import modules.cookbook.ddl.ShopProductCategoryChildDDL;
import modules.cookbook.ddl.ShopProductCategoryDDL;
import modules.cookbook.ddl.ShopProductCategoryRelDDL;
import modules.cookbook.ddl.ShopProductDDL;
import modules.cookbook.ddl.ShopProductGroupDDL;
import modules.cookbook.ddl.ShopProductImagesDDL;
import modules.cookbook.service.ApplyService;
import modules.cookbook.service.ShopCategoryService;
import modules.cookbook.service.ShopExpressService;
import modules.cookbook.service.ShopOrderService;
import modules.cookbook.service.ShopProductAttrService;
import modules.cookbook.service.ShopProductGroupService;
import modules.cookbook.service.ShopProductImageService;
import modules.cookbook.service.ShopProductService;
import modules.cookbook.service.SmsService;
import modules.cookbook.service.UserService;
import util.API;
import util.AmountUtil;
import util.DateUtil;
import util.RtnUtil;

public class ShopMng extends Controller{
	private static Gson gson = new GsonBuilder().disableHtmlEscaping().create(); 
	public static void attrSource(String keyword){
		List<Object> sources = new ArrayList<Object>();
		try{ 
			List<ShopProductAttrDDL> list = ShopProductAttrService.searchByName(keyword);
			if(list==null || list.size() == 0){
				renderJSON(sources); 
			}
			for(ShopProductAttrDDL attr : list){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", attr.getAttrId());
				map.put("value", attr.getAttrName());
				map.put("sort", 0);
				map.put("selected",false);
				sources.add(map);
			}
			renderJSON(sources);
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(sources);
		}
	}
	
	public static void categoryFirSource(String keyword){
		try{ 
			List<ShopProductCategoryDDL> list = ShopCategoryService.searchByPCategoryName(keyword);
			List<Object> sources = new ArrayList<Object>();
			if(list==null || list.size() == 0){
				renderJSON(sources); 
			}
			for(ShopProductCategoryDDL cat : list){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", cat.getCategoryId());
				map.put("value",cat.getCategoryName());
				map.put("sort", 0);
				map.put("selected",false);
				sources.add(map);
			}
			renderJSON(sources);
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(null);
		}
	}
	
	public static void categorySecSource(String keyword){
		try{ 
			List<ShopProductCategoryChildDDL> list = ShopCategoryService.searchBySubCategoryName(keyword);
			List<Object> sources = new ArrayList<Object>();
			if(list==null || list.size() == 0){
				renderJSON(sources); 
			}
			for(ShopProductCategoryChildDDL cat : list){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", cat.getCategoryId());
				map.put("value",cat.getCategoryName());
				map.put("sort", 0);
				map.put("selected",false);
				sources.add(map);
			}
			renderJSON(sources);
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(null);
		}
	}
	/**
	 * 枚举源
	 * @param type
	 */
	public static void listSource(int type){
		try{ 
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			if(type == 1){
				for(ShopProductService.Type t : ShopProductService.Type.values()){
					Map<String,Object> one = new HashMap<String,Object>();
					one.put("id",t.getValue());
					one.put("value", t.getText());
					list.add(one);
				}
			}
			
			if(type == 2){
				for(ShopProductService.Status t : ShopProductService.Status.values()){
					Map<String,Object> one = new HashMap<String,Object>();
					one.put("id",t.getValue());
					one.put("value", t.getText());
					list.add(one);
				}
			}
			if(type == 3){
				List<ShopExpressCodeDDL>  expressList = ShopExpressService.listExpress();
				for(ShopExpressCodeDDL express : expressList){
					Map<String,Object> one = new HashMap<String,Object>();
					one.put("id",express.getShipperCode());
					one.put("value",express.getShipperName());
					list.add(one);
				}
			}
			renderJSON(list);
			 
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(null);
		}
	}
	
	
	/**
	 * 	{
			"title": "【测试】我商品标题",
			"p_category_id": "CAT-20171225155134-145353",
			"sub_category_id": "CAT_SUB-20171226145637-101671",
			"attrs_1": "ATTR-20171226145402-196377",
			"attrs_2": "ATTR-20171225160551-181366",
			"attrs_3": "ATTR-20171225160455-141030",
			"play_pics": ["d9883fb70d9b45779085179d1cd80add", "db6d15dcd99644bfb38e11271a1dfe17", "db6d15dcd99644bfb38e11271a1dfe17", "929c47677cfc461086309a2593caba39", "7b32f69c5dc94c808d1ffe267596c74b"],
			"banner_pic": "69d749ebec5f49c89713d92a6fac2e9d",
			"price": ["99", "88"],
			"join_together": true,
			"together_info": {
				"price": "66",
				"num": "5",
				"hour": "8",
				"vcount": "50000"
			},
			"groups": [{
				"title": "规格1",
				"price1": "66",
				"price2": "44",
				"logo": "http://tmp/wx1aebecae797c6598.o6zAJs4t197FbNSzH8Eev4ZO_ETk.65196005472a5bd971a0d6ef5ce8f08b.jpg",
				"logoKey": "e1137cc427f24eaa91ef8f1f578e57c7"
			}, {
				"title": "规格2",
				"price1": "77",
				"price2": "33",
				"logo": "http://tmp/wx1aebecae797c6598.o6zAJs4t197FbNSzH8Eev4ZO_ETk.7e9b9583a5f1ec932b0c9c21747fda7a.jpg",
				"logoKey": "db6d15dcd99644bfb38e11271a1dfe17"
			}],
			"contact_mobile": "13726759844",
			"contact_wx": "55555",
			"text_details": [{
				"value": "文字详情111"
			}, {
				"value": "文字详情22"
			}, {
				"value": "文字详情33"
			}],
			"pic_details": ["13a7bed6e60b4a5783c19b32401f4c43", "07467e15c5554808b64eb413cb030aa7"]
		}
	 * @param session
	 * @param productInfo
	 */
	public static void saveProduct(){
		try{
			String bodyStr = Http.Request.current().params.get("body");
			JsonObject requestBody = (new JsonParser()).parse(bodyStr).getAsJsonObject();
			String session = requestBody.has("session")?requestBody.get("session").getAsString():"";
			String productInfo = requestBody.has("productInfo")?requestBody.get("productInfo").getAsString():"";
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			if(user.getIsSeller()==null || user.getIsSeller() !=1){
				renderJSON(RtnUtil.returnFail("您还不是店小二"));
			}
		
			String decodeProductInfo = URLDecoder.decode(productInfo, "utf-8");
			
			Logger.info("开始建立商品数据库，%s", decodeProductInfo);
			ProductInfoDto product = gson.fromJson(decodeProductInfo, ProductInfoDto.class);
			String productId = ShopProductService.saveProduct(product, user);
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("productId",productId);
			Logger.info("建立商品数据库完成，%s", decodeProductInfo);
			
			renderJSON(RtnUtil.returnSuccess("ok",result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void findProduct(String session,String productId){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			if(user.getIsSeller()==null || user.getIsSeller() !=1){
				renderJSON(RtnUtil.returnFail("您还不是店小二"));
			}
			ShopProductDDL product = ShopProductService.getByProductId(productId);
			if(product==null || product.getSellerUserId().intValue() != user.getId()){
				throw new Exception("纳尼，非法");
			}
 			//组装商品信息
			ProductInfoDto dto = new ProductInfoDto();
			
			Image banner = new ProductInfoDto().new Image();			
			banner.remoteUrl =  API.getAliOssAccessUrl("tasty", product.getProductBanner(), 0);
			dto.banner_pic = banner;
			
 			dto.contact_mobile = product.getSellerTelNumber();
			dto.contact_wx = product.getSellerWxNumber();
			dto.join_together = (product.getJoinTogether()!=null && product.getJoinTogether()==1)?true:false;
			dto.price[0] = String.valueOf(AmountUtil.f2y(product.getProductOriginAmount()));
			dto.price[1] = String.valueOf(AmountUtil.f2y(product.getProductNowAmount()));
			dto.productId = product.getProductId();
			dto.productType = product.getProductType();
			dto.store = product.getStore();
			dto.title = product.getProductName();
			//处理团信息
			if(product.getJoinTogether() != null && product.getJoinTogether() == 1){
				TogetherInfo tinfo = new ProductInfoDto().new TogetherInfo();
				tinfo.hour = product.getTogetherExpirHour();
				tinfo.num = product.getTogetherNumber();
				tinfo.price = String.valueOf(AmountUtil.f2y( product.getProductTogetherAmount()));
				tinfo.vcount = product.getTogetherSales();
				dto.together_info = tinfo;
			}
			//处理商品详情
			List<TextDetail> textDetails = new ArrayList<TextDetail>();
			if(!StringUtils.isEmpty(product.getProductDesc())){
				for(String desc : product.getProductDesc().split("`")){
					TextDetail text = new ProductInfoDto().new TextDetail();
					text.value = desc;
					textDetails.add(text);
				}
			}
			dto.text_details = textDetails;
			//处理商品组
			List<Group> groupList = new ArrayList<Group>();
			List<ShopProductGroupDDL> groups = ShopProductGroupService.findByProductId(productId);
			for(ShopProductGroupDDL g : groups){
				Group group = new ProductInfoDto().new Group();
				group.remoteUrl = API.getAliOssAccessUrl("tasty", g.getGroupImage(), 0);
				group.osskey = g.getGroupImage();
				group.price1 = String.valueOf(AmountUtil.f2y( g.getGroupPrice()));
				group.price2 = String.valueOf(AmountUtil.f2y( g.getGroupTogetherPrice()));
				group.title = g.getGroupName();
				groupList.add(group);
			}
			dto.groups = groupList;
			//处理分类
			ShopProductCategoryRelDDL categoryRel = ShopCategoryService.listByProductId(productId).get(0);
			dto.p_category_id = categoryRel.getPCategoryId();
			dto.sub_category_id = categoryRel.getSubCategoryId();
			//处理轮播图片
			List<Image> playList = new ArrayList<Image>();
			List<ShopProductImagesDDL> plays = ShopProductImageService.listImages(productId, ShopProductImageService.SCREENSHOT_TYPE, 0, 6);
			for(ShopProductImagesDDL img : plays){
				Image ig = new ProductInfoDto().new Image();
				ig.remoteUrl = API.getAliOssAccessUrl("tasty", img.getImageKey(), 0);
				playList.add(ig);  
			}
			dto.play_pics = playList;
			//处理详情图片
			List<Image> detailPicsList = new ArrayList<Image>();
			List<ShopProductImagesDDL> details = ShopProductImageService.listImages(productId, ShopProductImageService.DETAIL_TYPE, 0, 12);
			if(details!=null && details.size()>0){
				for(ShopProductImagesDDL img : details){
					Image ig = new ProductInfoDto().new Image();
					ig.remoteUrl = API.getAliOssAccessUrl("tasty", img.getImageKey(), 0);
					detailPicsList.add(ig); 
				}
			}
			dto.pic_details = detailPicsList;	
			//处理属性
			List<ShopProductAttrRelDDL> attrs = ShopProductAttrService.listByProduct(productId);
			if(attrs!=null && attrs.size() > 0){
				dto.attrs_1 = attrs.get(0).getAttrId();
			}
			if(attrs!=null && attrs.size() > 1){
				dto.attrs_2 = attrs.get(1).getAttrId();
			}
			if(attrs!=null && attrs.size() > 2){
				dto.attrs_3 = attrs.get(2).getAttrId();
			}
			Logger.info("查询商品结果：%s", gson.toJson(dto));
			renderJSON(RtnUtil.returnSuccess("ok",dto));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void upProductOnSell(String session,String productId){
		try{
			
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			if(user.getIsSeller()==null || user.getIsSeller() !=1){
				renderJSON(RtnUtil.returnFail("您还不是店小二"));
			}
			ShopProductDDL product = ShopProductService.getByProductId(productId);
			if(product==null || product.getSellerUserId().intValue() != user.getId()){
				throw new Exception("纳尼，非法");
			}
			//上架商品
			ShopProductService.updateStatus(product.getProductId(), ShopProductService.Status.PRODUCT_STATUS_SELL.getValue());
			renderJSON(RtnUtil.returnSuccess("ok"));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void listMyProduct(String session,String keyword,int status,int page,int pageSize){
		try{
			CookBookUsersDDL user = UserService.findBySession(session);
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			if(user.getIsSeller()==null || user.getIsSeller() !=1){
				renderJSON(RtnUtil.returnNotSeller("还没成为商家"));
			}
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			List<ShopProductDDL>  list = ShopProductService.listMyProduct(user.getId().intValue(), keyword, status, page, pageSize);
			if( list == null || list.size() == 0 ) {
				renderJSON(RtnUtil.returnSuccess("ok", result));
			}
			for(ShopProductDDL p : list){
				Map<String,Object> one = new HashMap<String,Object>();
				one.put("productId", p.getProductId());
				one.put("produtName", p.getProductName());
				one.put("status", p.getStatus());
				one.put("createTime", DateUtil.format(p.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				result.add(one);
			}
			renderJSON(RtnUtil.returnSuccess("ok",result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail(e.getMessage()));
		}
	}
	
	public static void apply(/*String session,String frontCardKey,String backCardKey,String mobile,int code,String sellerWx*/){
	
		try{
			String bodyStr = Http.Request.current().params.get("body");
			JsonObject requestBody = (new JsonParser()).parse(bodyStr).getAsJsonObject();
			String session = requestBody.has("session")?requestBody.get("session").getAsString():"";
			String frontCardKey = requestBody.has("frontCardKey")?requestBody.get("frontCardKey").getAsString():"";
			String backCardKey = requestBody.has("backCardKey")?requestBody.get("backCardKey").getAsString():"";
			String mobile = requestBody.has("mobile")?requestBody.get("mobile").getAsString():"";
			int code = requestBody.has("code")?requestBody.get("code").getAsInt():0;
			String sellerWx = requestBody.has("sellerWx")?requestBody.get("sellerWx").getAsString():"";
			
			
			CookBookUsersDDL user = UserService.findBySession(session);
			
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			}
			
			if(user.getIsSeller() !=null && user.getIsSeller() ==1){
				renderJSON(RtnUtil.returnFail("你已经入住过"));
			}
			
			boolean authSms = SmsService.validateSmsCode(mobile, code);
			if( !authSms ){
				renderJSON(RtnUtil.returnFail("短信验证失败"));
			}
			
			ApplyService.apply(frontCardKey, backCardKey, mobile, sellerWx, user.getId().intValue(), 2);
			renderJSON(RtnUtil.returnSuccess());
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail("服务器异常"));
		}
	}
	
	public static void applyInfo(String session){
		try{
			 
			CookBookUsersDDL user = UserService.findBySession(session);
			
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			Map<String,Object> result = new HashMap<String,Object>();
			ShopApplyInfoDDL applyInfo = ApplyService.getApplyInfo(user.getId().intValue());
			if(applyInfo==null){
				result.put("isSeller", false);
				renderJSON(RtnUtil.returnSuccess("ok",result));
			}			
			result.put("isSeller", true);
			result.put("backCardUrl",API.getAliOssAccessUrl("tasty", applyInfo.getBackCardKey(), 0));
			result.put("frontCardUrl",API.getAliOssAccessUrl("tasty", applyInfo.getFrontCardKey(), 0));
			result.put("mobile", applyInfo.getMobile());
			result.put("wx", applyInfo.getWx());
			renderJSON(RtnUtil.returnSuccess("ok",result));
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail("服务器异常"));
		}
	}
	
	public static void deliver(String session,String orderId,String expressName,String expressCode,String expressOrderCode){
		try{
			 
			CookBookUsersDDL user = UserService.findBySession(session);
			
			if(user==null){
				renderJSON(RtnUtil.returnLoginFail());
			} 
			
			ShopOrderDDL order = ShopOrderService.findByOrderId(orderId);
			
			if(order==null){
				renderJSON(RtnUtil.returnFail("订单不存在"));
			}
			ShopExpressDDL express = ShopExpressService.getByOrderId(orderId);
			boolean result = ShopExpressService.commitShipper(express, expressOrderCode, expressCode, expressName);
			if(result){
				order.setStatus(ShopOrderService.ORDER_DELIVERED);
				if(Dal.update(order, "ShopOrderDDL.status", new Condition("ShopOrderDDL.orderId","=",orderId))>0){
					renderJSON(RtnUtil.returnSuccess());
				}else{
					renderJSON(RtnUtil.returnFail("订单更新状态失败"));
				}
				
			}else{
				renderJSON(RtnUtil.returnFail());
			}
		}catch(Exception e){
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail("服务器异常"));
		}
	}
}
