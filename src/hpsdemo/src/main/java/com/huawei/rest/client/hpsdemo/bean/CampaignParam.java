
package com.huawei.rest.client.hpsdemo.bean;





public class CampaignParam
{
    
    /**
     * 外呼活动的名称
     */
    private String name;
    
    /**
     * 外呼活动的描述
     */
    private String description;
    
    
    /**
     * 外呼活动开始时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private String strBeginTime;
    
    /**
     * 外呼活动结束时间，格式为yyyy-MM-dd HH:mm:ss，同时需要满足结束时间 > 开始时间
     */
    private String strEndTime;
    
    /**
     * 外呼设备类型，取值范围：2：技能队列，3：自动语音流程IVR
     */
    private int deviceType;
    
    /**
     * 主叫号码，呼通客户后，客户的电话终端上显示的号码。长度1~24，有效字符为：0~9、*、#
     */
    private String callerNo;
    
    /**
     * 用户不应答最大振铃时长（单位秒），取值范围：5~60
     */
    private int maxAlertingTime;
    

    /**
     * 外呼时间段
     */
    private ScheduleParam scheduleParam;
    
    /**
     * deviceType为3时，必选
     */
    private IvrParam ivrParam;
    
    /**
     * deviceType为2时，必选
     */
    private SkillParam skillParam;

   
    
    /**
     * 结果回调地址
     */
    private String callBackUrl;
    
   

    
    
    public String getName()
    
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getStrBeginTime()
    {
        return strBeginTime;
    }

    public void setStrBeginTime(String strBeginTime)
    {
        this.strBeginTime = strBeginTime;
    }

    public String getStrEndTime()
    {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime)
    {
        this.strEndTime = strEndTime;
    }

    public int getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(int deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getCallerNo()
    {
        return callerNo;
    }

    public void setCallerNo(String callerNo)
    {
        this.callerNo = callerNo;
    }

    public int getMaxAlertingTime()
    {
        return maxAlertingTime;
    }

    public void setMaxAlertingTime(int maxAlertingTime)
    {
        this.maxAlertingTime = maxAlertingTime;
    }

    public IvrParam getIvrParam()
    {
        return ivrParam;
    }

    public void setIvrParam(IvrParam ivrParam)
    {
        this.ivrParam = ivrParam;
    }

    public ScheduleParam getScheduleParam()
    {
        return scheduleParam;
    }

    public void setScheduleParam(ScheduleParam scheduleParam)
    {
        this.scheduleParam = scheduleParam;
    }

    public SkillParam getSkillParam()
    {
        return skillParam;
    }

    public void setSkillParam(SkillParam skillParam)
    {
        this.skillParam = skillParam;
    }

    

    public String getCallBackUrl()
    {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl)
    {
        this.callBackUrl = callBackUrl;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CampaignParam {");
        builder.append("' name ': '");
        builder.append(name);
        builder.append("', description ': '");
        builder.append(description);
        builder.append("', strBeginTime ': '");
        builder.append(strBeginTime);
        builder.append("', strEndTime ': '");
        builder.append(strEndTime);
        builder.append("', deviceType ': '");
        builder.append(deviceType);
        builder.append("', callerNo ': '");
        builder.append(callerNo);
        builder.append("', maxAlertingTime ': '");
        builder.append(maxAlertingTime);
        builder.append("', scheduleParam ': '");
        builder.append(scheduleParam);
        builder.append("', ivrParam ': '");
        builder.append(ivrParam);
        builder.append("', skillParam ': '");
        builder.append(skillParam);
        builder.append("', callBackUrl ': '");
        builder.append(callBackUrl);
        builder.append("}");
        return builder.toString();
    }

    
    
    
    
}
