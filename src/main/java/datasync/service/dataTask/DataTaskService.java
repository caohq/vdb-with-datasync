package datasync.service.dataTask;

import datasync.entity.DataTask;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DataTaskService {

    public int insertDatatask(DataTask datatask,String connData,String dataSourceName) throws SQLException {
        return new DataTaskDao().insertDatatask(datatask,connData,dataSourceName);
    }

    //获取任务列表
    public List<DataTask> getDataTaskList(Map<Object,Object> params){
        return new DataTaskDao().getDataTaskList(params);
    }

    public DataTask getDataTaskInfById(String id){
        return new DataTaskDao().getDataTaskInfById(id);
    }

    public int deleteTaskById(String taskId){
        return new DataTaskDao().deleteTaskById(taskId);
    }

    public int updateDataTaskStatusById(String taskId){
        return new DataTaskDao().updateDataTaskStatusById(taskId);
    }

    public int updateSqlFilePathById(DataTask dataTask){
        return new DataTaskDao().updateSqlFilePathById(dataTask);
    }


}
