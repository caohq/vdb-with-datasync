package datasync.controller;

import datasync.entity.DataTask;
import datasync.entity.FtpUtil;
import datasync.service.DataConnDaoService;
import datasync.service.DataTaskService;
import datasync.service.LocalConnDaoService;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class MainSevlet extends HttpServlet{

    private Logger logger = LoggerFactory.getLogger(MainSevlet.class);
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //获取请求路径
        String path = req.getServletPath();
        if("/login.do".equals(path)) {

        }else if ("/searchDataList.do".equals(path)){
            //获取数据库列表
            searchDataList(req,res);
        }else if("/searchTables.do".equals(path)){
            try {
                //查询数据库下表单
                searchTables(req, res);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if("/searchBdDirList.do".equals(path)){
            searchBdDirList(req, res);
        }else if("/searchBdDirListPath.do".equals(path)){
            searchBdDirListPath(req, res);
        }else if("/searchDataBySql.do".equals(path)){
            try {
                searchDataBySql(req,res);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if("/submitSqlData.do".equals(path)){
            //新建任務（提交）
            try {
                submitSqlData(req,res);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if("/searchDataTaskList.do".equals(path)){
            //查询任务列表
            searchDataTaskList(req,res);
        }else if("/submitFileData.do".equals(path)){
            try {
                submitFileData(req,res);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if("/ftpLocalUpload.do".equals(path)){
            ftpLocalUpload(req,res);
        }else if ("/searchTaskDetailById.do".equals(path)){
            searchTaskDetailById(req,res);
        }else{
            //错误路径
            throw new RuntimeException("查无此页");
        }
    }

    //查询数据库名称列表
    public List<Object> searchDataList(HttpServletRequest req, HttpServletResponse res) throws IOException {//获取数据库LIST
        PrintWriter out = res.getWriter();
        DataConnDaoService dataConnDaoService=new DataConnDaoService();
        List<Object> list=dataConnDaoService.searchDataListImp(req, res);
        out.println(list);
        return list;
    }

    //获取本地数据源列表
    public List<Object> searchBdDirList(HttpServletRequest req, HttpServletResponse res) throws IOException {
        LocalConnDaoService localConnDaoService=new LocalConnDaoService();
        List<Object> list=localConnDaoService.searchBdDirListImp(req, res);
        PrintWriter out = res.getWriter();
        out.println(list);
        return list;
    }

    //获取本地数据源数据路径（路径/文件）
    public List<Object> searchBdDirListPath(HttpServletRequest req, HttpServletResponse res) throws IOException {
        LocalConnDaoService localConnDaoService=new LocalConnDaoService();
        JSONObject jsonObject = new JSONObject();
        List<Object> list=localConnDaoService.searchBdDirListPathImp(req, res);
        PrintWriter out = res.getWriter();
        jsonObject.put("list",list);
        out.println(jsonObject);
        return list;
    }

    //根据数据库查询表名称
    public String  searchTables(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException, IOException {
        PrintWriter out = response.getWriter();
        DataConnDaoService dataConnDaoService=new DataConnDaoService();
        String dataType=dataConnDaoService.judgeDataType(request.getParameter("connData"));
        List<Object> list=new ArrayList<Object>();
        try {
            if ("mysql".equals(dataType)){
                list=dataConnDaoService.getMysqlTablesNameList(request.getParameter("connData"));
                out.println(list);
            }else  if ("oracle".equals(dataType)){
                list=dataConnDaoService.getOracleTablesNameList(request.getParameter("connData"));
                out.println(list);
            }else if("sqlServer".equals(dataType)){
                list=dataConnDaoService.getSqlServerTablesNameList(request.getParameter("connData"));
                out.println(list);
            }
        } catch (SQLException e) {
            out.println("数据库连接异常！");
            e.printStackTrace();
        }
        return "success!";
    }

    //根据sql查询预览结果
    public List<Map<Object,Object>> searchDataBySql(HttpServletRequest req, HttpServletResponse res) throws SQLException, ClassNotFoundException, IOException {
        PrintWriter out = res.getWriter();
        Map<Object,Object> columnsMap=new HashMap<Object, Object>();
        String sql=req.getParameter("sql");//获取sql
        String connData=req.getParameter("connData");//获取数据库连接信息
        List<Map<Object,Object>> list =new ArrayList<Map<Object, Object>>();
        try {
            list=new DataConnDaoService().searchDataBySql(connData,sql);
            List<Map<Object,Object>> columnsListMap=new ArrayList<Map<Object, Object>>();
            columnsMap=list.get(list.size()-1);
            //columnsListMap.add(columnsMap.get("columnsList"));
            list.remove(list.size()-1);
            String dataList=list+"?*$*?"+(columnsMap.get("columnsList"));
            out.println(dataList.replaceAll("\"=\"","\":\""));
        } catch (SQLException e) {
            out.println("sql语句错误！");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

   //新建数据库任务
    public JSONObject submitSqlData(HttpServletRequest res, HttpServletResponse req) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        JSONObject jsonObject = new JSONObject();
        DataTask datatask = new DataTask();
        String connDataValue=res.getParameter("connDataValue");
        String [] connDataValueArray=connDataValue.split("\\$");
//        datatask.setDataSourceId(5);
        datatask.setDataTaskName(res.getParameter("taskName"));//任务名
        datatask.setTableName(res.getParameter("checkedValue"));//选择表的名称
        datatask.setSqlString(res.getParameter("sql"));//SQL语句
        datatask.setSqlTableNameEn(res.getParameter("checkedValue"));//新建表名
        datatask.setCreateTime(sdf.format(date));
        datatask.setDataSourceName(res.getParameter("connDataName"));//数据源名称
        if(connDataValueArray.length>2){
           datatask.setDataTaskType(connDataValueArray[connDataValueArray.length-2]);//数据源类型
        }
        datatask.setStatus("0");
        int flag = new DataTaskService().insertDatatask(datatask);
        jsonObject.put("result",flag);
        if(flag < 0){
            return  jsonObject;
        }
        return jsonObject;
    }

    //数据任务---获取任务列表
    public JSONObject searchDataTaskList(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        JSONObject jsonObject = new JSONObject();
        String connData="";
        List<DataTask> dataTasks = new DataTaskService().getDataTaskList(connData);
        jsonObject.put("dataTasks",dataTasks);
        out.println(jsonObject);
        return jsonObject;
    }

    //新建任务--本地文件上传任务
    public  JSONObject submitFileData(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        JSONObject jsonObject = new JSONObject();
        DataTask datatask = new DataTask();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String  connDataName=req.getParameter("connDataName");//本地连接名称
        String  getCheckedFile=req.getParameter("getCheckedFile");//文件路径
        String  getLocalTaskName=req.getParameter("getLocalTaskName");//获取任务名称
        datatask.setCreateTime(sdf.format(date));
        datatask.setDataSourceName(connDataName);//数据源名称
        datatask.setDataTaskName(getLocalTaskName);//任务名
        datatask.setFilePath(getCheckedFile);
        datatask.setDataTaskType("file");
        datatask.setStatus("0");
        int flag = new DataTaskService().insertDatatask(datatask);
        return jsonObject;
    }


    public  int ftpLocalUpload(HttpServletRequest req, HttpServletResponse res){
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        int dataTaskId=1;
        String processId="";
        String host = "10.0.86.77";
        String userName = "ftpUsercode2";
        String password = "ftpPasswordcode2";
        String port = "21";
        String remoteFilepath = "/";
        String subjectCode = "code2";
        String portalUrl ="10.0.86.77/portal";
        FtpUtil ftpUtil = new FtpUtil();
        DataTask dataTask = new DataTaskService().getDataTaskInfById("6");
        logger.info("数据任务名称为：本地测试任务\n");
        try {
            ftpUtil.connect(host, Integer.parseInt(port), userName, password);
            String result = "";
            if(dataTask.getDataTaskType().equals("file")){
                String[] localFileList = dataTask.getFilePath().split(",");//D:%_%workspace%_%workspace_datasync%_%datasync%_%out%_%artifacts%_%drsr_war_exploded%_%zipFile%_%code2_3.zip
                result = ftpUtil.upload(host, userName, password, port, localFileList, processId,remoteFilepath,dataTask,subjectCode).toString();
                if(localFileList.length == 0){
                    return 0;
                }
            }else if(dataTask.getDataTaskType().equals("mysql")){
                remoteFilepath = remoteFilepath+subjectCode+"_"+dataTask.getDataTaskId()+"/";
                String[] localFileList = {dataTask.getFilePath()};
                result = ftpUtil.upload(host, userName, password, port, localFileList, processId,remoteFilepath,dataTask,subjectCode).toString();
                if(localFileList.length == 0){
                    return 0;
                }
            }
            logger.info("ftpDataTaskId"+dataTask.getDataTaskId()+"上传状态:" + result + "\n");
            ftpUtil.disconnect();
            if(result.equals("Upload_New_File_Success")||result.equals("Upload_From_Break_Succes")){
                String dataTaskString = jsonObject.toJSONString(dataTask);
                com.alibaba.fastjson.JSONObject requestJSON=new com.alibaba.fastjson.JSONObject();
                requestJSON.put("dataTask",dataTaskString);
                requestJSON.put("subjectCode",subjectCode);
                String requestString = jsonObject.toJSONString(requestJSON);
                HttpClient httpClient = null;
                HttpPost postMethod = null;
                HttpResponse response = null;
                try {
                    httpClient = HttpClients.createDefault();
//                    postMethod = new HttpPost("http://localhost:8080/portal/service/getDataTask");
                    postMethod = new HttpPost("http://"+portalUrl+"/service/getDataTask");
//                    postMethod = new HttpPost(portalUrl);
                    postMethod.addHeader("Content-type", "application/json; charset=utf-8");
//                    postMethod.addHeader("X-Authorization", "AAAA");//设置请求头
                    postMethod.setEntity(new StringEntity(requestString, Charset.forName("UTF-8")));
                    response = httpClient.execute(postMethod);//获取响应
                    int statusCode = response.getStatusLine().getStatusCode();
                    System.out.println("HTTP Status Code:" + statusCode);
                    if (statusCode != HttpStatus.SC_OK) {
                        System.out.println("HTTP请求未成功！HTTP Status Code:" + response.getStatusLine());
                    }
                    HttpEntity httpEntity = response.getEntity();
                    String reponseContent = EntityUtils.toString(httpEntity);
                    EntityUtils.consume(httpEntity);//释放资源
                    System.out.println("响应内容：" + reponseContent);
                    if(reponseContent.equals("1")){
                        dataTask.setStatus("1");
                      //  dataTaskService.update(dataTask);
                        logger.info("导入成功"+ "\n");
                        logger.info("=========================上传流程结束========================" + "\r\n"+"\n\n\n\n\n");
                        return 1;
                    }else{
                        logger.info("导入失败"+ "\n");
                        logger.info("=========================上传流程结束========================" + "\r\n"+"\n\n\n\n\n");
                        return 0;
                    }
                } catch (IOException e) {
                    logger.info("导入失败"+ "\n");
                    logger.error("导入异常IOException:"+e+ "\n");
                    logger.info("=========================上传流程结束========================" + "\r\n"+"\n\n\n\n\n");
                    e.printStackTrace();
                }
            }else{
                logger.info("导入失败"+ "\n");
                logger.info("=========================上传流程结束========================" + "\r\n"+"\n\n\n\n\n");
                return 0;
            }
        } catch (IOException e) {
            logger.error("连接FTP出错:"+e+ "\n");
            logger.info("=========================上传流程结束========================" + "\r\n"+"\n\n\n\n\n");
            System.out.println("连接FTP出错：" + e.getMessage());
            return 0;
        }




        return 1;

    }

    public JSONObject searchTaskDetailById(HttpServletRequest req, HttpServletResponse res){
        String taskId=req.getParameter("taskId");
        JSONObject jsonObject = new JSONObject();
        DataTask dataTask = new DataTaskService().getDataTaskInfById(taskId);

      //  List

        return  jsonObject;

    }

}
