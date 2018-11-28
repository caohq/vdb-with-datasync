package datasync.utils;


import com.google.common.collect.Maps;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by xiajl on 2018/10/02.
 */
public class SqlUtils {

    public static HashMap<String, String> getTablesFromSelectSql(String sql) {

        Select select = getSelectFromSelectSql(sql);
        if (select == null) {
            return null;
        }
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        HashMap<String, String> map = Maps.newHashMap();
        if (plainSelect.getFromItem() instanceof Table) {
            Table fromItem = (Table) plainSelect.getFromItem();
            Alias alias = fromItem.getAlias();
            if (alias != null) {
                map.put(fromItem.getName(), alias.getName());
            } else {
                map.put(fromItem.getName(), null);
            }
        }
        List<Join> joins = plainSelect.getJoins();
        if (joins == null || joins.size() == 0) {
            return map;
        }
        for (Join join : joins) {
            if (!(join.getRightItem() instanceof Table)) {
                break;
            }
            Table joinTableName = (Table) join.getRightItem();
            Alias alias = joinTableName.getAlias();
            if (alias != null) {
                map.put(joinTableName.getName(), alias.getName());
            } else {
                map.put(joinTableName.getName(), null);
            }
        }
        return map;
    }


    public static boolean validateSelectSql(String sql) {

        return getSelectFromSelectSql(sql) == null ? false : true;

    }

    public static boolean containsWhereFromSql(String sql) {
        Select select = getSelectFromSelectSql(sql);
        if (select == null) {
            return false;
        }
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        plainSelect.getWhere();
        return plainSelect.getWhere() == null ? false : true;
    }

    private static Select getSelectFromSelectSql(String sql) {
        if (StringUtils.isBlank(sql)) {
            return null;
        }
        Select select;
        try {
            select = (Select) CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            return null;
        }

        if (!(select.getSelectBody() instanceof PlainSelect)) {
            return null;
        }
        return select;
    }


}
