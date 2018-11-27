package datasync.service;

import datasync.entity.DataTask;
import datasync.mapper.DataTaskMapper;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UploadTaskService {

    public boolean exportTaskData(String taskId) {
        return true;
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

    private DataTask getDataTaskInfo(String taskId) {
        String sql = "select * from t_datatask order  by  DataTaskId desc ";
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        BasicDataSource ds=new BasicDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl("jdbc:sqlite::resource:vdb_datasync.db");
//        ds.setUrl("jdbc:sqlite:D:/workspace/vdb2.0/vdb-2.0/src/main/resources/DataSyncSQLite3.db");
        jdbcTemplate.setDataSource(ds);
        List<DataTask> DataTaskList = jdbcTemplate.query(sql, new DataTaskMapper());

        return null;
    }
}