/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;

/**
 *
 * @author Itzik
 */
public class HairFaceDump {

    public static void main(String[] args) throws IOException {
        MapleDataProvider data;
        data = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Character.wz/Hair"));
        MapleDataDirectoryEntry root;
        StringBuilder sb = new StringBuilder();
        FileOutputStream out;
        out = new FileOutputStream("Styler.js", false);
        List<Integer> maleHair1 = new LinkedList<>();
        List<Integer> maleHair2 = new LinkedList<>();
        List<Integer> maleHair3 = new LinkedList<>();
        List<Integer> femaleHair1 = new LinkedList<>();
        List<Integer> femaleHair2 = new LinkedList<>();
        List<Integer> femaleHair3 = new LinkedList<>();
        System.out.println("Loading Hairs");
        root = data.getRoot();
        for (MapleDataFileEntry topDir : root.getFiles()) {
            int id = Integer.parseInt(topDir.getName().substring(0, 8));
            if (isMaleHair(id)) {
                if (maleHair1.size() < 0x7F) {
                    maleHair1.add(id);
                } else {
                    if (maleHair2.size() < 0x7F) {
                        maleHair2.add(id);
                    } else {
                        maleHair3.add(id);
                    }
                }
            }
        }
        if (maleHair2.isEmpty()) {
            maleHair2 = maleHair1;
            maleHair3 = maleHair1;
        } else if (maleHair3.isEmpty()) {
            maleHair3 = maleHair1;
        }
        for (MapleDataFileEntry topDir : root.getFiles()) {
            int id = Integer.parseInt(topDir.getName().substring(0, 8));
            if (isFemaleHair(id)) {
                if (femaleHair1.size() < 0x7F) {
                    femaleHair1.add(id);
                } else {
                    if (femaleHair2.size() < 0x7F) {
                        femaleHair2.add(id);
                    } else {
                        femaleHair3.add(id);
                    }
                }
            }
        }
        if (femaleHair2.isEmpty()) {
            femaleHair2 = femaleHair1;
            femaleHair3 = femaleHair1;
        } else if (femaleHair3.isEmpty()) {
            femaleHair3 = femaleHair1;
        }
        List<Integer> hairList = new LinkedList<>();
        for (MapleDataFileEntry topDir : root.getFiles()) {
            hairList.add(Integer.parseInt(topDir.getName().substring(0, 8)));
        }
        List<Integer> maleFace = new LinkedList<>();
        List<Integer> femaleFace = new LinkedList<>();
        System.out.println("Loading Faces");
        data = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Character.wz/Face"));
        root = data.getRoot();
        for (MapleDataFileEntry topDir : root.getFiles()) {
            int id = Integer.parseInt(topDir.getName().substring(0, 8));
            if (isMaleFace(id)) {
                maleFace.add(id);
            }
        }
        for (MapleDataFileEntry topDir : root.getFiles()) {
            int id = Integer.parseInt(topDir.getName().substring(0, 8));
            if (isFemaleFace(id)) {
                femaleFace.add(id);
            }
        }
        if (maleHair1.isEmpty() || maleHair2.isEmpty() || maleHair3.isEmpty() || femaleHair1.isEmpty() || femaleHair2.isEmpty() || femaleHair3.isEmpty() || maleFace.isEmpty() || femaleFace.isEmpty()) {
            System.out.println("Could not find any hairs/faces in an array.");
            System.exit(0);
            return;
        }
        List<Integer> faceList = new LinkedList<>();
        for (MapleDataFileEntry topDir : root.getFiles()) {
            faceList.add(Integer.parseInt(topDir.getName().substring(0, 8)));
        }
        sb.append(createStyler(maleHair1, maleHair2, maleHair3, femaleHair1, femaleHair2, femaleHair3, maleFace, femaleFace, hairList, faceList));
        out.write(sb.toString().getBytes());
    }

    public static boolean isMaleHair(int id) {
        if (id % 10 != 0) {
            return false;
        }
        if (id == 33030 || id == 33160 || id == 33590) {
            return false;
        }
        if (id / 1000 == 30 || id / 1000 == 33 || (id / 1000 == 32 && id >= 32370) || id / 1000 == 36 || (id / 1000 == 37 && id >= 37160 && id <= 37170)) {
            return true;
        }
        switch (id) {
            case 32160:
            case 32330:
            case 34740:
                return true;
        }
        return false;
    }

    public static boolean isFemaleHair(int id) {
        if (id % 10 != 0) {
            return false;
        }
        if (id == 32160 || id == 32330 || id == 34740) {
            return false;
        }
        if (id / 1000 == 31 || id / 1000 == 34 || (id / 1000 == 32 && id < 32370) || (id / 1000 == 37 && id < 37160)) {
            return true;
        }
        switch (id) {
            case 33030:
            case 33160:
            case 33590:
                return true;
        }
        return false;
        //return !isMaleHair(id);
    }

