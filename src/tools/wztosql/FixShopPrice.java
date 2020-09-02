/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.wztosql;

import database.DBConPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.MapleItemInformationProvider;
import tools.FileoutputUtil;

/**
 *
 * @author wubin
 */
public class FixShopPrice {

    //private final Connection con = DatabaseConnection.getConnection();
    private List<Integer> loadFromDB() {
        List<Integer> shopItemsId = new ArrayList<Integer>();
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM shopitems ORDER BY itemid"); ResultSet rs = ps.executeQuery()) {
                int itemId = 0;
                while (rs.next()) {
                    if (itemId != rs.getInt("itemid")) {
                        itemId = rs.getInt("itemid");
                        //System.out.println("商店物品ID:" + itemId);
                        shopItemsId.add(itemId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Could not load shop" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        return shopItemsId;
    }

    private void changePrice(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM shopitems WHERE itemid = ? ORDER BY price")) {
                ps.setInt(1, itemId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        double a = ii.getPrice(itemId);
                        if (ii.getPrice(itemId) > rs.getLong("price") && rs.getInt("price") != 0) {
                            System.out.println("更正物品价格, 物品ID: " + itemId + " 商店ID: " + rs.getInt("shopid") + " 原价格: " + rs.getLong("price") + " 改后价格:" + (long) ii.getPrice(itemId));
                            try (PreparedStatement pp = con.prepareStatement("UPDATE shopitems SET price = ? WHERE itemid = ? AND shopid = ?")) {
                                pp.setLong(1, (long) ii.getPrice(itemId));
                                pp.setInt(2, itemId);
                                pp.setInt(3, rs.getInt("shopid"));
                                pp.execute();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("处理物品失败, 物品ID:" + itemId + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public static void main(String[] args) {
        FixShopPrice i = new FixShopPrice();
        System.out.println("正在加载物品数据......");
        MapleItemInformationProvider.getInstance().runEtc();
        MapleItemInformationProvider.getInstance().runItems();
        System.out.println("正在读取商店所有物品......");
        List<Integer> list = i.loadFromDB();
        System.out.println("正在处理商店物品价格......");
        for (int ii : list) {
            //System.out.println("当前处理物品ID:" + ii);
            i.changePrice(ii);
        }
        System.out.println("处理商店物品价格过低结束。");
    }
}
