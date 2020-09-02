/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants.gmbling;

import database.DBConPool;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wubin
 */
public class BasicRandomGamblingHandler {

    private List<Map<String, Integer>> items = new ArrayList<>();
    private String table;

    public BasicRandomGamblingHandler(String table) {
        this.table = table;
    }

    private void load() {
        Connection conn = null;
        try {
            conn = DBConPool.getInstance().getDataSource().getConnection();
            //id 
            //itemId 物品id
            //level  1:小奖 2: 中奖 3:大奖 4:超级奖  (脚本自己根据级别处理对应的逻辑,例如:大奖和超级奖才发喇叭等.)
            //number 奖池可以抽取到的数量
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from " + table);
            Map<String, Integer> item = null;
            while (resultSet.next()) {
                item = new HashMap<>();
                item.put("id", resultSet.getInt("id"));
                item.put("itemId", resultSet.getInt("itemId"));
                item.put("level", resultSet.getInt("level"));
                int number = resultSet.getInt("number");
                item.put("number", number);
                for (int i = 0; i < number; i++) {
                    items.add(item);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BasicRandomGamblingHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BasicRandomGamblingHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public Map<String, Integer> next() {
        if (this.items.isEmpty()) {
            this.load();
            Collections.shuffle(items);
        }
        return items.remove(0);
    }
}
