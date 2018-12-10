package datasync.service.dataTask;

import datasync.connection.SqlLiteDataConnection;
import datasync.entity.DataSrc;
import datasync.entity.DataTask;
import datasync.mapper.DataSrcMapper;
import datasync.mapper.DataTaskMapperDsName;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;
import java.util.Map;


public class DataTaskDao {

    //添加任务
    public int insertDatatask(final DataTask datatask,String connData,String dataSourceName){
        boolean flag = false;
        final String sql = "insert into t_datatask(dataSourceId,dataTaskName,dataTaskType," +
                "tableName,sqlString,sqlTableNameEn,sqlFilePath,filePath,createTime,creator,status,datataskId,subjectCode) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlLiteDataConnection sqlLiteDataConnection=new SqlLiteDataConnection();
        JdbcTemplate jdbcTemplate=sqlLiteDataConnection.makeJdbcTemplate();
       // final String taskId=String.valueOf(UUID.randomUUID());
      //  final int dataSourceId= (int) System.currentTimeMillis();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, datatask.getDataSourceId());
                ps.setString(2,datatask.getDataTaskName());
                ps.setString(3,datatask.getDataTaskType());
                ps.setString(4,datatask.getTableName());
                ps.setString(5,datatask.getSqlString());
                ps.setString(6,datatask.getSqlTableNameEn());
                ps.setString(7,datatask.getSqlFilePath());
                ps.setString(8,datatask.getFilePath());
                ps.setTimestamp(9,new Timestamp(datatask.getCreateTime().getTime()));
                ps.setString(10,datatask.getCreator());
                ps.setString(11,datatask.getStatus());
                ps.setString(12, datatask.getDataTaskId());
                ps.setString(13,datatask.getSubjectCode());
                return ps;
            }
        },keyHolder);
        insertDataSrc(datatask.getDataSourceId(),dataSourceName,connData);

        int generatedId = keyHolder.getKey().intValue();

        return generatedId;
    }

    //查询任务列表
    public List<DataTask> getDataTaskList(Map<Object,Object> params){
        StringBuffer sql = new StringBuffer();
        sql.append("select ds.DataSourceName,* from  t_datatask t  LEFT JOIN t_datasource ds ON ds.DataSourceId=t.DataSourceId where 1=1 ");
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
        sql.append(" order  by   CreateTime desc");
        SqlLiteDataConnection sqlLiteDataConnection=new SqlLiteDataConnection();
        JdbcTemplate jdbcTemplate=sqlLiteDataConnection.makeJdbcTemplate();
        List<DataTask> DataTaskList = jdbcTemplate.query(sql+"", new DataTaskMapperDsName());
       // DataSrc dataSrc=getDataSourceById(DataTaskList);

        return DataTaskList;
    }

    //根据id获取任务对象
    public DataTask getDataTaskInfById(String taskId){
        DataTask dataTask=new DataTask();
        String sql = "select ds.DataSourceName,* from  t_datatask t  LEFT JOIN t_datasource ds ON ds.DataSourceId=t.DataSourceId  where dataTaskId = ?";
        SqlLiteDataConnection sqlLiteDataConnection=new SqlLiteDataConnection();
        JdbcTemplate jdbcTemplate=sqlLiteDataConnection.makeJdbcTemplate();
        List<DataTask> list = jdbcTemplate.query(sql, new Object[]{taskId}, new DataTaskMapperDsName());
        return list.size() > 0 ? list.get(0) : null;
    }

    //根据id删除task
    public int deleteTaskById(String taskId){
        String sql = "delete from t_datatask where  dataTaskId = '"+taskId+"'";
        SqlLiteDataConnection sqlLiteDataConnection=new SqlLiteDataConnection();
        JdbcTemplate jdbcTemplate=sqlLiteDataConnection.makeJdbcTemplate();
        int result = jdbcTemplate.update(sql);//query(sql, new Object[]{taskId}, new DataTaskMapper());
        return result;
    }
    //根据id修改task导入状态
    public int updateDataTaskStatusById(String taskId){
        String sql = "update  t_datatask  set  status ='1' where  dataTaskId = '"+taskId+"'";
        SqlLiteDataConnection sqlLiteDataConnection=new SqlLiteDataConnection();
        JdbcTemplate jdbcTemplate=sqlLiteDataConnection.makeJdbcTemplate();
        int result = jdbcTemplate.update(sql);//query(sql, new Object[]{taskId}, new DataTaskMapper());
        return result;
    }

    public int insertDataSrc(final int dataSourceId, final String dataSourceName, final String connData){
        final String sql = "insert into t_datasource(dataSourceId,dataSourceName,dataSourceType)" +
                "VALUES (?,?,?)";
        SqlLiteDataConnection sqlLiteDataConnection=new SqlLiteDataConnection();
        JdbcTemplate jdbcTemplate=sqlLiteDataConnection.makeJdbcTemplate();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1,dataSourceId);
                ps.setString(2,dataSourceName);
                ps.setString(3,connData);
                return ps;
            }
        },keyHolder);
        int generatedId = keyHolder.getKey().intValue();
        return generatedId;
    }

    public int  updateSqlFilePathById(DataTask dataTask){

        String sql = "update  t_datatask  set  sqlFilePath ='"+dataTask.getSqlFilePath()+"' where  dataTaskId = '"+dataTask.getDataTaskId()+"'";
        SqlLiteDataConnection sqlLiteDataConnection=new SqlLiteDataConnection();
        JdbcTemplate jdbcTemplate=sqlLiteDataConnection.makeJdbcTemplate();
        int result = jdbcTemplate.update(sql);//query(sql, new Object[]{taskId}, new DataTaskMapper());
        return result;
    }

    public DataSrc getDataSourceById(String dataSourceId){
        String sql="select * from t_datasource where t_datasource.DataSourceId=?";
        SqlLiteDataConnection sqlLiteDataConnection=new SqlLiteDataConnection();
        JdbcTemplate jdbcTemplate=sqlLiteDataConnection.makeJdbcTemplate();
        List<DataSrc> list = jdbcTemplate.query(sql, new Object[]{dataSourceId}, new DataSrcMapper());
        return list.size() > 0 ? list.get(0) : null;
    }
}