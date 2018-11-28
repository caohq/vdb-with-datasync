package datasync.utils.dataSrc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: DataSync
 * @description: DataSource of mysql handler
 * @author: Mr.huang
 * @create: 2018-09-27 14:47
 **/
public class MySqlDataSource extends IDataSource{

    private Logger logger = LoggerFactory.getLogger(MySqlDataSource.class);

    @Override
    public Connection getConnection(String host, String port, String userName, String password, String databaseName) {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
//            Connection connection = DriverManager.getConnection(url, userName, password);
//            return connection;
//        } catch (ClassNotFoundException e) {
//            logger.error("缺少mysql驱动包", e);
//            return null;
//        } catch (SQLException e) {
//            logger.error("无法获取连接", e);
//            return null;
//        }
        return DataBaseSource.getConnection("com.mysql.jdbc.Driver", host, port, userName, password, databaseName);
    }

    @Override
    public List<String> getTableList(Connection connection) {
        List<String> tables = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getTables(null, null, null, new String[]{"TABLE", "VIEW"})) {
            while (rs.next()) {
                if (rs.getString(4) != null
                        && (rs.getString(4).equalsIgnoreCase("TABLE") || rs
                        .getString(4).equalsIgnoreCase("VIEW"))) {
                    String tableName = rs.getString(3).toLowerCase();
                    tables.add(tableName);
                }
            }
            return tables;
        } catch (SQLException e) {
            logger.error("查询发生错误", e);
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("数据连接关闭错误", e);
            }
        }

    }

    @Override
    public Map<String, Map<String, String>> getTableColumns(Connection connection, String databaseName, String tableName) {
        Map<String, Map<String, String>> colInfos = new HashMap<String, Map<String, String>>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT  COLUMN_NAME ,DATA_TYPE,COLUMN_TYPE,COLUMN_COMMENT FROM information_schema.`COLUMNS` where" +
                    " TABLE_SCHEMA =? and TABLE_NAME =?");
            statement.setString(1, databaseName);
            statement.setString(2, tableName);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("type", rs.getString(2));
                map.put("value", "1");
                map.put("comment", rs.getString(4));
                colInfos.put(rs.getString(1), map);
            }
            return colInfos;
        } catch (SQLException e) {
            logger.error("查询表字段错误", e);
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("数据连接关闭错误", e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("数据连接关闭错误", e);
                }
            }
        }
    }


    @Override
    public PreparedStatement getPaginationSql(Connection connection, String sql, List<Object> params, int start, int pageSize) {
        sql = sql + " limit ?,?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (params == null)
                params = new ArrayList<>();
            params.add(start);
            params.add(pageSize);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            return ps;
        } catch (SQLException e) {
            logger.error("无法获取分页sql", e);
            return null;
        }
    }

//    @Override
//    public boolean validateSql(Connection connection, String sql) {
//        Statement statement = null;
//        try {
//            statement = connection.createStatement();
//            boolean result = statement.execute(sql);
//            statement.close();
//            return result;
//        } catch (SQLException e) {
//            logger.error("执行sql语句错误", e);
//            return false;
//        } finally {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                logger.error("数据连接关闭错误", e);
//            }
//        }
//    }
//
//    @Override
//    public boolean validateConnection(String host, String port, String userName, String password, String database) {
//        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
//        boolean isValid = false;
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection(url, userName, password);
//            isValid = !con.isClosed();
//            con.close();
//        } catch (ClassNotFoundException e) {
//            logger.error("无法找到驱动类");
//            return false;
//        } catch (SQLException e) {
//            logger.error("数据连接失败");
//            return false;
//        }
//        return isValid;
//    }


//    @Override
//public  List<TableInfo> getTableFieldComs(Connection connection, String tableName) throws SQLException {
//        HashMap<String, String> map = Maps.newHashMap();
//        PreparedStatement preparedStatement = connection.prepareStatement("show full COLUMNS FROM " + tableName);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while (resultSet.next()) {
//            String field = resultSet.getString("Field");
//            String comment = resultSet.getString("Comment");
//            map.put(field, StringUtils.isBlank(comment) ? "" : comment);
//        }
//        return map;
//}
}
