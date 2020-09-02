package server;

import database.DBConPool;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.CashItemInfo.CashModInfo;
import server.CashItemInfo.CashPackageInfo;
import tools.FileoutputUtil;

public class CashItemFactory {

    private final static CashItemFactory instance = new CashItemFactory();
    private boolean initialized = false;
    private final static int[] bestItems = new int[]{50400041, 50400016, 50400135, 50400061, 20800204};
    private final Map<Integer, CashItemInfo> itemStats = new HashMap<>();
    private final Map<Integer, List<Integer>> itemPackage = new HashMap<>();
    private final Map<Integer, CashPackageInfo> itemPks = new HashMap<>();
    private final Map<Integer, CashModInfo> itemMods = new HashMap<>();
    private final Map<Integer, List<Integer>> openBox = new HashMap<>();
    private final MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Etc.wz"));
    private final List<CashCategory> categories = new LinkedList<>();
    private final Map<Integer, CashItem> menuItems = new HashMap<>();
    private final Map<Integer, CashItem> categoryItems = new HashMap<>();

    public static CashItemFactory getInstance() {
        return instance;
    }

    public void initialize() {

        /*try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
         PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_modified_items");

         ResultSet rs = ps.executeQuery();
         while (rs.next()) {

         Integer ItemId = rs.getInt("itemid");
         Integer Count = rs.getInt("count");
         Integer Price = rs.getInt("discount_price");
         Integer SN = rs.getInt("serial");
         Integer Period = rs.getInt("Period");
         Integer Gender = rs.getInt("gender");
         Boolean showUp = rs.getInt("showup") > 0;

         final CashItemInfo stats = new CashItemInfo(ItemId, Count, Price, SN, Period, Gender, showUp, 0);

         if (SN > 0) {
         itemStats.put(SN, stats);
         }
         }

         } catch (SQLException e) {
         System.err.println("CashItemInfo" + e);
         FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
         }*/
        final List<MapleData> cccc = data.getData("Commodity.img").getChildren();
        for (MapleData field : cccc) {
            final int SN = MapleDataTool.getIntConvert("SN", field, 0);

            final CashItemInfo stats = new CashItemInfo(MapleDataTool.getIntConvert("ItemId", field, 0),
                    MapleDataTool.getIntConvert("Count", field, 1),
                    MapleDataTool.getIntConvert("Price", field, 0), SN,
                    MapleDataTool.getIntConvert("Period", field, 0),
                    MapleDataTool.getIntConvert("Gender", field, 2),
                    MapleDataTool.getIntConvert("OnSale", field, 0) > 0 && MapleDataTool.getIntConvert("Price", field, 0) > 0,
                    0);

            if (SN > 0) {
                itemStats.put(SN, stats);
            }
        }

        /*final List<MapleData> cccc = data.getData("Commodity.img").getChildren();
         for (MapleData field : cccc) {
         final int SN = MapleDataTool.getIntConvert("SN", field, 0);

         final CashItemInfo stats = new CashItemInfo(MapleDataTool.getIntConvert("ItemId", field, 0),
         MapleDataTool.getIntConvert("Count", field, 1),
         MapleDataTool.getIntConvert("Price", field, 0), SN,
         MapleDataTool.getIntConvert("Period", field, 0),
         MapleDataTool.getIntConvert("Gender", field, 2),
         MapleDataTool.getIntConvert("OnSale", field, 0) > 0 && MapleDataTool.getIntConvert("Price", field, 0) > 0,
         0);

         if (SN > 0) {
         itemStats.put(SN, stats);
         }
         }*/
        final MapleData b = data.getData("CashPackage.img");
        for (MapleData c : b.getChildren()) {
            if (c.getChildByPath("SN") == null) {
                continue;
            }
            final List<Integer> packageItems = new ArrayList<>();
            for (MapleData d : c.getChildByPath("SN").getChildren()) {
                packageItems.add(MapleDataTool.getIntConvert(d));
            }
            itemPackage.put(Integer.parseInt(c.getName()), packageItems);
        }

        //try {
        //    Connection con = DatabaseConnection.getConnection();
        //    try (PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_modified_items"); ResultSet rs = ps.executeQuery()) {
        //       if (rs.next()) {
        //           CashModInfo ret = new CashModInfo(rs.getInt("serial"), rs.getInt("discount_price"), rs.getInt("mark"), rs.getInt("showup") > 0, rs.getInt("itemid"), rs.getInt("priority"), rs.getInt("package") > 0, rs.getInt("period"), rs.getInt("gender"), rs.getInt("count"), rs.getInt("meso"), rs.getInt("unk_1"), rs.getInt("unk_2"), rs.getInt("unk_3"), rs.getInt("extra_flags"));
        //          itemMods.put(ret.sn, ret);
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_modified_items");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Integer sn = rs.getInt("serial");
                CashModInfo ret = new CashModInfo(sn, rs.getInt("discount_price"), rs.getInt("mark"), rs.getInt("showup") > 0, rs.getInt("itemid"), rs.getInt("priority"), rs.getInt("package") > 0, rs.getInt("period"), rs.getInt("gender"), rs.getInt("count"), rs.getInt("meso"), rs.getInt("unk_1"), rs.getInt("unk_2"), rs.getInt("unk_3"), rs.getInt("extra_flags"));
                itemMods.put(sn, ret);

                if (ret.showUp) {
                    final CashItemInfo cc = itemStats.get(Integer.valueOf(ret.sn));
                    if (cc != null) {
                        ret.toCItem(cc); //init
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("CashModInfo" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        /*try {
         Connection con = DatabaseConnection.getConnection();
         try (PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_categories"); ResultSet rs = ps.executeQuery()) {
         while (rs.next()) {
         CashCategory cat = new CashCategory(rs.getInt("categoryid"), rs.getString("name"), rs.getInt("parent"), rs.getInt("flag"), rs.getInt("sold"));
         categories.add(cat);
         }
         }
         } catch (SQLException e) {
         System.out.println("Failed to load cash shop categories. " + e);
         }*/

 /*try {
         Connection con = DatabaseConnection.getConnection();
         try (PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_menuitems"); ResultSet rs = ps.executeQuery()) {
         while (rs.next()) {
         CashItem item = new CashItem(rs.getInt("category"), rs.getInt("subcategory"), rs.getInt("parent"), rs.getString("image"), rs.getInt("sn"), rs.getInt("itemid"), rs.getInt("flag"), rs.getInt("price"), rs.getInt("discountPrice"), rs.getInt("quantity"), rs.getInt("expire"), rs.getInt("gender"), rs.getInt("likes"));
         menuItems.put(item.getSN(), item);
         }
         }
         } catch (SQLException e) {
         System.out.println("Failed to load cash shop categories. " + e);
         }*/

 /*try {
         Connection con = DatabaseConnection.getConnection();
         try (PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_items"); ResultSet rs = ps.executeQuery()) {
         while (rs.next()) {
         CashItem item = new CashItem(rs.getInt("category"), rs.getInt("subcategory"), rs.getInt("parent"), rs.getString("image"), rs.getInt("sn"), rs.getInt("itemid"), rs.getInt("flag"), rs.getInt("price"), rs.getInt("discountPrice"), rs.getInt("quantity"), rs.getInt("expire"), rs.getInt("gender"), rs.getInt("likes"));
         categoryItems.put(item.getSN(), item);
         }
         }
         } catch (SQLException e) {
         System.out.println("Failed to load cash shop categories. " + e);
         }*/
    }

    public final CashItemInfo getPackageItem(int sn) {
        final CashItemInfo stats = itemStats.get(sn);
        final CashPackageInfo cc = getitemPksInfo(sn);
        if (cc != null) {
            return cc.toCItem(stats); //null doesnt matter
        }
        if (stats == null) {
            return null;
        }
        return stats;
    }

    public final CashItemInfo getSimpleItem(int sn) {
        return itemStats.get(sn);
    }

    public final CashPackageInfo getitemPksInfo(int sn) {
        return itemPks.get(sn);
    }

    public final CashItemInfo getItem(int sn) {
        final CashItemInfo stats = itemStats.get(Integer.valueOf(sn));
        final CashModInfo z = getModInfo(sn);
        if (z != null && z.showUp) {
            return z.toCItem(stats); //null doesnt matter
        }
        if (stats == null || !stats.onSale()) {
            return null;
        }
        return stats;
    }

    public final CashItem getMenuItem(int sn) {
        for (CashItem ci : getMenuItems()) {
            if (ci.getSN() == sn) {
                return ci;
            }
        }
        return null;
    }

    public final CashItem getAllItem(int sn) {
        for (CashItem ci : getAllItems()) {
            if (ci.getSN() == sn) {
                return ci;
            }
        }
        return null;
    }

    public final List<Integer> getPackageItems(int itemId) {
        return itemPackage.get(itemId);
    }

    public final CashModInfo getModInfo(int sn) {
        return itemMods.get(sn);
    }

    public final Collection<CashModInfo> getAllModInfo() {
        return itemMods.values();
    }

    public final Map<Integer, List<Integer>> getRandomItemInfo() {
        return openBox;
    }

    public final int[] getBestItems() {
        return bestItems;
    }

    public final List<CashCategory> getCategories() {
        return categories;
    }

    public final List<CashItem> getMenuItems(int type) {
        List<CashItem> items = new LinkedList();
        for (CashItem ci : menuItems.values()) {
            if (ci.getSubCategory() / 10000 == type) {
                items.add(ci);
            }
        }
        return items;
    }

    public final List<CashItem> getMenuItems() {
        List<CashItem> items = new LinkedList();
        for (CashItem ci : menuItems.values()) {
            items.add(ci);
        }
        return items;
    }

    public final List<CashItem> getAllItems(int type) {
        List<CashItem> items = new LinkedList();
        for (CashItem ci : categoryItems.values()) {
            if (ci.getSubCategory() / 10000 == type) {
                items.add(ci);
            }
        }
        return items;
    }

    public final List<CashItem> getAllItems() {
        List<CashItem> items = new LinkedList();
        for (CashItem ci : categoryItems.values()) {
            items.add(ci);
        }
        return items;
    }

    public final List<CashItem> getCategoryItems(int subcategory) {
        List<CashItem> items = new LinkedList();
        for (CashItem ci : categoryItems.values()) {
            if (ci.getSubCategory() == subcategory) {
                items.add(ci);
            }
        }
        return items;
    }

    public final int getItemSN(int itemid) {
        for (Entry<Integer, CashItemInfo> ci : itemStats.entrySet()) {
            if (ci.getValue().getId() == itemid) {
                return ci.getValue().getSN();
            }
        }
        return 0;
    }

    public final void clearCashShop() {
        itemMods.clear();
        itemStats.clear();
        itemPackage.clear();
        itemPks.clear();
        initialize();
    }
}
