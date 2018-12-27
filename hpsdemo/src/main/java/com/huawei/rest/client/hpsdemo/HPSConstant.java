
package com.huawei.rest.client.hpsdemo;

public interface HPSConstant
{
    /**
     * HPS服务器IP
     */
    String IP_ADDRESS = "10.177.31.201";
    
    /**
     * HPS的端口
     */
    String IP_PORT = "8542";
    
    /**
     * 联络中心的开发者APPID
     */
    String HPS_APPID = "FRX3RE2OIPISZGSRJMVY";
    
    /**
     * 联络中心的开发者APPSECRET
     */
    String HPS_APPSECRET = "99nT3Lj10FEdSJvHjh2dq1OVl1YQ3XTnDHZErRLh";
    
    
    /**
     * 租户所在VDNID
     */
    int VDNID = 1;
    
    /**
     * 外呼结果回调地址
     */
    String CALLBACK_URL = "http://177.21.21.12/callback";
    
    /**
     * 智能外呼时的流程接入码
     */
    String IVR_DEVICE_SIGN = "70941";
    
    /**
     * 主叫号码，呼通客户后，客户的电话终端上显示的号码
     */
    String CALLER_NUMBER = "5656565";
}
