package datasync.utils.dataSrc;

/**
 * @program: DataSync
 * @description:
 * @author: Mr.huang
 * @create: 2018-09-27 14:51
 **/
public class DataSourceFactory {
    public static IDataSource getDataSource(String databaseType) {
        if (databaseType.equalsIgnoreCase("mysql")) {
            return new MySqlDataSource();
        } else if (databaseType.equals("oracle")) {
            return new OracleDataSource();
        }
        return null;
    }
}
