
package com.huawei.rest.client.hpsdemo.bean;



public class OffhookPredictParam
{
    /**
     * 摘机率
     */
    private int offHookRate = 100;

    public int getOffHookRate()
    {
        return offHookRate;
    }

    public void setOffHookRate(int offHookRate)
    {
        this.offHookRate = offHookRate;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("OffhookPredictParam {'offHookRate ': '");
        builder.append(offHookRate);
        builder.append("}");
        return builder.toString();
    }
    
    
}
