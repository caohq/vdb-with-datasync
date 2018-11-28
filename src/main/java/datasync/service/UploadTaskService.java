package datasync.service;

import datasync.entity.DataTask;
import datasync.mapper.DataTaskMapper;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UploadTaskService {

    public boolean exportTaskData(HttpServletRequest request, String dataTaskId) {
        //输入参数校验
        dataTaskId = dataTaskId.trim();
        if (dataTaskId == null || dataTaskId.equals(""))
        {
            return false;
        }

        //getDataTask有可能是null
        DataTask dataTask = getDataTask(dataTaskId);
        if (dataTask == null)
        {
            return false;
        }

        String dataTaskType = "";
        dataTaskType = dataTask.getDataTaskType();
        dataTaskType = dataTaskType.trim();

        //防止dataTaskType字段没有值的情况
        if (dataTaskType == null || dataTaskType.equals(""))
        {
            return false;
        }

        //file类型的数据任务, 目前在导出中对file类型的数据任务不做任何处理
        if (dataTaskType.equals("file"))
        {
        }

        //mysql类型的数据任务, mysql类型的数据任务需要做
        if (dataTaskType.equals("mysql"))
        {
            exportTaskDataFromMysql(request, dataTask);
        }

        return true;
    }

    private void exportTaskDataFromMysql(HttpServletRequest request, DataTask dataTask)
    {
        String exportedSQLDataDir = request.getSession().getServletContext().getRealPath("/exportedSQLData/");

        //直接选择的表名， tableName可能是多个
        String tableNames = dataTask.getTableName();
        for (String tableName : tableNames.split(","))
        {

        }

        //以select语句形式导出的数据
        String sqlTableName = dataTask.getSqlTableNameEn();
    }

    public boolean packTaskData(String taskId) {
        return true;
    }

    public boolean uploadTaskData(String taskId) {
        return true;
    }

    public boolean importTaskData(String taskId) {
        return true;
    }

    private DataTask getDataTask(String dataTaskId) {
        String sql = "select * from t_datatask where DataTaskId = ?" ;
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl("jdbc:sqlite::resource:vdb_datasync.db");
        jdbcTemplate.setDataSource(ds);
        List<DataTask> dataTaskList = jdbcTemplate.query(sql, new Object[]{dataTaskId}, new DataTaskMapper());

        if (dataTaskList.size() != 0) {
            return dataTaskList.get(0);
        }
        else {
            return null;
        }
    }
}