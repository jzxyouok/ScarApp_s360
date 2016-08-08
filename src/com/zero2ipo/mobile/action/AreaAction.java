package com.zero2ipo.mobile.action;

import com.zero2ipo.core.MobileContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.dictionary.IDictionaryService;
import com.zero2ipo.mobile.utils.GetLocation;
import com.zero2ipo.module.entity.code.CodeInfoEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/31.
 */
@Controller
public class AreaAction {
	/**
	 * 定位当前位置和服务范围之间的距离
	 */
	@RequestMapping(value = "/getAreaRegion.html", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> registerPost(HttpServletRequest request,
										   HttpServletResponse response, ModelMap model,
										   double lng, double lat) {
		Map<String,Object> map=new HashMap<String, Object>();
		/**
		 * 获取所有的服务范围列表
		 */
		String pcodeType=MobileContants.AREA_CODE_TYPE;
		/*判断经纬度是否为空*/
		boolean flg=false;
		try {
			List<CodeInfoEntity> list=dictionaryService.findCodeInfosBySortcode(pcodeType);
			int count=list.size();
			for(int i=0;i<count;i++){
				CodeInfoEntity codeInfoEntity=list.get(i);
				String lngstr=codeInfoEntity.getContent1();
				String latstr=codeInfoEntity.getContent2();
				String arealong=codeInfoEntity.getContent3();
				if(!StringUtil.isNullOrEmpty(latstr)&&!StringUtil.isNullOrEmpty(lngstr)){
					String quName= GetLocation.getQuname(String.valueOf(lng),String.valueOf(lat));
					System.out.println("当前位置所在的区======================================="+quName);
					//double lng2=Double.parseDouble(lngstr);
					//double lat2=Double.parseDouble(latstr);
					//System.out.println("dangqian weizhi :"+lat+"\t"+lng);
					//System.out.println("shujukuzhong :"+lat2+"\t"+lng2);
					//获取该经纬度和当前位置之间的距离
					//double d=DistanceUtil.GetDistance(lng,lat,lng2,lat2);
					//后台设置的范围,如果后台没有设置范围，默认1公里
					/*if(StringUtil.isNullOrEmpty(arealong)){
						arealong=MobileContants.DEFAULT_AREA_LONG;
					}*/
					//double da= Double.parseDouble(arealong);
					String name=codeInfoEntity.getCodeName();
					System.out.println("已开通："+codeInfoEntity.getCodeName());
					if(name.contains(quName)){//如果当前位置范围在后台配置的范围之内，可以正常下单
						flg=true;
						break;
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		map.put("success",flg);
		return map;
	}

	@Resource(name = "dictionaryService")
	private IDictionaryService dictionaryService;
}

