package com.huawei.rest.client.hpsdemo;

import java.util.Date;
import java.util.Map;

import com.huawei.rest.client.hpsdemo.bean.CampaignParam;
import com.huawei.rest.client.hpsdemo.bean.IvrParam;
import com.huawei.rest.client.hpsdemo.bean.ScheduleParam;
import com.huawei.rest.client.hpsdemo.http.HpsRequest;
import com.huawei.rest.client.hpsdemo.util.JsonUtils;

/**
 *测试Demo
 *
 */
public class DemoTest 
{
    public static void main( String[] args )
    {
        HpsRequest.init();
        CampaignParam campaignParam = new CampaignParam();
        campaignParam.setName("外呼活动测试" + new Date().getTime());
        campaignParam.setStrBeginTime("2018-11-12 12:12:12"); //外呼活动启动时间, 请按实际修改
        campaignParam.setStrEndTime("2018-11-13 12:12:12");//外呼活动结束时间, 请按实际修改
        campaignParam.setDeviceType(3); // 2表示转技能(用于自动外呼场景)， 2表示转IVR(用于智能外呼场景)
        campaignParam.setCallerNo(HPSConstant.CALLER_NUMBER);  //主叫号码，呼通客户后，客户的电话终端上显示的号码
        campaignParam.setMaxAlertingTime(60); // 用户不应答最大振铃时长（单位秒）
        ScheduleParam scheduleParam = new ScheduleParam();
        scheduleParam.setWorkdayBeginTime1("08:00"); //每日外呼工作时间
        scheduleParam.setWorkdayBeginTime1("18:00");
        campaignParam.setScheduleParam(scheduleParam);
        IvrParam ivrParam = new IvrParam();//设备类型选择2时，必须IVR外呼参数设置
        ivrParam.setDeviceSign(HPSConstant.IVR_DEVICE_SIGN);//流程接入码 ---联系华为侧获取
        ivrParam.setCallCount(10);//外呼任务每次呼出的数量
        ivrParam.setCallInterval(5);//外呼任务的呼出间隔时间
        campaignParam.setIvrParam(ivrParam);
        campaignParam.setCallBackUrl(HPSConstant.CALLBACK_URL);
        
        System.out.println("-----测试新增外呼任务   开始--------");
        System.out.println(JsonUtils.beanToJson(campaignParam));
        Map<String, Object> result = HpsRequest.post("/HPS/resource/paas/campaigns/" + HPSConstant.VDNID, campaignParam);
        System.out.println(JsonUtils.beanToJson(result));
        System.out.println("-----测试新增外呼任务  结束--------");
        
        
    }
}
