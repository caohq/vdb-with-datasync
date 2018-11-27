package datasync.service;

import datasync.entity.DataTask;

import java.sql.SQLException;
import java.util.List;

public class DataTaskService {

    public int insertDatatask(DataTask datatask) throws SQLException {
        return new DataTaskDao().insertDatatask(datatask);
    }

    public List<DataTask> getDataTaskList(String connData){
        return new DataTaskDao().getDataTaskList(connData);
    }

    public DataTask getDataTaskInfById(String id){
        return new DataTaskDao().getDataTaskInfById(id);
    }


}
