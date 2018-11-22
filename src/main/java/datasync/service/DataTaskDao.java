package datasync.service;

import datasync.entity.DataTask;
import datasync.mapper.DataTaskMapper;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;


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

    public List<DataTask> getDataTaskList(String connData){
//        StringBuilder sb = new StringBuilder();
//        sb.append("select * from t_datatask");
        String sql = "select * from t_datatask order  by  DataTaskId desc ";
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        BasicDataSource ds=new BasicDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl("jdbc:sqlite::resource:vdb_datasync.db");
//        ds.setUrl("jdbc:sqlite:D:/workspace/vdb2.0/vdb-2.0/src/main/resources/DataSyncSQLite3.db");
        jdbcTemplate.setDataSource(ds);
        List<DataTask> DataTaskList = jdbcTemplate.query(sql, new DataTaskMapper());

        return DataTaskList;
    }
}
