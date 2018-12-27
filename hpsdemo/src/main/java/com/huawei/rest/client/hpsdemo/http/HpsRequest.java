
package com.huawei.rest.client.hpsdemo.http;

import javax.net.ssl.SSLContext;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.rest.client.hpsdemo.HPSConstant;
import com.huawei.rest.client.hpsdemo.sign.SignInfo;
import com.huawei.rest.client.hpsdemo.util.JsonUtils;


public class HpsRequest
{
    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(HpsRequest.class);
    
    /**
     * Max connections of connection pool,unit:millisecond
     */
    private static final int MAXCONNECTION = 50;
    
    /**
     * Connections of every route,unit:millisecond
     */
    private static final int MAXPERROUTE = 1;
    
    /**
     * Max request time of getting a connection from connection pool,unit:millisecond
     */
    private static final int REQUESTTIMEOUT = 5000;
    
    /**
     * Max time of a request,unit:millisecond
     */
    private static final int CONNECTIMEOUT = 120000;
    
    /**
     * Max time of waiting for response message,unit:millisecond
     */
    private static final int SOCKETIMEOUT =300000;

    
    private static PoolingHttpClientConnectionManager connManager = null;
    
    private static CloseableHttpClient client = null;
    
    private static String hps_url;
    
    public static void init()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("https://").append(HPSConstant.IP_ADDRESS).append(":").append(HPSConstant.IP_PORT);
        hps_url = sb.toString();
        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
        
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext
                    , new X509HostnameVerifier(){
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
                public void verify(String host, SSLSocket ssl)
                        throws IOException {
                }
                public void verify(String host, X509Certificate cert)
                        throws SSLException {
                }
                public void verify(String host, String[] cns,
                        String[] subjectAlts) throws SSLException {
                }
            });
            
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();
            
            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connManager.setMaxTotal(MAXCONNECTION);
            connManager.setDefaultMaxPerRoute(MAXPERROUTE);
            
        } 
        catch (RuntimeException e) 
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("init connection pool failed \r\n {}: ", e.getMessage());
            return;
        }
        
        client = getConnection();
    }
    

    private static CloseableHttpClient getConnection()
    {
        RequestConfig restConfig = RequestConfig.custom().setConnectionRequestTimeout(REQUESTTIMEOUT)
                .setConnectTimeout(CONNECTIMEOUT)
                .setSocketTimeout(SOCKETIMEOUT).build();
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler()
        {
            public boolean retryRequest(IOException exception, int executionCount,
                    HttpContext context)
            {
                if (executionCount >= 3)
                {
                   return false; 
                }
                if (exception instanceof NoHttpResponseException) 
                {
                    return true;  
                } 
                if (exception instanceof InterruptedIOException) 
                {
                    return false;
                }
                if (exception instanceof SSLHandshakeException) 
                {
                    return false;  
                }  
                if (exception instanceof UnknownHostException) 
                {
                    return false;  
                }  
                if (exception instanceof ConnectTimeoutException) 
                {
                    return false;  
                }  
                if (exception instanceof SSLException) 
                {
                    return false;  
                }
                
                HttpClientContext clientContext = HttpClientContext.adapt(context);  
                HttpRequest request = clientContext.getRequest();  
                if (!(request instanceof HttpEntityEnclosingRequest)) 
                {  
                    return true;  
                }  
                return false;  
            }
        };
        CloseableHttpClient httpClient = HttpClients.custom()
                .disableCookieManagement()
                .setConnectionManager(connManager).setDefaultRequestConfig(restConfig).setRetryHandler(retryHandler).build();
        return httpClient;
    }
    
    /**
     * hps请求
     * @param url HPS的接口url淡出的接口url不带参数  /HPS/resource/开始
     * @param queryMap get请求的参数键值对,使用未编码的值，该方法内会对他进行编码
     * @return
     */
    public static Map<String, Object> get(String uri, Map<String,String> queryMap)
    {
       CloseableHttpResponse response = null;
        HttpGet get = null;
        Map<String, Object> result  = null;
        try 
        {
            //先计算authString，之后加入到请求的header中
            SignInfo signInfo = new SignInfo();
            String authString = signInfo.genAuthString("GET", uri, queryMap, null);
            
            //拼接请求的url = hps地址 +uri+queryMap转换拼接的字符串
            StringBuffer sb = new StringBuffer();
            sb.append(hps_url).append(uri);   
            if(null != queryMap)
            {
                buildHttpUrl(sb,queryMap);
            }
            
            //对url进行归一化
            String url = Normalizer.normalize(sb.toString(), Form.NFKC);
            get = new HttpGet(url);
            
            //设置headers
            setHeaders(get, authString);           
            response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK)
            {
                HttpEntity entity = response.getEntity();
                if (null != entity)
                {
                    String entityContent = EntityUtils.toString(entity,"UTF-8");
                    result = JsonUtils.jsonToMap(entityContent);          
                }
                else
                {
                    result = returnContentError();
                }
                try 
                {
                    EntityUtils.consume(entity);
                } 
                catch (IOException e) 
                {
                    LOG.error("release entity failed \r\n {}", e.getMessage());
                }
            }
            else
            {
                result = returnParamError(statusCode);
            }
        }
        catch (UnsupportedEncodingException e) 
        {
            result = returnConnectError(e);
        } catch (ClientProtocolException e)
        {
            result = returnConnectError(e);
        }
        catch (IOException e) 
        {
            result =  returnConnectError(e);
        }
        catch (InvalidKeyException e)
        {
            result =  returnConnectError(e);
        }
        catch (NoSuchAlgorithmException e)
        {
            result =  returnConnectError(e);
        }
        finally
        {
            if (null != response)
            {
                try
                {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } 
                catch (IOException e) 
                {
                    LOG.error("release response failed \r\n {}", e.getMessage());
                }
            }
        }
        
        return result;
    }
    
    /**
     * Send http's POST request
     * @param url:the address of the request
     * @param entityParams:the paramters of entity
     * @return
     */
    public static Map<String, Object> post(String uri, Object entityParams)
    {
        Map<String, Object> result = null;
        HttpPost post = null;
        CloseableHttpResponse response = null;
        try 
        {
            //先计算authString，之后加入到请求的header中
            SignInfo signInfo = new SignInfo();
            String authString = signInfo.genAuthString("POST", uri, null, entityParams);
            
            //拼接请求的url = hps地址 +uri+queryMap转换拼接的字符串
            StringBuffer sb = new StringBuffer();
            sb.append(hps_url).append(uri);            

            //对url进行归一化
            String url = Normalizer.normalize(sb.toString(), Form.NFKC);
     
            post = new HttpPost(url);
            if (null != entityParams)
            {               
                String jsonString = JsonUtils.beanToJson(entityParams);
                //使用utf-8进行传输
                HttpEntity entity = new StringEntity(jsonString,"utf-8"); 
                post.setEntity(entity);
            }
            setHeaders(post, authString);       
            response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK)
            {
                HttpEntity entity = response.getEntity();
                if (null != entity)
                {
                    String entityContent = EntityUtils.toString(entity,"UTF-8");
                    result = JsonUtils.jsonToMap(entityContent);
                }
                else
                {
                    result = returnContentError();
                }
                try 
                {
                    EntityUtils.consume(entity);
                } 
                catch (IOException e) 
                {
                    LOG.error("release entity failed \r\n {}", e.getMessage());
                }
            }
            else
            {
            	result = returnParamError(statusCode);
            }
        }
        catch (UnsupportedEncodingException e) 
        {
            result = returnConnectError(e);
        } catch (ClientProtocolException e)
        {
            result = returnConnectError(e);
        }
        catch (IOException e) 
        {
            result =  returnConnectError(e);
        }
        catch (InvalidKeyException e)
        {
            result =  returnConnectError(e);
        }
        catch (NoSuchAlgorithmException e)
        {
            result =  returnConnectError(e);
        }
        finally
        {
            if (null != response)
            {
                try
                {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } 
                catch (IOException e) 
                {
                    LOG.error("release response failed \r\n {}", e.getMessage());
                }
            }
        }
        return result;
    }
    
   
    /**
     * Send http's PUT request
     * @param url:the address of the request
     * @param entityParams:the paramters of entity
     * @return
     */
    public static Map<String, Object> put(String uri, Object entityParams)
    {
        CloseableHttpResponse response = null;
        HttpPut put = null;
        Map<String, Object> result = null;
        try 
        {
          //先计算authString，之后加入到请求的header中
            SignInfo signInfo = new SignInfo();
            String authString = signInfo.genAuthString("PUT", uri, null, entityParams);
            
            //拼接请求的url = hps地址 +uri+queryMap转换拼接的字符串
            StringBuffer sb = new StringBuffer();
            sb.append(hps_url).append(uri);            

            //对url进行归一化
            String url = Normalizer.normalize(sb.toString(), Form.NFKC);
            
            put = new HttpPut(url);
            if (null != entityParams) {
                String jsonString = JsonUtils.beanToJson(entityParams);
                HttpEntity entity = new StringEntity(jsonString,"utf-8");
                put.setEntity(entity);
            }
            
            setHeaders(put, authString);
            
            response = client.execute(put);         
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK)
            {
                HttpEntity entity = response.getEntity();
                if (null != entity)
                {
                    String entityContent = EntityUtils.toString(entity,"UTF-8");
                    result = JsonUtils.jsonToMap(entityContent);
                }
                else
                {
                    result = returnContentError();
                }
                try 
                {
                    EntityUtils.consume(entity);
                } 
                catch (IOException e) 
                {
                    LOG.error("release entity failed \r\n{} ", e.getMessage());
                }
            }
            else
            {
            	result = returnParamError(statusCode);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            result = returnConnectError(e);
        } 
        catch (ClientProtocolException e)
        {
            result =  returnConnectError(e);
        } 
        catch (IOException e)
        {
            result = returnConnectError(e);
        }
        catch (InvalidKeyException e)
        {
            result = returnConnectError(e);
        }
        catch (NoSuchAlgorithmException e)
        {
            result = returnConnectError(e);
        }
        finally
        {
            if (null != response)
            {
                try
                {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } 
                catch (IOException e) 
                {
                    LOG.error("release response failed \r\n {}", e.getMessage());
                }
            }
        }
        return result;
    }    
    
    
    /**
     * Send http's Delete request
     * @param url:the address of the request
     * @return
     */
    public static Map<String, Object> delete(String uri)
    {

        CloseableHttpResponse response = null;
        MyHttpDelete delete = null;
        Map<String, Object> result = null;
        try 
        {
          //先计算authString，之后加入到请求的header中
            SignInfo signInfo = new SignInfo();
            String authString = signInfo.genAuthString("DELETE", uri, null, null);
            
            //拼接请求的url = hps地址 +uri+queryMap转换拼接的字符串
            StringBuffer sb = new StringBuffer();
            sb.append(hps_url).append(uri);            
            
            //对url进行归一化
            String url = Normalizer.normalize(sb.toString(), Form.NFKC);
            delete = new MyHttpDelete(url);
            setHeaders(delete, authString);
            response = client.execute(delete);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK)
            {
                HttpEntity entity = response.getEntity();
                if (null != entity)
                {
                    String entityContent = EntityUtils.toString(entity,"UTF-8");
                    result = JsonUtils.jsonToMap(entityContent);                    
                }
                else
                {
                    result = returnContentError();
                }
                
                try 
                {
                    EntityUtils.consume(entity);
                } 
                catch (IOException e) 
                {
                    LOG.error("release entity failed \r\n {}", e.getMessage());
                }
            }
            else
            {
            	result = returnParamError(statusCode);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            result = returnConnectError(e);
        } 
        catch (ClientProtocolException e) 
        {
           result =  returnConnectError(e);
        }
        catch (IOException e) 
        {
            result = returnConnectError(e);
        }
        catch (InvalidKeyException e)
        {
            result = returnConnectError(e);
        }
        catch (NoSuchAlgorithmException e)
        {
            result = returnConnectError(e);
        }
        finally
        {
            if (null != response)
            {
                try
                {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } 
                catch (IOException e) 
                {
                    LOG.error("release response failed \r\n {}", e.getMessage());
                }
            }
            
        }
        return result;
        
    }
     
    
    /**
     * 
     * @param agentId
     * @param httpMethod
     * @param authString
     */
    private static void setHeaders(HttpRequestBase httpMethod, String authString)
    {
        
        httpMethod.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpMethod.setHeader("host", HPSConstant.IP_ADDRESS);
        httpMethod.setHeader("authorization", authString);
        
    }
    
    /**
     * 返回ConnectotError信息 
     * @param e
     * @return
     */
    private static Map<String, Object> returnParamError(int statusCode)
    {
       
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("returnCode", -3);
        resultMap.put("returnDesc", "The status code is :" + statusCode);
        return resultMap;
    }
    
    /**
     * 返回ConnectotError信息 
     * @param e
     * @return
     */
    private static Map<String, Object> returnConnectError(Exception e)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("returnCode", "-2");
        resultMap.put("returnDesc", "Request to  HPSSever failed");
        return resultMap;
    }
    
    /**
     * 返回结果不正确
     * @return
     */
    private static Map<String, Object> returnContentError()
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("returnCode", -1);
        resultMap.put("returnDesc", "The HPSSever return null");
        return resultMap;
    }
   
    /**
     * 构建完整URL
     * @param buffer
     * @param queryMap
     * @throws UnsupportedEncodingException
     */
    private static void buildHttpUrl(StringBuffer buffer, Map<String, String> queryMap) throws UnsupportedEncodingException
    {
        buffer.append("?");
        for (Entry<String, String> e : queryMap.entrySet())
        {
            buffer.append(URLEncoder.encode(e.getKey(), "utf-8"));
            buffer.append("=");
            buffer.append(URLEncoder.encode(e.getValue(), "utf-8"));
            buffer.append("&");
        }
        buffer.deleteCharAt(buffer.length() - 1);
    }

}
