<<<<<<< HEAD
package datasync.service;

import com.alibaba.fastjson.JSON;
import datasync.utils.ConfigUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

public class LoginService
{
    public int validateLogin(String userName, String password)
    {
        //1、访问中心端验证登录是否成功
        int loginStatus = 0;

        String configFilePath = LoginService.class.getClassLoader().getResource("config.properties").getFile();

        try {
            String portalUrl = ConfigUtil.getConfigItem(configFilePath, "PortalUrl");
            String loginApiPath = "/api/clientLogin";
            String url = "http://" + portalUrl + loginApiPath + "?" + "userName=" + userName + "&password=" + password;
            //RestTemplate restTemplate = new RestTemplate();
            //JSONObject loginObject = restTemplate.getForObject(url, JSONObject.class);

            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String content = "";
            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while ((line = reader.readLine()) != null)
            {
                content = content + line;
            }
            JSONObject loginObject = JSON.parseObject(content);
            loginStatus = Integer.parseInt((String)(loginObject.get("loginStatus")));

            //validate user pass
            if (loginStatus == 1)
            {
                getSubjectConfig(userName);
            }
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }

       return  loginStatus;
    }

    private boolean getSubjectConfig(String userName) throws Exception
    {
        String configFilePath = LoginService.class.getClassLoader().getResource("config.properties").getFile();

        String portalUrl = ConfigUtil.getConfigItem(configFilePath, "PortalUrl");
        String getSubjectApiPath = "/api/getSubjectByUser/" + userName;

        String url = "http://" + portalUrl + getSubjectApiPath;

        /*RestTemplate restTemplate = new RestTemplate();
        JSONObject subjectInfo = restTemplate.getForObject(url, JSONObject.class);*/
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String content = "";
        String line = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        while ((line = reader.readLine()) != null)
        {
            content = content + line;
        }
        JSONObject subjectInfo = JSON.parseObject(content);

        LinkedHashMap dataMap = (LinkedHashMap) subjectInfo.get("data");

        String subjectName = "";
        if (dataMap.get("subjectName") != null)
        {
            subjectName = dataMap.get("subjectName").toString();
        }
        String subjectCode = "";
        if (dataMap.get("subjectCode") != null)
        {
            subjectCode = dataMap.get("subjectCode").toString();
        }
        String admin = "";
        if (dataMap.get("admin") != null)
        {
            admin = dataMap.get("admin").toString();
        }
        String adminPasswd = "";
        if (dataMap.get("adminPasswd") != null)
        {
            adminPasswd = dataMap.get("adminPasswd").toString();
        }
        String contact = "";
        if(dataMap.get("contact") != null)
        {
            contact = dataMap.get("contact").toString();
        }
        String phone = "";
        if(dataMap.get("phone") != null)
        {
            phone = dataMap.get("phone").toString();
        }
        String email = "";
        if(dataMap.get("email") != null)
        {
            email = dataMap.get("email").toString();
        }
        String ftpUser = "";
        if (dataMap.get("ftpUser") != null)
        {
            ftpUser = dataMap.get("ftpUser").toString();
        }
        String ftpPassword = "";
        if (dataMap.get("ftpPassword") != null)
        {
            ftpPassword = dataMap.get("ftpPassword").toString();
        }
        String brief = "";
        if (dataMap.get("brief") != null)
        {
            brief = dataMap.get("brief").toString();
        }

        ConfigUtil.setConfigItem(configFilePath, "IsInitialized", "true");
        ConfigUtil.setConfigItem(configFilePath, "SubjectName", subjectName);
        ConfigUtil.setConfigItem(configFilePath, "SubjectCode", subjectCode);
        ConfigUtil.setConfigItem(configFilePath, "Admin", admin);
        ConfigUtil.setConfigItem(configFilePath, "AdminPasswd", adminPasswd);
        ConfigUtil.setConfigItem(configFilePath, "Contact", contact);
        ConfigUtil.setConfigItem(configFilePath, "Phone", phone);
        ConfigUtil.setConfigItem(configFilePath, "Email", email);
        ConfigUtil.setConfigItem(configFilePath, "FtpUser", ftpUser);
        ConfigUtil.setConfigItem(configFilePath, "FtpPassword", ftpPassword);
        ConfigUtil.setConfigItem(configFilePath, "Brief", brief);

        return true;
    }
}
