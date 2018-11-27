package datasync.service;

import datasync.entity.DataTask;
import datasync.mapper.DataTaskMapper;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


public class DataTaskDao {


    public int insertDatatask(final DataTask datatask){
        boolean flag = false;
        final String sql = "insert into t_datatask(dataSourceName,dataTaskName,dataTaskType," +
                "tableName,sqlString,sqlTableNameEn,sqlFilePath,filePath,createTime,creator,status) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        BasicDataSource ds=new BasicDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl("jdbc:sqlite::resource:vdb_datasync.db");
//        ds.setUrl("jdbc:sqlite:D:/workspace/vdb2.0/vdb-2.0/src/main/resources/DataSyncSQLite3.db");
        jdbcTemplate.setDataSource(ds);
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,datatask.getDataSourceName());
                ps.setString(2,datatask.getDataTaskName());
                ps.setString(3,datatask.getDataTaskType());
                ps.setString(4,datatask.getTableName());
                ps.setString(5,datatask.getSqlString());
                ps.setString(6,datatask.getSqlTableNameEn());
                ps.setString(7,datatask.getSqlFilePath());
                ps.setString(8,datatask.getFilePath());
                ps.setString(9,datatask.getCreateTime());
                ps.setString(10,datatask.getCreator());
                ps.setString(11,datatask.getStatus());
                return ps;
            }
        },keyHolder);

        int generatedId = keyHolder.getKey().intValue();

        return generatedId;
    }

    public List<DataTask> getDataTaskList(Map<Object,Object> params){
        StringBuffer sql = new StringBuffer();
        sql.append("select * from t_datatask t where 1=1 ");
        //String sql = "select * from t_datatask order  by  DataTaskId desc ";
        if(StringUtils.isNotEmpty((String) params.get("SearchDataTaskName"))) {//任务标识
            sql.append("  and t.dataTaskName like  '%"+params.get("SearchDataTaskName")+"%'");
        }
        if(StringUtils.isNotEmpty((String) params.get("dataSourceList"))) {//数据类型
            if("file".equals((String) params.get("dataSourceList"))){
            sql.append("  and t.dataTaskType = '"+params.get("dataSourceList")+"'");
            }else{
                sql.append("  and t.dataTaskType != 'file'");
            }
        }
        if(StringUtils.isNotEmpty((String) params.get("dataStatusList"))) {//状态
            sql.append("  and t.status = '"+params.get("dataStatusList")+"'");
        }
        sql.append(" order  by  DataTaskId desc");
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        BasicDataSource ds=new BasicDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl("jdbc:sqlite::resource:vdb_datasync.db");
//        ds.setUrl("jdbc:sqlite:D:/workspace/vdb2.0/vdb-2.0/src/main/resources/DataSyncSQLite3.db");
        jdbcTemplate.setDataSource(ds);
        List<DataTask> DataTaskList = jdbcTemplate.query(sql+"", new DataTaskMapper());

        return DataTaskList;
    }

    //根据id获取任务对象
    public DataTask getDataTaskInfById(String taskId){
        DataTask dataTask=new DataTask();
        String sql = "select * from t_datatask where dataTaskId = ?";
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        BasicDataSource ds=new BasicDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl("jdbc:sqlite::resource:vdb_datasync.db");
        jdbcTemplate.setDataSource(ds);
        List<DataTask> list = jdbcTemplate.query(sql, new Object[]{taskId}, new DataTaskMapper());
        return list.size() > 0 ? list.get(0) : null;
    }

    //根据id删除task
    public int deleteTaskById(String taskId){
        String sql = "delete from t_datatask where  dataTaskId = "+taskId+"";
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        BasicDataSource ds=new BasicDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl("jdbc:sqlite::resource:vdb_datasync.db");
        jdbcTemplate.setDataSource(ds);
        int result = jdbcTemplate.update(sql);//query(sql, new Object[]{taskId}, new DataTaskMapper());

        return result;
    }
}
