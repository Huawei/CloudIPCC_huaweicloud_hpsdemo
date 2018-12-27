
package com.huawei.rest.client.hpsdemo.bean;

public class PredictParam
{
    /**
     * 预测外呼算法，5：摘机率预测算法
     */
    private int predictMethod = 5;
    
    private OffhookPredictParam offhookPredictParam;

    public int getPredictMethod()
    {
        return predictMethod;
    }

    public void setPredictMethod(int predictMethod)
    {
        this.predictMethod = predictMethod;
    }

    public OffhookPredictParam getOffhookPredictParam()
    {
        return offhookPredictParam;
    }

    public void setOffhookPredictParam(OffhookPredictParam offhookPredictParam)
    {
        this.offhookPredictParam = offhookPredictParam;
    }
    
    
}
