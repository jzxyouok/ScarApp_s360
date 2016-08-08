package com.zero2ipo.weixin.templateMessage;

import com.zero2ipo.common.entity.AdminBo;
import com.zero2ipo.common.entity.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/8.
 */
public class TemplateMessageUtils {
    /**
     * 获取模板数据
     * @return
     */
    public static WxTemplate getWxTemplateToAdmin(String openId,String msgTemplateId,String url,String mobile,String sendOrderTime,String chezhu,String carNo,String washAddr,String preTime,String washType){
        WxTemplate temp = new WxTemplate();
        temp.setTouser(openId);
        temp.setTemplate_id(msgTemplateId);
        temp.setUrl(url);
        temp.setTopcolor("#0099FF");
        Map<String,TemplateData> paramMap=new HashMap<String, TemplateData>();
        TemplateData data0=new TemplateData();
        data0.setValue("您有一条新的派单提醒,请注意查收");
        data0.setColor("#0099FF");
        paramMap.put("first",data0);
        TemplateData data1=new TemplateData();
        data1.setValue(sendOrderTime);
        data1.setColor("#0099FF");
        paramMap.put("keyword1",data1);
        TemplateData data2=new TemplateData();
        data2.setValue(mobile);
        data2.setColor("#0099FF");
        paramMap.put("keyword2",data2);
        TemplateData remark=new TemplateData();
        remark.setValue("车主:"+chezhu+",车牌号:"+carNo+",洗车地址:"+washAddr+"洗车类型:"+washType+",预约时间:"+preTime);
        remark.setColor("#0099FF");
        paramMap.put("remark",remark);
        temp.setData(paramMap);
        return temp;
    }
    public static WxTemplate getPaiDanTemplate(String openId,String msgTemplateId,String url,Order order,AdminBo bo){
        WxTemplate temp = new WxTemplate();
        temp.setTouser(openId);
        temp.setTemplate_id(msgTemplateId);
        temp.setUrl(url);
        temp.setTopcolor("#0099FF");
        Map<String,TemplateData> paramMap=new HashMap<String, TemplateData>();
        TemplateData data0=new TemplateData();
        data0.setValue("您有一条新的派单提醒,请注意查收");
        data0.setColor("#0099FF");
        paramMap.put("first",data0);
        TemplateData data1=new TemplateData();
        data1.setValue(bo.getUserNo());
        data1.setColor("#0099FF");
        paramMap.put("keyword1",data1);
        TemplateData data2=new TemplateData();
        data2.setValue(bo.getMobile());
        data2.setColor("#0099FF");
        paramMap.put("keyword2",data2);
        TemplateData remark=new TemplateData();
        remark.setValue("车主:"+order.getUserName()+",车牌号:"+order.getCarNum()+",洗车地址:"+order.getAddress()+"洗车类型:"+order.getWashType()+",预约时间:"+order.getWashTime());
        remark.setColor("#0099FF");
        paramMap.put("remark",remark);
        temp.setData(paramMap);
        return temp;
    }
 public static WxTemplate getQiangDanTemplate(String openId,String msgTemplateId,String url,Order order,AdminBo bo,String qiangdanTime){
        WxTemplate temp = new WxTemplate();
        temp.setTouser(openId);
        temp.setTemplate_id(msgTemplateId);
        temp.setUrl(url);
        temp.setTopcolor("#0099FF");
        Map<String,TemplateData> paramMap=new HashMap<String, TemplateData>();
        TemplateData data0=new TemplateData();
        data0.setValue("您好,您有新的订单可抢!");
        data0.setColor("#0099FF");
        paramMap.put("first",data0);
        TemplateData data1=new TemplateData();
        data1.setValue("抢单倒计时"+qiangdanTime+"秒");
        data1.setColor("#0099FF");
        paramMap.put("keyword1",data1);
        TemplateData data2=new TemplateData();
        data2.setValue("洗车信息:车主:"+order.getUserName() + ",车牌号:" + order.getCarNum()+",洗车地址:"+order.getAddress()+"洗车类型:"+order.getWashType()+",预约时间:"+order.getWashTime());
        data2.setColor("#0099FF");
        paramMap.put("keyword2",data2);
        TemplateData remark=new TemplateData();
        remark.setValue("点我开始抢单");
        remark.setColor("#0099FF");
        paramMap.put("remark",remark);
        temp.setData(paramMap);
        return temp;
    }

}
