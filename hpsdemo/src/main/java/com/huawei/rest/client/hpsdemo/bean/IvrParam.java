
package com.huawei.rest.client.hpsdemo.bean;


public class IvrParam
{
    /**
     * IVR流程接入码
     */
    private String deviceSign;
    
    /**
     * IVR外呼任务每次呼出的数量
     */
    private int callCount;
    
    /**
     * IVR外呼任务的呼出间隔时间
     */
    private int callInterval;

    public String getDeviceSign()
    {
        return deviceSign;
    }

    public void setDeviceSign(String deviceSign)
    {
        this.deviceSign = deviceSign;
    }

    public int getCallCount()
    {
        return callCount;
    }

    public void setCallCount(int callCount)
    {
        this.callCount = callCount;
    }

    public int getCallInterval()
    {
        return callInterval;
    }

    public void setCallInterval(int callInterval)
    {
        this.callInterval = callInterval;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("IvrParam {'deviceSign ': '");
        builder.append(deviceSign);
        builder.append("', callCount ': '");
        builder.append(callCount);
        builder.append("', callInterval ': '");
        builder.append(callInterval);
        builder.append("}");
        return builder.toString();
    }
    
}
