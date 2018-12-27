
package com.huawei.rest.client.hpsdemo.bean;


public class SkillParam
{
    /**
     * 技能队列Id
     */
    private int skillId;
    
    /**
     * 呼出方式，外呼设备类型为技能队列时必选，取值范围：2：预测呼出，
     */
    private int outBoundType = 2;
    
    /**
     * 预侧外呼参数配置
     */
    private PredictParam predictParam;


    public int getSkillId()
    {
        return skillId;
    }

    public void setSkillId(int skillId)
    {
        this.skillId = skillId;
    }

    public int getOutBoundType()
    {
        return outBoundType;
    }

    public void setOutBoundType(int outBoundType)
    {
        this.outBoundType = outBoundType;
    }

    public PredictParam getPredictParam()
    {
        return predictParam;
    }

    public void setPredictParam(PredictParam predictParam)
    {
        this.predictParam = predictParam;
    }

   
    
}