    public static boolean isMaleFace(int id) {
        if (id % 1000 >= 100) {
            return false;
        }
        return id / 1000 == 20;
    }

    public static boolean isFemaleFace(int id) {
        if (id % 1000 >= 100) {
            return false;
        }
        return id / 1000 == 21;
    }

    public static boolean hairExists(int hair) {
        MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Character.wz/Hair"));
        final MapleDataDirectoryEntry root = data.getRoot();
        for (MapleDataFileEntry topDir : root.getFiles()) {
            int id = Integer.parseInt(topDir.getName().substring(0, 8));
            if (id == hair) {
                return true;
            }
        }
        return false;
    }

    public static boolean faceExists(int face) {
        MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Character.wz/Face"));
        final MapleDataDirectoryEntry root = data.getRoot();
        for (MapleDataFileEntry topDir : root.getFiles()) {
            int id = Integer.parseInt(topDir.getName().substring(0, 8));
            if (id == face) {
                return true;
            }
        }
        return false;
    }

    public static String createStyler(List<Integer> maleHair1, List<Integer> maleHair2, List<Integer> maleHair3, List<Integer> femaleHair1, List<Integer> femaleHair2, List<Integer> femaleHair3, List<Integer> maleFace, List<Integer> femaleFace, List<Integer> hairList, List<Integer> faceList) {
        StringBuilder sb = new StringBuilder();
        addLine(sb, "var v1;");
        addLine(sb, "var v2;");
        addLine(sb, "var v3;");
        addLine(sb, "var v4;");
        addLine(sb, "var v5;");
        addLine(sb, "var v6;");
        addLine(sb, "var v7;");
        addLine(sb, "var v8;");
        addLine(sb, "var v9;");
        addLine(sb, "var v10;");
        addLine(sb, "var v11;");
        addLine(sb, "var v12;");
        addLine(sb, "var v13;");
        addLine(sb, "var v14;");
        addLine(sb, "var v15;");
        addLine(sb, "var v16;");
        addLine(sb, "var v17;");
        addLine(sb, "var v18;");
        addLine(sb, "var v19;");
        addLine(sb, "var v20;");
        addLine(sb, "var v21;");
        addLine(sb, "var v22;");
        addLine(sb, "var v23;");
        addLine(sb, "var v24;");
        addLine(sb, "var v25;");
        addLine(sb, "var v26;");
        addLine(sb, "var v27;");
        addLine(sb, "");
        addLine(sb, "function start() {");
        addLine(sb, "   v1 = 1;");
        addLine(sb, "   v2 = Array();");
        addLine(sb, "   v3 = Array();");
        addLine(sb, "   v4 = Array();");
        addLine(sb, "   v5 = Array();");
        addLine(sb, "   v6 = [0, 1, 2, 3, 4, 5, 9, 10];");
        addLine(sb, "   v7 = " + maleHair1.toString() + ";");
        addLine(sb, "   v8 = " + femaleHair1.toString() + ";");
        addLine(sb, "   v9 = " + maleHair2.toString() + ";");
        addLine(sb, "   v10 = " + femaleHair2.toString() + ";");
        addLine(sb, "   v11 = " + maleHair3.toString() + ";");
        addLine(sb, "   v12 = " + femaleHair3.toString() + ";");
        addLine(sb, "   v13 = cm.getPlayerStat(\"GENDER\");");
        addLine(sb, "   v18 = 0;");
        addLine(sb, "   v19 = " + maleFace.toString() + ";");
        addLine(sb, "   v20 = " + femaleFace.toString() + ";");
        addLine(sb, "   v21 = 0;");
        addLine(sb, "   v22 = cm.getPlayer().getHair();");
        addLine(sb, "   v23 = cm.getPlayer().getFace();");
        addLine(sb, "   v25 = 0;");
        addLine(sb, "   v26 = " + hairList.toString() + ";");
        addLine(sb, "   v27 = " + faceList.toString() + ";");
        addLine(sb, "   cm.sendSimple(\"Time for make-up!\\r\\n#L0#Skin#l\\r\\n#L1#Haircut#l\" + (v13 == 0 ? ((\"\" + v7 != v9 ? \"\\r\\n#L2#Haircut2#l\" : \"\") + (\"\" + v7 != v11 ? \"\\r\\n#L3#Haircut3#l\" : \"\")) : ((\"\" + v8 != v10 ? \"\\r\\n#L2#Haircut2#l\" : \"\") + (\"\" + v8 != v12 ? \"\\r\\n#L3#Haircut3#l\" : \"\"))) + \"\\r\\n#L4#Hair Color#l\\r\\n#L5#Face#l\\r\\n#L6#Eye Color#l\");");
        addLine(sb, "}");
        addLine(sb, "");
        addLine(sb, "function action(mode, type, selection) {");
        addLine(sb, "    if (mode != 1) {");
        addLine(sb, "        cm.dispose();");
        addLine(sb, "        return;");
        addLine(sb, "    }");
        addLine(sb, "    switch (v1) {");
        addLine(sb, "        case 1:");
        addLine(sb, "            v14 = selection;");
        addLine(sb, "            switch (v14) {");
        addLine(sb, "                case 0:");
        addLine(sb, "                    sendStyle(v6);");
        addLine(sb, "                    break;");
        addLine(sb, "                case 1:");
        addLine(sb, "                    for each(v15 in v13 == 0 ? v7 : v8)");
        addLine(sb, "                        if (hairExists(v15))");
        addLine(sb, "                            v2.push(v15);");
        addLine(sb, "                    sendStyle(v2);");
        addLine(sb, "                    break;");
        addLine(sb, "                case 2:");
        addLine(sb, "                    for each(v15 in v13 == 0 ? v9 : v10)");
        addLine(sb, "                        if (hairExists(v15))");
        addLine(sb, "                            v2.push(v15);");
        addLine(sb, "                    sendStyle(v2);");
        addLine(sb, "                    break;");
        addLine(sb, "                case 3:");
        addLine(sb, "                    for each(v15 in v13 == 0 ? v11 : v12)");
        addLine(sb, "                        if (hairExists(v15))");
        addLine(sb, "                            v2.push(v15);");
        addLine(sb, "                    sendStyle(v2);");
        addLine(sb, "                    break;");
        addLine(sb, "                case 4:");
        addLine(sb, "                    for (v18; v18 < 8; v18++)");
        addLine(sb, "                        if (hairExists(v22 + v18))");
        addLine(sb, "                            v3.push(v22 + v18);");
        addLine(sb, "                    sendStyle(v3);");
        addLine(sb, "                    break;");
        addLine(sb, "                case 5:");
        addLine(sb, "                    for each(v15 in v13 == 0 ? v19 : v20)");
        addLine(sb, "                        if (faceExists(v15))");
        addLine(sb, "                            v4.push(v15);");
        addLine(sb, "                    sendStyle(v4);");
        addLine(sb, "                    break;");
        addLine(sb, "                case 6:");
        addLine(sb, "                    for(v21; v21 < 9; v21++)");
        addLine(sb, "                        if (faceExists(v23 + (v21 * 100)))");
        addLine(sb, "                            v5.push(v23 + (v21 * 100));");
        addLine(sb, "                    sendStyle(v5);");
        addLine(sb, "                    break;");
        addLine(sb, "            }");
        addLine(sb, "            v1 = 2;");
        addLine(sb, "            break;");
        addLine(sb, "        case 2:");
        addLine(sb, "            v24 = selection;");
        addLine(sb, "            switch (v14) {");
        addLine(sb, "                case 0:");
        addLine(sb, "                    cm.setSkin(v6[v24]);");
        addLine(sb, "                    break;");
        addLine(sb, "                case 1:");
        addLine(sb, "                case 2:");
        addLine(sb, "                case 3:");
        addLine(sb, "                    cm.setHair(v2[v24]);");
        addLine(sb, "                    break;");
        addLine(sb, "                case 4:");
        addLine(sb, "                    cm.setHair(v3[v24]);");
        addLine(sb, "                    break;");
        addLine(sb, "                case 5:");
        addLine(sb, "                    cm.setFace(v4[v24]);");
        addLine(sb, "                    break;");
        addLine(sb, "                case 6:");
        addLine(sb, "                    cm.setFace(v5[v24]);");
        addLine(sb, "                    break;");
        addLine(sb, "            }");
        addLine(sb, "            cm.dispose();");
        addLine(sb, "            break;");
        addLine(sb, "        default:");
        addLine(sb, "            cm.dispose();");
        addLine(sb, "            return;");
        addLine(sb, "   }");
        addLine(sb, "}");
        addLine(sb, "");
        addLine(sb, "function sendStyle(array) {");
        addLine(sb, "   v17 = array;");
        addLine(sb, "   cm.sendStyle(\"Pick your favorite\", v17);");
        addLine(sb, "}");
        addLine(sb, "");
        addLine(sb, "function hairExists(hair) {");
        addLine(sb, "    for (v25; v25 < v26.length; v25++)");
        addLine(sb, "        if (v26[v25] == hair) {");
        addLine(sb, "            return true;");
        addLine(sb, "            break;");
        addLine(sb, "        }");
        addLine(sb, "    return false;");
        addLine(sb, "}");
        addLine(sb, "");
        addLine(sb, "function faceExists(face) {");
        addLine(sb, "    for (v25; v25 < v27.length; v25++)");
        addLine(sb, "        if (v27[v25] == face) {");
        addLine(sb, "            return true;");
        addLine(sb, "            break;");
        addLine(sb, "        }");
        addLine(sb, "    return false;");
        addLine(sb, "}");
        return sb.toString();
    }

    public static void addLine(StringBuilder sb, String string) {
        sb.append(string).append("\r\n");
    }
}
