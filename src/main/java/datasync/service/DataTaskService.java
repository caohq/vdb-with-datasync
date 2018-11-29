package datasync.service;

import datasync.entity.DataTask;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DataTaskService {

    public int insertDatatask(DataTask datatask) throws SQLException {
        return new DataTaskDao().insertDatatask(datatask);
    }

    public List<DataTask> getDataTaskList(Map<Object,Object> params){
        return new DataTaskDao().getDataTaskList(params);
    }

    public DataTask getDataTaskInfById(String id){
        return new DataTaskDao().getDataTaskInfById(id);
    }

    public int deleteTaskById(String taskId){
        return new DataTaskDao().deleteTaskById(taskId);
    }


}
