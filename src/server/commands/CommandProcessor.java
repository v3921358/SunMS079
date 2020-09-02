/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server.commands;

import client.MapleCharacter;
import client.MapleClient;
import constants.ServerConstants.CommandType;
import constants.ServerConstants.PlayerGMRank;
import database.DBConPool;
import handling.channel.ChannelServer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import tools.FileoutputUtil;

public class CommandProcessor {

    private final static List<String> showcommands = new LinkedList<>();
    private final static HashMap<String, MapleCommand> commands = new HashMap<>();
    private final static HashMap<Integer, ArrayList<String>> commandList = new HashMap<>();

    static {

        Class<?>[] CommandFiles = {
            PlayerCommand.class,
            InternCommand.class,
            GMCommand.class,
            SuperGMCommand.class,
            AdminCommand.class
        };

        for (Class<?> clasz : CommandFiles) {
            try {
                PlayerGMRank rankNeeded = (PlayerGMRank) clasz.getMethod("getPlayerLevelRequired", new Class<?>[]{}).invoke(null, (Object[]) null);
                Class<?>[] a = clasz.getDeclaredClasses();
                ArrayList<String> cL = new ArrayList<>();
                for (Class<?> c : a) {
                    try {
                        if (!Modifier.isAbstract(c.getModifiers()) && !c.isSynthetic()) {
                            Object o = c.newInstance();
                            boolean enabled;
                            try {
                                enabled = c.getDeclaredField("enabled").getBoolean(c.getDeclaredField("enabled"));
                            } catch (NoSuchFieldException ex) {
                                enabled = true; //Enable all coded commands by default.
                            }
                            if (o instanceof CommandExecute && enabled) {
                                cL.add(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase());
                                commands.put(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase(), new MapleCommand((CommandExecute) o, rankNeeded.getLevel()));
                                showcommands.add(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase());
                                if (!rankNeeded.getCommandPrefix().equals(PlayerGMRank.GM.getCommandPrefix()) && !rankNeeded.getCommandPrefix().equals(PlayerGMRank.NORMAL.getCommandPrefix())) { //add it again for GM
                                    commands.put("!" + c.getSimpleName().toLowerCase(), new MapleCommand((CommandExecute) o, PlayerGMRank.GM.getLevel()));
                                    showcommands.add("!" + c.getSimpleName().toLowerCase());
                                }
                            }
                        }
                    } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException ex) {
                        FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
                    }
                }
                Collections.sort(cL);
                commandList.put(rankNeeded.getLevel(), cL);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
            }
        }
    }

    private static void sendDisplayMessage(MapleClient c, String msg, CommandType type) {
        if (c.getPlayer() == null) {
            return;
        }
        switch (type) {
            case NORMAL:
                c.getPlayer().dropMessage(6, msg);
                break;
            case TRADE:
                c.getPlayer().dropMessage(-2, "Error : " + msg);
                break;
        }
    }

    public static void dropHelp(MapleClient c) {
        final StringBuilder sb = new StringBuilder("Command list: ");
        for (int i = 0; i <= c.getPlayer().getGMLevel(); i++) {
            if (commandList.containsKey(i)) {
                for (String s : commandList.get(i)) {
                    sb.append(s);
                    sb.append(" ");
                }
            }
        }
        c.getPlayer().dropMessage(6, sb.toString());
    }

    /**
     * 处理命令，若是命令的花，将在此执行。
     * @param c
     * @param line
     * @param type
     * @return
     */
    public static boolean processCommand(MapleClient c, String line, CommandType type) {
        for (PlayerGMRank prefix : PlayerGMRank.values()) {
            if (line.startsWith(prefix.getCommandPrefix() + prefix.getCommandPrefix())) {
                return false;
            }
        }
        if (String.valueOf(line.charAt(0)).equals(PlayerGMRank.NORMAL.getCommandPrefix())) {
            String[] splitted = line.split(" ");
            splitted[0] = splitted[0].toLowerCase();


            MapleCommand co = commands.get(splitted[0]);
            if (co == null || co.getType() != type) {
                sendDisplayMessage(c, "本玩家指令不存在.", type);
                return true;
            }
            try {
                int ret = co.execute(c, splitted); //Don't really care about the return value. ;D
            } catch (Exception e) {
                sendDisplayMessage(c, "发生错误.", type);
                if (c.getPlayer().isGM()) {
                    sendDisplayMessage(c, "错误: " + e, type);
                    FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
                }
            }
            return true;
        }

        if (c.getPlayer().getGMLevel() > PlayerGMRank.NORMAL.getLevel()) {
            if (line.charAt(0) == '`' && c.getPlayer().getGMLevel() > 2) {
                for (final ChannelServer cserv : ChannelServer.getAllInstances()) {
                    cserv.broadcastGMMessage(tools.packet.CField.multiChat("[GM Chat] " + c.getPlayer().getName(), line.substring(1), 6));
                }
                return true;
            }
            if (line.split(" ")[0].equals("cmd") || String.valueOf(line.charAt(0)).equals(PlayerGMRank.INTERN.getCommandPrefix()) || String.valueOf(line.charAt(0)).equals(PlayerGMRank.GM.getCommandPrefix()) || String.valueOf(line.charAt(0)).equals(PlayerGMRank.SUPERGM.getCommandPrefix())) { //Redundant for now, but in case we change symbols later. This will become extensible.
                String[] splitted = line.split(" ");
                splitted[0] = splitted[0].toLowerCase();

                MapleCommand co = commands.get(splitted[0]);
                if (co == null) {
                    if (splitted[0].equals(line.charAt(0) + "help")) {
                        dropHelp(c);
                        return true;
                    } else if ("!".equals(splitted[0])) {
                        sendDisplayMessage(c, "本指令不存在.", type);
                    } else {
                        List<String> CommandList = new LinkedList<>();
                        for (String sco : showcommands) {
                            if (sco.contains(splitted[0])) {
                                CommandList.add(sco);
                            }
                        }
                        String show = "";

                        for (String sco : CommandList) {
                            show += " " + sco;
                        }
                        if (show.isEmpty()) {
                            sendDisplayMessage(c, "本指令不存在.", type);
                        } else {
                            sendDisplayMessage(c, "相关指令为[" + show + "].", type);
                        }
                    }
                    return true;
                }
                if (c.getPlayer().getGMLevel() >= co.getReqGMLevel()) {
                    int ret = 0;
                    try {
                        ret = co.execute(c, splitted);
                    } catch (ArrayIndexOutOfBoundsException x) {
                        sendDisplayMessage(c, "指令使用方法错误: " + x, type);
                    } catch (Exception e) {
                        FileoutputUtil.outputFileError(FileoutputUtil.CommandEx_Log, e);
                    }
                    if (ret > 0 && c.getPlayer() != null) { //incase d/c after command or something
                        if (c.getPlayer().isGM()) {
                            logCommandToDB(c.getPlayer(), line, "gmlog");
                        } else {
                            logCommandToDB(c.getPlayer(), line, "internlog");
                        }
                    }
                } else {
                    sendDisplayMessage(c, "没有足够的权限来使用本指令.", type);
                }
                return true;
            }
        }
        return false;
    }

    public static void logCommandToDB(MapleCharacter player, String command, String table) {
        PreparedStatement ps = null;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            ps = con.prepareStatement("INSERT INTO " + table + " (cid, command, mapid) VALUES (?, ?, ?)");
            ps.setInt(1, player.getId());
            ps.setString(2, command);
            ps.setInt(3, player.getMap().getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("logCommandToDB" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);

        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("logCommandToDB" + e);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
            }
        }
    }

    public static String getCommandsForLevel(int level) {
        String commandlist = "";
        for (int i = 0; i < commandList.get(level).size(); i++) {
            commandlist += commandList.get(level).get(i);
            if (i + 1 < commandList.get(level).size()) {
                commandlist += ", ";
            }
        }
        return commandlist;
    }
}
