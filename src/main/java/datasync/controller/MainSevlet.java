package datasync.controller;

import datasync.entity.DataTask;
import datasync.service.DataConnDaoService;
import datasync.service.DataTaskService;
import datasync.service.LocalConnDaoService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;
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
        List<Object> list=localConnDaoService.searchBdDirListPathImp(req, res);
        PrintWriter out = res.getWriter();
        out.println(list);
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
           datatask.setDataTaskType("db");//数据源类型
        }
        datatask.setStatus("0");
        int flag = new DataTaskService().insertDatatask(datatask);
        jsonObject.put("result",flag);
        if(flag < 0){
            return  jsonObject;
        }
        return jsonObject;
    }

    public JSONObject searchDataTaskList(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        JSONObject jsonObject = new JSONObject();
        String connData="";
        List<DataTask> dataTasks = new DataTaskService().getDataTaskList(connData);
        jsonObject.put("dataTasks",dataTasks);
        out.println(jsonObject);
        return jsonObject;
    }

    public  JSONObject submitFileData(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        JSONObject jsonObject = new JSONObject();
        DataTask datatask = new DataTask();
        int flag = new DataTaskService().insertDatatask(datatask);
        return jsonObject;
    }

}
