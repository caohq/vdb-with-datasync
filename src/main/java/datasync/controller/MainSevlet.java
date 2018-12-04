package datasync.controller;

import datasync.entity.DataTask;
import datasync.entity.FtpUtil;
import datasync.service.*;
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
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        }else if("/deleteTaskById.do".equals(path)){
            deleteTaskById(req,res);
        } else if ("/exportTaskData.do".equals(path))
        {
          //  uploadTask(req, res,data);
        }
        else{
            //错误路径
            throw new RuntimeException("查无此页");
        }
    }

    /*
     * 根据taskId完成task的上传任务, 任务上传包括 导出数据、打包数据、上传数据到中心端，中心端导入数据到存放数据的数据库
     *
     */
    public String uploadTask(HttpServletRequest req, HttpServletResponse res,String dataTaskId) throws IOException {
        PrintWriter out = res.getWriter();
//      getWriter  String dataTaskId = req.getParameter("dataTaskId");

      //  System.out.println("enterring uploadTask - dataTaskId = " + dataTaskId);

        UploadTaskService uploadTaskService = new UploadTaskService();
        String zipFilePath = uploadTaskService.exportDataTask(req, dataTaskId);
        if (zipFilePath!=null || zipFilePath!="")
        {
            try {
                res.getWriter().println("success");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                res.getWriter().println("failed");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return zipFilePath;

        /*uploadTaskService.packTaskData(dataTaskId);
        uploadTaskService.uploadTaskData(dataTaskId);
        uploadTaskService.importTaskData(dataTaskId);*/
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
    public JSONObject submitSqlData(HttpServletRequest res, HttpServletResponse req) throws SQLException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        JSONObject jsonObject = new JSONObject();
        DataTask datatask = new DataTask();
        HttpSession session=res.getSession();
        String datataskId=String.valueOf(UUID.randomUUID());
        String connDataValue=res.getParameter("connDataValue");
        String [] connDataValueArray=connDataValue.split("\\$");
        String dataSourceName=res.getParameter("connDataName");
        int dataSourceId= (int) System.currentTimeMillis();
//        datatask.setDataSourceId(5);
        datatask.setDataTaskId(datataskId);
        datatask.setDataTaskName(res.getParameter("taskName"));//任务名
        datatask.setTableName(res.getParameter("checkedValue"));//选择表的名称
        datatask.setSqlString(res.getParameter("sql"));//SQL语句
        datatask.setSqlTableNameEn(res.getParameter("createNewTableName"));//新建表名
        datatask.setCreateTime(date);
        datatask.setDataSourceId(dataSourceId);//(res.getParameter("connDataName"));//数据源名称
        if(connDataValueArray.length>2){
           datatask.setDataTaskType(connDataValueArray[connDataValueArray.length-2]);//数据源类型
        }
        datatask.setCreator(session.getAttribute("SPRING_SECURITY_LAST_USERNAME")==null?"": (String) session.getAttribute("SPRING_SECURITY_LAST_USERNAME"));
        datatask.setStatus("0");
        int flag = new DataTaskService().insertDatatask(datatask,connDataValue,dataSourceName);

        String zipFilePath=uploadTask(res, req,datataskId);//打包
        String fileName = "sdc001"+"_"+datataskId+"_sql.zip";
        datatask.setSqlFilePath((zipFilePath+fileName).replace(File.separator,"%_%"));
        //datatask.setSqlFilePath(fileName);
        int flag1= new DataTaskService().updateSqlFilePathById(datatask);


        jsonObject.put("result",flag);

        if(flag < 0){
            return  jsonObject;
        }
        return jsonObject;
    }

    //数据任务---获取任务列表
    public JSONObject searchDataTaskList(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String SearchDataTaskName=req.getParameter("SearchDataTaskName");//查询条件
        String dataSourceList=req.getParameter("dataSourceList");//查询条件
        String dataStatusList=req.getParameter("dataStatusList");//查询条件
        Map<Object,Object> params=new HashMap<Object, Object>();
        params.put("SearchDataTaskName",SearchDataTaskName);
        params.put("dataSourceList",dataSourceList);
        params.put("dataStatusList",dataStatusList);
        PrintWriter out = res.getWriter();
        JSONObject jsonObject = new JSONObject();
        String connData="";
        List<DataTask> dataTasks = new DataTaskService().getDataTaskList(params);
        jsonObject.put("dataTasks",dataTasks);
        out.println(jsonObject);
        return jsonObject;
    }

    //新建任务--本地文件上传任务
    public  JSONObject submitFileData(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException {
   //     PrintWriter out = res.getWriter();
        JSONObject jsonObject = new JSONObject();
        DataTask datatask = new DataTask();
    //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        int dataSourceId= (int) System.currentTimeMillis();
        HttpSession session=req.getSession();
        String  connDataName=req.getParameter("connDataName");//本地连接名称
        String connDataValue=req.getParameter("connDataValue");//数据源名称
        String  getCheckedFile=req.getParameter("getCheckedFile");//文件路径
        String  getLocalTaskName=req.getParameter("getLocalTaskName");//获取任务名称
        String datataskId=String.valueOf(UUID.randomUUID());
        datatask.setCreateTime(date);
        datatask.setDataSourceId(dataSourceId);//数据源名称
        datatask.setDataTaskName(getLocalTaskName);//任务名
        datatask.setFilePath(getCheckedFile);
        datatask.setDataTaskType("file");
        datatask.setStatus("0");
        datatask.setDataTaskId(datataskId);
        datatask.setCreator(session.getAttribute("SPRING_SECURITY_LAST_USERNAME")==null?"": (String) session.getAttribute("SPRING_SECURITY_LAST_USERNAME"));

        List<String> filepaths =new LinkedList<String>();
        String [] filepathArray=getCheckedFile.split(",");
          for (String str:filepathArray){
              ((LinkedList<String>) filepaths).add(str);
          }
        String fileName = "sdc001"+"_"+datataskId;
        FileResourceService fileResourceService=new FileResourceService();
        fileResourceService.packDataResource(fileName,filepaths,req.getSession().getServletContext().getRealPath("/"));
        String zipFile =req.getSession().getServletContext().getRealPath("/") + "zipFile" + File.separator + fileName + ".zip";
        DataTaskService dataTaskService=new DataTaskService();
        datatask.setSqlFilePath(zipFile.replace(File.separator,"%_%"));
        //boolean upresult = dataTaskService.update(dt);
        int flag = new DataTaskService().insertDatatask(datatask,connDataName,connDataValue);
        return jsonObject;
    }


    public  int ftpLocalUpload(HttpServletRequest req, HttpServletResponse res) throws IOException {

        //
//        PrintWriter out = res.getWriter();
//        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
//        String taskId=req.getParameter("taskId");
//        int dataTaskId=1;
//        String processId="";
//        String host = "10.0.86.77";
//        String userName = "ftpUserssdd";
//        String password = "ftpPasswordssdd";
//        String port = "21";
//        String remoteFilepath = "/";
//        String subjectCode = "ssdd";
//        String portalUrl ="10.0.86.77/portal";
//        FtpUtil ftpUtil = new FtpUtil();
//        DataTask dataTask = new DataTaskService().getDataTaskInfById(taskId);
        String processId="1";
        String taskId=req.getParameter("taskId");
        DataTask dataTask = new DataTaskService().getDataTaskInfById(taskId);
        String fileName = dataTask.getDataTaskName()+"log.txt";//文件名及类型
        String path = "/logs/";
        FileWriter fw = null;
        File file = new File(path, fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
                fw = new FileWriter(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try{
                fw = new FileWriter(file, true);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        PrintWriter pw = new PrintWriter(fw);
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String current = dateFormat.format(now);
        pw.println(current+":"+"=========================上传流程开始========================" + "\n");
        if("file".equals(dataTask.getDataTaskType())){
            pw.println("###########上传的文件为###########" + "\n");
            String[] fileAttr = dataTask.getFilePath().split(";");
            for(String fileAttrName : fileAttr){
                pw.println(fileAttrName+ "\n");
            }
        }
      //  String configFilePath = LoginService.class.getClassLoader().getResource("config.properties").getFile();
        String subjectCode = "ssdd";
        String host = "10.0.86.77";
        String userName = "ftpUserssdd";
        String password = "ftpPasswordssdd";
        String port = "21";
        String ftpRootPath = "/";
        String portalUrl ="10.0.86.77/portal";
        FtpUtil ftpUtil = new FtpUtil();
        pw.println("数据任务名称为：" + dataTask.getDataTaskName() +"\n");
        try {
            ftpUtil.connect(host, Integer.parseInt(port), userName, password);
            String result = "";
            if(dataTask.getDataTaskType().equals("file")){
                String[] localFileList = {dataTask.getSqlFilePath()};
                result = ftpUtil.upload(localFileList, processId,ftpRootPath,dataTask,subjectCode).toString();
                if(result.equals("File_Exits")){
                    ftpUtil.removeDirectory(ftpRootPath+subjectCode+"_"+dataTask.getDataTaskId());
                    ftpUtil.deleteFile(ftpRootPath+subjectCode+"_"+dataTask.getDataTaskId()+".zip");
                    result = ftpUtil.upload(localFileList, processId,ftpRootPath,dataTask,subjectCode).toString();
                }
                if(localFileList.length == 0){
                    now = new Date();
                    dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                    String current1 = dateFormat.format(now);
                    pw.println(current1+":"+"上传失败"+ "\n");
                    return 0;
                }
            }else if(dataTask.getDataTaskType().equals("mysql")){
                String remoteFilepath = ftpRootPath+subjectCode+"_"+dataTask.getDataTaskId()+"_sql/";
                String[] localFileList = {dataTask.getSqlFilePath()};
                result = ftpUtil.upload(localFileList, processId,remoteFilepath,dataTask,subjectCode+"_sql").toString();
                if(result.equals("File_Exits")){
                    ftpUtil.removeDirectory(ftpRootPath+subjectCode+"_"+dataTask.getDataTaskId()+"_sql");
                    result = ftpUtil.upload(localFileList, processId,remoteFilepath,dataTask,subjectCode).toString();
                }
                if(localFileList.length == 0){
                    now = new Date();
                    dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                    String current1 = dateFormat.format(now);
                    pw.println(current1+":"+"上传失败"+ "\n");
                    return 0;
                }
            }
            pw.println("ftpDataTaskId"+dataTask.getDataTaskId()+"上传状态:" + result + "\n");
            ftpUtil.disconnect();
            if(result.equals("Upload_New_File_Success")||result.equals("Upload_From_Break_Succes")){
                String dataTaskString = com.alibaba.fastjson.JSONObject.toJSONString(dataTask);
                com.alibaba.fastjson.JSONObject requestJSON = new com.alibaba.fastjson.JSONObject();
                requestJSON.put("dataTask",dataTaskString);
                requestJSON.put("subjectCode",subjectCode);
                String requestString = com.alibaba.fastjson.JSONObject.toJSONString(requestJSON);
                HttpClient httpClient = null;
                HttpPost postMethod = null;
                HttpResponse response = null;
                try {
                    if("mysql".equals(dataTask.getDataTaskType())){
                        now = new Date();
                        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                        String current1 = dateFormat.format(now);
                        pw.println(current1+":"+"=========================导入流程开始========================" + "\n");
                    }
                    if("file".equals(dataTask.getDataTaskType())){
                        now = new Date();
                        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                        String current1 = dateFormat.format(now);
                        pw.println(current1+":"+"=========================解压流程开始========================" + "\n");
                    }
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
                    System.out.println("响应内容："  + reponseContent);
                    if(reponseContent.equals("1")){
                        if("mysql".equals(dataTask.getDataTaskType())){
                            now = new Date();
                            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                            String current1 = dateFormat.format(now);
                            pw.println(current1+":"+"导入成功"+ "\n");
                            pw.println(current1+":"+"=========================导入流程结束========================" + "\n");
                        }
                        if("file".equals(dataTask.getDataTaskType())){
                            now = new Date();
                            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                            String current1 = dateFormat.format(now);
                            pw.println(current1+":"+"解压成功"+ "\n");
                            pw.println(current1+":"+"=========================解压流程结束========================" + "\n");
                        }
                        dataTask.setStatus("1");
                        new DataTaskService().updateDataTaskStatusById(taskId);
                        return 1;
                    }else{
                        if("mysql".equals(dataTask.getDataTaskType())){
                            now = new Date();
                            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                            String current1 = dateFormat.format(now);
                            pw.println(current1+":"+"导入失败"+ "\n");
                            pw.println(current1+":"+"=========================导入流程结束========================" + "\n");
                        }
                        if("file".equals(dataTask.getDataTaskType())){
                            now = new Date();
                            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                            String current1 = dateFormat.format(now);
                            pw.println(current1+":"+"解压失败"+ "\n");
                            pw.println(current1+":"+"=========================解压流程结束========================" + "\n");
                        }
                        return 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if("mysql".equals(dataTask.getDataTaskType())){
                        now = new Date();
                        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                        String current1 = dateFormat.format(now);
                        pw.println(current1+":"+"导入失败"+ e+"\n");
                        pw.println(current1+":"+"=========================导入流程结束========================" + "\n");
                    }
                    if("file".equals(dataTask.getDataTaskType())){
                        now = new Date();
                        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                        String current1 = dateFormat.format(now);
                        pw.println(current1+":"+"解压失败"+ e+"\n");
                        pw.println(current1+":"+"=========================解压流程结束========================" + "\n");
                    }
                }
            }else{
                return 0;
            }
        } catch (IOException e) {
            now = new Date();
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
            current = dateFormat.format(now);
            pw.println(current+":"+"连接FTP出错:"+e+ "\n");
            System.out.println("连接FTP出错：" + e.getMessage());
            return 0;
        }finally {
            now = new Date();
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
            String current1 = dateFormat.format(now);
            pw.println(current1+":"+"=========================上传流程结束========================" + "\r\n"+"\n\n\n\n\n");
            try {
                fw.flush();
                pw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 1;

    }

    //通过id查询task信息
    public JSONObject searchTaskDetailById(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String taskId=req.getParameter("taskId");
        PrintWriter out = res.getWriter();
        JSONObject jsonObject = new JSONObject();
        DataTask dataTask = new DataTaskService().getDataTaskInfById(taskId);
        jsonObject.put("dataTask",dataTask);
        out.println(jsonObject);
        return  jsonObject;
    }


    //通过id删除task
    public int deleteTaskById(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        String taskId=req.getParameter("taskId");
        int result = new DataTaskService().deleteTaskById(taskId);
        out.println(result);
        return result;
    }

}
