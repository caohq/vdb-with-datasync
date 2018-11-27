package datasync.entity;

import java.util.Date;


public class DataTask {
    private int dataTaskId;
    private String dataSourceName;
    private String dataTaskName;
    private String dataTaskType;
    private String tableName;
    private String sqlString;
    private String sqlTableNameEn;
    private String sqlFilePath;
    private String filePath;
    private String createTime;
    private String creator;
    private String status;
    private DataSrc dataSrc;

    public String getDataSourceName() { return dataSourceName; }

    public void setDataSourceName(String dataSourceName) { this.dataSourceName = dataSourceName; }

    public DataSrc getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(DataSrc dataSrc) {
        this.dataSrc = dataSrc;
    }

    public String getDataTaskName() {
        return dataTaskName;
    }

    public void setDataTaskName(String dataTaskName) {
        this.dataTaskName = dataTaskName;
    }

    public String getDataTaskType() {
        return dataTaskType;
    }

    public void setDataTaskType(String dataTaskType) {
        this.dataTaskType = dataTaskType;
    }

    public int getDataTaskId() {
        return dataTaskId;
    }

    public void setDataTaskId(int dataTaskId) {
        this.dataTaskId = dataTaskId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSqlString() {
        return sqlString;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public String getSqlTableNameEn() {
        return sqlTableNameEn;
    }

    public void setSqlTableNameEn(String sqlTableNameEn) {
        this.sqlTableNameEn = sqlTableNameEn;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSqlFilePath() {
        return sqlFilePath;
    }

    public void setSqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
    }
}
